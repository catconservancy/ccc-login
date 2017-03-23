package org.rmcc.ccc.controller;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.files.Metadata;
import org.rmcc.ccc.model.Photo;
import org.rmcc.ccc.repository.PhotoRepository;
import org.rmcc.ccc.service.PhotoService;
import org.rmcc.ccc.service.user.DropboxService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

    public static final String UNCATALOGED_ROOT = "/ccc camera study project/uncataloged camera study area photos";
	public static final String ARCHIVED_ROOT = "/ccc camera study project/archived photos";
    private static final Logger LOGGER = LoggerFactory.getLogger(PhotoController.class);
    @Autowired
    Environment env;
    private PhotoRepository photoRepository;
    private PhotoService photoService;
	private DropboxService dropboxService;
	
	@Autowired
	public PhotoController(PhotoRepository photoRepository,
                           PhotoService photoService,
                           DropboxService dropboxService) {
        this.photoRepository = photoRepository;
        this.photoService = photoService;
		this.dropboxService = dropboxService;
	}	

	@RequestMapping(method = RequestMethod.GET)
	public List<Photo> findAll(@RequestParam Map<String,String> params, Pageable pageable) throws Exception {

		String path = params.get("path");
		boolean isArchived = params.get("isArchived") != null && Boolean.valueOf(params.get("isArchived"));

		if (isArchived) {
            return photoService.getDbPhotos(params, pageable);
        } else {
            return photoService.getDropboxPhotos(path);
        }
    }

	@RequestMapping(value = "/{photoId}", method = RequestMethod.GET)
	Photo findById(@PathVariable Integer photoId) {
		return photoRepository.findOne(photoId);
	}

	@RequestMapping(method = RequestMethod.PUT)
	public Photo update(@RequestBody Photo photo) {
        return photoService.savePhoto(photo);
    }

	@RequestMapping(method = RequestMethod.POST)
	public Photo save(@RequestBody Photo photo) {
		photo.setId(null);
        return photoService.savePhoto(photo);
    }

	@RequestMapping(method = RequestMethod.DELETE)
	public void delete(UsernamePasswordAuthenticationToken token, @RequestParam Map<String,String> params) throws DbxException {
		String path = params.get("path");
		LOGGER.info("delete called for path: " + path + ", by user: " + token.getName());
		dropboxService.deleteFile(path);
		Optional<Photo> optPhoto = photoRepository.findOneByDropboxPath(path);
		if (optPhoto.isPresent()) {
			photoRepository.delete(optPhoto.get());
		}
	}

	@RequestMapping(value = "/{photoId}", method = RequestMethod.POST)
	public Photo archive(@PathVariable Integer photoId, @RequestParam Map<String,String> params) throws DbxException {
		Photo photo = photoRepository.findOne(photoId);
		LOGGER.info("archive called for path: " + photo.getDropboxPath());
		String archivedPath = photoService.convertToArchivedPath(photo);
        LOGGER.debug("attempting to move file from path: " + photo.getDropboxPath() + ", to path: " + archivedPath);
        Metadata m = dropboxService.moveFile(photo.getDropboxPath(),archivedPath);
		if (m != null) {
			LOGGER.info("photo with id: " + photoId + " successfully moved to " + m.getPathLower());
			photo.setDropboxPath(m.getPathLower());
        } else {
            LOGGER.error("failed to move file from path: " + photo.getDropboxPath() + ", to path: " + archivedPath);
        }

		return photoRepository.save(photo);
	}

    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    public void resetDropboxPath() throws Exception {

        String activeProfile = env.getActiveProfiles() != null && env.getActiveProfiles().length > 0 ? env.getActiveProfiles()[0] : null;
        if (activeProfile.equalsIgnoreCase("heroku")) {
            List<Photo> photos = new ArrayList<>();
            photoRepository.findAll().forEach(photo -> {
                Metadata m = null;
                try {
                    m = dropboxService.moveFile(photo.getDropboxPath(), photo.getOrigDropboxPath());
                } catch (DbxException e) {
                    LOGGER.error("Error occurred moving photo from: " + photo.getDropboxPath() + " to: " + photo.getOrigDropboxPath());
                }
                if (m != null) {
                    LOGGER.info("photo with id: " + photo.getId() + " successfully moved to " + m.getPathLower());
                    photo.setDropboxPath(m.getPathLower());
                    photoRepository.save(photo);
                } else {
                    LOGGER.error("failed to move file from path: " + photo.getDropboxPath() + ", to path: " + photo.getOrigDropboxPath());
                }
            });
        } else {
            throw new Exception("You are not authorized to reset dropbox path");
        }
    }
}