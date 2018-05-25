package github.jdrost1818.drill;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.metamodel.SingularAttribute;
import java.util.Date;
import java.util.function.Function;

public class AttributeDateDrill<T> extends AttributeComparableDrill<T, Date> {

    AttributeDateDrill(SingularAttribute<T, Date> attribute, Function<Specification<T>, Drill<T>> appendMethod) {
        super(attribute, appendMethod);
    }

    public Drill<T> before(Date date) {
        return super.lessThan(date);
    }

    public Drill<T> onOrBefore(Date date) {
        return super.lessThanOrEqualTo(date);
    }

    public Drill<T> after(Date date) {
        return super.greaterThan(date);
    }

    public Drill<T> onOrAfter(Date date) {
        return super.greaterThanOrEqualTo(date);
    }

}
