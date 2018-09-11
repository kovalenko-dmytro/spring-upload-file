package com.dkovalenko.uploadfile.controller.avatar;

import com.dkovalenko.uploadfile.dto.avatar.Avatar;
import com.dkovalenko.uploadfile.exception.StorageException;
import com.dkovalenko.uploadfile.exception.StorageFileNotFoundException;
import com.dkovalenko.uploadfile.service.avatar.AvatarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;


import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AvatarController {

    private final AvatarService avatarService;

    @Autowired
    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @GetMapping(value = "users/{userID}/avatars")
    public String find(Model model, @PathVariable(value = "userID") long userID) {

        String view = "/pages/user-view";

        try {

            List<Avatar> avatars = avatarService.find(userID);

            avatars.forEach(avatar -> avatar.setAvatarUri(MvcUriComponentsBuilder.fromMethodName(AvatarController.class,
                    "serveFile", avatar.getAvatarPath().getFileName().toString()).build().toString()));

            model.addAttribute("avatars", avatars);

        } catch (StorageException e) {

            e.getMessage();
        }


        return view;
    }

    @GetMapping("/avatars/{avatarName:.+}")
    public Resource serveFile(@PathVariable String avatarName) {

        return avatarService.loadAsResource(avatarName);
    }
}
