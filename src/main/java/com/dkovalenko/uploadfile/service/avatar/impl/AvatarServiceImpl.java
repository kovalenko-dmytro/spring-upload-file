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
import org.springframework.util.FileSystemUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    public List<Avatar> find(long userID) {

        List<Avatar> avatars = avatarDAO.find(userID);

        Stream<Path> pathStream = loadAll(avatars);

        avatars = avatars.stream().peek(avatar -> avatar.setAvatarPath(pathStream.filter(path -> path.getFileName().toString().equals(avatar.getAvatarName())).findFirst().get())).collect(Collectors.toList());


        return avatars;
    }


    private Stream<Path> loadAll(List<Avatar> avatars) {
        try {

            Stream<Path> result = Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize)
                    .filter(path -> avatars
                            .stream()
                            .anyMatch(avatar -> avatar.getAvatarName().equals(path.getFileName().toString())));

            return result;
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

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}
