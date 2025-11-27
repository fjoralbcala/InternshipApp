package myy803.TraineeshipApp.TraineeshipApp.service;

import myy803.TraineeshipApp.TraineeshipApp.model.TraineeshipPosition;
import myy803.TraineeshipApp.TraineeshipApp.repositories.TraineeshipPositionRepository;
import org.springframework.stereotype.Service;
import myy803.TraineeshipApp.TraineeshipApp.model.Student;
import myy803.TraineeshipApp.TraineeshipApp.repositories.StudentRepository;
import myy803.TraineeshipApp.TraineeshipApp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;


@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TraineeshipPositionRepository traineeshipPositionRepository;

    @Override
    public Student saveProfile(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public Student getStudentProfile(String username) {
        return studentRepository.findByUsername(username);
    }

    @Override
    public TraineeshipPosition getStudentTraineeship(String username) {
        Student student = studentRepository.findByUsername(username);
        if (student != null) {
            return student.getAssignedTraineeship();
        }
        return null;
    }

    @Override
    public void updateLogbook(String username, String logbookContent) {
        Student student = studentRepository.findByUsername(username);
        if (student != null && student.getAssignedTraineeship() != null) {
            TraineeshipPosition position = student.getAssignedTraineeship();
            position.setStudentLogbook(logbookContent);
            traineeshipPositionRepository.save(position);
        }
    }
}
