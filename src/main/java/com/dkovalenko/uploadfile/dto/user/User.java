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

}
