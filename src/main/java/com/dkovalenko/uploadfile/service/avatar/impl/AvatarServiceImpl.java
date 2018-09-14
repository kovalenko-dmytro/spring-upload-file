package com.dkovalenko.uploadfile.service.avatar.impl;

import com.dkovalenko.uploadfile.dao.avatar.AvatarDAO;
import com.dkovalenko.uploadfile.dao.user.UserDAO;
import com.dkovalenko.uploadfile.dto.avatar.Avatar;
import com.dkovalenko.uploadfile.dto.property.StorageProperties;
import com.dkovalenko.uploadfile.dto.user.User;
import com.dkovalenko.uploadfile.exception.StorageException;
import com.dkovalenko.uploadfile.exception.StorageFileNotFoundException;
import com.dkovalenko.uploadfile.service.avatar.AvatarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AvatarServiceImpl implements AvatarService {

    private final AvatarDAO avatarDAO;
    private final UserDAO userDAO;
    private final Path rootLocation;

    @Autowired
    public AvatarServiceImpl(AvatarDAO avatarDAO,
                             UserDAO userDAO,
                             StorageProperties properties) {

        this.avatarDAO = avatarDAO;
        this.userDAO = userDAO;
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
    public void store(MultipartFile file) {

        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        System.out.println(filename);

        try {

            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {

                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }

            filename = "def_" + String.valueOf(new Date().getTime()) + filename.substring(filename.indexOf('.'));

            try (InputStream inputStream = file.getInputStream()) {

                Files.copy(inputStream, this.rootLocation.resolve(filename),
                        StandardCopyOption.REPLACE_EXISTING);

                avatarDAO.save(filename, 0);

            }
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void store(long userID, MultipartFile file) {

        String userName = userDAO.find(userID).getFirstName();

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

            filename = userName + "_" + String.valueOf(new Date().getTime()) + filename.substring(filename.indexOf('.'));

            try (InputStream inputStream = file.getInputStream()) {

                Files.copy(inputStream, this.rootLocation.resolve(filename),
                        StandardCopyOption.REPLACE_EXISTING);

                avatarDAO.save(filename, userID);

                //take money for upload file

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(long avatarID) {

        Avatar avatar = avatarDAO.find()
                .stream()
                .filter(a -> a.getAvatarID() == avatarID)
                .findFirst()
                .orElse(null);

        try {

            if (avatar != null) {

                User user = userDAO.find()
                        .stream()
                        .filter(u -> u.getAvatar() != null && u.getAvatar().getAvatarID() == avatarID)
                        .findFirst()
                        .orElse(null);

                Files.delete(rootLocation.resolve(avatar.getAvatarName()));

                avatarDAO.delete(avatar.getAvatarID());

                if(user != null) {
                    userDAO.resetAvatar(user.getUserID());
                }
            }

        } catch (IOException e) {

            throw new StorageFileNotFoundException(
                    "Could not delete file with id: " + avatar.getAvatarID());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(long userID, long avatarID) {

        Avatar avatar = avatarDAO.find()
                .stream()
                .filter(a -> a.getUploadedByUserID() == userID && a.getAvatarID() == avatarID)
                .findFirst()
                .orElse(null);

        try {

            if (avatar != null && avatar.getUploadedByUserID() != 0) {

                User user = userDAO.find(userID);

                Files.delete(rootLocation.resolve(avatar.getAvatarName()));

                avatarDAO.delete(avatar.getAvatarID());

                if(user != null  && user.getAvatar() != null && user.getAvatar().getAvatarID() == avatarID) {
                    userDAO.resetAvatar(userID);
                }

            } else {
                throw new StorageException(
                        "Could not delete default file");
            }

        } catch (IOException e) {

            throw new StorageFileNotFoundException(
                    "Could not delete file with id: " + avatar.getAvatarID());
        }
    }

    @Override
    public void setAvatarCounterIncrement(long userID) {

        avatarDAO.setAvatarCounterIncrement(userID);
    }

    @Override
    public int getSetAvatarCounter(long userID) {

        return  avatarDAO.getSetAvatarCounter(userID);
    }
}
