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

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class AttributeDrill<T, Y> implements Specification<T> {

    protected final SingularAttribute<T, Y> attribute;

    private final Function<Specification<T>, Drill<T>> appendMethod;

    private boolean isNegated;

    private Specification<T> query;

    public AttributeDrill<T, Y> not() {
        this.isNegated = !this.isNegated;

        return this;
    }

    public <X extends Y> Drill<T> equalTo(X val) {
        return this.append(
                (root, query, cb) -> cb.equal(root.get(this.attribute), val));
    }

    public Drill<T> nullValue() {
        return this.append(
                (root, query, cb) -> cb.isNull(root.get(this.attribute)));
    }

    public <X extends Y> Drill<T> in(Collection<X> collection) {
        return this.append(
                (root, query, cb) -> root.get(attribute).in(collection));
    }

    Drill<T> append(Specification<T> spec) {
        this.query = spec;

        return this.appendMethod.apply(this);
    }

    private Predicate properlyNegate(Predicate predicate) {
        return this.isNegated ? predicate.not() : predicate;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        return this.properlyNegate(this.query.toPredicate(root, criteriaQuery, criteriaBuilder));
    }
}
