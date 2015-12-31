package org.rmcc.ccc.controller;

import java.util.List;

import org.rmcc.ccc.model.IndividualID;
import org.rmcc.ccc.repository.IndividualIDRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/individualIDs")
public class IndividualIDController {

	private IndividualIDRepository individualIDRepository;
	
	@Autowired
	public IndividualIDController(IndividualIDRepository individualIDRepository) {
		this.individualIDRepository = individualIDRepository;
	}	

	@RequestMapping(method = RequestMethod.GET)
    public List<IndividualID> findAll() {
        return (List<IndividualID>) individualIDRepository.findAll();
    }
	
	@RequestMapping(value = "/{detectionId}", method = RequestMethod.GET)
	IndividualID findById(@PathVariable Integer detectionId) {
		return individualIDRepository.findOne(detectionId);
	}
}
