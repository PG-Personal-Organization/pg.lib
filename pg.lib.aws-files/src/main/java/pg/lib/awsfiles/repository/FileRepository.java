package pg.lib.awsfiles.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pg.lib.awsfiles.entity.FileEntity;

import java.util.UUID;


public interface FileRepository extends JpaRepository<FileEntity, UUID>, JpaSpecificationExecutor<FileEntity> {
    boolean existsByFileName(String concatedFileName);

    FileEntity getByFileName(String concatedFileName);
}
