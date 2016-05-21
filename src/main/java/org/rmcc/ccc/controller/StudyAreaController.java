package org.rmcc.ccc.controller;

import java.util.List;

import org.rmcc.ccc.model.StudyArea;
import org.rmcc.ccc.repository.StudyAreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
    public List<StudyArea> findAll() {
        return (List<StudyArea>) studyAreaRepository.findAll();
    }
	
	@RequestMapping(value = "/{studyAreaId}", method = RequestMethod.GET)
	StudyArea findById(@PathVariable Integer studyAreaId) {
		return studyAreaRepository.findOne(studyAreaId);
	}

	@RequestMapping(method = RequestMethod.PUT)
	public StudyArea update(@RequestBody StudyArea studyArea) {
		return studyAreaRepository.save(studyArea);
	}

	@RequestMapping(method = RequestMethod.POST)
	public StudyArea save(@RequestBody StudyArea studyArea) {
		studyArea.setId(null);
		return studyAreaRepository.save(studyArea);
	}

	@RequestMapping(value = "/{studyAreaId}", method = RequestMethod.DELETE)
	public void delete(@PathVariable Integer studyAreaId) {
		studyAreaRepository.delete(studyAreaId);
	}
}
