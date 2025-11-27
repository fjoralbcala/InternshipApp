package myy803.TraineeshipApp.TraineeshipApp.repositories;

import myy803.TraineeshipApp.TraineeshipApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommitteeRepository extends JpaRepository<User,Integer> {
    User findByUsername(String username);
}
