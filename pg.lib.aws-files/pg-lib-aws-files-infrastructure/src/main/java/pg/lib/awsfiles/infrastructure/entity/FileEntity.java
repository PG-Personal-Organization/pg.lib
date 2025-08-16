package pg.lib.awsfiles.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

/**
 * The type File entity.
 */
@Entity
@Table(name = "file")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileEntity implements Serializable {
    @Id
    @GeneratedValue(
            strategy = GenerationType.UUID
    )
    @Column(
            nullable = false,
            updatable = false
    )
    private UUID fileId;
    private String fileName;
}
