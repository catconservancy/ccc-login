package org.rmcc.ccc.repository;

import org.rmcc.ccc.model.Photo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhotoRepository extends CrudRepository<Photo, Integer>,
		QueryDslPredicateExecutor<Photo> {

	Optional<Photo> findOneByDropboxPath(String path);

	@Query(value = "select count(distinct a.id) " +
			"from photos a " +
			"join detections b on a.id = b.image_id " +
			"where a.image_date > cast(cast(current_timestamp as date) - 60 as timestamp) " +
			"and lower(a.dropbox_path) like '/ccc camera study project/archived photos%' ", nativeQuery = true)
	Integer getNewTaggedCount();

	@Query(value = "select * " +
			"from photos a " +
			"join detections b on a.id = b.image_id " +
			"where a.image_date > cast(cast(current_timestamp as date) - 60 as timestamp) " +
			"and lower(a.dropbox_path) like '/ccc camera study project/archived photos%' \n--#pageable\n",
			nativeQuery = true)
	List<Photo> getNewTagged(Pageable pageable);

	@Query(value = "select count(distinct a.id) " +
			"from photos a " +
			"where a.image_date > cast(cast(current_timestamp as date) - 60 as timestamp) " +
			"and a.highlight = true " +
			"and lower(a.dropbox_path) like '/ccc camera study project/archived photos%' ", nativeQuery = true)
	Integer getNewHighlightedCount();

	@Query(value = "select * " +
			"from photos a " +
			"where a.image_date > cast(cast(current_timestamp as date) - 60 as timestamp) " +
			"and a.highlight = true " +
			"and lower(a.dropbox_path) like '/ccc camera study project/archived photos%' \n--#pageable\n", nativeQuery = true)
	List<Photo> getNewHighlighted(Pageable pageable);

	@Query(value = "select count(distinct a.id) " +
			"from photos a " +
			"join detections b on a.id = b.image_id ", nativeQuery = true)
	Integer getTaggedCount();

	@Query(value = "select count(distinct a.id) " +
			"from photos a " +
			"where a.highlight = true ", nativeQuery = true)
	Integer getHighlightedCount();
}
