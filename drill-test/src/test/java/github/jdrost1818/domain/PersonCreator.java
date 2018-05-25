package github.jdrost1818.domain;

import github.jdrost1818.drilltest.domain.Person;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PersonCreator {

    private static final Random random = new Random();

    public static List<Person> create(int number) {
        return IntStream.range(0, number)
                .mapToObj(i -> PersonCreator.create())
                .collect(Collectors.toList());
    }

    public static Person create() {
        return Person.builder()
                .name("name " + random.nextInt())
                .dateOfBirth(new Date(random.nextLong()))
                .build();
    }

}
