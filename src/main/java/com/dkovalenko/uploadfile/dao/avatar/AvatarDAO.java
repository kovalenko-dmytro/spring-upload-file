package com.dkovalenko.uploadfile.dao.avatar;

import com.dkovalenko.uploadfile.dto.avatar.Avatar;

import java.util.List;

public interface AvatarDAO {

    void save(long userID, Avatar avatar);

    List<Avatar> find(long userID);
}
