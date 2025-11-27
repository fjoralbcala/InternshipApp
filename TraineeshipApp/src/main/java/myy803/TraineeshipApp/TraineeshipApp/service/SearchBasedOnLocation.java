package myy803.TraineeshipApp.TraineeshipApp.service;

import myy803.TraineeshipApp.TraineeshipApp.model.Student;
import myy803.TraineeshipApp.TraineeshipApp.model.TraineeshipPosition;
import myy803.TraineeshipApp.TraineeshipApp.repositories.StudentRepository;
import myy803.TraineeshipApp.TraineeshipApp.repositories.TraineeshipPositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class SearchBasedOnLocation implements PositionSearchStrategy {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TraineeshipPositionRepository positionRepository;

    @Override
    public List<TraineeshipPosition> search(String applicantUsername) {
        Student student = studentRepository.findByUsername(applicantUsername);
        if (student == null || student.getPreferredLocation() == null || student.getPreferredLocation().isEmpty()) {
            return new ArrayList<>();
        }

        List<TraineeshipPosition> availablePositions = positionRepository.findByIsAssigned(false);

        return availablePositions.stream()
                .filter(position -> hasMatchingSkills(student,position))
                .filter(position -> matchesLocation(student, position))
                .collect(Collectors.toList());
    }

    private boolean hasMatchingSkills(Student student, TraineeshipPosition position) {
        if (student.getSkills() == null || student.getSkills().isEmpty()) {
            return false;
        }

        if (position.getRequiredSkills() == null || position.getRequiredSkills().isEmpty()) {
            return true;  // No skills required, so any student can apply
        }

        // Check if student has at least some of the required skills
        for (String requiredSkill : position.getRequiredSkills()) {
            if (student.getSkills().contains(requiredSkill)) {
                return true;  // Student has at least one required skill
            }
        }

        return false;  // Student has none of the required skills
    }

    private boolean matchesLocation(Student student, TraineeshipPosition position) {
        return student.getPreferredLocation().equalsIgnoreCase(position.getCompany().getCompanyLocation());
    }
}
