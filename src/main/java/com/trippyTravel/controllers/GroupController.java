package com.trippyTravel.controllers;

import com.trippyTravel.models.Groups;
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
        List<Groups> allGroups = groupDao.findAll();
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
        viewModel.addAttribute("createPost", new Groups());
        return "groups/create-group";
    }
    @PostMapping(path = "/groups/create")
    public String viewPost(@ModelAttribute Groups newGroup) {
        User groupOwner = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        newGroup.setOwner(groupOwner);
        Groups createdGroup = groupDao.save(newGroup);
        return "redirect:/groups/";
    }
}
