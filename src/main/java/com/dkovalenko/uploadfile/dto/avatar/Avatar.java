package com.dkovalenko.uploadfile.dto.avatar;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Avatar {

    private long avatarID;
    private String fileName;
    private String fileType;
    private byte[] fileData;
    private long userID;

    public Avatar(String fileName, String fileType, byte[] fileData) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileData = fileData;
    }

}
