package com.dkovalenko.uploadfile.service.user.impl;

import com.dkovalenko.uploadfile.controller.avatar.AvatarController;
import com.dkovalenko.uploadfile.dao.user.UserDAO;
import com.dkovalenko.uploadfile.dto.user.User;
import com.dkovalenko.uploadfile.exception.StorageException;
import com.dkovalenko.uploadfile.service.avatar.AvatarService;
import com.dkovalenko.uploadfile.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;
    private final AvatarService avatarService;

    @Autowired
    public UserServiceImpl(UserDAO userDAO, AvatarService avatarService) {
        this.userDAO = userDAO;
        this.avatarService = avatarService;
    }

    @Override
    public List<User> find() {

        List<User> users = userDAO.find();

        users.forEach(user -> {

            if (user.getAvatar() != null) {

                try {

                    user.getAvatar().setAvatarUri(MvcUriComponentsBuilder.fromMethodName(AvatarController.class,
                            "serveFile", user.getAvatar().getAvatarName()).build().toString());

                } catch (StorageException e) {

                    e.getMessage();
                }
            }

        });

        return users;
    }

    @Override
    public User find(long userID) {

        User user = userDAO.find(userID);

        if (user.getAvatar() != null) {

            try {

                user.getAvatar().setAvatarUri(MvcUriComponentsBuilder.fromMethodName(AvatarController.class,
                        "serveFile", user.getAvatar().getAvatarName()).build().toString());


            } catch (StorageException e) {

                e.getMessage();
            }
        }

        return user;
    }

    @Override
    public void save(User user) {
        userDAO.save(user);
    }

    @Override
    public User update(long userID, User user) {

        userDAO.update(user.getFirstName(), user.getLastName(), userID);

        return user;
    }

    @Override
    public void delete(long userID) {
        userDAO.delete(userID);
    }

    @Override
    public void saveAvatar(long userID, long avatarID) {

        userDAO.saveAvatar(userID, avatarID);

        avatarService.setAvatarCounterIncrement(userID);

        int setAvatarCount = avatarService.getSetAvatarCounter(userID);

        if (setAvatarCount > 1) {

            //take money from user for second and more set avatar
        }
    }

    @Override
    public void resetAvatar(long userID) {

        userDAO.resetAvatar(userID);
    }
}
