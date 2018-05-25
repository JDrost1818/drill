package github.jdrost1818.drill;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.metamodel.SingularAttribute;
import java.util.function.Function;

public class AttributeComparableDrill<T, Y extends Comparable<? super Y>> extends AttributeDrill<T, Y> {

    AttributeComparableDrill(SingularAttribute<T, Y> attribute, Function<Specification<T>, Drill<T>> appendMethod) {
        super(attribute, appendMethod);
    }

    public <X extends Y> Drill<T> greaterThan(X val) {
        return super.build(
                (root, query, cb) -> cb.greaterThan(root.get(this.attribute), val));
    }

    public <X extends Y> Drill<T> greaterThanOrEqualTo(X val) {
        return super.build(
                (root, query, cb) -> cb.greaterThanOrEqualTo(root.get(this.attribute), val));
    }

    public <X extends Y> Drill<T> lessThan(X val) {
        return super.build(
                (root, query, cb) -> cb.lessThan(root.get(this.attribute), val));
    }

    public <X extends Y> Drill<T> lessThanOrEqualTo(X val) {
        return super.build(
                (root, query, cb) -> cb.lessThanOrEqualTo(root.get(this.attribute), val));
    }

}
