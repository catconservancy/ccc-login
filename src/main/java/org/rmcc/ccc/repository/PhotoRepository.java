package org.rmcc.ccc.repository;

import java.util.Optional;

import org.rmcc.ccc.model.Photo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoRepository extends CrudRepository<Photo, Integer> {

	Optional<Photo> findOneByDropboxPath(String path);
}
