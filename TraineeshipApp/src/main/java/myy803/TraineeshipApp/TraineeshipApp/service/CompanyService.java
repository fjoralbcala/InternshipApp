package myy803.TraineeshipApp.TraineeshipApp.service;

import myy803.TraineeshipApp.TraineeshipApp.model.Company;
import myy803.TraineeshipApp.TraineeshipApp.model.Evaluation;
import myy803.TraineeshipApp.TraineeshipApp.model.TraineeshipPosition;

import java.util.List;

public interface CompanyService {

    Company saveProfile(Company company);
    Company getCompanyProfile(String username);
    List<TraineeshipPosition> getAssignedPositions(String companyUsername);
    void evaluateTraineeship(String companyUsername, int positionId, Evaluation evaluation);
    Evaluation getCompanyEvaluation(int positionId);

}
