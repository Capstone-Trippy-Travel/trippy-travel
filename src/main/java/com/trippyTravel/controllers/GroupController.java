package com.trippyTravel.controllers;

import com.trippyTravel.repositories.GroupMembersRepository;
import com.trippyTravel.repositories.GroupsRepository;
import org.springframework.stereotype.Controller;

@Controller
public class GroupController {
    private GroupsRepository groupDao;
    private GroupMembersRepository groupMemberDao;
}
