package pg.lib.awsfiles.infrastructure.s3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pg.lib.awsfiles.infrastructure.s3.entity.FileEntity;

import java.util.UUID;


/**
 * The interface File repository.
 */
public interface FileRepository extends JpaRepository<FileEntity, UUID>, JpaSpecificationExecutor<FileEntity> {
    /**
     * Exists by file name boolean.
     *
     * @param concatenatedFileName the concated file name
     * @return the boolean
     */
    @Query("SELECT COUNT(f) > 0 FROM FileEntity f WHERE f.fileName = :concatenatedFileName")
    boolean existsByFileName(@Param("concatenatedFileName") String concatenatedFileName);

    /**
     * Gets by file name.
     *
     * @param concatenatedFileName the concated file name
     * @return the by file name
     */
    @Query("SELECT f FROM FileEntity f WHERE f.fileName = :concatenatedFileName")
    FileEntity getByFileName(@Param("concatenatedFileName") String concatenatedFileName);
}
