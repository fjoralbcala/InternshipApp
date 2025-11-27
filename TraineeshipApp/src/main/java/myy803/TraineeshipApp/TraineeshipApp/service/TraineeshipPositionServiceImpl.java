package myy803.TraineeshipApp.TraineeshipApp.service;

import myy803.TraineeshipApp.TraineeshipApp.model.Company;
import myy803.TraineeshipApp.TraineeshipApp.model.TraineeshipPosition;
import myy803.TraineeshipApp.TraineeshipApp.repositories.TraineeshipPositionRepository;
import myy803.TraineeshipApp.TraineeshipApp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class TraineeshipPositionServiceImpl implements TraineeshipPositionService {

    @Autowired
    private TraineeshipPositionRepository traineeshipPositionRepository;


    @Override
    public TraineeshipPosition savePosition(TraineeshipPosition traineeshipPosition) {
        return traineeshipPositionRepository.save(traineeshipPosition);
    }

    @Override
    public TraineeshipPosition findById(int id) {
        Optional<TraineeshipPosition> result = traineeshipPositionRepository.findById(id);
        return result.orElse(null);
    }

    @Override
    public List<TraineeshipPosition> findByCompany(Company company) {
        return traineeshipPositionRepository.findByCompany(company);
    }

    @Override
    public List<TraineeshipPosition> findAvailablePositionsByCompany(Company company) {
        return traineeshipPositionRepository.findByCompanyAndIsAssigned(company,false);
    }

    @Override
    public List<TraineeshipPosition> findAssignedPositionsByCompany(Company company) {
        return traineeshipPositionRepository.findByCompanyAndIsAssigned(company, true);
    }

    @Override
    public TraineeshipPosition deleteById(int id) {
        Optional<TraineeshipPosition> result = traineeshipPositionRepository.findById(id);
        if (result.isPresent()) {
            traineeshipPositionRepository.deleteById(id);
            return result.get();
        }
        return null;
    }

    @Override
    public List<TraineeshipPosition> findAll() {
        return traineeshipPositionRepository.findAll();
    }
}
