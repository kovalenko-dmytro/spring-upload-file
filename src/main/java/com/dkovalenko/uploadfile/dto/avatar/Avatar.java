package com.dkovalenko.uploadfile.dto.avatar;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.file.Path;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Avatar {

    private long avatarID;
    private String avatarName;
    private AvatarType avatarType;
    private long uploadedByUserID;
    private boolean isSetting;

    private Path avatarPath;
    private String avatarUri;

    public Avatar(long avatarID, String avatarName, AvatarType avatarType, long uploadedByUserID, boolean isSetting) {
        this.avatarID = avatarID;
        this.avatarName = avatarName;
        this.avatarType = avatarType;
        this.uploadedByUserID = uploadedByUserID;
        this.isSetting = isSetting;
    }
}
