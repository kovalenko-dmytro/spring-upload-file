package com.dkovalenko.uploadfile.dao.user.impl;

import com.dkovalenko.uploadfile.dao.user.UserDAO;
import com.dkovalenko.uploadfile.dao.user.mapper.UserRowMapper;
import com.dkovalenko.uploadfile.dto.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.*;

import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> find() {

        return jdbcTemplate.query("SELECT u.user_id, u.first_name, u.last_name, u.birthday, " +
                "a.avatar_id, a.file_name, a.file_type, a.data, a.user_id " +
                "FROM users u " +
                "LEFT JOIN avatars a ON u.user_id = a.user_id",
                new UserRowMapper());
    }

    @Override
    public User find(long userID) {

        Object[] params = {userID};

        return jdbcTemplate.queryForObject("SELECT u.user_id, u.first_name, u.last_name, u.birthday, " +
                "a.avatar_id, a.file_name, a.file_type, a.data, a.user_id " +
                "FROM users u " +
                "LEFT JOIN avatars a ON u.user_id = a.user_id " +
                "WHERE u.user_id = ?",
                params,
                new UserRowMapper());
    }

}