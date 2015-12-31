package org.rmcc.ccc.repository;

import org.rmcc.ccc.model.Detection;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetectionRepository extends CrudRepository<Detection, Integer> {

}
