package com.dkovalenko.uploadfile.dao.user;

import com.dkovalenko.uploadfile.dto.user.User;

import java.util.List;

public interface UserDAO {

    List<User> find();

    User find(long userID);
}
