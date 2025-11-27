package myy803.TraineeshipApp.TraineeshipApp.service;

import myy803.TraineeshipApp.TraineeshipApp.model.Evaluation;
import myy803.TraineeshipApp.TraineeshipApp.model.Professor;
import myy803.TraineeshipApp.TraineeshipApp.model.Student;
import myy803.TraineeshipApp.TraineeshipApp.model.TraineeshipPosition;
import myy803.TraineeshipApp.TraineeshipApp.repositories.EvaluationRepository;
import myy803.TraineeshipApp.TraineeshipApp.repositories.ProfessorRepository;
import myy803.TraineeshipApp.TraineeshipApp.repositories.StudentRepository;
import myy803.TraineeshipApp.TraineeshipApp.repositories.TraineeshipPositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommitteeServiceImpl implements CommitteeService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    TraineeshipPositionRepository positionRepository;

    @Autowired
    private PositionSearchFactory positionSearchFactory;

    @Autowired
    private SupervisorAssignmentFactory supervisorAssignmentFactory;

    @Autowired
    private EvaluationRepository evaluationRepository;


    @Override
    public List<Student> getStudentsAppliedForTraineeship() {
        return studentRepository.findByLookingForTraineeshipTrue();

    }

    @Override
    public List<TraineeshipPosition> retrievePositionsForApplicant(String applicantUsername, String strategy) {
        PositionSearchStrategy searchStrategy = positionSearchFactory.create(strategy);
        return searchStrategy.search(applicantUsername);
    }

    @Override
    public void assignPosition(int positionId, String studentUsername) {
        TraineeshipPosition position = positionRepository.findById(positionId).orElse(null);
        Student student = studentRepository.findByUsername(studentUsername);

        if (position != null && student != null) {
            if (student.getAssignedTraineeship() != null) {
                TraineeshipPosition previousPosition = student.getAssignedTraineeship();
                previousPosition.setStudent(null);
                previousPosition.setAssigned(false);
                positionRepository.save(previousPosition);
            }
            position.setStudent(student);
            position.setAssigned(true);
            student.setLookingForTraineeship(false);
            student.setAssignedTraineeship(position);

            positionRepository.save(position);
            studentRepository.save(student);
        }

    }

    @Override
    public boolean assignSupervisor(int positionId, String strategy) {
        SupervisorAssignmentStrategy assignmentStrategy = supervisorAssignmentFactory.create(strategy);
        Professor supervisor = assignmentStrategy.assign(positionId);

        if (supervisor != null) {
            TraineeshipPosition position = positionRepository.findById(positionId).orElse(null);
            if (position != null) {
                position.setSupervisor(supervisor);
                supervisor.addSupervisedPosition(position);

                positionRepository.save(position);
                professorRepository.save(supervisor);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<TraineeshipPosition> getActiveTraineeships() {
        List<TraineeshipPosition> allPositions = positionRepository.findAll();
        return allPositions.stream()
                .filter(position -> position.isAssigned() && position.getSupervisor() != null && position.getPassFailGrade() == null)
                .collect(Collectors.toList());
    }

    @Override
    public TraineeshipPosition getTraineeshipById(int positionId) {
        return positionRepository.findById(positionId).orElse(null);
    }

    @Override
    public void completeTraineeship(int positionId, boolean passFail) {
        TraineeshipPosition position = positionRepository.findById(positionId).orElse(null);

        if (position != null && position.isAssigned() && position.getSupervisor() != null) {
            position.setPassFailGrade(passFail);
            positionRepository.save(position);
            Student student = position.getStudent();
            if (student != null) {
                student.setAssignedTraineeship(null);
                student.setLookingForTraineeship(false);
                position.setStudent(null);
                position.setAssigned(false);
                studentRepository.save(student);
            }
            positionRepository.save(position);
        }
    }

    @Override
    public Map<String, Double> getAverageEvaluationScores(int positionId) {
        TraineeshipPosition position = positionRepository.findById(positionId).orElse(null);
        Map<String, Double> scores = new HashMap<>();

        if (position == null || position.getEvaluations() == null || position.getEvaluations().isEmpty()) {
            return scores;
        }

        // Initialize scores
        scores.put("motivation", 0.0);
        scores.put("effectiveness", 0.0);
        scores.put("efficiency", 0.0);
        scores.put("companyFacilities", 0.0);
        scores.put("companyGuidance", 0.0);
        scores.put("studentAverage", 0.0);
        scores.put("companyAverage", 0.0);
        scores.put("overallAverage", 0.0);

        int evaluationCount = 0;
        int companyEvaluationCount = 0;

        // Sum up all scores
        for (Evaluation eval : position.getEvaluations()) {
            evaluationCount++;

            // Student performance metrics (from both company and professor)
            scores.put("motivation", scores.get("motivation") + eval.getMotivation());
            scores.put("effectiveness", scores.get("effectiveness") + eval.getEffectiveness());
            scores.put("efficiency", scores.get("efficiency") + eval.getEfficiency());

            // Company metrics (only from professor evaluation)
            if (eval.getEvaluationType() == Evaluation.EvaluationType.PROFESSOR_EVALUATION) {
                companyEvaluationCount++;
                if (eval.getCompanyFacilities() != null) {
                    scores.put("companyFacilities", scores.get("companyFacilities") + eval.getCompanyFacilities());
                }
                if (eval.getCompanyGuidance() != null) {
                    scores.put("companyGuidance", scores.get("companyGuidance") + eval.getCompanyGuidance());
                }
            }
        }

        // Calculate averages
        if (evaluationCount > 0) {
            scores.put("motivation", scores.get("motivation") / evaluationCount);
            scores.put("effectiveness", scores.get("effectiveness") / evaluationCount);
            scores.put("efficiency", scores.get("efficiency") / evaluationCount);

            // Calculate student average
            double studentAvg = (scores.get("motivation") + scores.get("effectiveness") + scores.get("efficiency")) / 3.0;
            scores.put("studentAverage", studentAvg);
        }

        if (companyEvaluationCount > 0) {
            scores.put("companyFacilities", scores.get("companyFacilities") / companyEvaluationCount);
            scores.put("companyGuidance", scores.get("companyGuidance") / companyEvaluationCount);

            // Calculate company average
            double companyAvg = (scores.get("companyFacilities") + scores.get("companyGuidance")) / 2.0;
            scores.put("companyAverage", companyAvg);
        }

        // Calculate overall average
        double overallAvg = 0.0;
        int avgCount = 0;

        if (scores.get("studentAverage") > 0) {
            overallAvg += scores.get("studentAverage");
            avgCount++;
        }

        if (scores.get("companyAverage") > 0) {
            overallAvg += scores.get("companyAverage");
            avgCount++;
        }

        if (avgCount > 0) {
            scores.put("overallAverage", overallAvg / avgCount);
        }

        return scores;
    }

    @Override
    public boolean hasAllRequiredEvaluations(int positionId) {
        TraineeshipPosition position = positionRepository.findById(positionId).orElse(null);

        if (position == null || position.getEvaluations() == null) {
            return false;
        }

        boolean hasCompanyEvaluation = false;
        boolean hasProfessorEvaluation = false;

        for (Evaluation eval : position.getEvaluations()) {
            if (eval.getEvaluationType() == Evaluation.EvaluationType.COMPANY_EVALUATION) {
                hasCompanyEvaluation = true;
            } else if (eval.getEvaluationType() == Evaluation.EvaluationType.PROFESSOR_EVALUATION) {
                hasProfessorEvaluation = true;
            }
        }

        return hasCompanyEvaluation && hasProfessorEvaluation;
    }
}

