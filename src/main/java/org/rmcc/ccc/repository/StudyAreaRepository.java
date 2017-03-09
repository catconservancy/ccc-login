package org.rmcc.ccc.repository;

import org.rmcc.ccc.model.StudyArea;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyAreaRepository extends CrudRepository<StudyArea, Integer> {

    StudyArea findOneByDropboxPathIgnoreCase(String dropboxPath);
    List<StudyArea> findByDropboxPathIgnoreCase(String dropboxPath);

    @Query("select count(e) from StudyArea e")
    int getCount();
}
