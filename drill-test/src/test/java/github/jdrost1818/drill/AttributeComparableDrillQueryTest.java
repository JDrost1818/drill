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
public class AttributeComparableDrillQueryTest {

    @Autowired
    private PersonRepository personRepository;

    @Before
    public void setUp() {
        personRepository.deleteAll();
    }

    @Test
    public void testGreaterThan() {
        Person shouldBeFound = Person.builder().age(10L).build();
        Person shouldNotBeFound = Person.builder().age(0L).build();

        personRepository.saveAll(Arrays.asList(shouldBeFound, shouldNotBeFound));

        List<Person> people = personRepository.findAll(Drill.whereComparable(Person_.age).greaterThan(5L));

        assertThat(people, hasSize(1));
        assertThat(people.get(0).getAge(), equalTo(10L));
    }

    @Test
    public void testGreaterThan_not() {
        Person shouldBeFound = Person.builder().age(0L).build();
        Person shouldNotBeFound = Person.builder().age(10L).build();

        personRepository.saveAll(Arrays.asList(shouldBeFound, shouldNotBeFound));

        List<Person> people = personRepository.findAll(Drill.whereComparable(Person_.age).not().greaterThan(5L));

        assertThat(people, hasSize(1));
        assertThat(people.get(0).getAge(), equalTo(0L));
    }

    @Test
    public void testGreaterThanOrEqual() {
        Person shouldBeFound1 = Person.builder().age(5L).build();
        Person shouldBeFound2 = Person.builder().age(10L).build();
        Person shouldNotBeFound = Person.builder().age(0L).build();

        personRepository.saveAll(Arrays.asList(shouldBeFound1, shouldBeFound2, shouldNotBeFound));

        List<Person> people = personRepository.findAll(Drill.whereComparable(Person_.age).greaterThanOrEqualTo(5L));

        assertThat(people, hasSize(2));
        assertThat(people.get(0).getAge(), equalTo(5L));
        assertThat(people.get(1).getAge(), equalTo(10L));
    }

    @Test
    public void testGreaterThanOrEqual_not() {
        Person shouldNotBeFound1 = Person.builder().age(5L).build();
        Person shouldNotBeFound2 = Person.builder().age(10L).build();
        Person shouldBeFound = Person.builder().age(0L).build();

        personRepository.saveAll(Arrays.asList(shouldNotBeFound1, shouldNotBeFound2, shouldBeFound));

        List<Person> people = personRepository.findAll(Drill.whereComparable(Person_.age).not().greaterThanOrEqualTo(5L));

        assertThat(people, hasSize(1));
        assertThat(people.get(0).getAge(), equalTo(0L));
    }

}
