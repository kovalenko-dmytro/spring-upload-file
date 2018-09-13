package com.dkovalenko.uploadfile.controller.avatar;

import com.dkovalenko.uploadfile.dto.avatar.Avatar;
import com.dkovalenko.uploadfile.exception.StorageException;
import com.dkovalenko.uploadfile.service.avatar.AvatarService;
import com.dkovalenko.uploadfile.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RestController
public class AvatarController {

    private final AvatarService avatarService;

    @Autowired
    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @GetMapping(value = "users/{userID}/avatars")
    public ModelAndView find(@PathVariable(value = "userID") long userID) {

        ModelAndView view = new ModelAndView();
        view.setViewName("/pages/user-avatars");

        try {

            List<Avatar> avatars = avatarService.find(userID);

            avatars.forEach(avatar -> avatar.setAvatarUri(MvcUriComponentsBuilder.fromMethodName(AvatarController.class,
                    "serveFile", avatar.getAvatarName()).build().toString()));

            view.addObject("userID", userID);
            view.addObject("avatars", avatars);

        } catch (StorageException e) {

            e.getMessage();
        }

        return view;
    }

    @PostMapping(value = "users/{userID}/avatars")
    public ModelAndView handleFileUpload(@RequestParam("file") MultipartFile file,
                                         @PathVariable(value = "userID") long userID,
                                         RedirectAttributes redirectAttributes) {

        System.out.println(userID);

        ModelAndView view = new ModelAndView();

        avatarService.store(userID, file);

        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        view.setViewName("redirect:/users/" + userID + "/avatars");

        return view;
    }

    @GetMapping(value = "users/{userID}/avatars/{avatarID}/delete")
    public ModelAndView delete(@PathVariable(value = "userID") long userID,
                               @PathVariable(value = "avatarID") long avatarID,
                               RedirectAttributes redirectAttributes) {

        ModelAndView view = new ModelAndView();

        if (userID != 0) {
            avatarService.delete(userID, avatarID);

            redirectAttributes.addFlashAttribute("message",
                    "You successfully deleted file");
        }

        view.setViewName("redirect:/users/" + userID + "/avatars");

        return view;
    }

    @GetMapping("upload-avatars/{avatarName:.+}")
    public Resource serveFile(@PathVariable String avatarName) {

        return avatarService.loadAsResource(avatarName);
    }
}
