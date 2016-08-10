package org.rmcc.ccc.controller;

import java.util.List;

import org.rmcc.ccc.model.CameraMonitor;
import org.rmcc.ccc.repository.CameraMonitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cameraMonitor")
public class CameraMonitorController {

	private CameraMonitorRepository carmeraMonitorRepository;
	
	@Autowired
	public CameraMonitorController(CameraMonitorRepository carmeraMonitorRepository) {
		this.carmeraMonitorRepository = carmeraMonitorRepository;
	}	

	@RequestMapping(method = RequestMethod.GET)
    public List<CameraMonitor> findAll() {
        return (List<CameraMonitor>) carmeraMonitorRepository.findAll();
    }
	
	@RequestMapping(value = "/{carmeraMonitorId}", method = RequestMethod.GET)
	CameraMonitor findById(@PathVariable Integer carmeraMonitorId) {
		return carmeraMonitorRepository.findOne(carmeraMonitorId);
	}
	
	@RequestMapping(value = "/deployment/{deploymentId}", method = RequestMethod.GET)
	List<CameraMonitor> findByDeploymentId(@PathVariable Integer deploymentId) {
		return carmeraMonitorRepository.findByDeploymentId(deploymentId);
	}
	
	@RequestMapping(method = RequestMethod.PUT)
	public CameraMonitor update(@RequestBody CameraMonitor cameraMonitor) {
		return carmeraMonitorRepository.save(cameraMonitor);
	}

	@RequestMapping(method = RequestMethod.POST)
	public CameraMonitor save(@RequestBody CameraMonitor cameraMonitor) {
		cameraMonitor.setId(null);
		return carmeraMonitorRepository.save(cameraMonitor);
	}

	@RequestMapping(value = "/{carmeraMonitorId}", method = RequestMethod.DELETE)
	public void delete(@PathVariable Integer carmeraMonitorId) {
		carmeraMonitorRepository.delete(carmeraMonitorId);
	}
}
