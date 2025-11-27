package myy803.TraineeshipApp.TraineeshipApp.model;

import jakarta.persistence.*;
import java.util.List;


@Entity
@Table(name = "students")
public class Student {

    @Id
    @Column(name = "id")
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "username")
    private String username;

    @Column(name = "student_name")
    private String studentName;

    @Column(name = "student_number")
    private String studentNumber;

    @Column(name = "avg_grade")
    private Double avgGrade;

    @Column(name = "preferred_location")
    private String preferredLocation;

    @ElementCollection
    @CollectionTable(name = "student_interests", joinColumns = @JoinColumn(name = "student_id"))
    @Column(name = "interests")
    private List<String> interests;

    @ElementCollection
    @CollectionTable(name = "student_skills", joinColumns = @JoinColumn(name = "student_id"))
    @Column(name = "skills")
    private List<String> skills;

    @Column(name = "looking_for_traineeship")
    private boolean lookingForTraineeship;

    @OneToOne(mappedBy = "student")
    private TraineeshipPosition assignedTraineeship;

    public Student() {
    }

    public Student(User user, String studentName, Double avgGrade, String preferredLocation, List<String> interests, List<String> skills, boolean lookingForTraineeship) {
        this.user = user;
        this.id = user.getId();
        this.studentName = studentName;
        this.avgGrade = avgGrade;
        this.preferredLocation = preferredLocation;
        this.interests = interests;
        this.skills = skills;
        this.lookingForTraineeship = lookingForTraineeship;
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

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Double getAvgGrade() {
        return avgGrade;
    }

    public void setAvgGrade(Double avgGrade) {
        this.avgGrade = avgGrade;
    }

    public String getPreferredLocation() {
        return preferredLocation;
    }

    public void setPreferredLocation(String preferredLocation) {
        this.preferredLocation = preferredLocation;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public boolean isLookingForTraineeship() {
        return lookingForTraineeship;
    }

    public void setLookingForTraineeship(boolean lookingForTraineeship) {
        this.lookingForTraineeship = lookingForTraineeship;
    }

    public TraineeshipPosition getAssignedTraineeship() {
        return assignedTraineeship;
    }

    public void setAssignedTraineeship(TraineeshipPosition assignedTraineeship) {
        this.assignedTraineeship = assignedTraineeship;
    }

    @Override
    public String toString() {
        return "Student{" + "id=" + id + ", user=" + user.getUsername() + ", studentName=" + studentName + ", avgGrade=" + avgGrade + ", preferredLocation=" + preferredLocation + ", interests=" + interests + ", skills=" + skills + ", lookingForTraineeship=" + lookingForTraineeship + '}';
    }

}
