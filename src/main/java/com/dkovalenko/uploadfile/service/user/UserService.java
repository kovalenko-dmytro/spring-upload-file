package com.dkovalenko.uploadfile.service.user;

import com.dkovalenko.uploadfile.dto.user.User;

import java.util.List;

public interface UserService {

    List<User> find();

    User find(long userID);

    User save(User user);

    User update(long userID, User user);

    void delete(long userID);
}
