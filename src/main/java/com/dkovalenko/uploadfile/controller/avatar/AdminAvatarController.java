package com.dkovalenko.uploadfile.controller.avatar;

import com.dkovalenko.uploadfile.dto.avatar.Avatar;
import com.dkovalenko.uploadfile.exception.StorageException;
import com.dkovalenko.uploadfile.service.avatar.AvatarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class AdminAvatarController {

    private final AvatarService avatarService;

    @Autowired
    public AdminAvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @GetMapping(value = "users/avatars")
    public ModelAndView find(HttpServletRequest request) {

        ModelAndView view = new ModelAndView();
        view.setViewName("/pages/avatars");

        try {

            List<Avatar> avatars = avatarService.find();

            String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/avatars/";

            avatars.forEach(avatar -> avatar.setAvatarUri(url+avatar.getAvatarName()));

            view.addObject("avatars", avatars);

        } catch (StorageException e) {

            e.getMessage();
        }

        return view;
    }

    @PostMapping(value = "users/avatars")
    public ModelAndView upload(@RequestParam("file") MultipartFile file,
                                         RedirectAttributes redirectAttributes) {

        ModelAndView view = new ModelAndView();

        avatarService.store(file);

        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        view.setViewName("redirect:/users/avatars");

        return view;
    }

    @GetMapping(value = "users/avatars/{avatarID}/delete")
    public ModelAndView delete(@PathVariable(value = "avatarID") long avatarID,
                               RedirectAttributes redirectAttributes) {

        ModelAndView view = new ModelAndView();


        avatarService.delete(avatarID);

        redirectAttributes.addFlashAttribute("message",
                "You successfully deleted file");

        view.setViewName("redirect:/users/avatars");

        return view;
    }
}
