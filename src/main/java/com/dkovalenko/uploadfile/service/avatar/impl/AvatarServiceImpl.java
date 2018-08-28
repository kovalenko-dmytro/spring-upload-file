package com.dkovalenko.uploadfile.service.avatar.impl;

import com.dkovalenko.uploadfile.dao.avatar.AvatarDAO;
import com.dkovalenko.uploadfile.dao.user.UserDAO;
import com.dkovalenko.uploadfile.dto.avatar.Avatar;
import com.dkovalenko.uploadfile.dto.user.User;
import com.dkovalenko.uploadfile.service.avatar.AvatarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class AvatarServiceImpl implements AvatarService {

    private final AvatarDAO avatarDAO;

    @Autowired
    public AvatarServiceImpl(AvatarDAO avatarDAO) {
        this.avatarDAO = avatarDAO;
    }

    @Override
    public void store(long userID, MultipartFile file) {

        Avatar existAvatar = avatarDAO.find(userID);

        if (existAvatar == null) {

            String fileName = StringUtils.cleanPath(file.getOriginalFilename());

            Avatar avatar;
            try {
                avatar = new Avatar(fileName, file.getContentType(), file.getBytes());
                avatarDAO.save(userID, avatar);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
