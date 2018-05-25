package github.jdrost1818.drill;

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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DrillTestApplication.class)
@ActiveProfiles("test")
public class AttributeDrillQueryTest {

    @Autowired
    private PersonRepository personRepository;

    @Before
    public void setUp() {
        personRepository.deleteAll();
    }

    @Test
    public void testEqualTo() {
        Person shouldBeFound = Person.builder().name("should be found").build();
        Person shouldNotBeFound = Person.builder().name("should not be found").build();

        personRepository.saveAll(Arrays.asList(shouldBeFound, shouldNotBeFound));

        List<Person> people = personRepository.findAll(Drill.where(Person_.name).equalTo(shouldBeFound.getName()));

        assertThat(people, hasSize(1));
        assertThat(people.get(0).getName(), equalTo(shouldBeFound.getName()));
    }

    @Test
    public void testEqualTo_not() {
        Person shouldBeFound = Person.builder().name("should be found").build();
        Person shouldNotBeFound = Person.builder().name("should not be found").build();

        personRepository.saveAll(Arrays.asList(shouldBeFound, shouldNotBeFound));

        List<Person> people = personRepository.findAll(Drill.where(Person_.name).not().equalTo(shouldNotBeFound.getName()));

        assertThat(people, hasSize(1));
        assertThat(people.get(0).getName(), equalTo(shouldBeFound.getName()));
    }

    @Test
    public void testNullValue() {
        Person shouldBeFound = Person.builder().name(null).build();
        Person shouldNotBeFound = Person.builder().name("should not be found").build();

        personRepository.saveAll(Arrays.asList(shouldBeFound, shouldNotBeFound));

        List<Person> people = personRepository.findAll(Drill.where(Person_.name).nullValue());

        assertThat(people, hasSize(1));
        assertThat(people.get(0).getName(), nullValue());
    }

    @Test
    public void testNullValue_not() {
        Person shouldBeFound = Person.builder().name("should be found").build();
        Person shouldNotBeFound = Person.builder().name(null).build();

        personRepository.saveAll(Arrays.asList(shouldBeFound, shouldNotBeFound));

        List<Person> people = personRepository.findAll(Drill.where(Person_.name).not().nullValue());

        assertThat(people, hasSize(1));
        assertThat(people.get(0).getName(), equalTo(shouldBeFound.getName()));
    }

    @Test
    public void testIn() {
        Person shouldBeFound1 = Person.builder().name("name1").build();
        Person shouldBeFound2 = Person.builder().name("name2").build();
        Person shouldBeFound3 = Person.builder().name("name3").build();
        Person shouldNotBeFound = Person.builder().name("should not be found").build();

        personRepository.saveAll(Arrays.asList(shouldBeFound1, shouldBeFound2, shouldBeFound3, shouldNotBeFound));

        List<String> validNames = Arrays.asList("name1", "name2", "name3");
        List<Person> people = personRepository.findAll(Drill.where(Person_.name).in(validNames));

        assertThat(people, hasSize(3));
        assertTrue(people.stream().map(Person::getName).allMatch(validNames::contains));
    }

    @Test
    public void testIn_not() {
        Person shouldNotBeFound1 = Person.builder().name("name1").build();
        Person shouldNotBeFound2 = Person.builder().name("name2").build();
        Person shouldNotBeFound3 = Person.builder().name("name3").build();
        Person shouldBeFound = Person.builder().name("should be found").build();

        personRepository.saveAll(Arrays.asList(shouldNotBeFound1, shouldNotBeFound2, shouldNotBeFound3, shouldBeFound));

        List<String> invalidNames = Arrays.asList("name1", "name2", "name3");
        List<Person> people = personRepository.findAll(Drill.where(Person_.name).not().in(invalidNames));

        assertThat(people, hasSize(1));
        assertThat(people.get(0).getName(), equalTo(shouldBeFound.getName()));
    }

}
