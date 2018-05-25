package github.jdrost1818.drilltest.repository;

import github.jdrost1818.drilltest.domain.Person;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PersonRepository extends CrudRepository<Person, Long> {

    List<Person> findAll(Specification<Person> specification);

}
