package org.rmcc.ccc.jpa;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.mysema.query.types.expr.BooleanExpression;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rmcc.ccc.CccApplication;
import org.rmcc.ccc.model.Photo;
import org.rmcc.ccc.repository.PhotoPredicatesBuilder;
import org.rmcc.ccc.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CccApplication.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@DatabaseSetup("/photo-entries.xml")
public class PhotoRepositoryTest {

    @Autowired
    private PhotoRepository repository;

    @Test
    public void testFindByDeployment() throws Exception {
        List<Photo> photos = new ArrayList<Photo>();
        PhotoPredicatesBuilder builder = new PhotoPredicatesBuilder();

        builder.with("deployment.id", ":", 5);
        Sort sort = new Sort(Sort.Direction.ASC,"imageDate");
        Pageable pageable = new PageRequest(0,5,sort);

        BooleanExpression exp = builder.build();
        repository.findAll(exp, pageable).forEach(photos::add);
        assertThat(photos).hasSize(4);
        assertTrue(photos.get(0).getId() == 1);
    }

    @Test
    public void testFindByStudyArea() throws Exception {
        List<Photo> photos = new ArrayList<Photo>();
        PhotoPredicatesBuilder builder = new PhotoPredicatesBuilder();

        builder.with("deployment.studyArea.id", ":", 1);
        Sort sort = new Sort(Sort.Direction.ASC,"imageDate");
        Pageable pageable = new PageRequest(0,5,sort);

        BooleanExpression exp = builder.build();
        repository.findAll(exp, pageable).forEach(photos::add);
        assertThat(photos).hasSize(4);
        assertTrue(photos.get(0).getId() == 1);
    }

    @Test
    public void testFindByHighlight() throws Exception {
        List<Photo> photos = new ArrayList<Photo>();
        PhotoPredicatesBuilder builder = new PhotoPredicatesBuilder();

        builder.with("highlight", ":", true);
        Sort sort = new Sort(Sort.Direction.ASC,"imageDate");
        Pageable pageable = new PageRequest(0,5,sort);

        BooleanExpression exp = builder.build();
        repository.findAll(exp, pageable).forEach(photos::add);
        assertThat(photos).hasSize(1);
        assertTrue(photos.get(0).getId() == 3);
    }

    @Test
    public void testFindByDateRange() throws Exception {
        List<Photo> photos = new ArrayList<Photo>();
        PhotoPredicatesBuilder builder = new PhotoPredicatesBuilder();

        Timestamp startDate = Timestamp.valueOf("2015-10-05 07:03:00");
        Timestamp endDate = Timestamp.valueOf("2015-10-05 07:06:00");
        builder.with("imageDate", ">", startDate);
        builder.with("imageDate", "<", endDate);
        Sort sort = new Sort(Sort.Direction.ASC,"imageDate");
        Pageable pageable = new PageRequest(0,5,sort);

        BooleanExpression exp = builder.build();
        repository.findAll(exp, pageable).forEach(photos::add);
        assertThat(photos).hasSize(2);
        assertTrue(photos.get(0).getId() == 1);
    }

    @Test
    public void testFindBySpeciesIds() throws Exception {
        List<Photo> photos = new ArrayList<Photo>();
        PhotoPredicatesBuilder builder = new PhotoPredicatesBuilder();

        Integer[] speciesIds = new Integer[]{11,39};

        builder.with("species.id", "in", speciesIds);
        Sort sort = new Sort(Sort.Direction.ASC,"imageDate");
        Pageable pageable = new PageRequest(0,5,sort);

        BooleanExpression exp = builder.build();
        repository.findAll(exp, pageable).forEach(photos::add);
        assertThat(photos).hasSize(3);
    }
}
