package org.rmcc.ccc.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.rmcc.ccc.model.CccMetadata;
import org.rmcc.ccc.model.Detection;
import org.rmcc.ccc.model.Photo;
import org.rmcc.ccc.repository.DetectionRepository;
import org.rmcc.ccc.repository.PhotoRepository;
import org.rmcc.ccc.service.PhotoService;
import org.rmcc.ccc.service.user.DropboxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.drew.imaging.ImageProcessingException;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.files.Metadata;

@RestController
@RequestMapping("/api/photos")
public class PhotoController {	
	
	private static final String UNCATALOGED_ROOT = "/ccc camera study project/uncataloged camera study area photos";
	private static final String ARCHIVED_ROOT = "/ccc camera study project/archived photos";

	private PhotoRepository photoRepository;
	private PhotoService photoService;
	private DetectionRepository detectionRepository;
	private DropboxService dropboxService;
	
	@Autowired
	public PhotoController(PhotoRepository photoRepository,
			PhotoService photoService,
			DetectionRepository detectionRepository,
			DropboxService dropboxService) {
		this.photoRepository = photoRepository;
		this.photoService = photoService;
		this.detectionRepository = detectionRepository;
		this.dropboxService = dropboxService;
	}	

	@RequestMapping(method = RequestMethod.GET)

	public List<Photo> findAll(@RequestParam Map<String,String> params) throws Exception {
		List<Photo> photos = new ArrayList<Photo>();
		
		// TODO: move this logic to a PhotService
		
		List<Metadata> dropboxMetadata = new ArrayList<Metadata>();
		if (params.get("path") != null) {
			dropboxMetadata = dropboxService.getFolderContentsByPath(params.get("path"));
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
				photo = photoRepository.save(photo);
				PhotoService.getFileMetadata(dropboxService.getInputStreamByPath(metadata.getPathLower()), metadata.getName());
			} else {
				//TODO: add logic to populate create deployment and set on photo.
			}
			photos.add(photo);
		}
		
        return photos;
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
}