package org.rmcc.ccc.controller;

import java.util.List;

import org.rmcc.ccc.model.LookupOption;
import org.rmcc.ccc.repository.LookupOptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lookupOption")
public class LookupOptionController {

	private LookupOptionRepository lookupOptionRepository;
	
	@Autowired
	public LookupOptionController(LookupOptionRepository lookupOptionRepository) {
		this.lookupOptionRepository = lookupOptionRepository;
	}	

	@RequestMapping(method = RequestMethod.GET)
    public List<LookupOption> findAll() {
        return (List<LookupOption>) lookupOptionRepository.findAll();
    }
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	LookupOption findById(@PathVariable Integer id) {
		return lookupOptionRepository.findOne(id);
	}
	
	@RequestMapping(value = "/options/{listCode}", method = RequestMethod.GET)
	List<LookupOption> findBySpeciesId(@PathVariable String listCode) {
		return lookupOptionRepository.findByListCode(listCode);
	}

	@RequestMapping(method = RequestMethod.PUT)
	public LookupOption update(@RequestBody LookupOption lookupOption) {
		return lookupOptionRepository.save(lookupOption);
	}

	@RequestMapping(method = RequestMethod.POST)
	public LookupOption save(@RequestBody LookupOption lookupOption) {
		lookupOption.setId(null);
		return lookupOptionRepository.save(lookupOption);
	}

	@RequestMapping(value = "/{lookupOptionId}", method = RequestMethod.DELETE)
	public void delete(@PathVariable Integer lookupOptionId) {
		lookupOptionRepository.delete(lookupOptionId);
	}
}
