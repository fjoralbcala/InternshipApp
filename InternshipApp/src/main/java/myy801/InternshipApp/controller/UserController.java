package myy801.InternshipApp.controller;


import java.util.List;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import myy801.InternshipApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import myy801.InternshipApp.entity.User;
import myy801.InternshipApp.service.UserService;

@Controller
@RequestMapping
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    public UserController(UserService theUserService) {
        userService = theUserService;
    }

    @RequestMapping
    public String listUsers(Model model) {

        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "listUsers";
    }

    @RequestMapping
    public String showUserForm(@RequestParam("userId") int theId, Model model) {

        User user = userService.findById(theId);

        model.addAttribute("user", user);

        return "userForm";
    }

    @RequestMapping
    public String saveUser(@ModelAttribute("user") User theUser) {

        userService.save(theUser);
        return "redirect:/listUsers";
    }

    @RequestMapping
    public String deleteUser(@RequestParam("userId") int theId, Model model) {

        userService.deleteById(theId);
        return "redirect:/listUsers";
    }
}
