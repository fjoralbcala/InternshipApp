package myy803.TraineeshipApp.TraineeshipApp.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "companies")
public class Company {

    @Id
    @Column(name = "id")
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "username")
    private String username;

    @Column(name = "companyName")
    private String companyName;

    @Column(name = "companyLocation")
    private String companyLocation;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TraineeshipPosition> traineeshipPositions = new ArrayList<>();

    public Company() {

    }

    public Company(User user, String username, String companyName, String companyLocation) {
        this.user = user;
        this.id = user.getId();
        this.username = username;
        this.companyName = companyName;
        this.companyLocation = companyLocation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        this.id = user.getId();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyLocation() {
        return companyLocation;
    }

    public void setCompanyLocation(String companyLocation) {
        this.companyLocation = companyLocation;
    }

    public List<TraineeshipPosition> getTraineeshipPositions() {
        return traineeshipPositions;
    }

    public void setTraineeshipPositions(List<TraineeshipPosition> traineeshipPositions) {
        this.traineeshipPositions = traineeshipPositions;
    }

    public void addTraineeshipPosition(TraineeshipPosition traineeshipPosition) {
        this.traineeshipPositions.add(traineeshipPosition);
        traineeshipPosition.setCompany(this);
    }

    public void removeTraineeshipPosition(TraineeshipPosition traineeshipPosition) {
        this.traineeshipPositions.remove(traineeshipPosition);
        traineeshipPosition.setCompany(null);
    }

    public List<TraineeshipPosition> getAvailablePositions() {
        List<TraineeshipPosition> availablePositions = new ArrayList<>();
        for (TraineeshipPosition position : traineeshipPositions ) {
            if (!position.isAssigned()) {
                availablePositions.add(position);
            }
        }
        return availablePositions;
    }

    public List<TraineeshipPosition> getAssignedPositions() {
        List<TraineeshipPosition> assignedPositions = new ArrayList<>();
        for (TraineeshipPosition position : traineeshipPositions ) {
            if (position.isAssigned()) {
                assignedPositions.add(position);
            }
        }
        return assignedPositions;
    }

    @Override
    public String toString() {
        return "Company{" + "id=" + id + ", username=" + username + ", companyName=" + companyName + ", companyLocation=" + companyLocation + '}';
    }

}
