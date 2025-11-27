package myy803.TraineeshipApp.TraineeshipApp.service;

import myy803.TraineeshipApp.TraineeshipApp.model.Professor;

public interface SupervisorAssignmentStrategy {
    Professor assign(int positionId);
}
