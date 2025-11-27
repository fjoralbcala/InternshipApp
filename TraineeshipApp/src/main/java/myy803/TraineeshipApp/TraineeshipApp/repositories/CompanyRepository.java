package myy803.TraineeshipApp.TraineeshipApp.repositories;

import myy803.TraineeshipApp.TraineeshipApp.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface CompanyRepository extends JpaRepository<Company,Integer> {
    Company findByUsername(String username);
}
