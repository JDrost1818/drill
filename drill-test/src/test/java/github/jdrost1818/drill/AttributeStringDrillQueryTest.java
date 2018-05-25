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
import java.util.regex.Pattern;

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
        Person shouldBeFound1 = Person.builder().name("John Doe").build();
        Person shouldBeFound2 = Person.builder().name("JoHn DoE").build();
        Person shouldNotBeFound = Person.builder().name("something else entirely").build();

        personRepository.saveAll(Arrays.asList(shouldBeFound1, shouldBeFound2, shouldNotBeFound));

        List<Person> people = personRepository.findAll(Drill.whereString(Person_.name).equalToIgnoreCase("john doe"));

        assertThat(people, hasSize(2));
        assertThat(people.get(0).getName(), equalTo(shouldBeFound1.getName()));
        assertThat(people.get(1).getName(), equalTo(shouldBeFound2.getName()));
    }

    @Test
    public void testEqualToIgnoreCase_not() {
        Person shouldBeFound = Person.builder().name("something else entirely").build();
        Person shouldNotBeFound1 = Person.builder().name("JoHn DoE").build();
        Person shouldNotBeFound2 = Person.builder().name("John Doe").build();

        personRepository.saveAll(Arrays.asList(shouldBeFound, shouldNotBeFound1, shouldNotBeFound2));

        List<Person> people = personRepository.findAll(Drill.whereString(Person_.name).not().equalToIgnoreCase("john doe"));

        assertThat(people, hasSize(1));
        assertThat(people.get(0).getName(), equalTo(shouldBeFound.getName()));
    }

    @Test
    public void like() {
        Person shouldBeFound1 = Person.builder().name("John T Doe").build();
        Person shouldBeFound2 = Person.builder().name("Phohn Doel").build();
        Person shouldNotBeFound1 = Person.builder().name("Simple Simple").build();

        personRepository.saveAll(Arrays.asList(shouldBeFound1, shouldBeFound2, shouldNotBeFound1));

        List<Person> people = personRepository.findAll(Drill.whereString(Person_.name).like("%ohn%Do%"));

        assertThat(people, hasSize(2));
        assertThat(people.get(0).getName(), equalTo(shouldBeFound1.getName()));
        assertThat(people.get(1).getName(), equalTo(shouldBeFound2.getName()));
    }

    @Test
    public void like_not() {
        Person shouldBeFound = Person.builder().name("Simple Simple").build();
        Person shouldNotBeFound1 = Person.builder().name("Phohn Doel").build();
        Person shouldNotBeFound2 = Person.builder().name("John T Doe").build();

        personRepository.saveAll(Arrays.asList(shouldBeFound, shouldNotBeFound1, shouldNotBeFound2));

        List<Person> people = personRepository.findAll(Drill.whereString(Person_.name).not().like("%ohn%Do%"));

        assertThat(people, hasSize(1));
        assertThat(people.get(0).getName(), equalTo(shouldBeFound.getName()));
    }

}
