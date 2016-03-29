package org.rmcc.ccc.repository;

import java.util.List;

import org.rmcc.ccc.model.DetectionDetail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetectionDetailRepository extends CrudRepository<DetectionDetail, Integer> {

	List<DetectionDetail> findBySpeciesId(Integer speciesId);

}
