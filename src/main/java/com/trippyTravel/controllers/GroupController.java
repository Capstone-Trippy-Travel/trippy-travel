package com.trippyTravel.controllers;

import com.trippyTravel.models.Group;
import com.trippyTravel.models.User;
import com.trippyTravel.repositories.GroupMembersRepository;
import com.trippyTravel.repositories.GroupsRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class GroupController {
    private GroupsRepository groupDao;
    private GroupMembersRepository groupMemberDao;

    public GroupController(GroupsRepository groupDao, GroupMembersRepository groupMemberDao) {
        this.groupDao = groupDao;
        this.groupMemberDao = groupMemberDao;
    }

    @GetMapping(path = "/groups")
    public String viewAllGroups(Model viewModel) {
        List<Group> allGroups = groupDao.findAll();
        viewModel.addAttribute("group", allGroups);
        return "groups/index";
    }
    @GetMapping(path = "/groups/{id}")
    public String viewGroup(@PathVariable Long id, Model viewModel) {
        viewModel.addAttribute("post", groupDao.getOne(id));
        return "groups/show";
    }

    @GetMapping(path = "/groups/create")
    public String createGroup(Model viewModel){
        viewModel.addAttribute("group", new Group());
        return "groups/create-group";
    }
    @PostMapping(path = "/groups/create")
    public String viewPost(@ModelAttribute Group newGroup) {
        User groupOwner = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        newGroup.setOwner(groupOwner);
        Group createdGroup = groupDao.save(newGroup);
        return "redirect:/groups/{id}";
    }
}
