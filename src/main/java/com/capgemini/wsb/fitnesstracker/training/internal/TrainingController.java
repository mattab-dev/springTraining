package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingProvider;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingTO;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/v1/training")
@RequiredArgsConstructor
public class TrainingController {
    private final TrainingProvider trainingProvider;
    private final TrainingMapper trainingMapper;
    private final TrainingRepository trainingRepository;
    @GetMapping
    public List<TrainingTO> getTrainings() {
        return trainingProvider.getTrainings().stream().map(trainingMapper::toTraining).collect(toList());
    }

    @PostMapping("/add")
    public ResponseEntity<TrainingTO> addTraining(@RequestBody TrainingTO trainingTO) {
        final Training training = trainingRepository.save(trainingMapper.toEntity(trainingTO));
        trainingTO.setId(training.getId());
        return ResponseEntity.ok().body(trainingTO);
    }

    @GetMapping("/{userId}")
    public List<TrainingTO> getTrainingsForUser(@PathVariable("userId") final Long userId) {
        return trainingRepository.findByUserId(userId).stream().map(trainingMapper::toTraining).collect(toList());
    }

    @GetMapping("/endDate/{endDate}")
    public List<TrainingTO> getTrainingsForUser(@PathVariable("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") final Date endDate) {
        return trainingRepository.findByEndTimeAfter(endDate).stream().map(trainingMapper::toTraining).collect(toList());
    }
    @GetMapping("/type/{activityType}")
    public List<TrainingTO> getTrainingsForActivityType(@PathVariable("activityType") final ActivityType activityType) {
        return trainingRepository.findByActivityType(activityType).stream().map(trainingMapper::toTraining).collect(toList());
    }

    @PutMapping("/update")
    public ResponseEntity<TrainingTO> updateTraining(@RequestBody TrainingTO trainingTO) {
        trainingRepository.save(trainingMapper.toEntityUpdate(trainingTO));
        return ResponseEntity.ok().body(trainingTO);
    }

}
