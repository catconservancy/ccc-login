//package org.rmcc.ccc.jpa;
//
//import org.junit.Before;
//import org.junit.runner.RunWith;
//import org.rmcc.ccc.model.Photo;
//import org.rmcc.ccc.repository.PhotoRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.transaction.TransactionConfiguration;
//import org.springframework.transaction.annotation.Transactional;
//
//@RunWith(SpringJUnit4ClassRunner.class)
////@ContextConfiguration(classes = { PersistenceConfig.class })
//@Transactional
//@TransactionConfiguration
//public class JPAQuerydslTest {
//
//    @Autowired
//    private PhotoRepository repo;
//
//    private Photo photoOne;
//    private Photo photoTwo;
//
//    @Before
//    public void init() {
//        photoOne = new Photo();
//        photoOne.setDeployment(deployment);
//        photoOne.setLastName("Doe");
//        photoOne.setEmail("john@doe.com");
//        photoOne.setAge(22);
//        repo.save(photoOne);
//
//        photoTwo = new Photo();
//        photoTwo.setFirstName("Tom");
//        photoTwo.setLastName("Doe");
//        photoTwo.setEmail("tom@doe.com");
//        photoTwo.setAge(26);
//        repo.save(photoTwo);
//    }
//}