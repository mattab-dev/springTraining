package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingProvider;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
