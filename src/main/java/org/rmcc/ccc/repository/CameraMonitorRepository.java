package org.rmcc.ccc.repository;

import java.util.List;

import org.rmcc.ccc.model.CameraMonitor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CameraMonitorRepository extends CrudRepository<CameraMonitor, Integer> {
	
	List<CameraMonitor> findByDeploymentId(Integer deploymentId);

}

