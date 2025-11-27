package myy803.TraineeshipApp.TraineeshipApp.service;

import myy803.TraineeshipApp.TraineeshipApp.model.Student;
import myy803.TraineeshipApp.TraineeshipApp.model.TraineeshipPosition;

public interface StudentService {
    Student saveProfile(Student student);
    Student getStudentProfile(String username);

    TraineeshipPosition getStudentTraineeship(String username);

    void updateLogbook(String username, String logbookContent);

}
