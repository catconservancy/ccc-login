package org.rmcc.ccc;

import org.rmcc.ccc.model.*;
import org.rmcc.ccc.repository.*;
import org.rmcc.ccc.service.user.UserService;
import org.rmcc.ccc.utils.CsvFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile({"dev"/*, "postgres"*/})
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

    private CsvFileReader csvFileReader;
    private SpeciesRepository speciesRepository;
    private StudyAreaRepository studyAreaRepository;
    private DeploymentRepository deploymentRepository;
    private PhotoRepository photoRepository;
    private LookupOptionRepository lookupOptionRepository;
    private UserService userService;

    @Autowired
    public ApplicationStartup(CsvFileReader csvFileReader,
                              SpeciesRepository speciesRepository,
                              StudyAreaRepository studyAreaRepository,
                              DeploymentRepository deploymentRepository,
                              PhotoRepository photoRepository,
                              LookupOptionRepository lookupOptionRepository,
                              UserService userService) {
        super();
        this.csvFileReader = csvFileReader;
        this.speciesRepository = speciesRepository;
        this.studyAreaRepository = studyAreaRepository;
        this.deploymentRepository = deploymentRepository;
        this.photoRepository = photoRepository;
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

            List<BaseModel> studyAreaList = csvFileReader.readCsvFile(new StudyArea());
            for (BaseModel studyArea : studyAreaList) {
                StudyArea s = (StudyArea) studyArea;
                s.setId(null);
                studyAreaRepository.save(s);
            }

            List<BaseModel> deploymentList = csvFileReader.readCsvFile(new Deployment());
            for (BaseModel deployment : deploymentList) {
                Deployment d = (Deployment) deployment;
                d.setId(null);
                d.setStudyArea(studyAreaRepository.findOne(Integer.valueOf(d.getStudyAreaId())));
                deploymentRepository.save(d);
            }

            List<BaseModel> photoList = csvFileReader.readCsvFile(new Photo());
            for (BaseModel photo : photoList) {
                Photo p = (Photo) photo;
                p.setId(null);
                p.setDeployment(deploymentRepository.findOne(Integer.valueOf(p.getDeploymentId())));
                photoRepository.save(p);
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