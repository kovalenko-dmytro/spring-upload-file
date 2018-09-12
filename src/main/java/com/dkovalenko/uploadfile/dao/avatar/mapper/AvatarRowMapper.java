package com.dkovalenko.uploadfile.dao.avatar.mapper;

import com.dkovalenko.uploadfile.dto.avatar.Avatar;
import com.dkovalenko.uploadfile.dto.avatar.AvatarType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AvatarRowMapper implements RowMapper<Avatar> {

    @Override
    public Avatar mapRow(ResultSet resultSet, int i) throws SQLException {

        return new Avatar(
                resultSet.getLong("avatar_id"),
                resultSet.getString("file_name"),
                resultSet.getLong("uploaded_user_id")
        );
    }
}
