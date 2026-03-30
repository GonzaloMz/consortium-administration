package com.consortium.admin.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StorageService {

    /**
     * Uploads a file and returns the resulting storage path (object key).
     *
     * @param file the multipart file to upload
     * @return the object key of the uploaded file in the storage backend
     * @throws IOException if the file cannot be read or uploaded
     */
    String upload(MultipartFile file) throws IOException;
}
