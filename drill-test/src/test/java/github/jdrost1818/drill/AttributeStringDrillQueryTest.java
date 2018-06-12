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
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DrillTestApplication.class)
@ActiveProfiles("test")
public class AttributeStringDrillQueryTest {

    @Autowired
    private PersonRepository personRepository;

    @Before
    public void setUp() {
        personRepository.deleteAll();
    }

    @Test
    public void testEqualToIgnoreCase() {
        Person shouldBeFound1 = Person.builder().firstName("John Doe").build();
        Person shouldBeFound2 = Person.builder().firstName("JoHn DoE").build();
        Person shouldNotBeFound = Person.builder().firstName("something else entirely").build();

        personRepository.saveAll(Arrays.asList(shouldBeFound1, shouldBeFound2, shouldNotBeFound));

        List<Person> people = personRepository.findAll(Drill.whereString(Person_.firstName).equalToIgnoreCase("john doe"));

        assertThat(people, hasSize(2));
        assertThat(people.get(0).getFirstName(), equalTo(shouldBeFound1.getFirstName()));
        assertThat(people.get(1).getFirstName(), equalTo(shouldBeFound2.getFirstName()));
    }

    @Test
    public void testEqualToIgnoreCase_not() {
        Person shouldBeFound = Person.builder().firstName("something else entirely").build();
        Person shouldNotBeFound1 = Person.builder().firstName("JoHn DoE").build();
        Person shouldNotBeFound2 = Person.builder().firstName("John Doe").build();

        personRepository.saveAll(Arrays.asList(shouldBeFound, shouldNotBeFound1, shouldNotBeFound2));

        List<Person> people = personRepository.findAll(Drill.whereString(Person_.firstName).not().equalToIgnoreCase("john doe"));

        assertThat(people, hasSize(1));
        assertThat(people.get(0).getFirstName(), equalTo(shouldBeFound.getFirstName()));
    }

    @Test
    public void like() {
        Person shouldBeFound1 = Person.builder().firstName("John T Doe").build();
        Person shouldBeFound2 = Person.builder().firstName("Phohn Doel").build();
        Person shouldNotBeFound1 = Person.builder().firstName("Simple Simple").build();

        personRepository.saveAll(Arrays.asList(shouldBeFound1, shouldBeFound2, shouldNotBeFound1));

        List<Person> people = personRepository.findAll(Drill.whereString(Person_.firstName).like("%ohn%Do%"));

        assertThat(people, hasSize(2));
        assertThat(people.get(0).getFirstName(), equalTo(shouldBeFound1.getFirstName()));
        assertThat(people.get(1).getFirstName(), equalTo(shouldBeFound2.getFirstName()));
    }

    @Test
    public void like_not() {
        Person shouldBeFound = Person.builder().firstName("Simple Simple").build();
        Person shouldNotBeFound1 = Person.builder().firstName("Phohn Doel").build();
        Person shouldNotBeFound2 = Person.builder().firstName("John T Doe").build();

        personRepository.saveAll(Arrays.asList(shouldBeFound, shouldNotBeFound1, shouldNotBeFound2));

        List<Person> people = personRepository.findAll(Drill.whereString(Person_.firstName).not().like("%ohn%Do%"));

        assertThat(people, hasSize(1));
        assertThat(people.get(0).getFirstName(), equalTo(shouldBeFound.getFirstName()));
    }

}
