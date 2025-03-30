package pg.lib.filters.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import pg.lib.filters.common.Criteria;
import pg.lib.filters.exception.EmptySpecificationException;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Specifications.
 *
 * @param <T> the type parameter
 */
@AllArgsConstructor
@Builder
@Data
public class Specifications<T> implements Specification<T>, Cloneable {
    private final List<Criteria> searchCriteriaListAnd;
    private final List<Criteria> searchCriteriaListOr;

    /**
     * Instantiates a new Specifications.
     *
     * @param specifications the specifications
     */
    public Specifications(final Specifications<T> specifications) {
        this.searchCriteriaListAnd = new ArrayList<>(specifications.searchCriteriaListAnd);
        this.searchCriteriaListOr = new ArrayList<>(specifications.searchCriteriaListOr);
    }

    /**
     * And specifications.
     *
     * @param searchCriteria the search criteria
     * @return the specifications
     */
    public Specifications<T> and(final Criteria searchCriteria) {
        searchCriteriaListAnd.add(searchCriteria);
        return this;
    }

    /**
     * Or specifications.
     *
     * @param searchCriteria the search criteria
     * @return the specifications
     */
    public Specifications<T> or(final Criteria searchCriteria) {
        searchCriteriaListOr.add(searchCriteria);
        return this;
    }

    /**
     * Is empty boolean.
     *
     * @return the boolean
     */
    public boolean isEmpty() {
        return searchCriteriaListAnd.isEmpty() && searchCriteriaListOr.isEmpty();
    }

    @Override
    public Predicate toPredicate(final @NonNull Root<T> root, final @Nullable CriteriaQuery<?> query,
                                 final @NonNull CriteriaBuilder builder) {
        Predicate and = builder.and(searchCriteriaListAnd.stream()
                .map(criteria -> criteria.getOperation().getPredicate(root, criteria, builder))
                .toList()
                .toArray(new Predicate[0])
        );

        Predicate or = builder.or(searchCriteriaListOr.stream()
                .map(criteria -> criteria.getOperation().getPredicate(root, criteria, builder))
                .toList()
                .toArray(new Predicate[0])
        );

        if (isEmpty()) {
            throw new EmptySpecificationException();
        }

        //If both are not empty
        if (!isEmpty()) {
            return builder.and(and, or);
        }

        return searchCriteriaListAnd.isEmpty() ? or : and;

    }

    @Override
    public Specifications<T> clone() {
        return new Specifications<>(this);
    }
}