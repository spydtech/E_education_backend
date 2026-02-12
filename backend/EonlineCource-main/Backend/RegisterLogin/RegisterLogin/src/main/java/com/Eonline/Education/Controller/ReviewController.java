//package com.Eonline.Education.Controller;
//
//import com.Eonline.Education.Request.ReviewRequest;
//import com.Eonline.Education.Service.ReviewService;
//import com.Eonline.Education.Service.UserService;
//import com.Eonline.Education.exceptions.CourseException;
//import com.Eonline.Education.exceptions.UserException;
//import com.Eonline.Education.modals.Review;
//import com.Eonline.Education.modals.User;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/reviews")
//public class ReviewController {
//
//    private ReviewService reviewService;
//    private UserService userService;
//
//    public ReviewController(ReviewService reviewService, UserService userService) {
//        this.reviewService=reviewService;
//        this.userService=userService;
//        // TODO Auto-generated constructor stub
//    }
//    @PostMapping("/create")
//    public ResponseEntity<Review> createReviewHandler(@RequestBody ReviewRequest req, @RequestHeader("Authorization") String jwt) throws Exception {
//        User user=userService.findUserProfileByJwt(jwt);
//        System.out.println("product id "+req.getCourseId()+" - "+req.getReview());
//        Review review=reviewService.createReview(req, user);
//        System.out.println("product review "+req.getReview());
//        return new ResponseEntity<Review>(review, HttpStatus.ACCEPTED);
//    }
//
//    @GetMapping("/course/{courseId}")
//    public ResponseEntity<List<Review>> getProductsReviewHandler(@PathVariable Long courseId){
//        List<Review>reviews=reviewService.getAllReview(courseId);
//        return new ResponseEntity<List<Review>>(reviews,HttpStatus.OK);
//    }
//}
