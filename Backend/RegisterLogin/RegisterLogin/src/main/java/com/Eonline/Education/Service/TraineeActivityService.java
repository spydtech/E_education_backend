package com.Eonline.Education.Service;

import com.Eonline.Education.modals.TraineeCredentialGenerator;
import com.Eonline.Education.repository.TraineeRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TraineeActivityService {

    @Getter
    private final Map<String, TraineeCredentialGenerator> activeTrainees = new ConcurrentHashMap<>();

    @Autowired
    private TraineeRepository traineeRepository;

    public void traineeLoggedIn(TraineeCredentialGenerator trainee) {
        activeTrainees.put(trainee.getUserId(), trainee);
    }

    public void traineeLoggedOut(String username) {
        activeTrainees.remove(username);
    }

    public int getActiveTraineeCount() {
        return activeTrainees.size();
    }

    public int getInactiveTraineeCount() {
        long totalTraineeCount = traineeRepository.count();
        return (int) (totalTraineeCount - activeTrainees.size());
    }
}
