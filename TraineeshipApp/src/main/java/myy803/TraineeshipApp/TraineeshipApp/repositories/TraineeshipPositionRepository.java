package myy803.TraineeshipApp.TraineeshipApp.repositories;

import myy803.TraineeshipApp.TraineeshipApp.model.Company;
import myy803.TraineeshipApp.TraineeshipApp.model.TraineeshipPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TraineeshipPositionRepository extends JpaRepository<TraineeshipPosition,Integer> {
    List<TraineeshipPosition> findByCompany(Company company);
    List<TraineeshipPosition> findByCompanyAndIsAssigned(Company company, boolean isAssigned);
    List<TraineeshipPosition> findByIsAssigned(boolean isAssigned);
}
