package org.rmcc.ccc.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.rmcc.ccc.model.Photo;
import org.rmcc.ccc.repository.PhotoRepository;
import org.rmcc.ccc.service.user.DropboxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.files.Metadata;

@RestController
@RequestMapping("/api/photos")
public class PhotoController {	
	
	private static final String UNCATALOGED_ROOT = "/ccc camera study project/uncataloged camera study area photos";
	private static final String ARCHIVED_ROOT = "/ccc camera study project/archived photos";

	private PhotoRepository photoRepository;
	private DropboxService dropboxService;
	
	@Autowired
	public PhotoController(PhotoRepository photoRepository,
			DropboxService dropboxService) {
		this.photoRepository = photoRepository;
		this.dropboxService = dropboxService;
	}	

	@RequestMapping(method = RequestMethod.GET)
    public List<Photo> findAll() {
        return (List<Photo>) photoRepository.findAll();
    }
	
	@RequestMapping(value = "/{photoId}", method = RequestMethod.GET)
	Photo findById(@PathVariable Integer photoId) {
		return photoRepository.findOne(photoId);
	}

	@RequestMapping(method = RequestMethod.GET)
    public List<Metadata> findAll(@RequestParam Map<String,String> params) throws DbxException, IOException {
		if (params.get("path") != null) {
			return (List<Metadata>) dropboxService.getFolderContentsByPath(params.get("path"));
		}		
		return (List<Metadata>) dropboxService.getFolderContentsByPath(UNCATALOGED_ROOT);
    }
}