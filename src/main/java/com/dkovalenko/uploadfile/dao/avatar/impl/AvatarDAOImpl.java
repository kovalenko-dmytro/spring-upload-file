package com.dkovalenko.uploadfile.dao.avatar.impl;

import com.dkovalenko.uploadfile.dao.avatar.AvatarDAO;
import com.dkovalenko.uploadfile.dao.avatar.mapper.AvatarRowMapper;
import com.dkovalenko.uploadfile.dto.avatar.Avatar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AvatarDAOImpl implements AvatarDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AvatarDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(long userID, Avatar avatar) {

        Object[] params = {avatar.getFileName(), avatar.getFileType(), avatar.getFileData(), userID};
        jdbcTemplate.update("INSERT INTO avatars (file_name, file_type, data, user_id) VALUES(?, ?, ?, ?)",
                params);
    }

    @Override
    public Avatar find(long userID) {

        Object[] params = {userID};
        Avatar avatar;

        try {
            avatar =  jdbcTemplate.queryForObject("SELECT avatar_id, file_name, file_type, data, user_id " +
                            "FROM avatars " +
                            "WHERE user_id = ?",
                    params,
                    new AvatarRowMapper());
        } catch (DataAccessException e) {
            avatar = null;
        }
        return avatar;
    }
}
