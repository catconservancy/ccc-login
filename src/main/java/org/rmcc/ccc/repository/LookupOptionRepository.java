package org.rmcc.ccc.repository;

import java.util.List;

import org.rmcc.ccc.model.LookupOption;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LookupOptionRepository extends CrudRepository<LookupOption, Integer> {

	List<LookupOption> findByListCode(String listCode);

}
