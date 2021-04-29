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
    public @ResponseBody List<Comment> submitTripComment(@PathVariable long id, @RequestParam(name="comment", required=false) String comment) {
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //will save new comment to datbase, if one is passed to it
        if (comment!=null) {
            Trip trip = tripDao.getOne(id);
            commentDao.save(new Comment(comment, userDao.getOne(user.getId()), trip));

            List<GroupMember> groupMembers = trip.getGroup().getGroupMembers();
            for (GroupMember groupMember : groupMembers) {
                if (groupMember.getMember().getId() != user.getId()) {
                    groupMember.setUnreadComment(true);
                    groupMember.setUnreadCommentTrip(trip);
                    groupMembersRepository.save(groupMember);
                }
            }
        }

        return commentDao.findCommentsByTrip_Id(id);

    }

    @RequestMapping(value="/trip/{trip_id}/activity/{activity_id}", method=RequestMethod.GET, produces="application/json")
    public @ResponseBody List<CommentReplies> retrieveActivityComments(@PathVariable long trip_id, @PathVariable long activity_id, @RequestParam(name="comment", required = false) String comment) {
        List <CommentReplies> commentReplies = null;

        //if no comment is submitted, return all available activity comments
        if (comment==null) {

            //creating boolean to see if any activity comments exist
            boolean commentExists=commentDao.existsCommentByActivity_Id(activity_id);

            if (commentExists){
                //grab comment if one exists
                Comment originalActivityComment=commentDao.findCommentByActivity_Id(activity_id);

                //if a comment does exist, check to see if any replies exist
                boolean commentRepliesExist=commentRepliesRepository.existsCommentRepliesByParentComment(originalActivityComment);
                if (commentRepliesExist){
                    commentReplies= commentRepliesRepository.findCommentRepliesByParentComment(originalActivityComment);
                } else{
                    commentReplies= new ArrayList<>();
                    CommentReplies originalCommentAsCommentReply = new CommentReplies(originalActivityComment.getComment_text(), originalActivityComment.getUser(), originalActivityComment);
                    commentReplies.add(originalCommentAsCommentReply);
                }
            } else{
                commentReplies=new ArrayList<>();
            }

            //if a comment is submitted, check to see if any comment existed before, then save new comment or comment reply
        } else{
            //grabbing the user, trip and activity objects to save new activity comment
            User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Trip trip = tripDao.getOne(trip_id);
            Activity activity= activityDao.getOne(activity_id);

            //will notify all new group members of new comment / comment reply
            List<GroupMember> groupMembers = trip.getGroup().getGroupMembers();
            for (GroupMember groupMember : groupMembers) {
                if (groupMember.getMember().getId() != user.getId()) {
                    groupMember.setUnreadComment(true);
                    groupMember.setUnreadCommentTrip(trip);
                    groupMembersRepository.save(groupMember);
                }
            }

            //checking to see if comment exists, will create new comment if not. If so, will create reply.
            boolean commentExists=commentDao.existsCommentByActivity_Id(activity_id);

            //if comment already exists, create a new comment reply
            if (commentExists){
                Comment originalActivityComment=commentDao.findCommentByActivity_Id(activity_id);
                commentRepliesRepository.save(new CommentReplies(comment, user, originalActivityComment));
                commentReplies=commentRepliesRepository.findCommentRepliesByParentComment(originalActivityComment);

                //if no comment exists, create a new comment
            }else {
                commentReplies = new ArrayList<>();
                Comment newComment=commentDao.save(new Comment(comment, userDao.getOne(user.getId()), trip, activity));
                CommentReplies originalCommentAsCommentReply = new CommentReplies(newComment.getComment_text(), newComment.getUser(), newComment);
                commentReplies.add(originalCommentAsCommentReply);

            }
        }
        return commentReplies;

    }


}
