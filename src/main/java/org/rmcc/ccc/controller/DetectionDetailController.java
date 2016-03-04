package org.rmcc.ccc.controller;

import org.rmcc.ccc.model.DetectionDetail;
import org.rmcc.ccc.repository.DetectionDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
