package pg.lib.awsfiles.service.api;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
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
    Optional<FileView> findById(UUID fileId);

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
    FileView getFileById(UUID fileId);

    /**
     * Gets files urls.
     *
     * @param fileViews the file views
     * @return the files urls
     */
    List<String> getFilesUrls(List<FileView> fileViews);

    /**
     * Delete file.
     *
     * @param fileId the file id
     */
    void deleteFile(UUID fileId);

    InputStream getFileStream(UUID fileId);

    Optional<InputStream> findFileStream(UUID fileId);
}
