package myy803.TraineeshipApp.TraineeshipApp.service;

import myy803.TraineeshipApp.TraineeshipApp.model.Company;
import myy803.TraineeshipApp.TraineeshipApp.model.TraineeshipPosition;

import java.util.List;

public interface TraineeshipPositionService {
    TraineeshipPosition savePosition(TraineeshipPosition traineeshipPosition);
    TraineeshipPosition findById(int id);
    List<TraineeshipPosition> findByCompany(Company company);
    List<TraineeshipPosition> findAvailablePositionsByCompany(Company company);
    List<TraineeshipPosition> findAssignedPositionsByCompany(Company company);
    TraineeshipPosition deleteById(int id);
    List<TraineeshipPosition> findAll();

}
