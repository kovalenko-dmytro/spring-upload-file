package com.dkovalenko.uploadfile.service.user.impl;

import com.dkovalenko.uploadfile.dao.user.UserDAO;
import com.dkovalenko.uploadfile.dto.user.User;
import com.dkovalenko.uploadfile.service.user.UserService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;

    @Autowired
    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public List<User> find() {

        List<User> users = userDAO.find();

        users = users.stream().peek(user -> {

            if (user.getAvatar() != null) {
                String base64Encoded = null;
                try {
                    byte[] encodeBase64 = Base64.encodeBase64(user.getAvatar().getFileData());
                    base64Encoded = new String(encodeBase64, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                user.setImage(base64Encoded);
            }

        }).collect(Collectors.toList());

        return users;
    }

    @Override
    public User find(long userID) {

        User user = userDAO.find(userID);

        if (user.getAvatar() != null) {

            byte[] encodeBase64 = Base64.encodeBase64(user.getAvatar().getFileData());
            String base64Encoded = null;
            try {
                base64Encoded = new String(encodeBase64, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            user.setImage(base64Encoded);
        }

        return user;
    }

    @Override
    public User save(User user) {
        return null;
    }

    @Override
    public User update(long userID, User user) {
        return null;
    }

    @Override
    public void delete(long userID) {

    }
}
