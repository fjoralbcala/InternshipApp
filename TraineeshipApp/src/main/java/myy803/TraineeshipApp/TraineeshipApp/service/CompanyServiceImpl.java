package myy803.TraineeshipApp.TraineeshipApp.service;

import myy803.TraineeshipApp.TraineeshipApp.model.Evaluation;
import myy803.TraineeshipApp.TraineeshipApp.model.TraineeshipPosition;
import myy803.TraineeshipApp.TraineeshipApp.model.Company;
import myy803.TraineeshipApp.TraineeshipApp.repositories.CompanyRepository;
import myy803.TraineeshipApp.TraineeshipApp.repositories.EvaluationRepository;
import myy803.TraineeshipApp.TraineeshipApp.repositories.TraineeshipPositionRepository;
import myy803.TraineeshipApp.TraineeshipApp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TraineeshipPositionRepository traineeshipPositionRepository;

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Override
    public Company saveProfile(Company company) {
        return companyRepository.save(company);
    }

    @Override
    public Company getCompanyProfile(String username) {
        return companyRepository.findByUsername(username);
    }

    @Override
    public List<TraineeshipPosition> getAssignedPositions(String companyUsername) {
        Company company = companyRepository.findByUsername(companyUsername);
        if (company != null) {
            return traineeshipPositionRepository.findByCompanyAndIsAssigned(company, true);
        }
        return new ArrayList<>();
    }

    @Override
    public void evaluateTraineeship(String companyUsername, int positionId, Evaluation evaluation) {
        Company company = companyRepository.findByUsername(companyUsername);
        TraineeshipPosition position = traineeshipPositionRepository.findById(positionId).orElse(null);

        if (company != null && position != null && position.getCompany().getId() == company.getId()) {
            // Check if an evaluation already exists
            Evaluation existingEvaluation = null;

            if (position.getEvaluations() != null) {
                for (Evaluation eval : position.getEvaluations()) {
                    if (eval.getEvaluationType() == Evaluation.EvaluationType.COMPANY_EVALUATION) {
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

                // Save the updated evaluation
                evaluationRepository.save(existingEvaluation);
            } else {
                // Create new evaluation
                evaluation.setEvaluationType(Evaluation.EvaluationType.COMPANY_EVALUATION);
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
    public Evaluation getCompanyEvaluation(int positionId) {
        TraineeshipPosition position = traineeshipPositionRepository.findById(positionId).orElse(null);

        if (position == null || position.getEvaluations() == null) {
            return null;
        }

        for (Evaluation eval : position.getEvaluations()) {
            if (eval.getEvaluationType() == Evaluation.EvaluationType.COMPANY_EVALUATION) {
                return eval;
            }
        }

        return null;
    }
}
