package myy803.TraineeshipApp.TraineeshipApp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "evaluations")
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "evaluation_type")
    private EvaluationType evaluationType;

    @Column(name = "motivation")
    private int motivation; // Scale 1-5

    @Column(name = "efficiency")
    private int efficiency; // Scale 1-5

    @Column(name = "effectiveness")
    private int effectiveness; // Scale 1-5

    // For professor evaluations only - company assessment
    @Column(name = "company_facilities")
    private Integer companyFacilities; // Scale a1-5, nullable

    @Column(name = "company_guidance")
    private Integer companyGuidance; // Scale 1-5, nullable

    @ManyToOne
    @JoinColumn(name = "position_id")
    private TraineeshipPosition traineeshipPosition;

    // Define the evaluation type (who created this evaluation)
    public enum EvaluationType {
        COMPANY_EVALUATION,
        PROFESSOR_EVALUATION
    }

    // Default constructor
    public Evaluation() {
    }

    // Constructor for company evaluations
    public Evaluation(int motivation, int efficiency, int effectiveness, TraineeshipPosition traineeshipPosition) {
        this.motivation = motivation;
        this.efficiency = efficiency;
        this.effectiveness = effectiveness;
        this.traineeshipPosition = traineeshipPosition;
        this.evaluationType = EvaluationType.COMPANY_EVALUATION;
    }

    // Constructor for professor evaluations
    public Evaluation(int motivation, int efficiency, int effectiveness,
                      int companyFacilities, int companyGuidance,
                      TraineeshipPosition traineeshipPosition) {
        this.motivation = motivation;
        this.efficiency = efficiency;
        this.effectiveness = effectiveness;
        this.companyFacilities = companyFacilities;
        this.companyGuidance = companyGuidance;
        this.traineeshipPosition = traineeshipPosition;
        this.evaluationType = EvaluationType.PROFESSOR_EVALUATION;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public EvaluationType getEvaluationType() {
        return evaluationType;
    }

    public void setEvaluationType(EvaluationType evaluationType) {
        this.evaluationType = evaluationType;
    }

    public int getMotivation() {
        return motivation;
    }

    public void setMotivation(int motivation) {
        if (motivation < 1 || motivation > 5) {
            throw new IllegalArgumentException("Motivation rating must be between 1 and 5");
        }
        this.motivation = motivation;
    }

    public int getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(int efficiency) {
        if (efficiency < 1 || efficiency > 5) {
            throw new IllegalArgumentException("Efficiency rating must be between 1 and 5");
        }
        this.efficiency = efficiency;
    }

    public int getEffectiveness() {
        return effectiveness;
    }

    public void setEffectiveness(int effectiveness) {
        if (effectiveness < 1 || effectiveness > 5) {
            throw new IllegalArgumentException("Effectiveness rating must be between 1 and 5");
        }
        this.effectiveness = effectiveness;
    }

    public Integer getCompanyFacilities() {
        return companyFacilities;
    }

    public void setCompanyFacilities(Integer companyFacilities) {
        if (companyFacilities != null && (companyFacilities < 1 || companyFacilities > 5)) {
            throw new IllegalArgumentException("Company facilities rating must be between 1 and 5");
        }
        this.companyFacilities = companyFacilities;
    }

    public Integer getCompanyGuidance() {
        return companyGuidance;
    }

    public void setCompanyGuidance(Integer companyGuidance) {
        if (companyGuidance != null && (companyGuidance < 1 || companyGuidance > 5)) {
            throw new IllegalArgumentException("Company guidance rating must be between 1 and 5");
        }
        this.companyGuidance = companyGuidance;
    }

    public TraineeshipPosition getTraineeshipPosition() {
        return traineeshipPosition;
    }

    public void setTraineeshipPosition(TraineeshipPosition traineeshipPosition) {
        this.traineeshipPosition = traineeshipPosition;
    }

    /**
     * Calculate the average score of student performance (motivation, efficiency, effectiveness)
     */
    public double getStudentAverageScore() {
        return (motivation + efficiency + effectiveness) / 3.0;
    }

    /**
     * Calculate the average company score (only for professor evaluations)
     */
    public Double getCompanyAverageScore() {
        if (companyFacilities == null || companyGuidance == null) {
            return null;
        }
        return (companyFacilities + companyGuidance) / 2.0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Evaluation{")
                .append("id=").append(id)
                .append(", type=").append(evaluationType)
                .append(", student ratings: motivation=").append(motivation)
                .append(", efficiency=").append(efficiency)
                .append(", effectiveness=").append(effectiveness);

        if (evaluationType == EvaluationType.PROFESSOR_EVALUATION) {
            sb.append(", company ratings: facilities=").append(companyFacilities)
                    .append(", guidance=").append(companyGuidance);
        }

        sb.append(", position=").append(traineeshipPosition != null ? traineeshipPosition.getId() : null)
                .append('}');

        return sb.toString();
    }
}