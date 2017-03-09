package org.rmcc.ccc.repository;

import org.rmcc.ccc.model.CameraMonitor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CameraMonitorRepository extends CrudRepository<CameraMonitor, Integer> {
	
	List<CameraMonitor> findByDeploymentId(Integer deploymentId);

    @Query("select count(e) from CameraMonitor e")
    int getCount();

}

