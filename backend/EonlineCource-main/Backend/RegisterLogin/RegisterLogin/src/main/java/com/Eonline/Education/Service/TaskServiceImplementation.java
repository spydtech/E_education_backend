package com.Eonline.Education.Service;

import com.Eonline.Education.Configuration.JwtTokenProvider;
import com.Eonline.Education.modals.*;
import com.Eonline.Education.repository.*;
import com.Eonline.Education.repository.TaskUserRepository;
import com.Eonline.Education.response.ApiResponse;
import com.Eonline.Education.response.TaskResponse;
import com.Eonline.Education.response.UserResponse;
import com.Eonline.Education.user.SubmissionStatus;
import com.Eonline.Education.user.TaskStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TaskServiceImplementation implements TaskService {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    TraineeRepository traineeRepository;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    ChatGroupRepository chatGroupRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    TaskUserRepository taskUserRepository;

    // trainer
    @Override
    public TaskResponse taskUploadByTrainer(String jwt, MultipartFile file, String assignmentDescription,
                                            LocalDate dueDate, String chatGroup, List<String> users,
                                            LocalDate assignmentDate) throws IOException {
        String traineeEmail = jwtTokenProvider.getEmailFromJwtToken(jwt);
        TraineeCredentialGenerator trainer = traineeRepository.findByEmail(traineeEmail);

        Task task = new Task();
        task.setAssignmentDescription(assignmentDescription);
        task.setAssignmentDate(assignmentDate);
        task.setDueDate(dueDate);
        task.setFile(file.getBytes());
        task.setName(file.getOriginalFilename());
        task.setType(file.getContentType());
        task.setTraineeEmail(trainer.getEmail());

        Optional<ChatGroup> optional = chatGroupRepository.findByNameIgnoreCase(chatGroup);
        if (optional.isPresent()) {
            ChatGroup group = optional.get();
            task.setChatGroup(group);
        }

        List<TaskUser> taskUsers = new ArrayList<>();
        if (users != null && !users.isEmpty()) {
            for (String email : users) {
                // ✅ FIXED: Handle Optional properly
                Optional<User> userOptional = userRepository.findByEmail(email);
                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    TaskUser taskUser = new TaskUser();
                    taskUser.setTask(task);
                    taskUser.setUser(user);
                    taskUser.setTaskStatus(TaskStatus.PENDING);
                    taskUser.setSubmissionStatus(SubmissionStatus.PENDING);
                    taskUsers.add(taskUser);
                }
            }
        }

        task.setUsers(taskUsers);
        taskRepository.save(task);
        return taskToResponse(task);
    }

    @Override
    public List<TaskResponse> getAll(String jwt) {
        String email = jwtTokenProvider.getEmailFromJwtToken(jwt);
        TraineeCredentialGenerator trainee = traineeRepository.findByEmail(email);
        List<TaskUser> taskUsers = taskUserRepository.findByTaskTraineeEmail(trainee.getEmail());
        Set<Task> uniqueTasks = taskUsers.stream()
                .map(TaskUser::getTask)
                .collect(Collectors.toSet());
        return uniqueTasks.stream()
                .map(this::taskToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskResponse> getAllSubmission(String jwt) {
        String email = jwtTokenProvider.getEmailFromJwtToken(jwt);
        TraineeCredentialGenerator trainee = traineeRepository.findByEmail(email);
        List<TaskUser> taskUsers = taskUserRepository.findByTaskTraineeEmail(trainee.getEmail());
        Set<Long> processedTaskIds = new HashSet<>();
        return taskUsers.stream()
                .map(TaskUser::getTask)
                .distinct()
                .filter(task -> processedTaskIds.add(task.getId()))
                .flatMap(task -> getAllSubmissionResponse(task).stream())
                .collect(Collectors.toList());
    }

    public List<TaskResponse> getAllSubmissionResponse(Task task) {
        TraineeCredentialGenerator trainer = traineeRepository.findByEmail(task.getTraineeEmail());

        List<TaskResponse> responses = new ArrayList<>();

        for (TaskUser taskUser : task.getUsers()) {
            TaskResponse response = new TaskResponse();
            response.setId(task.getId());
            response.setAssignmentDescription(task.getAssignmentDescription());
            response.setAssignmentDate(task.getAssignmentDate());
            response.setDueDate(task.getDueDate());

            if (trainer.getLastName() != null) {
                response.setTrainerName(trainer.getFirstName() + " " + trainer.getLastName());
            } else {
                response.setTrainerName(trainer.getFirstName());
            }

            response.setParticipants(task.getUsers().size());

            if (task.getChatGroup() != null) {
                response.setChatGroup(task.getChatGroup().getName());
            }

            User user = taskUser.getUser();
            String fullName = (user.getLastName() == null || user.getLastName().isEmpty())
                    ? user.getFirstName()
                    : user.getFirstName() + " " + user.getLastName();

            List<Payment> payment = paymentRepository.findAllByUserEmail(user.getEmail());
            if (!payment.isEmpty()) {
                response.setUsers(Collections.singletonList(
                        new UserResponse(user.getEmail(), fullName, user.getStatus(),
                                payment.get(0).getUserId(), user.getCreatedAt(),
                                taskUser.getTaskStatus(), taskUser.getSubmissionStatus(),
                                taskUser.getSubmittedFile(), taskUser.getSubmittedDate(),
                                taskUser.getId())
                ));
            }
            responses.add(response);
        }
        return responses;
    }

    @Override
    public TaskResponse getById(Long taskId) {
        // Optional<TaskUser> task = taskUserRepository.findById(taskId);
        // return task.map(this::taskToResponseForUser).orElse(null);
        return null;
    }

    @Override
    public ApiResponse taskApproval(String jwt, Long taskId, TaskStatus taskStatus) {
        String email = jwtTokenProvider.getEmailFromJwtToken(jwt);
        TraineeCredentialGenerator trainee = traineeRepository.findByEmail(email);
        if (trainee == null) {
            return new ApiResponse("Trainee not found with email: " + email, false, new ArrayList<>());
        }
        Optional<TaskUser> task = taskUserRepository.findByTask_Id(taskId);
        if (task.isEmpty()) {
            return new ApiResponse("Task not found with id: " + taskId, false, new ArrayList<>());
        }
        if (!task.get().getTask().getTraineeEmail().equalsIgnoreCase(email)) {
            return new ApiResponse("Unauthorized: Logged in user does not match task's trainer",
                    false, HttpStatus.FORBIDDEN);
        }
        TaskUser taskUser = task.get();
        taskUser.setTaskStatus(taskStatus);
        taskUserRepository.save(taskUser);
        return new ApiResponse("Task approval successfully", true, taskToResponse(taskUser.getTask()));
    }

    // users
    @Override
    public List<TaskResponse> getAllTasksByUser(String jwt) {
        String email = jwtTokenProvider.getEmailFromJwtToken(jwt);
        // ✅ FIXED: Handle Optional properly
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        List<TaskUser> taskUserList = taskUserRepository.findByUser(user);
        return taskUserList.stream()
                .map(taskUser -> getAllTasksByUserResponse(taskUser.getTask(), user))
                .collect(Collectors.toList());
    }

    public TaskResponse getAllTasksByUserResponse(Task task, User loggedInUser) {
        TraineeCredentialGenerator trainer = traineeRepository.findByEmail(task.getTraineeEmail());
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setAssignmentDescription(task.getAssignmentDescription());
        response.setAssignmentDate(task.getAssignmentDate());
        response.setDueDate(task.getDueDate());

        if (task.getChatGroup() != null) {
            response.setChatGroup(task.getChatGroup().getName());
            List<UserResponse> teamMembers = task.getChatGroup().getMembers().stream()
                    .map(user -> {
                        String userId = null;
                        List<Payment> payments = paymentRepository.findAllByUserEmail(user.getEmail());
                        if (!payments.isEmpty()) {
                            userId = payments.get(0).getUserId();
                        }
                        String fullName = (user.getLastName() == null || user.getLastName().isEmpty())
                                ? user.getFirstName()
                                : user.getFirstName() + " " + user.getLastName();
                        return new UserResponse(
                                user.getEmail(),
                                fullName,
                                userId,
                                user.getCreatedAt(),
                                user.getStatus()
                        );
                    })
                    .collect(Collectors.toList());

            response.setTeamMembers(teamMembers);
        }

        TaskUser loggedInTaskUser = task.getUsers().stream()
                .filter(taskUser -> taskUser.getUser().getId().equals(loggedInUser.getId()))
                .findFirst()
                .orElse(null);

        if (loggedInTaskUser != null) {
            User user = loggedInTaskUser.getUser();
            String userId = null;
            String fullName = (user.getLastName() == null || user.getLastName().isEmpty())
                    ? user.getFirstName()
                    : user.getFirstName() + " " + user.getLastName();

            List<Payment> payment = paymentRepository.findAllByUserEmail(user.getEmail());
            if (!payment.isEmpty()) {
                userId = payment.get(0).getUserId();
            }

            UserResponse userResponse = new UserResponse(
                    user.getEmail(),
                    fullName,
                    user.getStatus(),
                    userId,
                    user.getCreatedAt(),
                    loggedInTaskUser.getTaskStatus(),
                    loggedInTaskUser.getSubmissionStatus(),
                    loggedInTaskUser.getSubmittedFile(),
                    loggedInTaskUser.getSubmittedDate(),
                    loggedInTaskUser.getId()
            );
            response.setFile(task.getFile());
            response.setUsers(Collections.singletonList(userResponse));
            response.setTaskStatus(loggedInTaskUser.getTaskStatus());
            response.setSubmissionStatus(loggedInTaskUser.getSubmissionStatus());
        }

        if (trainer.getLastName() != null) {
            response.setTrainerName(trainer.getFirstName() + " " + trainer.getLastName());
        } else {
            response.setTrainerName(trainer.getFirstName());
        }

        response.setParticipants(task.getUsers().size());
        return response;
    }

    @Override
    public ApiResponse taskSubmission(String jwt, Long taskId, String answer, MultipartFile file) throws IOException {
        String email = jwtTokenProvider.getEmailFromJwtToken(jwt);
        // ✅ FIXED: Handle Optional properly
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        Optional<Task> optional = taskRepository.findById(taskId);
        if (optional.isEmpty()) {
            return new ApiResponse("Task not found with id: " + taskId, false, new ArrayList<>());
        }

        Task task = optional.get();
        Optional<TaskUser> optionalTaskUser = taskUserRepository.findByTaskAndUser(task, user);
        if (optionalTaskUser.isEmpty()) {
            return new ApiResponse("Task submission not found for this userId " + user.getId() + " and taskId " + taskId,
                    false, new ArrayList<>());
        }

        TaskUser taskUser = optionalTaskUser.get();
        taskUser.setAnswer(answer);
        taskUser.setSubmittedFile(file.getBytes());
        taskUser.setSubmissionStatus(SubmissionStatus.SUBMITTED);
        taskUser.setSubmittedDate(LocalDate.now());
        TaskUser updatedTaskUser = taskUserRepository.save(taskUser);
        return new ApiResponse("Task Submitted Successfully", true, taskSubmissionResponse(updatedTaskUser));
    }

    @Override
    public TaskUser getFile(Long taskId) {
        Optional<TaskUser> optionalTaskUser = taskUserRepository.findById(taskId);
        return optionalTaskUser.orElse(null);
    }

    @Override
    public Map<String, Long> taskOverview(String jwt) {
        Map<String, Long> map = new HashMap<>();
        String email = jwtTokenProvider.getEmailFromJwtToken(jwt);
        TraineeCredentialGenerator trainee = traineeRepository.findByEmail(email);
        List<TaskUser> taskUsers = taskUserRepository.findByTaskTraineeEmail(trainee.getEmail());

        long totalTasks = taskUsers.size();
        long evaluatedTasks = taskUsers.stream()
                .filter(task -> task.getTaskStatus().equals(TaskStatus.ACCEPTED) ||
                        task.getTaskStatus().equals(TaskStatus.REJECTED))
                .count();
        long underReviewTasks = taskUsers.stream()
                .filter(task -> task.getTaskStatus().equals(TaskStatus.PENDING))
                .count();

        map.put("totalTasks", totalTasks);
        map.put("evaluatedTasks", evaluatedTasks);
        map.put("underReviewTasks", underReviewTasks);
        return map;
    }

    public TaskResponse taskSubmissionResponse(TaskUser taskUser) {
        Task task = taskUser.getTask();
        TaskResponse response = new TaskResponse();
        response.setAssignmentDescription(task.getAssignmentDescription());
        response.setAssignmentDate(task.getAssignmentDate());
        response.setDueDate(task.getDueDate());
        if (task.getChatGroup() != null) {
            response.setChatGroup(task.getChatGroup().getName());
        }
        response.setFile(task.getFile());
        response.setTrainerName(task.getTraineeEmail());
        response.setAnswer(taskUser.getAnswer());
        response.setSubmittingFile(taskUser.getSubmittedFile());
        response.setSubmissionStatus(taskUser.getSubmissionStatus());
        response.setSubmittedDate(taskUser.getSubmittedDate());
        return response;
    }

    public TaskResponse taskToResponse(Task task) {
        TaskResponse response = new TaskResponse();
        TraineeCredentialGenerator trainer = traineeRepository.findByEmail(task.getTraineeEmail());
        response.setId(task.getId());
        response.setAssignmentDescription(task.getAssignmentDescription());
        response.setAssignmentDate(task.getAssignmentDate());
        response.setDueDate(task.getDueDate());
        // response.setFile(task.getFile());

        if (trainer.getLastName() != null) {
            response.setTrainerName(trainer.getFirstName() + " " + trainer.getLastName());
        } else {
            response.setTrainerName(trainer.getFirstName());
        }

        response.setParticipants(task.getUsers().size());

        if (task.getChatGroup() != null) {
            response.setChatGroup(task.getChatGroup().getName());
            response.setChatGroupStartDate(task.getChatGroup().getCourseStartDate());
            response.setChatGroupEndDate(task.getChatGroup().getCourseEndDate());
        }

        if (task.getUsers() != null && !task.getUsers().isEmpty()) {
            List<UserResponse> userResponses = task.getUsers().stream()
                    .map(taskUser -> {
                        User user = taskUser.getUser();
                        List<Payment> payments = paymentRepository.findAllByUserEmail(user.getEmail());
                        String userID = (!payments.isEmpty()) ? payments.get(0).getUserId() : null;
                        String fullName = (user.getLastName() == null || user.getLastName().isEmpty())
                                ? user.getFirstName()
                                : user.getFirstName() + " " + user.getLastName();
                        return new UserResponse(user.getEmail(), fullName, user.getStatus(),
                                userID, user.getCreatedAt(), taskUser.getTaskStatus(),
                                taskUser.getSubmissionStatus(), taskUser.getSubmittedFile(),
                                taskUser.getSubmittedDate());
                    })
                    .collect(Collectors.toList());
            response.setUsers(userResponses);
        }
        return response;
    }
}