package com.dkovalenko.uploadfile.service.avatar;

import com.dkovalenko.uploadfile.dto.avatar.Avatar;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AvatarService {

    List<Avatar> find();

    List<Avatar> find(long userID);

    void store(MultipartFile file);

    void store(long userID, MultipartFile file);

    Resource loadAsResource(String filename);

    void delete();

    void delete(long avatarID);

    void delete(long userID, long avatarID);
}
