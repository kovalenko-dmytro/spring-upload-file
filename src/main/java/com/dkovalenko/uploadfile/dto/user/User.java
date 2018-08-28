package com.dkovalenko.uploadfile.dto.user;

import com.dkovalenko.uploadfile.dto.avatar.Avatar;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private long userID;
    private String firstName;
    private String lastName;
    private LocalDate birthday;
    private Avatar avatar;
    private String image;

    public User(long userID, String firstName, String lastName, LocalDate birthday) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
    }
}
