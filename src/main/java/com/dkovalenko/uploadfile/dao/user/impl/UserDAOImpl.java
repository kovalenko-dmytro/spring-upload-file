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

        return jdbcTemplate.query("SELECT u.user_id, u.first_name, u.last_name, a.avatar_id, a.file_name, a.uploaded_user_id  " +
                "FROM users u " +
                "LEFT JOIN avatars a ON u.avatar_id = a.avatar_id",
                new UserRowMapper());
    }

    @Override
    public User find(long userID) {

        Object[] params = {userID};

        return jdbcTemplate.queryForObject("SELECT u.user_id, u.first_name, u.last_name, a.avatar_id, a.file_name, a.uploaded_user_id " +
                "FROM users u " +
                        "LEFT JOIN avatars a ON u.avatar_id = a.avatar_id " +
                "WHERE u.user_id = ?",
                params,
                new UserRowMapper());
    }

    @Override
    public void save(User user) {

        Object[] params = {user.getFirstName(), user.getLastName()};

         jdbcTemplate.update("INSERT INTO users (first_name, last_name) VALUES(?, ?)",
                params);
    }

    @Override
    public void delete(long userID) {

        Object[] params = {userID};

        jdbcTemplate.update("DELETE FROM users WHERE user_id = ?", params);
    }

    @Override
    public void update(String firstName, String lastName, long userID) {

        Object[] params = {firstName, lastName, userID};

        jdbcTemplate.update("UPDATE users SET first_name = ?, last_name = ? WHERE user_id = ?", params);

    }

    @Override
    public void saveAvatar(long userID, long avatarID) {

        Object[] params = {avatarID, userID};

        jdbcTemplate.update("UPDATE users SET avatar_id = ? WHERE user_id = ?", params);
    }

    @Override
    public void resetAvatar(long userID) {

        Object[] params = {userID};

        jdbcTemplate.update("UPDATE users SET avatar_id = 0 WHERE user_id = ?", params);
    }

}