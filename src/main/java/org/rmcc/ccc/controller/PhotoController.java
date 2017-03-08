package org.rmcc.ccc.controller;

import com.dropbox.core.v2.files.Metadata;
import org.rmcc.ccc.exception.InvalidPathException;
import org.rmcc.ccc.model.CccMetadata;
import org.rmcc.ccc.model.Deployment;
import org.rmcc.ccc.model.Detection;
import org.rmcc.ccc.model.Photo;
import org.rmcc.ccc.model.StudyArea;
import org.rmcc.ccc.repository.DeploymentRepository;
import org.rmcc.ccc.repository.DetectionRepository;
import org.rmcc.ccc.repository.PhotoRepository;
import org.rmcc.ccc.repository.StudyAreaRepository;
import org.rmcc.ccc.service.PhotoService;
import org.rmcc.ccc.service.user.DropboxService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/photos")
public class PhotoController {	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PhotoController.class);
	
	public static final String UNCATALOGED_ROOT = "/ccc camera study project/uncataloged camera study area photos";
	public static final String ARCHIVED_ROOT = "/ccc camera study project/archived photos";

	private PhotoRepository photoRepository;
	private PhotoService photoService;
	private DetectionRepository detectionRepository;
	private DropboxService dropboxService;
	private DeploymentRepository deploymentRepository;
	private StudyAreaRepository studyAreaRepository;
	
	@Autowired
	public PhotoController(PhotoRepository photoRepository,
			PhotoService photoService,
			DetectionRepository detectionRepository,
			DropboxService dropboxService,
		    DeploymentRepository deploymentRepository,
			StudyAreaRepository studyAreaRepository) {
		this.photoRepository = photoRepository;
		this.photoService = photoService;
		this.detectionRepository = detectionRepository;
		this.dropboxService = dropboxService;
		this.deploymentRepository = deploymentRepository;
		this.studyAreaRepository = studyAreaRepository;
	}	

	@RequestMapping(method = RequestMethod.GET)
	public List<Photo> findAll(@RequestParam Map<String,String> params, Pageable pageable) throws Exception {
		List<Photo> photos = new ArrayList<Photo>();
		String path = params.get("path");
		boolean isArchived = params.get("isArchived") != null && Boolean.valueOf(params.get("isArchived"));

		if (isArchived) {
			return photoService.getPhotos(params, pageable);
		} else {

			List<Metadata> dropboxMetadata = new ArrayList<Metadata>();
			if (path != null) {
				if (!path.equalsIgnoreCase(UNCATALOGED_ROOT) && !isValidPath(path)) {
					throw new InvalidPathException(path);
				}
				dropboxMetadata = dropboxService.getFolderContentsByPath(path);
			} else {
				dropboxMetadata = dropboxService.getFolderContentsByPath(UNCATALOGED_ROOT);
			}

			for (Metadata m : dropboxMetadata) {
				CccMetadata metadata = (CccMetadata) m;
				Optional<Photo> optPhoto = photoRepository.findOneByDropboxPath(m.getPathLower());
				Photo photo = optPhoto.isPresent() ? optPhoto.get() : new Photo();
				photo.setMetadata(metadata);
				photo.setDropboxPath(metadata.getPathLower());
				if (!metadata.isDir()) {
					photo.setDeployment(!getDeploymentsByPath(path).isEmpty() ? getDeploymentsByPath(path).get(0): null);
					photo = photoRepository.save(photo);
				}
				photos.add(photo);
			}

		}
		
        return photos;
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

	@RequestMapping(value = "/{photoId}", method = RequestMethod.GET)
	Photo findById(@PathVariable Integer photoId) {
		return photoRepository.findOne(photoId);
	}

	@RequestMapping(method = RequestMethod.PUT)
	public Photo update(@RequestBody Photo photo) {
		return savePhoto(photo);
	}

	@RequestMapping(method = RequestMethod.POST)
	public Photo save(@RequestBody Photo photo) {
		photo.setId(null);
		return savePhoto(photo);
	}
	
	private Photo savePhoto(Photo photo) {
		Photo dbPhoto = photoRepository.findOne(photo.getId());
		for (Detection d : dbPhoto.getDetections()) {
			d.setPhoto(null);
			if (d.getDetectionDetail() != null) {
				d.getDetectionDetail().removeDetection(d);
			}
		}
		photoRepository.save(photo);
		for (Detection d: photo.getDetections()) {
			d.setPhoto(photo);
			if (d.getDetectionDetail() != null) {
				d.getDetectionDetail().addDetection(d);
			}
		}
		return photoRepository.save(photo);
	}

	@RequestMapping(method = RequestMethod.DELETE)
	public void delete(@RequestParam Map<String,String> params) {
		String path = params.get("path");
		LOGGER.info("delete called for path: " + path);
	}

	@RequestMapping(value = "/{photoId}", method = RequestMethod.POST)
	public Map<String,String> archive(@PathVariable Integer photoId, @RequestParam Map<String,String> params) {
		Photo photo = photoRepository.findOne(photoId);
		LOGGER.info("archive called for path: " + photo.getDropboxPath());
		return params;
	}
}