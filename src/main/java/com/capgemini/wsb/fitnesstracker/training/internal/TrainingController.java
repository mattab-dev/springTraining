package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.NewTrainingTO;
import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingProvider;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingTO;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/v1/trainings")
@RequiredArgsConstructor
class TrainingController {
    private final TrainingProvider trainingProvider;
    private final TrainingMapper trainingMapper;
    private final TrainingRepository trainingRepository;
    private final TrainingServiceImpl trainingService;
    @GetMapping
    public List<TrainingTO> getTrainings() {
        return trainingProvider.getTrainings().stream().map(trainingMapper::toTraining).collect(toList());
    }

    @PostMapping()
    @ResponseStatus(CREATED)
    public TrainingTO addTraining(@RequestBody NewTrainingTO trainingTO) {
        final Training savedEntity = trainingService.processTrainingEntity(trainingTO);
        return trainingMapper.toTraining(savedEntity);
    }

    @GetMapping("/{userId}")
    public List<TrainingTO> getTrainingsForUser(@PathVariable("userId") final Long userId) {
        return trainingRepository.findByUserId(userId).stream().map(trainingMapper::toTraining).collect(toList());
    }

    @GetMapping("/finished/{afterTime}")
    public List<TrainingTO> getTrainingsForUser(@PathVariable("afterTime") @DateTimeFormat(pattern = "yyyy-MM-dd") final Date endDate) {
        return trainingRepository.findByEndTimeAfter(endDate).stream().map(trainingMapper::toTraining).collect(toList());
    }
    @GetMapping("/activityType")
    public List<TrainingTO> getTrainingsForActivityType(@RequestParam("activityType") final ActivityType activityType) {
        return trainingRepository.findByActivityType(activityType).stream().map(trainingMapper::toTraining).collect(toList());
    }

    @PutMapping("/{trainingId}")
    public TrainingTO updateTraining(@PathVariable Long trainingId, @RequestBody NewTrainingTO trainingTO) {
        Training mappedEntity = trainingMapper.toEntityUpdate(trainingTO);
        mappedEntity.setId(trainingId);
        trainingRepository.save(mappedEntity);
        return trainingMapper.toTraining(mappedEntity);
    }

}
