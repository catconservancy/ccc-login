package org.rmcc.ccc.controller;

import java.util.List;

import org.rmcc.ccc.model.Detection;
import org.rmcc.ccc.repository.DetectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/detection")
public class DetectionController {

	private DetectionRepository detectionRepository;
	
	@Autowired
	public DetectionController(DetectionRepository detectionRepository) {
		this.detectionRepository = detectionRepository;
	}	

	@RequestMapping(method = RequestMethod.GET)
    public List<Detection> findAll() {
        return (List<Detection>) detectionRepository.findAll();
    }
	
	@RequestMapping(value = "/{detectionId}", method = RequestMethod.GET)
	Detection findById(@PathVariable Integer detectionId) {
		return detectionRepository.findOne(detectionId);
	}
}
