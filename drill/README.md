# Drill
```
Specification
                .<Person>where((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.notEqual(root.get(Person_.id), 1))
                .<Person>and((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get(Person_.name)), "John".toUpperCase()))
                .<Person>and((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.lessThan(root.get(Person_.dateOfBirth), new Date()));
```

Goes To


```
Drill
    .where(Person_.id).not().equalTo(1)
    .andString(Person_.name).equalsIgnoreCase("John")
    .andDate(Person_.name).before(new Date())
```

Since `Drill` extends `Specification`, you can easily replace all uses of specifications with a `Drill` instance.
