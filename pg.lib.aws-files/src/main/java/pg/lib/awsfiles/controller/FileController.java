package pg.lib.awsfiles.controller;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import pg.lib.awsfiles.service.FileService;

import java.util.UUID;

@RestController
@RequestMapping(path = "api/v1/files")
@AllArgsConstructor
@Tag(name = "Files")
public class FileController {
    private final FileService fileService;

    @GetMapping("{fileId}")
    public String getFileUrl(final @NonNull @PathVariable UUID fileId) {
        return fileService.getFileUrl(fileId);
    }

    @PostMapping(value = "/rest/upload", consumes = {
            "multipart/form-data"})
    public UUID uploadFile(final @NonNull @RequestParam("file") MultipartFile file) {
        return fileService.uploadFile(file);
    }

    @DeleteMapping("{fileId}")
    public void deleteFile(final @NonNull @PathVariable UUID fileId) {
        fileService.deleteFile(fileId);
    }
}
