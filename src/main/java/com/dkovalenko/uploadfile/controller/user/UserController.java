package com.dkovalenko.uploadfile.controller.user;

import com.dkovalenko.uploadfile.dto.user.User;
import com.dkovalenko.uploadfile.service.avatar.AvatarService;
import com.dkovalenko.uploadfile.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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

    @GetMapping(value = "/")
    public ModelAndView find() {

        ModelAndView view = new ModelAndView();

        List<User> users = userService.find();

        view.addObject("users", users);
        view.setViewName("index");

        return view;
    }

    @GetMapping(value = "/users/{userID}")
    public ModelAndView find(@PathVariable(value = "userID") long userID) {

        ModelAndView view = new ModelAndView();

        User user = userService.find(userID);

        view.addObject("user", user);
        view.setViewName("/pages/user-view");
        return view;
    }

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
        view.setViewName("redirect:/");
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
        view.setViewName("redirect:/");
        return view;
    }

    @RequestMapping(value = "/users/{userID}/delete")
    public ModelAndView delete(@PathVariable(value = "userID") long userID) {

        ModelAndView view = new ModelAndView();

        userService.delete(userID);

        view.setViewName("redirect:/");

        return view;
    }

    @GetMapping(value = "/users/{userID}/setAvatar")
    public ModelAndView setAvatar(@PathVariable(value = "userID") long userID) {

        ModelAndView view = new ModelAndView();
        view.addObject("user", userService.find(userID));
        view.addObject("avatars", avatarService.find(userID));
        view.setViewName("/pages/user-add-avatar");
        return view;
    }

    @PostMapping(value = "/users/{userID}/setAvatar")
    public ModelAndView saveAvatar(@PathVariable(value = "userID") long userID,
                                   @RequestParam(value = "avatar") long avatarID) {

        ModelAndView view = new ModelAndView();

        userService.saveAvatar(userID, avatarID);

        view.setViewName("redirect:/users/" + userID + "");

        return view;
    }

    @GetMapping(value = "/users/{userID}/resetAvatar")
    public ModelAndView resetAvatar(@PathVariable(value = "userID") long userID) {

        ModelAndView view = new ModelAndView();

        userService.resetAvatar(userID);

        view.setViewName("redirect:/users/" + userID + "");

        return view;
    }
}
