package org.rmcc.ccc.repository;

import java.util.List;

import org.rmcc.ccc.model.Deployment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeploymentRepository extends CrudRepository<Deployment, Integer> {

	List<Deployment> findByDropboxPathIgnoreCase(String dropboxPath);
	List<Deployment> findByStudyAreaNameIgnoreCase(String studyAreaName);
	List<Deployment> findByStudyAreaNameAndLocationIDIgnoreCase(String studyAreaName, String locationId);

}
