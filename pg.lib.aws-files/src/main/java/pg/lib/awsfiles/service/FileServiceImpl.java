package pg.lib.awsfiles.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import pg.lib.awsfiles.config.AmazonConfig;
import pg.lib.awsfiles.entity.FileEntity;
import pg.lib.awsfiles.repository.FileRepository;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class FileServiceImpl implements FileService {
    private static final Integer ONE_MB = 1_050_000;
    private static final String FILE_IS_TO_BIG = "File is to big";

    private final AmazonS3 s3client;
    private final AmazonConfig amazonConfig;
    private final FileRepository fileRepository;
    private final String awsUrl;

    public FileServiceImpl(final AmazonS3 s3client,
                           final AmazonConfig amazonConfig,
                           final FileRepository fileRepository,
                           @Value("${aws.url}") String awsUrl) {
        this.s3client = s3client;
        this.amazonConfig = amazonConfig;
        this.fileRepository = fileRepository;
        this.awsUrl = awsUrl;

        if (awsUrl == null)
            log.error("Aws url property is not set");
    }

    private static String notFound(final UUID fileId) {
        return "File with id " + fileId + " doesn't exists in database";
    }

    private static void validateFile(MultipartFile file) {
        if (file.getSize() > ONE_MB) {
            throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE, FILE_IS_TO_BIG);
        }
    }

    public Optional<FileEntity> findById(final @NonNull UUID fileId) {
        return fileRepository.findById(fileId);
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
        String concatedFileName = fileName + "." + extension;

        if (fileRepository.existsByFileName(concatedFileName))
            return fileRepository.getByFileName(concatedFileName).getFileId();

        FileEntity newFile = saveNewFile(file, concatedFileName);

        return newFile.getFileId();
    }

    private FileEntity saveNewFile(final MultipartFile file, final String name) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        try {
            s3client.putObject(amazonConfig.bucketName, name, file.getInputStream(), metadata);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileEntity newFile = new FileEntity();
        newFile.setFileName(name);

        return fileRepository.save(newFile);
    }

    public String getFileUrl(final UUID fileId) {
        FileEntity fileEntity = fileRepository.findById(fileId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, notFound(fileId))
        );

        return awsUrl + fileEntity.getFileName();
    }

    public List<String> getFilesUrls(final List<FileEntity> fileEntities) {
        return fileEntities
                .stream()
                .map(fileEntity -> awsUrl + fileEntity.getFileName())
                .toList();

    }

    public void deleteFile(final UUID fileId) {
        FileEntity fileEntity = getFileById(fileId);

        s3client.deleteObject(amazonConfig.bucketName, fileEntity.getFileName());

        fileRepository.deleteById(fileId);
    }

    public FileEntity getFileById(final UUID fileId) {
        return fileRepository.findById(fileId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, notFound(fileId))
        );
    }
}