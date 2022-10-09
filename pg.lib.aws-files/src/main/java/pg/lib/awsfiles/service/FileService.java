package pg.lib.awsfiles.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.extern.slf4j.Slf4j;
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
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FileService implements IFileService {
    private static final Integer ONE_MB = 1_050_000;
    private static final String FILE_IS_TO_BIG = "File is to big";

    private final AmazonS3 s3client;
    private final AmazonConfig amazonConfig;
    private final FileRepository fileRepository;
    private final String awsUrl;

    public FileService(final AmazonS3 s3client,
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

    private static String notFound(UUID fileId) {
        return "File with id " + fileId + " doesn't exists in database";
    }

    public UUID uploadFile(MultipartFile file) {

        if (file.getSize() > ONE_MB) {
            throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE, FILE_IS_TO_BIG);
        }

        String extension = Objects.requireNonNull(file.getOriginalFilename()).split("\\.")[1];
        String fileName = UUID.randomUUID().toString();
        String concatedFileName = fileName + "." + extension;

        if (fileRepository.existsByFileName(concatedFileName))
            return fileRepository.getByFileName(concatedFileName).getFileId();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        try {
            s3client.putObject(amazonConfig.bucketName, concatedFileName, file.getInputStream(), metadata);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileEntity newFile = new FileEntity();
        newFile.setFileName(concatedFileName);

        fileRepository.save(newFile);

        return newFile.getFileId();
    }

    public String getFileUrl(UUID fileId) {

        FileEntity fileEntity = fileRepository.findById(fileId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, notFound(fileId))
        );

        return awsUrl + fileEntity.getFileName();

    }

    public List<String> getFilesUrls(List<FileEntity> fileEntities) {
        return fileEntities
                .stream()
                .map(fileEntity -> awsUrl + fileEntity.getFileName())
                .collect(Collectors.toList());

    }

    public void deleteFile(UUID fileId) {

        FileEntity fileEntity = getFileById(fileId);

        s3client.deleteObject(amazonConfig.bucketName, fileEntity.getFileName());

        fileRepository.deleteById(fileId);
    }

    public FileEntity getFileById(UUID fileId) {
        return fileRepository.findById(fileId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, notFound(fileId))
        );
    }
}
