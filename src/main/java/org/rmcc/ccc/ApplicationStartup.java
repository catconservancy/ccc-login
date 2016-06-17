package org.rmcc.ccc;

import java.util.List;

import org.rmcc.ccc.model.BaseModel;
import org.rmcc.ccc.model.LookupOption;
import org.rmcc.ccc.model.Role;
import org.rmcc.ccc.model.Species;
import org.rmcc.ccc.model.User;
import org.rmcc.ccc.model.UserCreateForm;
import org.rmcc.ccc.repository.LookupOptionRepository;
import org.rmcc.ccc.repository.SpeciesRepository;
import org.rmcc.ccc.repository.UserRepository;
import org.rmcc.ccc.service.user.UserService;
import org.rmcc.ccc.utils.CsvFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
@Profile({"dev"/*, "postgres"*/})
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {
	
	private CsvFileReader csvFileReader;
	private SpeciesRepository speciesRepository;
	private LookupOptionRepository lookupOptionRepository;
	private UserService userService;

	@Autowired
	public ApplicationStartup(CsvFileReader csvFileReader, 
			SpeciesRepository speciesRepository, 
			LookupOptionRepository lookupOptionRepository,
			UserService userService) {
		super();
		this.csvFileReader = csvFileReader;
		this.speciesRepository = speciesRepository;
		this.lookupOptionRepository = lookupOptionRepository;
		this.userService = userService;
	}


	@Override
	public void onApplicationEvent(final ContextRefreshedEvent event) {
		
		try {
			List<BaseModel> speciesList = csvFileReader.readCsvFile(new Species());
			for (BaseModel species : speciesList) {
				Species s = (Species) species;
				s.setId(null);
				speciesRepository.save(s);
			}
			
			List<BaseModel> lookupOptionList = csvFileReader.readCsvFile(new LookupOption());
			for (BaseModel lookupOption : lookupOptionList) {
				LookupOption o = (LookupOption) lookupOption;
				o.setId(null);
				lookupOptionRepository.save(o);
			}
			
			List<BaseModel> userList = csvFileReader.readCsvFile(new User());
			for (BaseModel user : userList) {
				User u = (User) user;
				UserCreateForm uf = new UserCreateForm();
				uf.setEmail(u.getEmail());
				uf.setFullName(u.getFullName());
				uf.setPassword(u.getPasswordHash());
				uf.setPasswordRepeated(u.getPasswordHash());
				uf.setActive(true);
				uf.setRole(Role.ADMIN);
				userService.create(uf);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}