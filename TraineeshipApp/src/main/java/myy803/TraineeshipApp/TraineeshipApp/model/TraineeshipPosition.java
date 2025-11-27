package myy803.TraineeshipApp.TraineeshipApp.model;

import jakarta.persistence.*;

import javax.annotation.processing.Generated;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "traineeship_positions")
public class TraineeshipPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name="title")
    private String title;

    @Column(name="description")
    private String description;

    @Column(name= "from_date")
    private LocalDate fromDate;

    @Column(name= "to_date")
    private LocalDate toDate;

    @ElementCollection
    @CollectionTable(name = "traineeship_topics", joinColumns = @JoinColumn(name = "position_id"))
    @Column(name = "topics")
    private List<String> topics = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "traineeship_required_skills", joinColumns = @JoinColumn(name = "position_id"))
    @Column(name = "skills")
    private List<String> requiredSkills = new ArrayList<>();

    @Column(name = "is_assigned")
    private boolean isAssigned = false;

    @Column(name = "student_logbook")
    private String studentLogbook;

    @Column(name = "pass_fail_grade")
    private Boolean passFailGrade;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "professor_id")
    private Professor supervisor;

    @OneToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @OneToMany(mappedBy = "traineeshipPosition", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Evaluation> evaluations = new ArrayList<>();

    public TraineeshipPosition() {
    }

    public TraineeshipPosition(String title, String description, LocalDate fromDate, LocalDate toDate,
                               List<String> topics, List<String> requiredSkills, Company company) {
        this.title = title;
        this.description = description;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.topics = topics;
        this.requiredSkills = requiredSkills;
        this.company = company;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    public List<String> getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(List<String> requiredSkills) {
        this.requiredSkills = requiredSkills;
    }

    public boolean isAssigned() {
        return isAssigned;
    }

    public void setAssigned(boolean isAssigned) {
        this.isAssigned = isAssigned;
    }

    public String getStudentLogbook() {
        return studentLogbook;
    }

    public void setStudentLogbook(String studentLogbook) {
        this.studentLogbook = studentLogbook;
    }

    public Boolean getPassFailGrade() {
        return passFailGrade;
    }

    public void setPassFailGrade(Boolean passFail) {
        this.passFailGrade = passFail;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Professor getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Professor supervisor) {
        this.supervisor = supervisor;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
        if (student != null) {
            this.isAssigned = true;
        } else {
            this.isAssigned = false;
        }
    }

    public List<Evaluation> getEvaluations() {
        return evaluations;
    }

    public void setEvaluations(List<Evaluation> evaluations) {
        this.evaluations = evaluations;
    }

    public void addEvaluation(Evaluation evaluation) {
        evaluations.add(evaluation);
        evaluation.setTraineeshipPosition(this);
    }

    public void removeEvaluation(Evaluation evaluation) {
        evaluations.remove(evaluation);
        evaluation.setTraineeshipPosition(null);
    }

    @Override
    public String toString() {
        return "TraineeshipPosition{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", fromDate=" + fromDate +
                ", toDate=" + toDate +
                ", topics=" + topics +
                ", requiredSkills=" + requiredSkills +
                ", isAssigned=" + isAssigned +
                ", company=" + (company != null ? company.getId() : null) +
                ", supervisor=" + (supervisor != null ? supervisor.getId() : null) +
                ", student=" + (student != null ? student.getId() : null) +
                '}';
    }









}
