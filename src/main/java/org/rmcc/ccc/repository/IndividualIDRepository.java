package org.rmcc.ccc.repository;

import org.rmcc.ccc.model.IndividualID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndividualIDRepository extends CrudRepository<IndividualID, Integer> {

}
