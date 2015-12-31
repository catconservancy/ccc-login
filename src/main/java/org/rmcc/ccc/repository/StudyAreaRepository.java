package org.rmcc.ccc.repository;

import org.rmcc.ccc.model.Species;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyAreaRepository extends CrudRepository<Species, Integer> {

}
