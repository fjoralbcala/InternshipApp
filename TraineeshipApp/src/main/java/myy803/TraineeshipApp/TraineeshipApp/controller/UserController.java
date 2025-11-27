package myy803.TraineeshipApp.TraineeshipApp.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.Authentication;

@Controller
public class UserController {

    @RequestMapping("/user/dashboard")
    public String getUserHome() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        System.err.println(currentPrincipalName);

        return "user/dashboard";
    }
}
