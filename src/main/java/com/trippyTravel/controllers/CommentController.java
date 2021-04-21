package com.trippyTravel.controllers;


import com.trippyTravel.models.*;
import com.trippyTravel.repositories.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CommentController {
    private UsersRepository userDao;
    private CommentRepository commentDao;
    private TripRepository tripDao;
    private ActivityRepository activityDao;
    private GroupMembersRepository groupMembersRepository;

    public CommentController(UsersRepository userDao, CommentRepository commentDao, TripRepository tripDao, ActivityRepository activityDao, GroupMembersRepository groupMembersRepository) {
        this.userDao = userDao;
        this.commentDao = commentDao;
        this.tripDao = tripDao;
        this.activityDao = activityDao;
        this.groupMembersRepository=groupMembersRepository;
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
}
