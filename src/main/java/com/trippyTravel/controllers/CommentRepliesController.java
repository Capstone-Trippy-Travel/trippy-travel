package com.trippyTravel.controllers;

import com.trippyTravel.models.Comment;
import com.trippyTravel.models.CommentReplies;
import com.trippyTravel.models.User;
import com.trippyTravel.repositories.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CommentRepliesController {
    private UsersRepository userDao;
    private CommentRepository commentDao;
    private CommentRepliesRepository commentReplyDao;

    public CommentRepliesController(UsersRepository userDao, CommentRepository commentDao, CommentRepliesRepository commentReplyDao) {
        this.userDao = userDao;
        this.commentDao = commentDao;
        this.commentReplyDao = commentReplyDao;
    }

    @PostMapping("/trip/{id}/reply")
    public String commentReply(@RequestParam(name = "reply") String reply, @PathVariable long id){
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CommentReplies newReply =new CommentReplies();
        newReply.setComment_text(reply);
        newReply.setUser(user);
        newReply.setParentComment(commentDao.getOne(id));
        commentReplyDao.save(newReply);
        return "redirect:/trip/" + id;
    }
}
