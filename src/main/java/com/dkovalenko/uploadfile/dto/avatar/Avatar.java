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
    private long uploadedByUserID;

    private Path avatarPath;
    private String avatarUri;

    public Avatar(long avatarID, String avatarName, long uploadedByUserID) {
        this.avatarID = avatarID;
        this.avatarName = avatarName;
        this.uploadedByUserID = uploadedByUserID;
    }
}
