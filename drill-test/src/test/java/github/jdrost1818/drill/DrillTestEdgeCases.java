package github.jdrost1818.drill;

import github.jdrost1818.domain.PersonCreator;
import github.jdrost1818.drilltest.DrillTestApplication;
import github.jdrost1818.drilltest.domain.Person;
import github.jdrost1818.drilltest.repository.PersonRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DrillTestApplication.class)
@ActiveProfiles("test")
public class DrillTestEdgeCases {

    @Autowired
    private PersonRepository personRepository;

    @Test
    public void testEmptySpec() {
        Drill<Person> emptyDrill = Drill.where((Specification<Person>) null);
        List<Person> people = PersonCreator.create(10);
        personRepository.saveAll(people);

        assertThat(personRepository.findAll(emptyDrill), hasSize(people.size()));
    }

}
