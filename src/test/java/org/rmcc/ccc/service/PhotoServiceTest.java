package org.rmcc.ccc.service;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rmcc.ccc.CccApplication;
import org.rmcc.ccc.model.Photo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CccApplication.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@DatabaseSetup("/photo-entries.xml")
public class PhotoServiceTest {

    @Autowired
    private PhotoService photoService;

    @Test
    public void testGetPhotos() throws Exception {
        Map<String,String> searchCriteria = new HashMap<>();
        searchCriteria.put("locationId", "5");
        searchCriteria.put("startDate", "2014-03-03 13:55:00");
        searchCriteria.put("endDate", "2017-03-03 13:55:00");
        Pageable pageable = new PageRequest(0,5);

        List<Photo> photos = photoService.getPhotos(searchCriteria,pageable);
        assertThat(photos).hasSize(4);
        assertTrue(photos.get(0).getId() == 1);
    }

    @Test
    public void testGetHighlightedPhotos() throws Exception {
        Map<String,String> searchCriteria = new HashMap<>();
        searchCriteria.put("highlighted", "true");
        searchCriteria.put("startDate", "2014-03-03 13:55:00");
        searchCriteria.put("endDate", "2017-03-03 13:55:00");
        Pageable pageable = new PageRequest(0,5);

        List<Photo> photos = photoService.getPhotos(searchCriteria,pageable);
        assertThat(photos).hasSize(1);
        assertTrue(photos.get(0).getId() == 3);
    }

    @Test
    public void testGetPhotosByStudyArea() throws Exception {
        Map<String,String> searchCriteria = new HashMap<>();
        searchCriteria.put("studyAreaId", "1");
        searchCriteria.put("startDate", "2014-03-03 13:55:00");
        searchCriteria.put("endDate", "2017-03-03 13:55:00");
        Pageable pageable = new PageRequest(0,5);

        List<Photo> photos = photoService.getPhotos(searchCriteria,pageable);
        assertThat(photos).hasSize(4);
        assertTrue(photos.get(0).getId() == 1);
    }

    @Test
    public void testGetPhotosByDateRange() throws Exception {
        Map<String,String> searchCriteria = new HashMap<>();
        searchCriteria.put("startDate", "2015-10-05 07:03:00");
        searchCriteria.put("endDate", "2015-10-05 07:06:00");
        Pageable pageable = new PageRequest(0,5);

        List<Photo> photos = photoService.getPhotos(searchCriteria,pageable);
        assertThat(photos).hasSize(2);
        assertTrue(photos.get(0).getId() == 1);
    }

    @Test
    public void testGetPhotosBySpecies() throws Exception {
        Map<String,String> searchCriteria = new HashMap<>();
        searchCriteria.put("speciesIds", "11,39");
        searchCriteria.put("startDate", "2014-03-03 13:55:00");
        searchCriteria.put("endDate", "2017-03-03 13:55:00");
        Pageable pageable = new PageRequest(0,5);

        List<Photo> photos = photoService.getPhotos(searchCriteria,pageable);
        assertThat(photos).hasSize(4);


        searchCriteria.put("speciesIds", "11,36,39");

        photos = photoService.getPhotos(searchCriteria,pageable);
        assertThat(photos).hasSize(4);
    }
}