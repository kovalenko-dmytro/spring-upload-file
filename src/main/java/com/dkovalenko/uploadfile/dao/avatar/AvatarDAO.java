package com.dkovalenko.uploadfile.dao.avatar;

import com.dkovalenko.uploadfile.dto.avatar.Avatar;

public interface AvatarDAO {
    void save(long userID, Avatar avatar);

    Avatar find(long userID);
}
