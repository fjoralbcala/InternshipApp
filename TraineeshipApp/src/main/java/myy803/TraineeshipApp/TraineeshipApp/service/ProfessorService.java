package myy803.TraineeshipApp.TraineeshipApp.service;

import myy803.TraineeshipApp.TraineeshipApp.model.Evaluation;
import myy803.TraineeshipApp.TraineeshipApp.model.Professor;
import myy803.TraineeshipApp.TraineeshipApp.model.TraineeshipPosition;

import java.util.List;

public interface ProfessorService {
    Professor saveProfile(Professor professor);
    Professor getProfessorProfile(String username);

    List<Professor> getAllProfessors();
    Professor getProfessorById(int id);
    List<TraineeshipPosition> getSupervisedPositions(String username);
    Evaluation getProfessorEvaluation(int positionId);
    void evaluateTraineeship(String username, int positionId, Evaluation evaluation);
}
