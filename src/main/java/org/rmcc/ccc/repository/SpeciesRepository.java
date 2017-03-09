package org.rmcc.ccc.repository;

import org.rmcc.ccc.model.Species;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpeciesRepository extends CrudRepository<Species, Integer> {

    @Query(value = "select count(distinct a.species_id) " +
            "from species a " +
            "join detections b on a.species_id = b.species_id ", nativeQuery = true)
    Integer getDetectedSpeciesCount();
}
