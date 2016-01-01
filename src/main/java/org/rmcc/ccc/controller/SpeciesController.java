package org.rmcc.ccc.controller;

import java.util.List;

import org.rmcc.ccc.model.Species;
import org.rmcc.ccc.repository.SpeciesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/species")
public class SpeciesController {

	private SpeciesRepository speciesRepository;
	
	@Autowired
	public SpeciesController(SpeciesRepository speciesRepository) {
		this.speciesRepository = speciesRepository;
	}	

	@RequestMapping(method = RequestMethod.GET)
    public List<Species> findAll() {
        return (List<Species>) speciesRepository.findAll();
    }
	
	@RequestMapping(value = "/{speciesId}", method = RequestMethod.GET)
	public Species findById(@PathVariable Integer speciesId) {
		return speciesRepository.findOne(speciesId);
	}
	
	@RequestMapping(method = RequestMethod.PUT)
	public Species update(@RequestBody Species species) {
		return speciesRepository.save(species);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public Species save(@RequestBody Species species) {
		species.setId(null);
		return speciesRepository.save(species);
	}
	
	@RequestMapping(value = "/{speciesId}", method = RequestMethod.DELETE)
	public void delete(@PathVariable Integer speciesId) {
		speciesRepository.delete(speciesId);
	}
}
