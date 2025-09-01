package pg.lib.awsfiles.infrastructure.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import pg.lib.awsfiles.infrastructure.s3.entity.FileEntity;
import pg.lib.awsfiles.infrastructure.s3.repository.FileRepository;
import pg.lib.awsfiles.service.api.AmazonConfig;
import pg.lib.awsfiles.service.api.FileService;
import pg.lib.awsfiles.service.api.FileView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * The type File service.
 */
@Service
@Log4j2
public class FileServiceImpl implements FileService {
    private static final Integer ONE_MB = 1_050_000;
    private static final String FILE_IS_TO_BIG = "File is to big";

    private final AmazonS3 s3client;
    private final AmazonConfig amazonConfig;
    private final FileRepository fileRepository;

    /**
     * Instantiates a new File service.
     *
     * @param s3client       the s 3 client
     * @param amazonConfig   the amazon config
     * @param fileRepository the file repository
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public FileServiceImpl(final AmazonS3 s3client,
                           final AmazonConfig amazonConfig,
                           final FileRepository fileRepository) {
        this.s3client = s3client;
        this.amazonConfig = amazonConfig;
        this.fileRepository = fileRepository;

        if (amazonConfig.getAwsUrl() == null) {
            log.error("Aws url property is not set");
        }
    }

    private static String notFound(final UUID fileId) {
        return "File with id " + fileId + " doesn't exists in database";
    }

    private static void validateFile(final MultipartFile file) {
        if (file.getSize() > ONE_MB) {
            throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE, FILE_IS_TO_BIG);
        }
    }

    public Optional<FileView> findById(final @NonNull UUID fileId) {
        return fileRepository.findById(fileId).map(this::toFileView);
    }

    public UUID initFile(final @NonNull MultipartFile file) {
        validateFile(file);

        String fileName = file.getName();

        if (!fileRepository.existsByFileName(fileName)) {
            FileEntity fileEntity = saveNewFile(file, fileName);
            return fileEntity.getFileId();
        }
        return fileRepository.getByFileName(fileName).getFileId();
    }

    public UUID uploadFile(final MultipartFile file) {
        validateFile(file);

        String extension = Objects.requireNonNull(file.getOriginalFilename()).split("\\.")[1];
        String fileName = UUID.randomUUID().toString();
        String concatFileName = fileName + "." + extension;

        if (fileRepository.existsByFileName(concatFileName)) {
            return fileRepository.getByFileName(concatFileName).getFileId();
        }

        FileEntity newFile = saveNewFile(file, concatFileName);

        return newFile.getFileId();
    }

    private FileEntity saveNewFile(final MultipartFile file, final String name) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        try {
            s3client.putObject(amazonConfig.getBucketName(), name, file.getInputStream(), metadata);
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
        }

        FileEntity newFile = new FileEntity();
        newFile.setFileName(name);

        return fileRepository.save(newFile);
    }

    public String getFileUrl(final UUID fileId) {
        FileEntity fileEntity = fileRepository.findById(fileId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, notFound(fileId))
        );

        return amazonConfig.getAwsUrl() + fileEntity.getFileName();
    }

    public List<String> getFilesUrls(final List<FileView> fileViews) {
        return fileViews
                .stream()
                .map(fileEntity -> amazonConfig.getAwsUrl() + fileEntity.getFileName())
                .toList();

    }

    public void deleteFile(final UUID fileId) {
        FileView fileEntity = getFileById(fileId);

        s3client.deleteObject(amazonConfig.getBucketName(), fileEntity.getFileName());

        fileRepository.deleteById(fileId);
    }

    @Override
    public InputStream getFileStream(final UUID fileId) {
        var file = getFileById(fileId);
        return s3client.getObject(amazonConfig.getBucketName(), file.getFileName()).getObjectContent();
    }

    @Override
    public Optional<InputStream> findFileStream(final UUID fileId) {
        var file = findById(fileId);
        return file.map(f -> s3client.getObject(amazonConfig.getBucketName(), f.getFileName()).getObjectContent());
    }

    public FileView getFileById(final UUID fileId) {
        FileEntity file = fileRepository.findById(fileId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, notFound(fileId))
        );
        return toFileView(file);
    }

    private FileView toFileView(final @NonNull FileEntity fileEntity) {
        return FileView.builder()
                .fileId(fileEntity.getFileId())
                .fileName(fileEntity.getFileName())
                .build();
    }
}
