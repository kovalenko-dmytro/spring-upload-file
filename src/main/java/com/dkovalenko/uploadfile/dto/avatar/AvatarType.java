package com.dkovalenko.uploadfile.dto.avatar;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AvatarType {

    DEFAULT(1L, "default"),
    UPLOADED_BY_USER(2L, "uploaded by user");

    private long id;
    private String text;

    public static AvatarType getByID(long id) {

        AvatarType result = null;

        for (AvatarType type : AvatarType.values()) {
            if (type.id == id) {
                result = type;
            }
        }
        return result;
    }
}
