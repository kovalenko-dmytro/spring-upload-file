package com.dkovalenko.uploadfile.controller.user;

import com.dkovalenko.uploadfile.service.avatar.AvatarService;
import com.dkovalenko.uploadfile.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {

    private final UserService userService;
    private final AvatarService avatarService;

    @Autowired
    public UserController(UserService userService, AvatarService avatarService) {
        this.userService = userService;
        this.avatarService = avatarService;
    }

    @GetMapping(value = "/users")
    public ModelAndView find() {
        ModelAndView view = new ModelAndView();
        view.addObject("users", userService.find());
        view.setViewName("index");
        return view;
    }

    @GetMapping(value = "/users/{userID}/view")
    public ModelAndView find(@PathVariable(value = "userID") long userID) {
        ModelAndView view = new ModelAndView();
        view.addObject("user", userService.find(userID));
        view.setViewName("/pages/user-view");
        return view;
    }

    @GetMapping(value = "/users/{userID}/addAvatar")
    public ModelAndView addAvatar(@PathVariable(value = "userID") long userID) {
        ModelAndView view = new ModelAndView();
        view.addObject("user", userService.find(userID));
        view.setViewName("/pages/user-add-avatar");
        return view;
    }

    @PostMapping(value = "/users/{userID}/addAvatar")
    public ModelAndView save(@PathVariable(value = "userID") long userID,
                             @RequestParam("file") MultipartFile file) {

        ModelAndView view = new ModelAndView();

        if (file != null) {

            avatarService.store(userID, file);

            view.addObject("users", userService.find());

        }

        view.setViewName("redirect:/users");
        return view;
    }
}
