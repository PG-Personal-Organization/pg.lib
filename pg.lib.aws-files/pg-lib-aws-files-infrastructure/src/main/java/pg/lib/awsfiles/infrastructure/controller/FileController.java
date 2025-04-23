package pg.lib.awsfiles.infrastructure.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pg.lib.awsfiles.service.api.FileService;

import java.util.UUID;

/**
 * The type File controller.
 */
@RestController
@RequestMapping(path = "api/v1/files")
@AllArgsConstructor
@Tag(name = "Files")
public class FileController {
    private final FileService fileService;

    /**
     * Gets file url.
     *
     * @param fileId the file id
     * @return the file url
     */
    @GetMapping("{fileId}")
    public String getFileUrl(final @NonNull @PathVariable UUID fileId) {
        return fileService.getFileUrl(fileId);
    }

    /**
     * Upload file uuid.
     *
     * @param file the file
     * @return the uuid
     */
    @PostMapping(value = "/rest/upload", consumes = {
            "multipart/form-data"})
    public UUID uploadFile(final @NonNull @RequestParam("file") MultipartFile file) {
        return fileService.uploadFile(file);
    }

    /**
     * Delete file.
     *
     * @param fileId the file id
     */
    @DeleteMapping("{fileId}")
    public void deleteFile(final @NonNull @PathVariable UUID fileId) {
        fileService.deleteFile(fileId);
    }
}
