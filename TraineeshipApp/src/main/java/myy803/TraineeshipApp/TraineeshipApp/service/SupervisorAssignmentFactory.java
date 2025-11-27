package myy803.TraineeshipApp.TraineeshipApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SupervisorAssignmentFactory {

    @Autowired
    private AssignmentBasedOnInterests assignmentBasedOnInterests;

    @Autowired
    private AssignmentBasedOnLoad assignmentBasedOnLoad;

    public SupervisorAssignmentStrategy create(String strategy) {
        switch (strategy.toLowerCase()) {
            case "interests":
                return assignmentBasedOnInterests;
            case "load":
                return assignmentBasedOnLoad;
            default:
                // Default to interest-based strategy
                return assignmentBasedOnInterests;
        }
    }
}
