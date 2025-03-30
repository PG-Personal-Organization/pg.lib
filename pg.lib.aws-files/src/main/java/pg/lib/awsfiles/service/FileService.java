package pg.lib.awsfiles.service;

import org.springframework.web.multipart.MultipartFile;
import pg.lib.awsfiles.entity.FileEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The interface File service.
 */
public interface FileService {
    /**
     * Find by id optional.
     *
     * @param fileId the file id
     * @return the optional
     */
    Optional<FileEntity> findById(UUID fileId);

    /**
     * Init file uuid.
     *
     * @param file the file
     * @return the uuid
     */
    UUID initFile(MultipartFile file);

    /**
     * Upload file uuid.
     *
     * @param file the file
     * @return the uuid
     */
    UUID uploadFile(MultipartFile file);

    /**
     * Gets file url.
     *
     * @param fileId the file id
     * @return the file url
     */
    String getFileUrl(UUID fileId);

    /**
     * Gets file by id.
     *
     * @param fileId the file id
     * @return the file by id
     */
    FileEntity getFileById(UUID fileId);

    /**
     * Gets files urls.
     *
     * @param fileEntities the file entities
     * @return the files urls
     */
    List<String> getFilesUrls(List<FileEntity> fileEntities);

    /**
     * Delete file.
     *
     * @param fileId the file id
     */
    void deleteFile(UUID fileId);
}
