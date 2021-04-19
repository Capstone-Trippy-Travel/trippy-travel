package com.trippyTravel.controllers;

import com.trippyTravel.models.Comment;
import com.trippyTravel.models.Group;
import com.trippyTravel.models.GroupMember;
import com.trippyTravel.models.User;
import com.trippyTravel.repositories.CommentRepository;
import com.trippyTravel.repositories.GroupMembersRepository;
import com.trippyTravel.repositories.GroupsRepository;
import com.trippyTravel.repositories.UsersRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class GroupController {
    private GroupsRepository groupDao;
    private GroupMembersRepository groupMemberDao;
    private UsersRepository userDao;
    private CommentRepository commentDao;

    public GroupController(GroupsRepository groupDao, GroupMembersRepository groupMemberDao, UsersRepository userDao, CommentRepository commentDao) {
        this.groupDao = groupDao;
        this.groupMemberDao = groupMemberDao;
        this.userDao = userDao;
        this.commentDao = commentDao;
    }

    //    @GetMapping(path = "/groups")
//    public String viewAllGroups(Model viewModel) {
//        List<Group> allGroups = groupDao.findAll();
//        viewModel.addAttribute("group", allGroups);
//        return "groups/index";
//    }
    @GetMapping(path = "/groups/{id}")
    public String viewGroup(@PathVariable Long id, Model viewModel) {
        viewModel.addAttribute("group", groupDao.getOne(id));
        return "groups/view";
    }

    @GetMapping(path = "/groups/create")
    public String createGroup(Model viewModel){
        viewModel.addAttribute("group", new Group());
        return "groups/create-group";
    }
    @PostMapping(path = "/groups/create")
    public String createGroup(@ModelAttribute Group newGroup, @RequestParam(name = "groupMembersList", required = false) Integer[] groupMembers) {

        User groupOwner = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       newGroup.setOwner(groupOwner);

        Group createdGroup = groupDao.save(newGroup);
        if (groupMembers!=null) {
            for (int i = 0; i < groupMembers.length; i++) {
                Long memberId = Long.valueOf(groupMembers[i]);
                groupMemberDao.save(new GroupMember(false, userDao.getOne(memberId), newGroup));
            }
        }
        groupMemberDao.save(new GroupMember(true, groupOwner, newGroup));

        return "redirect:/groups/"+createdGroup.getId();
    }
    @GetMapping(path = "/groups/{id}/update")
    public String editGroup(Model viewModel, @PathVariable long id){
        Group oneGroup = groupDao.getOne(id);
        viewModel.addAttribute("editGroup", oneGroup);
        return "groups/edit-group";
    }
    @PostMapping(path = "/groups/{id}/update")
    public String editGroup(@ModelAttribute Group newGroup, @RequestParam(name = "groupMembersList", required = false) Integer[] groupMembers) {

        User groupOwner = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        newGroup.setOwner(groupOwner);
        Group createdGroup = groupDao.save(newGroup);
        for (int i=0; i<groupMembers.length; i++){
            Long memberId = Long.valueOf(groupMembers[i]);
            groupMemberDao.save(new GroupMember(false, userDao.getOne(memberId), newGroup) );
        }

        return "redirect:/groups/"+createdGroup.getId();
    }

}
