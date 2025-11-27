package myy803.TraineeshipApp.TraineeshipApp.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "professors")
public class Professor {

    @Id
    @Column(name = "id")
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "username")
    private String username;

    @Column(name = "professor_name")
    private String professorName;

    @ElementCollection
    @CollectionTable(name = "professor_interests", joinColumns = @JoinColumn(name = "professor_id"))
    @Column(name = "interests")
    private List<String> interests = new ArrayList<>();

    @OneToMany(mappedBy = "supervisor")
    private List<TraineeshipPosition> supervisedPositions = new ArrayList<>();

    public Professor() {
    }

    public Professor(User user, String username, String professorName, List<String> interests) {
        this.user = user;
        this.id = user.getId();
        this.username = username;
        this.professorName = professorName;
        this.interests = interests;
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

    public String getProfessorName() {
        return professorName;
    }

    public void setProfessorName(String professorName) {
        this.professorName = professorName;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public List<TraineeshipPosition> getSupervisedPositions() {
        return supervisedPositions;
    }

    public void setSupervisedPositions(List<TraineeshipPosition> supervisedPositions) {
        this.supervisedPositions = supervisedPositions;
    }

    public void addSupervisedPosition(TraineeshipPosition supervisedPosition) {
        this.supervisedPositions.add(supervisedPosition);
        supervisedPosition.setSupervisor(this);
    }

    public void removeSupervisedPosition(TraineeshipPosition supervisedPosition) {
        this.supervisedPositions.remove(supervisedPosition);
        supervisedPosition.setSupervisor(null);
    }

    public int getSupervisioLoad(){
        return supervisedPositions.size();
    }

    public int getActiveTraineeshipCount() {
        if (supervisedPositions == null || supervisedPositions.isEmpty()) {
            return 0;
        }

        int count = 0;
        for (TraineeshipPosition position : supervisedPositions) {
            // Count positions that are assigned, have a supervisor, but are not completed yet
            if (position.isAssigned() && position.getSupervisor() != null && position.getPassFailGrade() == null) {
                count++;
            }
        }
        return count;
    }


    @Override
    public String toString() {
        return "Professor{" + "id=" + id + ", username=" + username + ", professorName=" + professorName + ", interests=" + interests + '}';
    }

}
