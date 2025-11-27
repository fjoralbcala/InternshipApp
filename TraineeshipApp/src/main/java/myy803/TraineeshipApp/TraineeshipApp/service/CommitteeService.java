package myy803.TraineeshipApp.TraineeshipApp.service;
import myy803.TraineeshipApp.TraineeshipApp.model.Student;
import myy803.TraineeshipApp.TraineeshipApp.model.TraineeshipPosition;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public interface CommitteeService {

    List<Student> getStudentsAppliedForTraineeship();
    List<TraineeshipPosition> retrievePositionsForApplicant(String applicantUsername, String strategy);
    void assignPosition(int positionId, String studentUsername);
    boolean assignSupervisor(int positionId, String strategy);
    List<TraineeshipPosition> getActiveTraineeships();
    TraineeshipPosition getTraineeshipById(int positionId);
    void completeTraineeship(int positionId, boolean passFail);
    Map<String, Double> getAverageEvaluationScores(int positionId);
    boolean hasAllRequiredEvaluations(int positionId);

}
