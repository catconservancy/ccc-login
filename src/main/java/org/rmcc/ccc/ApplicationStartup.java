package org.rmcc.ccc;

import org.rmcc.ccc.model.BaseModel;
import org.rmcc.ccc.model.Deployment;
import org.rmcc.ccc.model.Detection;
import org.rmcc.ccc.model.DetectionDetail;
import org.rmcc.ccc.model.LookupOption;
import org.rmcc.ccc.model.Photo;
import org.rmcc.ccc.model.Role;
import org.rmcc.ccc.model.Species;
import org.rmcc.ccc.model.StudyArea;
import org.rmcc.ccc.model.User;
import org.rmcc.ccc.model.UserCreateForm;
import org.rmcc.ccc.repository.DeploymentRepository;
import org.rmcc.ccc.repository.DetectionDetailRepository;
import org.rmcc.ccc.repository.DetectionRepository;
import org.rmcc.ccc.repository.LookupOptionRepository;
import org.rmcc.ccc.repository.PhotoRepository;
import org.rmcc.ccc.repository.SpeciesRepository;
import org.rmcc.ccc.repository.StudyAreaRepository;
import org.rmcc.ccc.service.user.UserService;
import org.rmcc.ccc.utils.CsvFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile({"development"})
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

    private CsvFileReader csvFileReader;
    private SpeciesRepository speciesRepository;
    private DetectionDetailRepository detectionDetailRepository;
    private StudyAreaRepository studyAreaRepository;
    private DeploymentRepository deploymentRepository;
    private PhotoRepository photoRepository;
    private DetectionRepository detectionRepository;
    private LookupOptionRepository lookupOptionRepository;
    private UserService userService;

    @Autowired
    public ApplicationStartup(CsvFileReader csvFileReader,
                              SpeciesRepository speciesRepository,
                              DetectionDetailRepository detectionDetailRepository,
                              StudyAreaRepository studyAreaRepository,
                              DeploymentRepository deploymentRepository,
                              PhotoRepository photoRepository,
                              DetectionRepository detectionRepository,
                              LookupOptionRepository lookupOptionRepository,
                              UserService userService) {
        super();
        this.csvFileReader = csvFileReader;
        this.detectionDetailRepository = detectionDetailRepository;
        this.speciesRepository = speciesRepository;
        this.studyAreaRepository = studyAreaRepository;
        this.deploymentRepository = deploymentRepository;
        this.photoRepository = photoRepository;
        this.detectionRepository = detectionRepository;
        this.lookupOptionRepository = lookupOptionRepository;
        this.userService = userService;
    }


    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {

        try {
            List<BaseModel> speciesList = csvFileReader.readCsvFile(new Species());
            for (BaseModel species : speciesList) {
                Species s = (Species) species;
                speciesRepository.save(s);
            }

            List<BaseModel> detectionDetailList = csvFileReader.readCsvFile(new DetectionDetail());
            for (BaseModel detectionDetail : detectionDetailList) {
                DetectionDetail dd = (DetectionDetail) detectionDetail;
                dd.setSpecies(dd.getSpecId() != null ? speciesRepository.findOne(dd.getSpecId()) : null);
                detectionDetailRepository.save(dd);
            }

            List<BaseModel> studyAreaList = csvFileReader.readCsvFile(new StudyArea());
            for (BaseModel studyArea : studyAreaList) {
                StudyArea s = (StudyArea) studyArea;
                studyAreaRepository.save(s);
            }

            List<BaseModel> deploymentList = csvFileReader.readCsvFile(new Deployment());
            for (BaseModel deployment : deploymentList) {
                Deployment d = (Deployment) deployment;
                d.setStudyArea(studyAreaRepository.findOne(Integer.valueOf(d.getStudyAreaId())));
                deploymentRepository.save(d);
            }

            List<BaseModel> photoList = csvFileReader.readCsvFile(new Photo());
            for (BaseModel photo : photoList) {
                Photo p = (Photo) photo;
                p.setDeployment(deploymentRepository.findOne(Integer.valueOf(p.getDeploymentId())));
                photoRepository.save(p);
            }

            List<BaseModel> detectionList = csvFileReader.readCsvFile(new Detection());
            for (BaseModel detection : detectionList) {
                Detection d = (Detection) detection;
                d.setSpecies(d.getSpeciesId() != null ? speciesRepository.findOne(d.getSpeciesId()) : null);
                d.setDetectionDetail(d.getDetectionDetailId() != null ? detectionDetailRepository.findOne(d.getDetectionDetailId()) : null);
                d.setPhoto(d.getPhotoId() != null ? photoRepository.findOne(d.getPhotoId()) : null);
                detectionRepository.save(d);
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