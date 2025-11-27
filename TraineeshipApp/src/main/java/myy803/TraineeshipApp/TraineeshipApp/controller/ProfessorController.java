package myy803.TraineeshipApp.TraineeshipApp.controller;


import myy803.TraineeshipApp.TraineeshipApp.model.Evaluation;
import myy803.TraineeshipApp.TraineeshipApp.model.Professor;
import myy803.TraineeshipApp.TraineeshipApp.model.TraineeshipPosition;
import myy803.TraineeshipApp.TraineeshipApp.model.User;
import myy803.TraineeshipApp.TraineeshipApp.service.TraineeshipPositionService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import myy803.TraineeshipApp.TraineeshipApp.service.ProfessorService;
import myy803.TraineeshipApp.TraineeshipApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@RequestMapping("/professor")
public class ProfessorController {

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private UserService userService;

    @Autowired
    private TraineeshipPositionService traineeshipPositionService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Professor professorProfile = professorService.getProfessorProfile(username);
        if (professorProfile == null) {
            return "redirect:/professor/profile";
        }
        model.addAttribute("professorProfile", professorProfile);
        return "professor/dashboard";
    }

    @GetMapping("/profile")
    public String profileForm(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Professor professorProfile = professorService.getProfessorProfile(username);
        if (professorProfile == null) {
            User user = userService.findById(username);
            professorProfile = new Professor();
            professorProfile.setUser(user);
            professorProfile.setProfessorName(user.getFirstName() + " " + user.getLastName());
            professorProfile.setUsername(user.getUsername());
            professorProfile.setInterests(new ArrayList<>());
        }
        model.addAttribute("professorProfile", professorProfile);
        return "professor/profile";
    }

    @PostMapping("/profile")
    public String saveProfile(@ModelAttribute("professorProfile") Professor professorProfile,
                              @RequestParam(value = "interestsList",required = false)String interestsList) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findById(username);
        Professor existingProfessor = professorService.getProfessorProfile(username);
        if (existingProfessor == null) {
            professorProfile.setUser(user);
            professorProfile.setId(user.getId());
            professorProfile.setUsername(username);
        } else {
            professorProfile.setId(existingProfessor.getId());
            professorProfile.setUser(existingProfessor.getUser());
            professorProfile.setUsername(username);
        }

        if (interestsList != null && !interestsList.trim().isEmpty()) {
            String[] interests = interestsList.split(",");
            ArrayList<String> interestsList2 = new ArrayList<>();
            for (String interest : interests) {
                interestsList2.add(interest.trim());
            }
            professorProfile.setInterests(interestsList2);
        } else {
            professorProfile.setInterests(new ArrayList<>());
        }
        professorService.saveProfile(professorProfile);
        return "redirect:/professor/dashboard";
    }

    @GetMapping("/supervised-positions")
    public String viewSupervisedPositions(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Professor professor = professorService.getProfessorProfile(username);
        List<TraineeshipPosition> supervisedPositions = professorService.getSupervisedPositions(username);

        model.addAttribute("professor", professor);
        model.addAttribute("supervisedPositions", supervisedPositions);

        return "redirect:/professor/dashboard";
    }

    @GetMapping("/evaluate/{positionId}")
    public String showEvaluationForm(@PathVariable int positionId, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Professor professor = professorService.getProfessorProfile(username);
        TraineeshipPosition position = traineeshipPositionService.findById(positionId);

        // Verify the position is supervised by this professor
        if (professor == null || position == null ||
                position.getSupervisor() == null ||
                position.getSupervisor().getId() != professor.getId()) {
            return "redirect:/professor/dashboard";
        }

        // Check if evaluation already exists
        Evaluation existingEvaluation = professorService.getProfessorEvaluation(positionId);
        Evaluation evaluationModel;

        if (existingEvaluation != null) {
            evaluationModel = existingEvaluation;
        } else {
            evaluationModel = new Evaluation();
        }

        model.addAttribute("position", position);
        model.addAttribute("evaluation", evaluationModel);

        return "professor/evaluation-form";
    }

    @PostMapping("/evaluate/{positionId}")
    public String submitEvaluation(@PathVariable int positionId,
                                   @ModelAttribute("evaluation") Evaluation evaluation,
                                   RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        professorService.evaluateTraineeship(username, positionId, evaluation);

        redirectAttributes.addFlashAttribute("successMessage", "Evaluation saved successfully!");
        return "redirect:/professor/supervised-positions";
    }

}

