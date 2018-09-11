package com.dkovalenko.uploadfile.service.avatar;

import com.dkovalenko.uploadfile.dto.avatar.Avatar;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AvatarService {

    void init();

    List<Avatar> find(long userID);

    Resource loadAsResource(String filename);

    void delete();
}
