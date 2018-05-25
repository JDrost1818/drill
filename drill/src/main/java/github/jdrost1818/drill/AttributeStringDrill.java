package github.jdrost1818.drill;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.metamodel.SingularAttribute;
import java.util.function.Function;

public class AttributeStringDrill<T> extends AttributeComparableDrill<T, String> {

    AttributeStringDrill(SingularAttribute<T, String> attribute, Function<Specification<T>, Drill<T>> appendMethod) {
        super(attribute, appendMethod);
    }

    /**
     * Does an equality check while disregarding case
     *
     * @param str valid value
     * @return the builder for chaining
     */
    public Drill<T> equalsIgnoreCase(String str) {
        return super.build(
                (root, query, cb) -> cb.equal(cb.upper(root.get(this.attribute)), str.toUpperCase()));
    }

    /**
     * Looks for values matching the supplied pattern
     *
     * @param str valid value
     * @return the builder for chaining
     */
    public Drill<T> like(String str) {
        return super.build(
                (root, query, cb) -> cb.like(root.get(this.attribute), str));
    }

    /**
     * Looks for values matching the supplied pattern disregarding case
     *
     * @param str valid value
     * @return the builder for chaining
     */
    public Drill<T> likeIgnoreCase(String str) {
        return super.build(
                (root, query, cb) -> cb.like(cb.upper(root.get(this.attribute)), str.toUpperCase()));
    }

    /**
     * Looks for the string contained within the column
     *
     * @param str valid value
     * @return the builder for chaining
     */
    public Drill<T> contains(String str) {
        return this.like("%" + str + "%");
    }

    /**
     * Looks for the string contained within the column disregarding case
     *
     * @param str valid value
     * @return the builder for chaining
     */
    public Drill<T> containsIgnoreCase(String str) {
        return this.likeIgnoreCase("%" + str + "%");
    }

    /**
     * Looks for the string at the beginning of the value
     *
     * @param str valid value
     * @return the builder for chaining
     */
    public Drill<T> startsWith(String str) {
        return this.like(str + "%");
    }

    /**
     * Looks for the string at the beginning of the value disregarding case
     *
     * @param str valid value
     * @return the builder for chaining
     */
    public Drill<T> startsWithIgnoreCase(String str) {
        return this.likeIgnoreCase(str + "%");
    }

    /**
     * Looks for the string at the end of the value
     *
     * @param str valid value
     * @return the builder for chaining
     */
    public Drill<T> endsWith(String str) {
        return this.like("%" + str);
    }

    /**
     * Looks for the string at the end of the value disregarding case
     *
     * @param str valid value
     * @return the builder for chaining
     */
    public Drill<T> endsWithIgnoreCase(String str) {
        return this.likeIgnoreCase("%" + str);
    }

    @Override
    public AttributeStringDrill<T> not() {
        return (AttributeStringDrill<T>) super.not();
    }
}
