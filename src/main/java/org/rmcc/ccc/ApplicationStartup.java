package org.rmcc.ccc;

import java.util.List;

import org.rmcc.ccc.model.BaseModel;
import org.rmcc.ccc.model.Species;
import org.rmcc.ccc.repository.SpeciesRepository;
import org.rmcc.ccc.utils.CsvFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {
	
	private CsvFileReader csvFileReader;
	private SpeciesRepository speciesRepository;

	@Autowired
	public ApplicationStartup(CsvFileReader csvFileReader, 
			SpeciesRepository speciesRepository) {
		super();
		this.csvFileReader = csvFileReader;
		this.speciesRepository = speciesRepository;
	}


	@Override
	public void onApplicationEvent(final ContextRefreshedEvent event) {
		List<BaseModel> speciesList = csvFileReader.readCsvFile(new Species());
		for (BaseModel species : speciesList) {
			Species s = (Species) species;
			s.setId(null);
			speciesRepository.save(s);
		}
	}

}