package pg.lib.filters.specification;

import lombok.experimental.UtilityClass;

import org.springframework.data.jpa.domain.Specification;

import pg.lib.filters.common.JunctionType;

import java.util.Collections;
import java.util.List;

/**
 * The type Specification collector.
 */
@UtilityClass
public class SpecificationCollector {

    /**
     * Create specification specification.
     *
     * @param <T>                   the type parameter
     * @param specificationBuilders the specification builders
     * @return the specification
     */
    @SuppressWarnings("unchecked")
    public <T> Specification<T> createSpecification(List<SpecificationBuilder> specificationBuilders) {
        final Specification<T>[] finalSpecification = new Specification[1];

        specificationBuilders.forEach(builder -> {
            Specifications<T> specification = Specifications.<T>builder()
                    .searchCriteriaListAnd(builder.getFilterScheme().getOrDefault(JunctionType.AND, Collections.emptyList()))
                    .searchCriteriaListOr(builder.getFilterScheme().getOrDefault(JunctionType.OR, Collections.emptyList()))
                    .build();

            if (finalSpecification[0] != null)
                finalSpecification[0] = builder.getCombiner().equals(Combiner.AND) ? finalSpecification[0].and(specification)
                        : finalSpecification[0].or(specification);

            if (finalSpecification[0] == null)
                finalSpecification[0] = specification;

        });

        return finalSpecification[0];
    }
}