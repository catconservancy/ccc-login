package org.rmcc.ccc.repository;

import org.rmcc.ccc.model.DetectionDetail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetectionDetailRepository extends CrudRepository<DetectionDetail, Integer> {

}
