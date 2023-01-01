package pg.lib.filters.specification;

import lombok.AllArgsConstructor;
import lombok.Getter;

import pg.lib.filters.common.Criteria;
import pg.lib.filters.common.JunctionType;

import java.util.List;
import java.util.Map;

@AllArgsConstructor(staticName = "of")
@Getter
public class SpecificationBuilder {
    private Combiner combiner;
    private Map<JunctionType, List<Criteria>> filterScheme;
}