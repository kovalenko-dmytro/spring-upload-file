package com.dkovalenko.uploadfile.dao.avatar;

import com.dkovalenko.uploadfile.dto.avatar.Avatar;

import java.util.List;

public interface AvatarDAO {

    List<Avatar> find();

    List<Avatar> find(long userID);

    void save(String fileName, long createdByUserID);

    void delete(long avatarID);

    void setAvatarCounterIncrement(long userID);

    int getSetAvatarCounter(long userID);
}
