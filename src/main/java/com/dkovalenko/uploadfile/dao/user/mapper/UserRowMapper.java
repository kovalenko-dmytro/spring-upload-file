package com.dkovalenko.uploadfile.dao.user.mapper;

import com.dkovalenko.uploadfile.dto.avatar.Avatar;
import com.dkovalenko.uploadfile.dto.user.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {

        User user = new User(
                resultSet.getLong("user_id"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name"),
                resultSet.getDate("birthday").toLocalDate()
        );

        try {
            user.setAvatar(
                    new Avatar(
                            resultSet.getLong("avatar_id"),
                            resultSet.getString("file_name"),
                            resultSet.getString("file_type"),
                            resultSet.getBytes("data"),
                            resultSet.getLong("user_id")
                    )
            );
        } catch (SQLException e) {
            user.setAvatar(null);
        }

        return user;
    }
}
