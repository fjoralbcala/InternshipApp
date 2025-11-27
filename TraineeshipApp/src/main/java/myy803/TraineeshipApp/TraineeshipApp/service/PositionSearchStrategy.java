package myy803.TraineeshipApp.TraineeshipApp.service;

import myy803.TraineeshipApp.TraineeshipApp.model.Student;
import myy803.TraineeshipApp.TraineeshipApp.model.TraineeshipPosition;
import java.util.List;

public interface PositionSearchStrategy {

    List<TraineeshipPosition> search(String applicantUsername);
}
