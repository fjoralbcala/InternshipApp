package myy803.TraineeshipApp.TraineeshipApp.service;

import myy803.TraineeshipApp.TraineeshipApp.model.Professor;
import myy803.TraineeshipApp.TraineeshipApp.repositories.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class AssignmentBasedOnLoad implements SupervisorAssignmentStrategy {

    @Autowired
    private ProfessorRepository professorRepository;

    @Override
    public Professor assign(int positionId) {

        List<Professor> professors = professorRepository.findAll();

        if (professors.isEmpty()) {
            return null;
        }

        return professors.stream()
                .min(Comparator.comparingInt(Professor::getSupervisioLoad))
                .orElse(null);


    }
}
