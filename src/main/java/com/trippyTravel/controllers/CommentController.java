package com.trippyTravel.controllers;


import com.trippyTravel.models.Comment;
import com.trippyTravel.models.Group;
import com.trippyTravel.models.Trip;
import com.trippyTravel.models.User;
import com.trippyTravel.repositories.ActivityRepository;
import com.trippyTravel.repositories.CommentRepository;
import com.trippyTravel.repositories.TripRepository;
import com.trippyTravel.repositories.UsersRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CommentController {
    private UsersRepository userDao;
    private CommentRepository commentDao;
    private TripRepository tripDao;
    private ActivityRepository activityDao;

    public CommentController(UsersRepository userDao, CommentRepository commentDao, TripRepository tripDao, ActivityRepository activityDao) {
        this.userDao = userDao;
        this.commentDao = commentDao;
        this.tripDao = tripDao;
        this.activityDao = activityDao;
    }

    @PostMapping("/trip/{id}/comment")
    public String createComment(@RequestParam(name = "comment") String comment, @PathVariable long id, Model model){
        System.out.println("start process");
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Comment newComment =new Comment();
        newComment.setComment_text(comment);
        newComment.setUser(user);
        newComment.setTrip(tripDao.getOne(id));
        System.out.println("before save");
        commentDao.save(newComment);
        System.out.println("after save");
        return "redirect:/trip/" + id;
    }
}
