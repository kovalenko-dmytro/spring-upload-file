package com.dkovalenko.uploadfile.service.avatar;

import org.springframework.web.multipart.MultipartFile;

public interface AvatarService {


    void store(long userID, MultipartFile file);
}
