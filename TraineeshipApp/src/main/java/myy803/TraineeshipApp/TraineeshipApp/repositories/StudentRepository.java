package myy803.TraineeshipApp.TraineeshipApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import myy803.TraineeshipApp.TraineeshipApp.model.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student,Integer> {
    Student findByUsername(String username);
    List<Student> findByLookingForTraineeshipTrue();
}
