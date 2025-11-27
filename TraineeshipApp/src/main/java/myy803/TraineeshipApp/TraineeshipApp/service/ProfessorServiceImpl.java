package myy803.TraineeshipApp.TraineeshipApp.service;


import myy803.TraineeshipApp.TraineeshipApp.model.Evaluation;
import myy803.TraineeshipApp.TraineeshipApp.model.Professor;
import myy803.TraineeshipApp.TraineeshipApp.model.TraineeshipPosition;
import myy803.TraineeshipApp.TraineeshipApp.repositories.EvaluationRepository;
import myy803.TraineeshipApp.TraineeshipApp.repositories.ProfessorRepository;
import myy803.TraineeshipApp.TraineeshipApp.repositories.TraineeshipPositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProfessorServiceImpl implements ProfessorService{

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private TraineeshipPositionRepository traineeshipPositionRepository;

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Override
    public Professor saveProfile(Professor professor) {
        return professorRepository.save(professor);
    }

    @Override
    public Professor getProfessorProfile(String username) {
        return professorRepository.findByUsername(username);
    }

    @Override
    public List<Professor> getAllProfessors() {
        return professorRepository.findAll();
    }

    @Override
    public Professor getProfessorById(int id) {
        return professorRepository.findById(id).orElse(null);
    }

    @Override
    public List<TraineeshipPosition> getSupervisedPositions(String username) {
        Professor professor = professorRepository.findByUsername(username);
        if (professor != null && professor.getSupervisedPositions() != null) {
            return professor.getSupervisedPositions();
        }
        return new ArrayList<>();
    }

    @Override
    public void evaluateTraineeship(String username, int positionId, Evaluation evaluation) {
        Professor professor = professorRepository.findByUsername(username);
        TraineeshipPosition position = traineeshipPositionRepository.findById(positionId).orElse(null);

        if (professor != null && position != null && position.getSupervisor() != null &&
                position.getSupervisor().getId() == professor.getId()) {

            // Check if an evaluation already exists
            Evaluation existingEvaluation = null;

            if (position.getEvaluations() != null) {
                for (Evaluation eval : position.getEvaluations()) {
                    if (eval.getEvaluationType() == Evaluation.EvaluationType.PROFESSOR_EVALUATION) {
                        existingEvaluation = eval;
                        break;
                    }
                }
            }

            if (existingEvaluation != null) {
                // Update existing evaluation
                existingEvaluation.setMotivation(evaluation.getMotivation());
                existingEvaluation.setEffectiveness(evaluation.getEffectiveness());
                existingEvaluation.setEfficiency(evaluation.getEfficiency());
                existingEvaluation.setCompanyFacilities(evaluation.getCompanyFacilities());
                existingEvaluation.setCompanyGuidance(evaluation.getCompanyGuidance());

                // Save the updated evaluation
                evaluationRepository.save(existingEvaluation);
            } else {
                // Create new evaluation
                evaluation.setEvaluationType(Evaluation.EvaluationType.PROFESSOR_EVALUATION);
                evaluation.setTraineeshipPosition(position);

                // Save the new evaluation
                evaluationRepository.save(evaluation);

                // Add evaluation to position's evaluations list
                if (position.getEvaluations() == null) {
                    position.setEvaluations(new ArrayList<>());
                }
                position.getEvaluations().add(evaluation);
                traineeshipPositionRepository.save(position);
            }
        }
    }

    @Override
    public Evaluation getProfessorEvaluation(int positionId) {
        TraineeshipPosition position = traineeshipPositionRepository.findById(positionId).orElse(null);

        if (position == null || position.getEvaluations() == null) {
            return null;
        }

        for (Evaluation eval : position.getEvaluations()) {
            if (eval.getEvaluationType() == Evaluation.EvaluationType.PROFESSOR_EVALUATION) {
                return eval;
            }
        }

        return null;
    }




}
