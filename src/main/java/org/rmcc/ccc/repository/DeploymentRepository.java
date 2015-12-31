package org.rmcc.ccc.repository;

import org.rmcc.ccc.model.Deployment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeploymentRepository extends CrudRepository<Deployment, Integer> {

}
