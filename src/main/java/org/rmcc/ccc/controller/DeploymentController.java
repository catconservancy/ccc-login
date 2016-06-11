package org.rmcc.ccc.controller;

import java.util.List;

import org.rmcc.ccc.model.Deployment;
import org.rmcc.ccc.repository.DeploymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/deployment")
public class DeploymentController {

	private DeploymentRepository deploymentRepository;
	
	@Autowired
	public DeploymentController(DeploymentRepository deploymentRepository) {
		this.deploymentRepository = deploymentRepository;
	}	

	@RequestMapping(method = RequestMethod.GET)
    public List<Deployment> findAll() {
        return (List<Deployment>) deploymentRepository.findAll();
    }
	
	@RequestMapping(value = "/{deploymentId}", method = RequestMethod.GET)
	Deployment findById(@PathVariable Integer deploymentId) {
		if (deploymentId == 0) {
			return new Deployment();
		}
		return deploymentRepository.findOne(deploymentId);
	}

	@RequestMapping(method = RequestMethod.PUT)
	public Deployment update(@RequestBody Deployment deployment) {
		return deploymentRepository.save(deployment);
	}

	@RequestMapping(method = RequestMethod.POST)
	public Deployment save(@RequestBody Deployment deployment) {
		deployment.setId(null);
		return deploymentRepository.save(deployment);
	}

	@RequestMapping(value = "/{deploymentId}", method = RequestMethod.DELETE)
	public void delete(@PathVariable Integer deploymentId) {
		deploymentRepository.delete(deploymentId);
	}
}
