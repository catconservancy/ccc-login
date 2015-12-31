package org.rmcc.ccc.controller;

import java.util.List;

import org.rmcc.ccc.model.Photo;
import org.rmcc.ccc.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/photos")
public class PhotoController {

	private PhotoRepository photoRepository;
	
	@Autowired
	public PhotoController(PhotoRepository photoRepository) {
		this.photoRepository = photoRepository;
	}	

	@RequestMapping(method = RequestMethod.GET)
    public List<Photo> findAll() {
        return (List<Photo>) photoRepository.findAll();
    }
	
	@RequestMapping(value = "/{speciesId}", method = RequestMethod.GET)
	Photo findById(@PathVariable Integer photoId) {
		return photoRepository.findOne(photoId);
	}
}