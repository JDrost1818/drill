package github.jdrost1818.drill;

import github.jdrost1818.drilltest.DrillTestApplication;
import github.jdrost1818.drilltest.domain.Person;
import github.jdrost1818.drilltest.domain.Person_;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DrillTestApplication.class)
@ActiveProfiles("test")
public class DrillUnitTest {

    @Test
    public void testWhere_null_spec() {
        Drill<Person> personDrill = Drill.where((Specification<Person>) null);

        assertThat(personDrill.spec, nullValue());
    }

    @Test
    public void testAnd_nulls_filtered_out() {
        Drill<Person> personDrill = Drill
                .where((Specification<Person>) null)
                .and((Specification<Person>) null)
                .and(Person_.name).equalTo("Jake");

        assertThat(personDrill.spec, notNullValue());
    }

}
