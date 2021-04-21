package com.trippyTravel.controllers;

import com.trippyTravel.models.*;
import com.trippyTravel.repositories.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class GroupController {
    private GroupsRepository groupDao;
    private GroupMembersRepository groupMemberDao;
    private UsersRepository userDao;
    private CommentRepository commentDao;
    private FriendListRepository friendListRepository;

    public GroupController(GroupsRepository groupDao, GroupMembersRepository groupMemberDao, UsersRepository userDao, CommentRepository commentDao, FriendListRepository friendListRepository) {
        this.groupDao = groupDao;
        this.groupMemberDao = groupMemberDao;
        this.userDao = userDao;
        this.commentDao = commentDao;
        this.friendListRepository=friendListRepository;
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
        if (SecurityContextHolder.getContext().getAuthentication().getName()==null){
            List<FriendList> friendRequests= new ArrayList<>();
            viewModel.addAttribute("friendRequests", friendRequests);
        } else{
            User loggedInuser= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            viewModel.addAttribute("friendRequests", friendListRepository.findFriendListByFriendAndStatus(loggedInuser, FriendStatus.PENDING));
        }
        return "groups/view";
    }

    @GetMapping(path = "/groups/create")
    public String createGroup(Model viewModel){
        viewModel.addAttribute("group", new Group());
        if (SecurityContextHolder.getContext().getAuthentication().getName()==null){
            List<FriendList> friendRequests= new ArrayList<>();
            viewModel.addAttribute("friendRequests", friendRequests);
        } else{
            User loggedInuser= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            viewModel.addAttribute("friendRequests", friendListRepository.findFriendListByFriendAndStatus(loggedInuser, FriendStatus.PENDING));
        }
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
        if (SecurityContextHolder.getContext().getAuthentication().getName()==null){
            List<FriendList> friendRequests= new ArrayList<>();
            viewModel.addAttribute("friendRequests", friendRequests);
        } else{
            User loggedInuser= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            viewModel.addAttribute("friendRequests", friendListRepository.findFriendListByFriendAndStatus(loggedInuser, FriendStatus.PENDING));
        }
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
