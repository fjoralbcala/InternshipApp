package myy803.TraineeshipApp.TraineeshipApp.repositories;

import myy803.TraineeshipApp.TraineeshipApp.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessorRepository extends JpaRepository<Professor,Integer> {

    Professor findByUsername(String username);
}
