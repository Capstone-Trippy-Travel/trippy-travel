package com.trippyTravel.controllers;


import com.trippyTravel.models.*;
import com.trippyTravel.repositories.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.*;


import java.util.List;

@Controller
public class CommentController {
    private UsersRepository userDao;
    private CommentRepository commentDao;
    private TripRepository tripDao;
    private ActivityRepository activityDao;
    private GroupMembersRepository groupMembersRepository;
    private CommentRepliesRepository commentRepliesRepository;

    public CommentController(UsersRepository userDao, CommentRepository commentDao, TripRepository tripDao, ActivityRepository activityDao, GroupMembersRepository groupMembersRepository, CommentRepliesRepository commentRepliesRepository) {
        this.userDao = userDao;
        this.commentDao = commentDao;
        this.tripDao = tripDao;
        this.activityDao = activityDao;
        this.groupMembersRepository=groupMembersRepository;
        this.commentRepliesRepository=commentRepliesRepository;
    }


    @PostMapping("/trip/{id}/comment")
    public String createComment(@RequestParam(name = "comment") String comment, @PathVariable long id, Model model){
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Comment newComment =new Comment();
        newComment.setComment_text(comment);
        newComment.setUser(user);
        Trip trip = tripDao.getOne(id);
        newComment.setTrip(trip);

        List<GroupMember> groupMembers = trip.getGroup().getGroupMembers();
        for (GroupMember groupMember: groupMembers){
            if (groupMember.getMember().getId()!=user.getId()){
                groupMember.setUnreadComment(true);
                groupMember.setUnreadCommentTrip(trip);
                groupMembersRepository.save(groupMember);
            }
        }


        commentDao.save(newComment);
        return "redirect:/trip/" + id;
    }

    @RequestMapping(value="/trip/{id}/comments", method=RequestMethod.GET, produces="application/json")
    public @ResponseBody List<Comment> submitTripComment(@PathVariable long id, @RequestParam("comment") String comment) {
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        if (activityId==null){
        Trip trip = tripDao.getOne(id);
            commentDao.save(new Comment(comment, userDao.getOne(user.getId()), trip));

        List<GroupMember> groupMembers = trip.getGroup().getGroupMembers();
        for (GroupMember groupMember: groupMembers){
            if (groupMember.getMember().getId()!=user.getId()){
                groupMember.setUnreadComment(true);
                groupMember.setUnreadCommentTrip(trip);
                groupMembersRepository.save(groupMember);
            }
        }
//        } else{
//            if (commentDao.existsCommentByActivity_Id(activityId)){
//            commentDao.save(new Comment(comment, user, tripDao.getOne(id), activityDao.getOne(activityId)));
//            }
//        }
        List<Comment> comments=commentDao.findCommentsByTrip_Id(id);
        return comments;

    }
}
