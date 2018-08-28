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

            users.forEach(user -> {

                byte[] encodeBase64 = user.getAvatar() != null
                        ? Base64.encodeBase64(user.getAvatar().getFileData())
                        : Base64.encodeBase64(new byte[0]);

                String base64Encoded = null;
                try {
                    base64Encoded = new String(
                            encodeBase64 != null ? encodeBase64 : new byte[0],
                            "UTF-8"
                    );
                    user.setImage(base64Encoded);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            });

        return users;
    }

    @Override
    public User find(long userID) {

        User user = userDAO.find(userID);

        byte[] encodeBase64 = user.getAvatar() != null
                ? Base64.encodeBase64(user.getAvatar().getFileData())
                : Base64.encodeBase64(new byte[0]);

        String base64Encoded = null;
        try {
            base64Encoded = new String(
                    encodeBase64 != null ? encodeBase64 : new byte[0],
                    "UTF-8");
            user.setImage(base64Encoded);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
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
