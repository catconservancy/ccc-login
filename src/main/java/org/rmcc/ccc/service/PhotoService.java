package org.rmcc.ccc.service;

import com.dropbox.core.DbxException;
import com.mysema.query.types.expr.BooleanExpression;
import org.rmcc.ccc.exception.InvalidPathException;
import org.rmcc.ccc.model.CccMetadata;
import org.rmcc.ccc.model.Deployment;
import org.rmcc.ccc.model.Detection;
import org.rmcc.ccc.model.Photo;
import org.rmcc.ccc.model.StudyArea;
import org.rmcc.ccc.repository.DeploymentRepository;
import org.rmcc.ccc.repository.PhotoPredicatesBuilder;
import org.rmcc.ccc.repository.PhotoRepository;
import org.rmcc.ccc.repository.StudyAreaRepository;
import org.rmcc.ccc.service.user.DropboxService;
import org.rmcc.ccc.utils.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.rmcc.ccc.controller.PhotoController.ARCHIVED_ROOT;
import static org.rmcc.ccc.controller.PhotoController.UNCATALOGED_ROOT;

@Service
public class PhotoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(Deployment.class);

    private static final int DEFAULT_DAYS_BACK = 30;

    private PhotoRepository photoRepository;
    private DeploymentRepository deploymentRepository;
    private StudyAreaRepository studyAreaRepository;
    private DropboxService dropboxService;
    private ImageUtils imageUtils;

    @Autowired
    public PhotoService(PhotoRepository photoRepository,
                        DeploymentRepository deploymentRepository,
                        StudyAreaRepository studyAreaRepository,
                        DropboxService dropboxService,
                        ImageUtils imageUtils) {
        this.photoRepository = photoRepository;
        this.deploymentRepository = deploymentRepository;
        this.studyAreaRepository = studyAreaRepository;
        this.dropboxService = dropboxService;
        this.imageUtils = imageUtils;
    }

    public List<Photo> getDbPhotos(Map<String, String> params, Pageable pageable) {

        Sort sort = pageable.getSort() != null ? pageable.getSort() : new Sort(Sort.Direction.ASC,"imageDate");

        List<Photo> photos = new ArrayList<Photo>();

        Integer studyAreaId =  getInteger(params, "studyAreaId");
        Integer locationId =  getInteger(params, "locationId");
        Boolean highlighted = getBoolean(params, "highlighted");
        Timestamp startDate = getStartDate(params.get("startDate"));
        Timestamp endDate = getEndDate(params.get("endDate"));
        Integer[] speciesIds = getSpeciesIds(params.get("speciesIds"));

        PhotoPredicatesBuilder builder = new PhotoPredicatesBuilder();

        if (studyAreaId != null)
            builder.with("deployment.studyArea.id", ":", studyAreaId);
        if (locationId != null)
            builder.with("deployment.id", ":", locationId);
        if (speciesIds != null && speciesIds.length > 0)
            builder.with("species.id", "in", speciesIds);
        if (highlighted != null)
            builder.with("highlight", ":", highlighted);
        builder.with("imageDate", ">", startDate);
        builder.with("imageDate", "<", endDate);

        BooleanExpression exp = builder.build();
        photoRepository.findAll(exp, pageable).forEach(photos::add);

        return photos;
    }

    public List<Photo> getDropboxPhotos(String path) throws InvalidPathException, IOException, DbxException {

        List<Photo> photos = new ArrayList<>();

        List<com.dropbox.core.v2.files.Metadata> dropboxMetadata;
        if (path != null) {
            if (!path.equalsIgnoreCase(UNCATALOGED_ROOT) && !isValidPath(path)) {
                throw new InvalidPathException(path);
            }
            dropboxMetadata = dropboxService.getFolderContentsByPath(path);
        } else {
            dropboxMetadata = dropboxService.getFolderContentsByPath(UNCATALOGED_ROOT);
        }

        for (com.dropbox.core.v2.files.Metadata m : dropboxMetadata) {
            CccMetadata metadata = (CccMetadata) m;
            Optional<Photo> optPhoto = photoRepository.findOneByDropboxPath(m.getPathLower());
            Photo photo = optPhoto.isPresent() ? optPhoto.get() : new Photo();
            photo.setMetadata(metadata);
            photo.setDropboxPath(metadata.getPathLower());
            photo.setFileName(metadata.getName());
            if (!metadata.isDir()) {
                photo.setDeployment(!getDeploymentsByPath(path).isEmpty() ? getDeploymentsByPath(path).get(0) : null);
                photo = photoRepository.save(photo);
            }
            photos.add(photo);
        }

        return photos;
    }

    public Photo savePhoto(Photo photo) {
        Photo dbPhoto = photoRepository.findOne(photo.getId());
        for (Detection d : dbPhoto.getDetections()) {
            d.setPhoto(null);
            if (d.getDetectionDetail() != null) {
                d.getDetectionDetail().removeDetection(d);
            }
        }
        photoRepository.save(photo);
        for (Detection d : photo.getDetections()) {
            d.setPhoto(photo);
            if (d.getDetectionDetail() != null) {
                d.getDetectionDetail().addDetection(d);
            }
        }
        if (photo.getHighlight() != null && photo.getHighlight() && photo.getImageDate() == null) {
            try {
                InputStream inputStream = dropboxService.getInputStreamByPath(photo.getDropboxPath());
                photo.setImageDate(imageUtils.getImageDate(inputStream));
            } catch (Exception e) {
                LOGGER.error("Error occurred getting image date from input stream.", e);
            }
        }
        return photoRepository.save(photo);
    }

    private boolean isValidPath(String path) {
        String pathSubstr = path.substring(path.indexOf(UNCATALOGED_ROOT) + UNCATALOGED_ROOT.length() + 1);
        String[] pathElements = pathSubstr.split("/");
        boolean isValid = pathElements.length > 2;
        if (getStudyAreaByPath(path).size() > 0) {
            isValid = true;
        } else {
            if (getDeploymentsByPath(path).size() > 0) {
                isValid = true;
            }
        }
        return isValid;
    }

    private List<StudyArea> getStudyAreaByPath(String path) {
        String pathSubstr = path.substring(path.indexOf(UNCATALOGED_ROOT) + UNCATALOGED_ROOT.length() + 1);
        String[] pathElements = pathSubstr.split("/");
        List<StudyArea> studyAreas = new ArrayList<>();
        if (pathElements.length > 0) {
            studyAreas = studyAreaRepository.findByDropboxPathIgnoreCase(path);
        }
        return studyAreas;
    }

    private List<Deployment> getDeploymentsByPath(String path) {
        String pathSubstr = path.substring(path.indexOf(UNCATALOGED_ROOT) + UNCATALOGED_ROOT.length() + 1);
        String[] pathElements = pathSubstr.split("/");
        List<Deployment> deployments = new ArrayList<>();
        if (pathElements.length > 0) {
            deployments = deploymentRepository.findByDropboxPathIgnoreCase(path);
            if (deployments.isEmpty()) {
                if (pathElements.length == 1) {
                    deployments = deploymentRepository.findByStudyAreaNameIgnoreCase(pathElements[0]);
                } else {
                    StudyArea sa = studyAreaRepository.findOneByDropboxPathIgnoreCase(UNCATALOGED_ROOT + "/" + pathElements[0]);
                    deployments = deploymentRepository.findByStudyAreaNameAndLocationIDIgnoreCase(sa != null ? sa.getName() : pathElements[0], pathElements[1]);
                }
            }
        }
        return deployments;
    }

    public String convertToArchivedPath(Photo photo) {

        String archivedPath = null;

        if (photo.getDropboxPath() != null) {
            archivedPath = photo.getDropboxPath().toLowerCase().replace(UNCATALOGED_ROOT, ARCHIVED_ROOT + "/archived study area photos");
            if (photo.getHighlight() != null && photo.getHighlight()) {
                archivedPath = archivedPath.replace("/archived study area photos", "/highlight photos");
                String[] pathElements = archivedPath.split("/");
                String highlightPath = "";
                for (int i = 0; i < 6; i++)
                    highlightPath += pathElements[i] + "/";

                long timestamp = photo.getImageDate().getTime();
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(timestamp);
                int imageYear = cal.get(Calendar.YEAR);

                archivedPath = highlightPath + imageYear + "/";
            }
        }
        return archivedPath;
    }

    private Integer[] getSpeciesIds(String speciesIdsStr) {
        String[] speciesIdsStrings = new String[0];
        if (speciesIdsStr != null && !"".equalsIgnoreCase(speciesIdsStr)) {
            speciesIdsStrings = speciesIdsStr.split(",");
        }
        Integer[] speciesIds = new Integer[speciesIdsStrings.length];
        for (int i = 0; i < speciesIdsStrings.length; i++) {
            speciesIds[i] = Integer.parseInt(speciesIdsStrings[i]);
        }
        return speciesIds.length > 0 ? speciesIds : null;
    }

    private Timestamp getStartDate(String startDateStr) {
        Calendar cal = Calendar.getInstance();
        Timestamp timestamp = new Timestamp(new Date().getTime());
        cal.setTimeInMillis(timestamp.getTime());
        cal.add(Calendar.DAY_OF_MONTH, -DEFAULT_DAYS_BACK);

        Timestamp startDate = new Timestamp(cal.getTime().getTime());

        if (startDateStr != null) {
            Date start = convertToDate(startDateStr);
            if (start != null) {
                startDate = new Timestamp(start.getTime());
            }
        }

        return startDate;
    }

    private Date convertToDate(String startDateString) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        try {
            return df.parse(startDateString.replaceAll("Z$", "+0000"));
        } catch (ParseException e) {
            LOGGER.error("ParseException occurred converting string to date: " + startDateString, e);
        }
        return null;
    }

    private Timestamp getEndDate(String endDateStr) {
        Date today = new Date();
        Timestamp endDate = new Timestamp(today.getTime());

        if (endDateStr != null) {
            Date end = convertToDate(endDateStr);
            endDate = new Timestamp(end.getTime());
        }

        return endDate;
    }

    private Integer getInteger(Map<String,String> params, String param) {
        return params.get(param) != null ? Integer.parseInt(params.get(param)) : null;
    }

    private Boolean getBoolean(Map<String,String> params, String param) {
        return params.get(param) != null ? Boolean.valueOf(params.get(param)) : null;
    }

}
