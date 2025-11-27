package myy803.TraineeshipApp.TraineeshipApp.controller;

import myy803.TraineeshipApp.TraineeshipApp.model.TraineeshipPosition;
import myy803.TraineeshipApp.TraineeshipApp.service.StudentService;
import myy803.TraineeshipApp.TraineeshipApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import myy803.TraineeshipApp.TraineeshipApp.model.Student;
import myy803.TraineeshipApp.TraineeshipApp.model.User;
import org.springframework.ui.Model;

import java.util.ArrayList;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Student studentProfile = studentService.getStudentProfile(username);
        if (studentProfile == null) {
            return "redirect:/student/profile";
        }
        model.addAttribute("studentProfile", studentProfile);
        return "student/dashboard";
    }

    @GetMapping("/profile")
    public String profileForm(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Student studentProfile = studentService.getStudentProfile(username);
        if (studentProfile == null) {
            User user = userService.findById(username);
            studentProfile = new Student();
            studentProfile.setUser(user);
            studentProfile.setStudentName(user.getFirstName() + " " + user.getLastName());
            studentProfile.setUsername(user.getUsername());
            studentProfile.setInterests(new ArrayList<>());
            studentProfile.setSkills(new ArrayList<>());
            studentProfile.setLookingForTraineeship(true);
        }

        model.addAttribute("studentProfile", studentProfile);
        return "student/profile";
    }

    @PostMapping("/profile")
    public String saveProfile(@ModelAttribute("studentProfile") Student studentProfile,
                              @RequestParam(value = "interestsList", required = false) String interestsList,
                              @RequestParam(value = "skillsList", required = false) String skillsList) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findById(username);
        Student existingStudent = studentService.getStudentProfile(username);
        if (existingStudent == null) {
            studentProfile.setUser(user);
            studentProfile.setId(user.getId());
            studentProfile.setUsername(username);
        } else {
            studentProfile.setId(existingStudent.getId());
            studentProfile.setUser(existingStudent.getUser());
            studentProfile.setUsername(username);


            if (existingStudent.getAssignedTraineeship() != null) {
                studentProfile.setAssignedTraineeship(existingStudent.getAssignedTraineeship());
            }
        }

        if (interestsList != null && !interestsList.trim().isEmpty()) {
            String[] interests = interestsList.split(",");
            ArrayList<String> interestsList2 = new ArrayList<>();
            for (String interest : interests) {
                interestsList2.add(interest.trim());
            }
            studentProfile.setInterests(interestsList2);
        } else {
            studentProfile.setInterests(new ArrayList<>());
        }
        
        if (skillsList != null && !skillsList.trim().isEmpty()) {
            String[] skills = skillsList.split(",");
            ArrayList<String> skillsList2 = new ArrayList<>();
            for (String skill : skills) {
                skillsList2.add(skill.trim());
            }
            studentProfile.setSkills(skillsList2);
        } else {
            studentProfile.setSkills(new ArrayList<>());
        }

        System.out.println("Saving student profile: " + studentProfile);
        Student savedProfile = studentService.saveProfile(studentProfile);
        System.out.println("Saved profile: " + savedProfile);

        return "redirect:/student/dashboard";
    }

    @GetMapping("/logbook")
    public String viewLogbook(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Student student = studentService.getStudentProfile(username);
        TraineeshipPosition traineeship = studentService.getStudentTraineeship(username);

        if (student == null || traineeship == null) {
            return "redirect:/student/dashboard";
        }

        model.addAttribute("student", student);
        model.addAttribute("traineeship", traineeship);

        return "student/logbook";
    }

    @PostMapping("/logbook")
    public String updateLogbook(@RequestParam("logbookContent") String logbookContent) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        studentService.updateLogbook(username, logbookContent);

        return "redirect:/student/dashboard";
    }
}
