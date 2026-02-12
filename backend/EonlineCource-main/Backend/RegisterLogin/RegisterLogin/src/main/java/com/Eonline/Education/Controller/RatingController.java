//package com.Eonline.Education.Controller;
//
//import com.Eonline.Education.Request.RatingRequest;
//import com.Eonline.Education.Service.RatingServices;
//import com.Eonline.Education.Service.UserService;
//import com.Eonline.Education.exceptions.CourseException;
//import com.Eonline.Education.exceptions.UserException;
//import com.Eonline.Education.modals.Rating;
//import com.Eonline.Education.modals.User;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//@RestController
//@RequestMapping("/api/ratings")
//public class RatingController {
//
//    private UserService userService;
//    private RatingServices ratingServices;
//
//    public RatingController(UserService userService,RatingServices ratingServices) {
//        this.ratingServices=ratingServices;
//        this.userService=userService;
//        // TODO Auto-generated constructor stub
//    }
//
//    @PostMapping("/create")
//    public ResponseEntity<Rating> createRatingHandler(@RequestBody RatingRequest req, @RequestHeader("Authorization") String jwt) throws UserException, CourseException, Exception {
//        User user=userService.findUserProfileByJwt(jwt);
//        Rating rating=ratingServices.createRating(req, user);
//        return new ResponseEntity<>(rating, HttpStatus.ACCEPTED);
//    }
//
//    @GetMapping("/course/{courseId}")
//    public ResponseEntity<List<Rating>> getCoursesReviewHandler(@PathVariable Long courseId){
//
//        List<Rating> ratings=ratingServices.getCourseRating(courseId);
//        return new ResponseEntity<>(ratings,HttpStatus.OK);
//    }
//}
