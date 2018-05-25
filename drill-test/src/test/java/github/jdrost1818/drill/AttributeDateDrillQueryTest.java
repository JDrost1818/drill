package github.jdrost1818.drill;

import github.jdrost1818.drilltest.DrillTestApplication;
import github.jdrost1818.drilltest.domain.Person;
import github.jdrost1818.drilltest.domain.Person_;
import github.jdrost1818.drilltest.repository.PersonRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
public class AttributeDateDrillQueryTest {

    @Autowired
    private PersonRepository personRepository;

    @Before
    public void setUp() {
        personRepository.deleteAll();
    }

    @Test
    public void testBefore() {
        Person shouldBeFound = Person.builder().dateOfBirth(new Date(100L)).build();
        Person shouldNotBeFound1 = Person.builder().dateOfBirth(new Date(101L)).build();
        Person shouldNotBeFound2 = Person.builder().dateOfBirth(new Date(102L)).build();

        personRepository.saveAll(Arrays.asList(shouldBeFound, shouldNotBeFound1, shouldNotBeFound2));

        List<Person> people = personRepository.findAll(Drill.whereDate(Person_.dateOfBirth).before(new Date(101L)));

        assertThat(people, hasSize(1));
        assertThat(people.get(0).getDateOfBirth().getTime(), equalTo(shouldBeFound.getDateOfBirth().getTime()));
    }

    @Test
    public void testBefore_not() {
        Person shouldBeFound1 = Person.builder().dateOfBirth(new Date(100L)).build();
        Person shouldBeFound2 = Person.builder().dateOfBirth(new Date(101L)).build();
        Person shouldNotBeFound = Person.builder().dateOfBirth(new Date(99L)).build();

        personRepository.saveAll(Arrays.asList(shouldBeFound1, shouldBeFound2, shouldNotBeFound));

        List<Person> people = personRepository.findAll(Drill.whereDate(Person_.dateOfBirth).not().before(new Date(100L)));

        assertThat(people, hasSize(2));
        assertThat(people.get(0).getDateOfBirth().getTime(), equalTo(shouldBeFound1.getDateOfBirth().getTime()));
        assertThat(people.get(1).getDateOfBirth().getTime(), equalTo(shouldBeFound2.getDateOfBirth().getTime()));
    }

    @Test
    public void testOnOrBefore() {
        Person shouldBeFound1 = Person.builder().dateOfBirth(new Date(100L)).build();
        Person shouldBeFound2 = Person.builder().dateOfBirth(new Date(101L)).build();
        Person shouldNotBeFound = Person.builder().dateOfBirth(new Date(102L)).build();

        personRepository.saveAll(Arrays.asList(shouldBeFound1, shouldBeFound2, shouldNotBeFound));

        List<Person> people = personRepository.findAll(Drill.whereDate(Person_.dateOfBirth).onOrBefore(new Date(101L)));

        assertThat(people, hasSize(2));
        assertThat(people.get(0).getDateOfBirth().getTime(), equalTo(shouldBeFound1.getDateOfBirth().getTime()));
        assertThat(people.get(1).getDateOfBirth().getTime(), equalTo(shouldBeFound2.getDateOfBirth().getTime()));
    }

    @Test
    public void testOnOrBefore_not() {
        Person shouldBeFound = Person.builder().dateOfBirth(new Date(101L)).build();
        Person shouldNotBeFound1 = Person.builder().dateOfBirth(new Date(100L)).build();
        Person shouldNotBeFound2 = Person.builder().dateOfBirth(new Date(99L)).build();

        personRepository.saveAll(Arrays.asList(shouldBeFound, shouldNotBeFound1, shouldNotBeFound2));

        List<Person> people = personRepository.findAll(Drill.whereDate(Person_.dateOfBirth).not().onOrBefore(new Date(100L)));

        assertThat(people, hasSize(1));
        assertThat(people.get(0).getDateOfBirth().getTime(), equalTo(shouldBeFound.getDateOfBirth().getTime()));
    }

    @Test
    public void testAfter() {
        Person shouldBeFound = Person.builder().dateOfBirth(new Date(100L)).build();
        Person shouldNotBeFound1 = Person.builder().dateOfBirth(new Date(99L)).build();
        Person shouldNotBeFound2 = Person.builder().dateOfBirth(new Date(98L)).build();

        personRepository.saveAll(Arrays.asList(shouldBeFound, shouldNotBeFound1, shouldNotBeFound2));

        List<Person> people = personRepository.findAll(Drill.whereDate(Person_.dateOfBirth).after(new Date(99L)));

        assertThat(people, hasSize(1));
        assertThat(people.get(0).getDateOfBirth().getTime(), equalTo(shouldBeFound.getDateOfBirth().getTime()));
    }

    @Test
    public void testAfter_not() {
        Person shouldBeFound1 = Person.builder().dateOfBirth(new Date(100L)).build();
        Person shouldBeFound2 = Person.builder().dateOfBirth(new Date(99L)).build();
        Person shouldNotBeFound = Person.builder().dateOfBirth(new Date(101L)).build();

        personRepository.saveAll(Arrays.asList(shouldBeFound1, shouldBeFound2, shouldNotBeFound));

        List<Person> people = personRepository.findAll(Drill.whereDate(Person_.dateOfBirth).not().after(new Date(100L)));

        assertThat(people, hasSize(2));
        assertThat(people.get(0).getDateOfBirth().getTime(), equalTo(shouldBeFound1.getDateOfBirth().getTime()));
        assertThat(people.get(1).getDateOfBirth().getTime(), equalTo(shouldBeFound2.getDateOfBirth().getTime()));
    }

    @Test
    public void testOnOrAfter() {
        Person shouldBeFound1 = Person.builder().dateOfBirth(new Date(102L)).build();
        Person shouldBeFound2 = Person.builder().dateOfBirth(new Date(101L)).build();
        Person shouldNotBeFound = Person.builder().dateOfBirth(new Date(100L)).build();

        personRepository.saveAll(Arrays.asList(shouldBeFound1, shouldBeFound2, shouldNotBeFound));

        List<Person> people = personRepository.findAll(Drill.whereDate(Person_.dateOfBirth).onOrAfter(new Date(101L)));

        assertThat(people, hasSize(2));
        assertThat(people.get(0).getDateOfBirth().getTime(), equalTo(shouldBeFound1.getDateOfBirth().getTime()));
        assertThat(people.get(1).getDateOfBirth().getTime(), equalTo(shouldBeFound2.getDateOfBirth().getTime()));
    }

    @Test
    public void testOnOrAfter_not() {
        Person shouldBeFound = Person.builder().dateOfBirth(new Date(99L)).build();
        Person shouldNotBeFound1 = Person.builder().dateOfBirth(new Date(100L)).build();
        Person shouldNotBeFound2 = Person.builder().dateOfBirth(new Date(101L)).build();

        personRepository.saveAll(Arrays.asList(shouldBeFound, shouldNotBeFound1, shouldNotBeFound2));

        List<Person> people = personRepository.findAll(Drill.whereDate(Person_.dateOfBirth).not().onOrAfter(new Date(100L)));

        assertThat(people, hasSize(1));
        assertThat(people.get(0).getDateOfBirth().getTime(), equalTo(shouldBeFound.getDateOfBirth().getTime()));
    }
}
