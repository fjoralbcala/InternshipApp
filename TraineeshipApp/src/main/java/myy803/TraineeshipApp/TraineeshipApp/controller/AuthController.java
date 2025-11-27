package myy803.TraineeshipApp.TraineeshipApp.controller;

import org.springframework.ui.Model;
import myy803.TraineeshipApp.TraineeshipApp.model.User;
import myy803.TraineeshipApp.TraineeshipApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class AuthController {

    @Autowired
    UserService userService;

    @RequestMapping("/login")
    public String login() {
        return "auth/signin";
    }

    @RequestMapping("/register")
    public String register(Model model){
        model.addAttribute("user",new User());
        return "auth/signup";
    }

    @RequestMapping("/save")
    public String registerUser(@ModelAttribute("user") User user, Model model){

        if(userService.isUserPresent(user)){
            model.addAttribute("successMessage","User already registered");
            return "auth/signin";
        }

        userService.saveUser(user);
        model.addAttribute("successMessage","User registered success");
        return "auth/signin";
    }
}
