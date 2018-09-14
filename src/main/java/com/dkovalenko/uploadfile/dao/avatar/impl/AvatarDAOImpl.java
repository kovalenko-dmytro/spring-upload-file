package com.dkovalenko.uploadfile.dao.avatar.impl;

import com.dkovalenko.uploadfile.dao.avatar.AvatarDAO;
import com.dkovalenko.uploadfile.dao.avatar.mapper.AvatarRowMapper;
import com.dkovalenko.uploadfile.dto.avatar.Avatar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AvatarDAOImpl implements AvatarDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AvatarDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<Avatar> find() {

        return jdbcTemplate.query("SELECT * FROM avatars ORDER BY uploaded_user_id",
                new AvatarRowMapper());
    }

    @Override
    public List<Avatar> find(long userID) {

        Object[] params = {userID};

        return jdbcTemplate.query("SELECT * FROM avatars WHERE uploaded_user_id = 0 OR uploaded_user_id = ? ORDER BY uploaded_user_id",
                params,
                new AvatarRowMapper());
    }

    @Override
    public void save(String fileName, long createdByUserID) {

        Object[] params = {fileName, createdByUserID, fileName};

        jdbcTemplate.update("INSERT INTO avatars (avatar_id, file_name, uploaded_user_id) VALUES ((SELECT max(a.avatar_id) FROM avatars a)+1, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE file_name = ?",
                params);
    }

    @Override
    public void delete(long avatarID) {

        Object[] params = {avatarID};

        jdbcTemplate.update("DELETE FROM avatars WHERE avatar_id = ?", params);
    }

    @Override
    public void setAvatarCounterIncrement(long userID) {

        Object[] params = {userID};
        jdbcTemplate.update("INSERT INTO set_avatar_counter (user_id, count) VALUES (?, 1) " +
                        "ON DUPLICATE KEY UPDATE count = count + 1",
                params);
    }

    @Override
    public int getSetAvatarCounter(long userID) {

        Object[] params = {userID};

        return jdbcTemplate.queryForObject("SELECT count FROM set_avatar_counter WHERE user_id = ?",
                params,
                Integer.class);
    }
}
