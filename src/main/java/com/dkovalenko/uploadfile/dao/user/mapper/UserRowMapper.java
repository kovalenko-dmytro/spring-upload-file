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
                resultSet.getString("last_name")
        );

        if (resultSet.getLong("avatar_id") != 0) {
            user.setAvatar(
                    new Avatar(
                            resultSet.getLong("avatar_id"),
                            resultSet.getString("file_name"),
                            resultSet.getLong("uploaded_user_id")
                    )
            );
        }

        return user;
    }
}
