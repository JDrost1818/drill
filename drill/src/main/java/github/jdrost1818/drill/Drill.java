package github.jdrost1818.drill;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import javax.persistence.metamodel.SingularAttribute;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;

import static java.util.Objects.isNull;

public class Drill<T> {

    Specification<T> spec = null;

    private Drill() {

    }

    public static <T> Drill<T> where(Specification<T> specification) {
        return new Drill<T>().and(specification);
    }

    public static <T, Y> AttributeDrill<T, Y> where(SingularAttribute<T, Y> attribute) {
        return new Drill<T>().and(attribute);
    }

    /**
     * Joins all specifications to the currently-built specification by way of "AND"
     *
     * @param specifications specifications to join
     * @return the builder
     */
    public Drill<T> and(Specification<T>... specifications) {
        return this.safeAppend((safeSpec) -> this.spec.and(safeSpec), specifications);
    }

    public <Y> AttributeDrill<T, Y> and(SingularAttribute<T, Y> attribute) {
        return new AttributeDrill<>(attribute, this::and);
    }

    /**
     * Joins all specifications to the currently-built specification by way of "OR"
     *
     * @param specifications specifications to join
     * @return the builder
     */
    public Drill<T> or(Specification<T>... specifications) {
        return this.safeAppend((safeSpec) -> this.spec.or(safeSpec), specifications);
    }

    public <Y> AttributeDrill<T, Y> or(SingularAttribute<T, Y> attribute) {
        return new AttributeDrill<>(attribute, this::or);
    }

    @Nullable
    public Specification<T> build() {
        return this.spec;
    }

    private Drill<T> safeAppend(Function<Specification<T>, Specification<T>> appendAction, Specification<T>... specs) {
        Optional.of(specs)
                .filter(ArrayUtils::isEmpty)
                .map(Arrays::asList)
                .ifPresent(l -> l.forEach(spec -> this.safeAppend(spec, appendAction)));

        return this;
    }

    private Drill<T> safeAppend(Specification<T> spec, Function<Specification<T>, Specification<T>> appendAction) {
        Optional.ofNullable(spec).ifPresent(safeSpec ->
                this.spec = isNull(this.spec)
                        ? Specification.where(safeSpec)
                        : appendAction.apply(safeSpec));

        return this;
    }
}
