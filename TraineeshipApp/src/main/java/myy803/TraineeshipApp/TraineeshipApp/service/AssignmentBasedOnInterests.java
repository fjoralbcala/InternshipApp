package myy803.TraineeshipApp.TraineeshipApp.service;

import myy803.TraineeshipApp.TraineeshipApp.model.TraineeshipPosition;
import myy803.TraineeshipApp.TraineeshipApp.repositories.ProfessorRepository;
import myy803.TraineeshipApp.TraineeshipApp.repositories.TraineeshipPositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import myy803.TraineeshipApp.TraineeshipApp.model.Professor;

import java.util.*;

@Component
public class AssignmentBasedOnInterests implements SupervisorAssignmentStrategy {

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private TraineeshipPositionRepository positionRepository;

    @Override
    public Professor assign(int positionId) {
        TraineeshipPosition position = positionRepository.findById(positionId).orElse(null);
        if (position == null || position.getTopics() == null || position.getTopics().isEmpty()) {
            return null;
        }

        List<Professor> professors = professorRepository.findAll();
        Professor bestMatch = null;
        double highestSimilarity = 0;

        for (Professor professor : professors) {
            if (professor.getInterests() == null || professor.getInterests().isEmpty()) {
                continue;
            }

            double similarity = calculateJaccardSimilarity(professor.getInterests(), position.getTopics());
            if (similarity > highestSimilarity) {
                highestSimilarity = similarity;
                bestMatch = professor;
            }
        }
        if (highestSimilarity > 0.3) {
            return bestMatch;
        }

        return null;
    }

    private double calculateJaccardSimilarity(List<String> professorInterests, List<String> positionTopics) {
        if (professorInterests == null || positionTopics == null ||
                professorInterests.isEmpty() || positionTopics.isEmpty()) {
            return 0.0;
        }

        Set<String> interestsSet = new HashSet<>(professorInterests);
        Set<String> topicsSet = new HashSet<>(positionTopics);
        Set<String> intersection = new HashSet<>(interestsSet);
        intersection.retainAll(topicsSet);
        Set<String> union = new HashSet<>(interestsSet);
        union.addAll(topicsSet);
        return (double) intersection.size() / union.size();
    }
}
