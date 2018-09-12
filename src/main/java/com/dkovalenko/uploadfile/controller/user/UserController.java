package com.dkovalenko.uploadfile.controller.user;

import com.dkovalenko.uploadfile.controller.avatar.AvatarController;
import com.dkovalenko.uploadfile.dto.avatar.Avatar;
import com.dkovalenko.uploadfile.dto.user.User;
import com.dkovalenko.uploadfile.exception.StorageException;
import com.dkovalenko.uploadfile.service.avatar.AvatarService;
import com.dkovalenko.uploadfile.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

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

        try {

            List<Avatar> avatars = avatarService.find();

            avatars.forEach(avatar -> avatar.setAvatarUri(MvcUriComponentsBuilder.fromMethodName(AvatarController.class,
                    "serveFile", avatar.getUploadedByUserID(), avatar.getAvatarName()).build().toString()));

            view.addObject("avatars", avatars);

        } catch (StorageException e) {

            e.getMessage();
        }

        return view;
    }

    @GetMapping("users/{avatarName:.+}")
    public Resource serveFile(@PathVariable String avatarName) {

        return avatarService.loadAsResource(avatarName);
    }

    @GetMapping(value = "/users/{userID}/view")
    public ModelAndView find(@PathVariable(value = "userID") long userID) {
        ModelAndView view = new ModelAndView();
        view.addObject("user", userService.find(userID));
        view.setViewName("/pages/user-view");
        return view;
    }

    /*@GetMapping(value = "/users/{userID}/addAvatar")
    public ModelAndView addAvatar(@PathVariable(value = "userID") long userID) {
        ModelAndView view = new ModelAndView();
        view.addObject("user", userService.find(userID));
        view.setViewName("/pages/user-add-avatar");
        return view;
    }

    @PostMapping(value = "/users/{userID}/addAvatar")
    public ModelAndView addAvatar(@PathVariable(value = "userID") long userID,
                             @RequestParam("file") MultipartFile file) {

        ModelAndView view = new ModelAndView();

        if (file != null) {

            avatarService.store(userID, file);

            view.addObject("users", userService.find());

        }

        view.setViewName("redirect:/users");
        return view;
    }*/

    @GetMapping(value = "/users/create")
    public ModelAndView create(User user) {

        ModelAndView view = new ModelAndView();
        view.addObject("user", user);
        view.setViewName("/pages/user-create");
        return view;
    }

    @PostMapping(value = "/users/create")
    public ModelAndView save(@Valid @ModelAttribute("user") User user) {

        ModelAndView view = new ModelAndView();

        userService.save(user);
        view.setViewName("redirect:/users");
        return view;
    }

    @GetMapping(value = "/users/{userID}/edit")
    public ModelAndView edit(User user, @PathVariable(value = "userID") long userID) {

        ModelAndView view = new ModelAndView();
        view.addObject("user", userService.find(userID));
        view.setViewName("/pages/user-edit");
        return view;
    }

    @PostMapping(value = "/users/{userID}/update")
    public ModelAndView update(@Valid @ModelAttribute("user") User user,
                             @PathVariable(value = "userID") long userID) {

        ModelAndView view = new ModelAndView();

        userService.update(userID, user);
        view.setViewName("redirect:/users");
        return view;
    }

    @RequestMapping(value = "/users/{userID}/delete")
    public ModelAndView delete(@PathVariable(value = "userID") long userID) {

        ModelAndView view = new ModelAndView();

        userService.delete(userID);

        view.setViewName("redirect:/users");

        return view;
    }
}
