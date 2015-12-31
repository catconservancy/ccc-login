package org.rmcc.ccc.controller;

import java.util.List;

import org.rmcc.ccc.model.Species;
import org.rmcc.ccc.repository.StudyAreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/studyAreas")
public class StudyAreaController {

	private StudyAreaRepository studyAreaRepository;
	
	@Autowired
	public StudyAreaController(StudyAreaRepository studyAreaRepository) {
		this.studyAreaRepository = studyAreaRepository;
	}	

	@RequestMapping(method = RequestMethod.GET)
    public List<Species> findAll() {
        return (List<Species>) studyAreaRepository.findAll();
    }
	
	@RequestMapping(value = "/{speciesId}", method = RequestMethod.GET)
	Species findById(@PathVariable Integer studyAreaId) {
		return studyAreaRepository.findOne(studyAreaId);
	}
}
