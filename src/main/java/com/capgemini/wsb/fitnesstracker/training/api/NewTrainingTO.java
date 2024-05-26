package com.capgemini.wsb.fitnesstracker.training.api;

import com.capgemini.wsb.fitnesstracker.training.internal.ActivityType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewTrainingTO {
    private Long id;
    private Long userId;
    private Date startTime;
    private Date endTime;
    private ActivityType activityType;
    private  Double distance;
    private Double averageSpeed;
}
