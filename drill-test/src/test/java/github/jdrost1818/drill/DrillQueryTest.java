package github.jdrost1818.drill;

import github.jdrost1818.drill.domain.PersonCreator;
import github.jdrost1818.drilltest.DrillTestApplication;
import github.jdrost1818.drilltest.domain.Person;
import github.jdrost1818.drilltest.domain.Person_;
import github.jdrost1818.drilltest.repository.PersonRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DrillTestApplication.class)
@ActiveProfiles("test")
public class DrillQueryTest {

    @Autowired
    private PersonRepository personRepository;

    @Before
    public void setUp() {
        personRepository.deleteAll();
    }

    @Test
    public void testEmptySpec() {
        Drill<Person> emptyDrill = Drill.where((Specification<Person>) null);
        List<Person> people = PersonCreator.create(10);
        personRepository.saveAll(people);

        assertThat(personRepository.findAll(emptyDrill), hasSize(people.size()));
    }

    @Test
    public void testSpecificationsGetCopiedCorrectly() {
        Person shouldBeFound = Person.builder().name("Jake").build();
        Person shouldNotBeFound = Person.builder().name("John").build();
        Drill<Person> drill = Drill.where(Specification.where((root, query, builder) -> builder.equal(root.get(Person_.name), "Jake")));

        personRepository.saveAll(Arrays.asList(shouldBeFound, shouldNotBeFound));

        List<Person> foundPerson = personRepository.findAll(drill);

        assertThat(foundPerson, hasSize(1));
        assertThat(foundPerson.get(0).getName(), equalTo("Jake"));
    }

    @Test
    public void testSpecificationsGetAddedCorrectly() {
        Date dateOfBirth = new Date(10000);
        Person shouldBeFound = Person.builder().name("Jake").dateOfBirth(dateOfBirth).build();
        Person shouldNotBeFound = Person.builder().name("Jake").id(2L).build();
        Drill<Person> drill = Drill
                .where(Specification.<Person>where((root, query, builder) -> builder.equal(root.get(Person_.name), shouldBeFound.getName())))
                .and(Specification.<Person>where((root, query, builder) -> builder.equal(root.get(Person_.dateOfBirth), shouldBeFound.getDateOfBirth())));

        personRepository.saveAll(Arrays.asList(shouldBeFound, shouldNotBeFound));

        List<Person> foundPerson = personRepository.findAll(drill);

        assertThat(foundPerson, hasSize(1));
        assertThat(foundPerson.get(0).getName(), equalTo(shouldBeFound.getName()));
        assertThat(foundPerson.get(0).getDateOfBirth().getTime(), equalTo(shouldBeFound.getDateOfBirth().getTime()));
    }

    @Test
    public void testSpecificationsGetOredCorrectly() {
        Person shouldBeFound1 = Person.builder().name("Jake").build();
        Person shouldBeFound2 = Person.builder().name("John").build();
        Person shouldNotBeFound = Person.builder().name("Jeff").build();
        Drill<Person> drill = Drill
                .where(Specification.<Person>where((root, query, builder) -> builder.equal(root.get(Person_.name), "Jake")))
                .or(Specification.<Person>where((root, query, builder) -> builder.equal(root.get(Person_.name), "John")));

        personRepository.saveAll(Arrays.asList(shouldBeFound1, shouldBeFound2, shouldNotBeFound));

        List<Person> foundPerson = personRepository.findAll(drill);

        assertThat(foundPerson, hasSize(2));
        assertThat(foundPerson.get(0).getName(), equalTo("Jake"));
        assertThat(foundPerson.get(1).getName(), equalTo("John"));
    }

}
