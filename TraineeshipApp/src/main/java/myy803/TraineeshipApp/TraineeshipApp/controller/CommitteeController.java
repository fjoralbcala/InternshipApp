package myy803.TraineeshipApp.TraineeshipApp.controller;

import myy803.TraineeshipApp.TraineeshipApp.model.Professor;
import myy803.TraineeshipApp.TraineeshipApp.model.TraineeshipPosition;
import myy803.TraineeshipApp.TraineeshipApp.repositories.StudentRepository;
import myy803.TraineeshipApp.TraineeshipApp.repositories.TraineeshipPositionRepository;
import myy803.TraineeshipApp.TraineeshipApp.service.ProfessorService;
import myy803.TraineeshipApp.TraineeshipApp.service.TraineeshipPositionService;
import org.springframework.ui.Model;
import myy803.TraineeshipApp.TraineeshipApp.model.Student;
import myy803.TraineeshipApp.TraineeshipApp.service.CommitteeService;
import myy803.TraineeshipApp.TraineeshipApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/committee")
public class CommitteeController {

    @Autowired
    private CommitteeService committeeService;
    @Autowired
    private UserService userService;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TraineeshipPositionRepository traineeshipPositionRepository;
    @Autowired
    private TraineeshipPositionService traineeshipPositionService;
    @Autowired
    private ProfessorService professorService;


    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Get list of students who applied for traineeships
        List<Student> applicants = committeeService.getStudentsAppliedForTraineeship();
        model.addAttribute("applicants", applicants);

        // Get all traineeship positions
        List<TraineeshipPosition> allPositions = traineeshipPositionService.findAll();

        // Filter for assigned positions
        List<TraineeshipPosition> assignedPositions = allPositions.stream()
                .filter(position -> position.isAssigned())
                .collect(Collectors.toList());
        model.addAttribute("assignedPositions", assignedPositions);

        // Get all professors
        List<Professor> professors = professorService.getAllProfessors();
        model.addAttribute("professors", professors);

        // Calculate counts for tabs
        int pendingSupervisorCount = 0;
        int activeTraineeshipCount = 0;
        int completedTraineeshipCount = 0;

        if (assignedPositions != null) {
            for (TraineeshipPosition position : assignedPositions) {
                if (position.getSupervisor() == null) {
                    pendingSupervisorCount++;
                } else if (position.getPassFailGrade() == null) {
                    activeTraineeshipCount++;
                } else {
                    completedTraineeshipCount++;
                }
            }
        }

        model.addAttribute("pendingSupervisorCount", pendingSupervisorCount);
        model.addAttribute("activeTraineeshipCount", activeTraineeshipCount);
        model.addAttribute("completedTraineeshipCount", completedTraineeshipCount);

