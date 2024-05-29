package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.NewTrainingTO;
import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingProvider;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.api.UserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
class TrainingServiceImpl implements TrainingProvider {
    private final TrainingRepository trainingRepository;
    private final UserProvider userProvider;
    private final TrainingMapper trainingMapper;

    @Override
    public Optional<User> getTraining(final Long trainingId) {
        throw new UnsupportedOperationException("Not finished yet");
    }

    @Override
    public List<Training> getTrainings() {
        return trainingRepository.findAll();
    }

    @Override
    public List<Training> findAllForUserId(Long userId) {
        return trainingRepository.findByUserId(userId);
    }

    public Training processTrainingEntity(final NewTrainingTO trainingTO) {
        final User linkedUser = userProvider.getUser(trainingTO.getUserId()).get();
        final Training mappedTraining = trainingMapper.toEntity(trainingTO);
        mappedTraining.setUser(linkedUser);
        trainingRepository.save(mappedTraining);
        return  mappedTraining;
    }
}
