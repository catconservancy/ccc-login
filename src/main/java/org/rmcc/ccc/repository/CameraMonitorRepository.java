package org.rmcc.ccc.repository;

import org.rmcc.ccc.model.CameraMonitor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CameraMonitorRepository extends CrudRepository<CameraMonitor, Integer> {

}

