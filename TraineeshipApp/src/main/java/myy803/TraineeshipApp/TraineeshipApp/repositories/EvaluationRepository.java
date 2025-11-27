package myy803.TraineeshipApp.TraineeshipApp.repositories;

import myy803.TraineeshipApp.TraineeshipApp.model.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Integer> {

}
