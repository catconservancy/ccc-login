package org.rmcc.ccc.repository;

import org.rmcc.ccc.model.StudyArea;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyAreaRepository extends CrudRepository<StudyArea, Integer> {

}
