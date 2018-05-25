package github.jdrost1818.drill;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

import static java.util.Objects.isNull;

public class Drill<T> implements Specification<T> {

    Specification<T> spec = null;

    private Drill() {

    }

    public static <T> Drill<T> where(Specification<T> specification) {
        return new Drill<T>().<T>and(specification);
    }

    public static <T, Y> AttributeDrill<T, Y> where(SingularAttribute<T, Y> attribute) {
        return new Drill<T>().and(attribute);
    }

    public static <T, Y extends Comparable<? super Y>> AttributeComparableDrill<T, Y> whereComparable(SingularAttribute<T, Y> attribute) {
        return new Drill<T>().andComparable(attribute);
    }

    public static <T> AttributeDateDrill<T> whereDate(SingularAttribute<T, Date> attribute) {
        return new Drill<T>().andDate(attribute);
    }

    public static <T> AttributeStringDrill<T> whereString(SingularAttribute<T, String> attribute) {
        return new Drill<T>().andString(attribute);
    }

    /**
     * Joins all specifications to the currently-built specification by way of "AND"
     *
     * @param specification specifications to join
     * @return the builder
     */
    public Drill<T> and(Specification<T> specification) {
        return this.safeAppend(specification, (safeSpec) -> this.spec.and(safeSpec));
    }

    public <Y> AttributeDrill<T, Y> and(SingularAttribute<T, Y> attribute) {
        return new AttributeDrill<>(attribute, this::and);
    }

    public <Y extends Comparable<? super Y>> AttributeComparableDrill<T, Y> andComparable(SingularAttribute<T, Y> attribute) {
        return new AttributeComparableDrill<>(attribute, this::and);
    }

    public AttributeDateDrill<T> andDate(SingularAttribute<T, Date> attribute) {
        return new AttributeDateDrill<>(attribute, this::and);
    }

    public AttributeStringDrill<T> andString(SingularAttribute<T, String> attribute) {
        return new AttributeStringDrill<>(attribute, this::and);
    }

    /**
     * Joins all specifications to the currently-built specification by way of "OR"
     *
     * @param specification specifications to join
     * @return the builder
     */
    public Drill<T> or(Specification<T> specification) {
        return this.safeAppend(specification, (safeSpec) -> this.spec.or(safeSpec));
    }

    public <Y> AttributeDrill<T, Y> or(SingularAttribute<T, Y> attribute) {
        return new AttributeDrill<>(attribute, this::or);
    }

    public <Y extends Comparable<? super Y>> AttributeComparableDrill<T, Y> orComparable(SingularAttribute<T, Y> attribute) {
        return new AttributeComparableDrill<>(attribute, this::or);
    }

    public AttributeDateDrill<T> orDate(SingularAttribute<T, Date> attribute) {
        return new AttributeDateDrill<>(attribute, this::or);
    }

    public AttributeStringDrill<T> orString(SingularAttribute<T, String> attribute) {
        return new AttributeStringDrill<>(attribute, this::or);
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        return isNull(this.spec)
                ? null
                : this.spec.toPredicate(root, query, builder);
    }

    private Drill<T> safeAppend(Specification<T> spec, Function<Specification<T>, Specification<T>> appendAction) {
        Optional.ofNullable(spec).ifPresent(safeSpec ->
                this.spec = isNull(this.spec)
                        ? Specification.where(safeSpec)
                        : appendAction.apply(safeSpec));

        return this;
    }
}
