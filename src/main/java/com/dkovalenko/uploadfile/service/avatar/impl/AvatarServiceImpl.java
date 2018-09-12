package com.dkovalenko.uploadfile.service.avatar.impl;

import com.dkovalenko.uploadfile.controller.avatar.AvatarController;
import com.dkovalenko.uploadfile.dao.avatar.AvatarDAO;
import com.dkovalenko.uploadfile.dto.avatar.Avatar;
import com.dkovalenko.uploadfile.dto.property.StorageProperties;
import com.dkovalenko.uploadfile.exception.StorageException;
import com.dkovalenko.uploadfile.exception.StorageFileNotFoundException;
import com.dkovalenko.uploadfile.service.avatar.AvatarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AvatarServiceImpl implements AvatarService {

    private final AvatarDAO avatarDAO;
    private final Path rootLocation;

    @Autowired
    public AvatarServiceImpl(AvatarDAO avatarDAO, StorageProperties properties) {
        this.avatarDAO = avatarDAO;
        this.rootLocation = Paths.get(properties.getLocation());
    }


    @Override
    public List<Avatar> find() {

        return avatarDAO.find();
    }

    @Override
    public List<Avatar> find(long userID) {

        return avatarDAO.find(userID);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void store(long userID, MultipartFile file) {

        String filename = StringUtils.cleanPath(file.getOriginalFilename());

        try {

            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {

                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }

            try (InputStream inputStream = file.getInputStream()) {

                Files.copy(inputStream, this.rootLocation.resolve(filename),
                        StandardCopyOption.REPLACE_EXISTING);
                avatarDAO.save(filename, userID);
            }
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
    }


    private List<Path> loadAll(List<Avatar> avatars) {
        try {

            Stream<Path> result = Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize)
                    .filter(path -> avatars
                            .stream()
                            .anyMatch(avatar -> avatar.getAvatarName().equals(path.getFileName().toString())));

            return result.collect(Collectors.toList());
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    public Resource loadAsResource(String filename) {

        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);

            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    private Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public void delete() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }


}
