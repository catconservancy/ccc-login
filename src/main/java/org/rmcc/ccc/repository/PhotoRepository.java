package org.rmcc.ccc.repository;

import org.rmcc.ccc.model.Photo;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhotoRepository extends CrudRepository<Photo, Integer>,
		QueryDslPredicateExecutor<Photo> {

	Optional<Photo> findOneByDropboxPath(String path);
}
