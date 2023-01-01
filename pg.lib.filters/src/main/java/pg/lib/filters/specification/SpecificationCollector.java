package pg.lib.filters.specification;

import lombok.experimental.UtilityClass;

import org.springframework.data.jpa.domain.Specification;

import pg.lib.filters.common.JunctionType;

import java.util.Collections;
import java.util.List;

@UtilityClass
public class SpecificationCollector {

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