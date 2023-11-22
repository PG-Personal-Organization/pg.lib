package pg.lib.filters.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type Criteria.
 */
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Data
@Builder
public class Criteria implements Serializable {
    private String key;
    private Object value;
    private Operation operation;
}