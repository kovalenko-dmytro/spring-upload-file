package com.dkovalenko.uploadfile.dao.user;

import com.dkovalenko.uploadfile.dto.user.User;

import java.util.List;

public interface UserDAO {

    List<User> find();

    User find(long userID);

    void save(User user);

    void delete(long userID);

    void update(String firstName, String lastName, long userID);

    void saveAvatar(long userID, long avatarID);
}
