package github.jdrost1818.drill;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.metamodel.SingularAttribute;
import java.util.function.Function;

public class AttributeStringDrill<T> extends AttributeDrill<T, String> {

    AttributeStringDrill(SingularAttribute<T, String> attribute, Function<Specification<T>, Drill<T>> appendMethod) {
        super(attribute, appendMethod);
    }

    public Drill<T> equalsIgnoreCase(String val) {
        return super.append(
                (root, query, cb) -> cb.equal(cb.upper(root.get(this.attribute)), val.toUpperCase()));
    }
}
