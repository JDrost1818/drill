package github.jdrost1818.drill;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;
import java.util.Collection;
import java.util.function.Function;

/**
 * This is the most basic way to create queries for columns
 * in a database. Provides simple comparison operations
 * to give semantic understanding to {@link Specification}s.
 *
 * @param <T> Type of the model to be returned from the query
 * @param <Y> Type of the column on which the query will be applied
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class AttributeDrill<T, Y> implements Specification<T> {

    protected final SingularAttribute<T, Y> attribute;

    private final Function<Specification<T>, Drill<T>> appendMethod;

    private boolean isNegated;

    private Specification<T> query;

    /**
     * Negates the query. Can be stacked to negate a negation.
     * <p>
     * Usage:
     * <code>
     * something().not().equalTo(value)
     * something().not().not().equalTo(value) // not recommended
     * </code>
     *
     * @return itself for chaining
     */
    public AttributeDrill<T, Y> not() {
        this.isNegated = !this.isNegated;

        return this;
    }

    /**
     * Gives a better name for negating some queries, though
     * performs the same functionality as {@link AttributeDrill#not()}
     * <p>
     * Usage:
     * <code>
     * something().doesNot().contain(...)
     * </code>
     *
     * @return itself for chaining
     * @see AttributeDrill#not()
     */
    public AttributeDrill<T, Y> doesNot() {
        return this.not();
    }

    /**
     * Determines whether the column is equal to the given value.
     * <p>
     * This will not work with <code>null</code>.
     *
     * @param val value with which to check equality
     * @return the newly updated {@link Drill} for chaining
     * @see AttributeDrill#nullValue()
     */
    public <X extends Y> Drill<T> equalTo(X val) {
        return this.build(
                (root, query, cb) -> cb.equal(root.get(this.attribute), val));
    }

    /**
     * Determines whether the column is <code>null</code>
     *
     * @return the newly updated {@link Drill} for chaining
     */
    public Drill<T> nullValue() {
        return this.build(
                (root, query, cb) -> cb.isNull(root.get(this.attribute)));
    }

    /**
     * Determines whether the column is one of the many values
     * provided.
     *
     * @param collection a collection of valid values
     * @return the newly updated {@link Drill} for chaining
     */
    public <X extends Y> Drill<T> in(Collection<X> collection) {
        return this.build(
                (root, query, cb) -> root.get(attribute).in(collection));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        return this.properlyNegate(this.query.toPredicate(root, criteriaQuery, criteriaBuilder));
    }

    /**
     * Finishes the query by appending the chosen query
     * to the {@link Drill}.
     *
     * @param spec the {@link Specification} to add to the query
     * @return the newly updated {@link Drill} for chaining
     */
    protected Drill<T> build(Specification<T> spec) {
        this.query = spec;

        return this.appendMethod.apply(this);
    }

    private Predicate properlyNegate(Predicate predicate) {
        return this.isNegated ? predicate.not() : predicate;
    }
}
