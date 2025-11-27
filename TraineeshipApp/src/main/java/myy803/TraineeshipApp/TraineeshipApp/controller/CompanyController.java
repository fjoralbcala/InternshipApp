package myy803.TraineeshipApp.TraineeshipApp.controller;

import myy803.TraineeshipApp.TraineeshipApp.model.Company;
import myy803.TraineeshipApp.TraineeshipApp.model.Evaluation;
import myy803.TraineeshipApp.TraineeshipApp.model.TraineeshipPosition;
import myy803.TraineeshipApp.TraineeshipApp.service.TraineeshipPositionService;
import myy803.TraineeshipApp.TraineeshipApp.model.User;
import myy803.TraineeshipApp.TraineeshipApp.service.CompanyService;
import myy803.TraineeshipApp.TraineeshipApp.service.TraineeshipPositionServiceImpl;
import myy803.TraineeshipApp.TraineeshipApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserService userService;

    @Autowired
    private TraineeshipPositionService traineeshipPositionService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Company companyProfile = companyService.getCompanyProfile(username);
        if (companyProfile == null) {
            return "redirect:/company/profile";
        }

        model.addAttribute("companyProfile", companyProfile);
        return "company/dashboard";
    }

    @GetMapping("/profile")
    public String profileForm(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Company companyProfile = companyService.getCompanyProfile(username);
        if (companyProfile == null) {
            User user = userService.findById(username);
            companyProfile = new Company();
            companyProfile.setUser(user);
            companyProfile.setUsername(user.getUsername());
            companyProfile.setCompanyName(user.getFirstName() + " " + user.getLastName());
            companyProfile.setTraineeshipPositions(new ArrayList<>());
        }

        model.addAttribute("companyProfile", companyProfile);
        return "company/profile";
    }

    @PostMapping("/profile")
    public String saveProfile(@ModelAttribute("companyProfile") Company companyProfile) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findById(username);

        // Get existing company profile or create new one
        Company existingCompany = companyService.getCompanyProfile(username);
        if (existingCompany == null) {
            companyProfile.setUser(user);
            companyProfile.setId(user.getId());
            companyProfile.setUsername(username);
        } else {
            companyProfile.setId(existingCompany.getId());
            companyProfile.setUser(existingCompany.getUser());
            companyProfile.setUsername(username);

            // Keep existing fields that aren't updatable in the form
            companyProfile.setTraineeshipPositions( existingCompany.getTraineeshipPositions());
        }

        System.out.println("Saving company profile: " + companyProfile);
        Company savedProfile = companyService.saveProfile(companyProfile);
        System.out.println("Saved profile: " + savedProfile);

        return "redirect:/company/dashboard";
    }

    @GetMapping("/positions/create")
    public String createPositionForm(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Company company = companyService.getCompanyProfile(username);
        if (company == null) {
            return "redirect:/company/profile";
        }

        TraineeshipPosition position = new TraineeshipPosition();
        position.setCompany(company);
        position.setTopics(new ArrayList<>());
        position.setRequiredSkills(new ArrayList<>());

        model.addAttribute("position", position);
        return "company/position-form";
    }

    @PostMapping("/positions/save")
    public String savePosition(@ModelAttribute("position") TraineeshipPosition position,
                               @RequestParam(value = "topicsList", required = false) String topicsList,
                               @RequestParam(value = "skillsList", required = false) String skillsList) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Company company = companyService.getCompanyProfile(username);
        if (company == null) {
            return "redirect:/company/profile";
        }

        position.setCompany(company);
        position.setAssigned(false);

        // Process topics list
        if (topicsList != null && !topicsList.trim().isEmpty()) {
            String[] topics = topicsList.split(",");
            ArrayList<String> topicsList2 = new ArrayList<>();
            for (String topic : topics) {
                topicsList2.add(topic.trim());
            }
            position.setTopics(topicsList2);
        } else {
            position.setTopics(new ArrayList<>());
        }

        // Process skills list
        if (skillsList != null && !skillsList.trim().isEmpty()) {
            String[] skills = skillsList.split(",");
            ArrayList<String> skillsList2 = new ArrayList<>();
            for (String skill : skills) {
                skillsList2.add(skill.trim());
            }
            position.setRequiredSkills(skillsList2);
        } else {
            position.setRequiredSkills(new ArrayList<>());
        }

        traineeshipPositionService.savePosition(position);

        return "redirect:/company/dashboard";
    }

    @GetMapping("/positions/edit/{id}")
    public String editPositionForm(@PathVariable int id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Company company = companyService.getCompanyProfile(username);
        if (company == null) {
            return "redirect:/company/profile";
        }

        TraineeshipPosition position = traineeshipPositionService.findById(id);

        // Check if position exists and belongs to this company
        if (position == null || position.getCompany().getId() != company.getId()) {
            return "redirect:/company/dashboard";
        }

        // Check if position is already assigned - don't allow editing
        if (position.isAssigned()) {
            return "redirect:/company/dashboard";
        }

        // In your controller, before returning the view
        if (position.getTopics() == null) {
            position.setTopics(new ArrayList<>());
        }
        if (position.getRequiredSkills() == null) {
            position.setRequiredSkills(new ArrayList<>());
        }

        model.addAttribute("position", position);
        return "company/position-form";
    }

    @GetMapping("/positions/delete/{id}")
    public String deletePosition(@PathVariable int id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Company company = companyService.getCompanyProfile(username);
        if (company == null) {
            return "redirect:/company/profile";
        }

        TraineeshipPosition position = traineeshipPositionService.findById(id);

        // Check if position exists and belongs to this company
        if (position == null || position.getCompany().getId() != company.getId()) {
            return "redirect:/company/dashboard";
        }

        // Check if position is already assigned - don't allow deletion
        if (position.isAssigned()) {
            return "redirect:/company/dashboard";
        }

        traineeshipPositionService.deleteById(id);
        return "redirect:/company/dashboard";
    }

    @GetMapping("/evaluate/{positionId}")
    public String showEvaluationForm(@PathVariable int positionId, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Company company = companyService.getCompanyProfile(username);
        TraineeshipPosition position = traineeshipPositionService.findById(positionId);

        // Verify the position belongs to this company and has an assigned student
        if (company == null || position == null ||
                position.getCompany().getId() != company.getId() ||
                !position.isAssigned()) {
            return "redirect:/company/dashboard";
        }

        // Check if evaluation already exists
        Evaluation existingEvaluation = companyService.getCompanyEvaluation(positionId);
        Evaluation evaluationModel;

        if (existingEvaluation != null) {
            evaluationModel = existingEvaluation;
            System.out.println("Found existing evaluation with ID: " + existingEvaluation.getId());
        } else {
            evaluationModel = new Evaluation();
            System.out.println("Creating new evaluation");
        }

        model.addAttribute("position", position);
        model.addAttribute("evaluation", evaluationModel);
        model.addAttribute("isNewEvaluation", existingEvaluation == null);

        return "company/evaluation-form";
    }

    @PostMapping("/evaluate/{positionId}")
    public String submitEvaluation(@PathVariable int positionId,
                                   @ModelAttribute("evaluation") Evaluation evaluation,
                                   RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        companyService.evaluateTraineeship(username, positionId, evaluation);

        redirectAttributes.addFlashAttribute("successMessage", "Evaluation saved successfully!");
        return "redirect:/company/dashboard";
    }
}
