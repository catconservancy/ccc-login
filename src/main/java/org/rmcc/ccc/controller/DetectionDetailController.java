package org.rmcc.ccc.controller;

import java.util.List;

import org.rmcc.ccc.model.DetectionDetail;
import org.rmcc.ccc.repository.DetectionDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/detectionDetail")
public class DetectionDetailController {

	private DetectionDetailRepository detectionDetailRepository;
	
	@Autowired
	public DetectionDetailController(DetectionDetailRepository detectionDetailRepository) {
		this.detectionDetailRepository = detectionDetailRepository;
	}	

	@RequestMapping(method = RequestMethod.GET)
    public List<DetectionDetail> findAll() {
        return (List<DetectionDetail>) detectionDetailRepository.findAll();
    }
	
	@RequestMapping(value = "/{detectionId}", method = RequestMethod.GET)
	DetectionDetail findById(@PathVariable Integer detectionId) {
		return detectionDetailRepository.findOne(detectionId);
	}
	
	@RequestMapping(value = "/species/{speciesId}", method = RequestMethod.GET)
	List<DetectionDetail> findBySpeciesId(@PathVariable Integer speciesId) {
		return detectionDetailRepository.findBySpeciesId(speciesId);
	}

	@RequestMapping(method = RequestMethod.PUT)
	public DetectionDetail update(@RequestBody DetectionDetail detectionDetail) {
		return detectionDetailRepository.save(detectionDetail);
	}

	@RequestMapping(method = RequestMethod.POST)
	public DetectionDetail save(@RequestBody DetectionDetail detectionDetail) {
		detectionDetail.setId(null);
		return detectionDetailRepository.save(detectionDetail);
	}

	@RequestMapping(value = "/{detectionDetailId}", method = RequestMethod.DELETE)
	public void delete(@PathVariable Integer detectionDetailId) {
		detectionDetailRepository.delete(detectionDetailId);
	}
}
