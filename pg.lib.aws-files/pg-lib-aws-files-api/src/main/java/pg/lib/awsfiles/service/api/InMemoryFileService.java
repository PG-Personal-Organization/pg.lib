package pg.lib.awsfiles.service.api;

import lombok.NonNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryFileService implements FileService {

    private final Map<UUID, StoredFile> filesStorage = new ConcurrentHashMap<>();

    @Override
    public Optional<FileView> findById(final UUID fileId) {
        return Optional.ofNullable(filesStorage.get(fileId))
                .map(file -> FileView.builder()
                        .fileId(file.id())
                        .fileName(file.fileName())
                        .build());
    }

    @Override
    public UUID initFile(final MultipartFile file) {
        return storeFile(file);
    }

    @Override
    public UUID uploadFile(final MultipartFile file) {
        return storeFile(file);
    }

    @Override
    public String getFileUrl(final UUID fileId) {
        if (!filesStorage.containsKey(fileId)) {
            throw new IllegalArgumentException("Plik o ID " + fileId + " nie istnieje");
        }
        return "memory://" + fileId.toString();
    }

    @Override
    public FileView getFileById(final UUID fileId) {
        return findById(fileId)
                .orElseThrow(() -> new IllegalArgumentException("Plik o ID " + fileId + " nie istnieje"));
    }

    @Override
    public List<String> getFilesUrls(final List<FileView> fileViews) {
        return fileViews.stream()
                .map(FileView::getFileId)
                .map(this::getFileUrl)
                .toList();
    }

    @Override
    public void deleteFile(final UUID fileId) {
        filesStorage.remove(fileId);
    }

    @Override
    public InputStream getFileStream(final UUID fileId) {
        return Optional.ofNullable(filesStorage.get(fileId))
                .map(StoredFile::content)
                .map(ByteArrayInputStream::new)
                .orElseThrow(() -> new IllegalArgumentException("Plik o ID " + fileId + " nie istnieje"));
    }

    @Override
    public Optional<InputStream> findFileStream(final UUID fileId) {
        return Optional.ofNullable(filesStorage.get(fileId))
                .map(StoredFile::content)
                .map(ByteArrayInputStream::new);
    }

    private UUID storeFile(final MultipartFile file) {
        try {
            UUID fileId = UUID.randomUUID();
            StoredFile storedFile = new StoredFile(
                    fileId,
                    file.getOriginalFilename(),
                    file.getBytes()
            );
            filesStorage.put(fileId, storedFile);
            return fileId;
        } catch (IOException e) {
            throw new RuntimeException("Błąd podczas zapisywania pliku", e);
        }
    }

    private record StoredFile(UUID id, String fileName, byte[] content) {

        @Override
        public boolean equals(final Object o) {
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            StoredFile that = (StoredFile) o;
            return Objects.equals(id, that.id) && Objects.deepEquals(content, that.content) && Objects.equals(fileName, that.fileName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, fileName, Arrays.hashCode(content));
        }

        @Override
        @NonNull
        public String toString() {
            return "StoredFile{" + "id=" + id + ", fileName='" + fileName + '\'' + ", content.length=" + content.length + '}';
        }
    }
}