        return "committee/dashboard";
    }

    @GetMapping("/applicants")
    public String applicants(Model model) {
        List<Student> applicants = committeeService.getStudentsAppliedForTraineeship();
        model.addAttribute("applicants", applicants);

        return "committee/applicants";
    }

    @GetMapping("/find-positions/{username}")
    public String findPositions(@PathVariable String username, @RequestParam(defaultValue = "combined") String strategy, Model model) {
        Student student = studentRepository.findByUsername(username);
        if (student == null) {
            return "redirect:/committee/applicants";
        }

        List<TraineeshipPosition> positions = committeeService.retrievePositionsForApplicant(username, strategy);

        model.addAttribute("student", student);
        model.addAttribute("positions", positions);
        model.addAttribute("currentStrategy", strategy);

        return "committee/positions-for-student";
    }

    @PostMapping("/assign-position")
    public String assignPosition(@RequestParam int positionId, @RequestParam String studentUsername) {
        committeeService.assignPosition(positionId, studentUsername);
        return "redirect:/committee/applicants";
    }

    @GetMapping("/assign-supervisor/{positionId}")
    public String assignSupervisorForm(@PathVariable int positionId, Model model) {
        TraineeshipPosition position = traineeshipPositionRepository.findById(positionId).orElse(null);
        if (position == null) {
            return "redirect:/committee/dashboard";
        }

        model.addAttribute("position", position);
        return "committee/assign-supervisor";
    }

    @GetMapping("/assign-supervisor")
    public String showAssignSupervisorForm(@RequestParam("positionId") int positionId, Model model) {
        TraineeshipPosition position = traineeshipPositionRepository.findById(positionId).orElse(null);
        if (position == null) {
            return "redirect:/committee/dashboard";
        }

        model.addAttribute("position", position);
        return "committee/assign-supervisor";
    }

    @PostMapping("/assign-supervisor")
    public String assignSupervisor(@RequestParam("positionId") int positionId,
                                   @RequestParam("strategy") String strategy,
                                   RedirectAttributes redirectAttributes) {
        boolean success = committeeService.assignSupervisor(positionId, strategy);

        if (success) {
            redirectAttributes.addFlashAttribute("success",
                    "Supervisor assigned successfully using " + strategy + " strategy!");
            return "redirect:/committee/dashboard";
        } else {
            redirectAttributes.addFlashAttribute("error",
                    "No suitable supervisor found using " + strategy + " strategy. " +
                            "Try a different strategy or ensure professors have matching interests.");

            // Redirect back to the assignment page
            return "redirect:/committee/assign-supervisor?positionId=" + positionId;
        }
    }

    @GetMapping("/positions")
    public String viewPositions(Model model) {
        List<TraineeshipPosition> assignedPositions = traineeshipPositionService.findAll().stream()
                .filter(position -> position.isAssigned())
                .collect(Collectors.toList());
        model.addAttribute("assignedPositions", assignedPositions);

        return "committee/positions";
    }

    @GetMapping("/supervisors")
    public String viewSupervisors(Model model) {
        List<Professor> professors = professorService.getAllProfessors();
        model.addAttribute("professors", professors);

        return "committee/supervisors";
    }

    @GetMapping("/view-professor/{professorId}")
    public String viewProfessorDetails(@PathVariable int professorId, Model model) {
        Professor professor = professorService.getProfessorById(professorId);
        if (professor == null) {
            return "redirect:/committee/dashboard";
        }

        model.addAttribute("professor", professor);
        return "committee/professor-details";
    }

    @GetMapping("/active-traineeships")
    public String viewActiveTraineeships(Model model) {
        List<TraineeshipPosition> activeTraineeships = committeeService.getActiveTraineeships();
        model.addAttribute("traineeships", activeTraineeships);

        return "committee/active-traineeships";
    }

    @GetMapping("/view-traineeship/{positionId}")
    public String viewTraineeship(@PathVariable int positionId, Model model) {
        TraineeshipPosition position = committeeService.getTraineeshipById(positionId);

        if (position == null) {
            return "redirect:/committee/dashboard";
        }

        Map<String, Double> evaluationScores = committeeService.getAverageEvaluationScores(positionId);
        boolean hasAllEvaluations = committeeService.hasAllRequiredEvaluations(positionId);

        model.addAttribute("position", position);
        model.addAttribute("scores", evaluationScores);
        model.addAttribute("hasAllEvaluations", hasAllEvaluations);

        return "committee/traineeship-details";
    }

    @GetMapping("/complete-traineeship/{positionId}")
    public String showCompletionForm(@PathVariable int positionId, Model model) {
        TraineeshipPosition position = committeeService.getTraineeshipById(positionId);

        if (position == null || position.getPassFailGrade() != null) {
            return "redirect:/committee/dashboard";
        }

        Map<String, Double> evaluationScores = committeeService.getAverageEvaluationScores(positionId);
        boolean hasAllEvaluations = committeeService.hasAllRequiredEvaluations(positionId);

        // Calculate suggested grade based on overall average
        boolean suggestedGrade = false;
        if (evaluationScores.containsKey("overallAverage")) {
            suggestedGrade = evaluationScores.get("overallAverage") >= 3.0;
        }

        model.addAttribute("position", position);
        model.addAttribute("scores", evaluationScores);
        model.addAttribute("hasAllEvaluations", hasAllEvaluations);
        model.addAttribute("suggestedGrade", suggestedGrade);

        return "committee/complete-traineeship";
    }

    @PostMapping("/complete-traineeship/{positionId}")
    public String completeTraineeship(@PathVariable int positionId,
                                      @RequestParam boolean passFail,
                                      RedirectAttributes redirectAttributes) {

        committeeService.completeTraineeship(positionId, passFail);

        redirectAttributes.addFlashAttribute("successMessage",
                "Traineeship has been successfully completed with a " + (passFail ? "PASS" : "FAIL") + " grade.");

        return "redirect:/committee/active-traineeships";
    }
}