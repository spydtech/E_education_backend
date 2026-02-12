package com.Eonline.Education.Service;

import com.Eonline.Education.modals.TraineeCredentialGenerator;
import jakarta.mail.MessagingException;

import java.util.List;
import java.util.Map;

public interface TraineeService {
    String createUserId(TraineeCredentialGenerator traineeCredentialGenerator) throws MessagingException;

    void logInTrainee(TraineeCredentialGenerator trainee);

    void logOutTrainee(String userId);

    public List<TraineeCredentialGenerator> getAllTrainees();
    TraineeCredentialGenerator findUserProfileByJwt(String jwt) throws Exception;

    void logInTraineeEmail(TraineeCredentialGenerator traineeCredentialGenerator);

    TraineeCredentialGenerator update(TraineeCredentialGenerator traineeCredentialGenerator);

    String delete(String email);

    Map<String, Long> countOfTrainers(String jwt);
}


//package com.Eonline.Education.Service;
//
//import com.Eonline.Education.modals.TraineeCredentialGenerator;
//import jakarta.mail.MessagingException;
//
//public interface TraineeService {
//    String createUserName(TraineeCredentialGenerator traineeCredentialGenerator) throws MessagingException;
//}
