package pg.lib.awsfiles.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pg.lib.awsfiles.entity.FileEntity;

import java.util.UUID;


/**
 * The interface File repository.
 */
public interface FileRepository extends JpaRepository<FileEntity, UUID>, JpaSpecificationExecutor<FileEntity> {
    /**
     * Exists by file name boolean.
     *
     * @param concatedFileName the concated file name
     * @return the boolean
     */
    boolean existsByFileName(String concatedFileName);

    /**
     * Gets by file name.
     *
     * @param concatedFileName the concated file name
     * @return the by file name
     */
    FileEntity getByFileName(String concatedFileName);
}
