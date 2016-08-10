package org.rmcc.ccc.controller;

import java.util.List;

import org.rmcc.ccc.model.Detection;
import org.rmcc.ccc.repository.DetectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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

	@RequestMapping(method = RequestMethod.PUT)
	public Detection update(@RequestBody Detection detection) {
		return detectionRepository.save(detection);
	}

	@RequestMapping(method = RequestMethod.POST)
	public Detection save(@RequestBody Detection detection) {
		detection.setId(null);
		return detectionRepository.save(detection);
	}

	@RequestMapping(value = "/{detectionId}", method = RequestMethod.DELETE)
	public void delete(@PathVariable Integer detectionId) {
		detectionRepository.delete(detectionId);
	}
}
