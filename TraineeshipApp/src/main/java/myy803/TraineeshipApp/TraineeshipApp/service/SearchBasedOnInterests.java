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
public class SearchBasedOnInterests implements PositionSearchStrategy {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TraineeshipPositionRepository traineeshipPositionRepository;

    @Override
    public List<TraineeshipPosition> search(String applicantUsername) {
        Student student = studentRepository.findByUsername(applicantUsername);
        if (student == null || student.getInterests() == null || student.getInterests().isEmpty()) {
            return new ArrayList<>();
        }

        List<TraineeshipPosition> availablePositions = traineeshipPositionRepository.findAll().stream()
                .filter(position -> !position.isAssigned())
                .toList();

        List<TraineeshipPosition> skillMatchedPositions = new ArrayList<>();
        for (TraineeshipPosition position : availablePositions) {
            if (checkSkillsMatch(student, position)) {
                skillMatchedPositions.add(position);
            }
        }

        List<TraineeshipPosition> results = new ArrayList<>();
        for (TraineeshipPosition position : skillMatchedPositions) {
            double similarity = calculateJaccardSimilarity(student.getInterests(), position.getTopics());
            if (similarity > 0.1) {
                results.add(position);
            }
        }

        return results;
    }

    private boolean checkSkillsMatch(Student student, TraineeshipPosition position) {
        if (student.getSkills() == null || student.getSkills().isEmpty()) {
            return false;
        }

        if (position.getRequiredSkills() == null || position.getRequiredSkills().isEmpty()) {
            return true;  // No skills required, so any student can apply
        }

        // Check if student has at least some of the required skills
        // This is more realistic for a traineeship position
        for (String requiredSkill : position.getRequiredSkills()) {
            if (student.getSkills().contains(requiredSkill)) {
                return true;  // Student has at least one required skill
            }
        }

        return false;  // Student has none of the required skills
    }

    private double calculateJaccardSimilarity(List<String> interests, List<String> topics) {
        if (interests == null || topics == null || interests.isEmpty() || topics.isEmpty()) {
            return 0.0;
        }

        // Convert to lowercase for case-insensitive comparison
        Set<String> interestsSet = interests.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        Set<String> topicsSet = topics.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        // Calculate intersection size
        Set<String> intersection = new HashSet<>(interestsSet);
        intersection.retainAll(topicsSet);

        // Calculate union size
        Set<String> union = new HashSet<>(interestsSet);
        union.addAll(topicsSet);

        // Jaccard similarity: |intersection| / |union|
        return (double) intersection.size() / union.size();
    }
}
