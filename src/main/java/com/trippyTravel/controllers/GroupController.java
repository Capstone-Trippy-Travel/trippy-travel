package com.trippyTravel.controllers;

import com.trippyTravel.models.*;
import com.trippyTravel.repositories.*;
import org.springframework.beans.factory.annotation.Value;
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
    private ImageRepository imageDao;
    private FriendListRepository friendListRepository;
    private TripRepository tripRepository;
    private CommentRepository commentRepository;
    @Value("${mapBoxToken}")
    private String mapBoxToken;
    @Value("${fileStackApiKey}")
    private String fileStackApiKey;
    @Value("${fourSquareId}")
    private String fourSquareId;
    @Value("${fourSquarePassword}")
    private String fourSquarePassword;
    @Value("${googleMapsKey}")
    private String googleMapsKey;

    public GroupController(GroupsRepository groupDao, ImageRepository imageDao, GroupMembersRepository groupMemberDao, UsersRepository userDao, CommentRepository commentDao, FriendListRepository friendListRepository, TripRepository tripRepository, CommentRepository commentRepository) {
        this.groupDao = groupDao;
        this.groupMemberDao = groupMemberDao;
        this.userDao = userDao;
        this.commentDao = commentDao;
        this.friendListRepository=friendListRepository;
        this.tripRepository=tripRepository;
        this.imageDao =imageDao;
        this.commentRepository=commentRepository;
    }

    @GetMapping(path = "/groups/{id}")
    public String viewGroup(@PathVariable Long id, Model viewModel) {
        Group group =groupDao.getOne(id);
        viewModel.addAttribute("group",group);

        //will pass list of trips sorted by latest to group profile
        viewModel.addAttribute("trips", tripRepository.findTripsByGroupOrderByIdDesc(group));

        if (SecurityContextHolder.getContext().getAuthentication().getName()==null || SecurityContextHolder.getContext().getAuthentication().getName().equalsIgnoreCase("anonymousUser")){
            List<FriendList> friendRequests= new ArrayList<>();
            viewModel.addAttribute("friendRequests", friendRequests);
            List<Trip> unreadCommentTrips = new ArrayList<>();
            viewModel.addAttribute("unreadCommentTrips", unreadCommentTrips);

        } else{
            User loggedInuser= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            viewModel.addAttribute("friendRequests", friendListRepository.findFriendListByFriendAndStatus(loggedInuser, FriendStatus.PENDING));
            List<Trip> unreadCommentTrips=tripRepository.getUnreadCommentTrips(loggedInuser);
            for (Trip unreadCommentTrip: unreadCommentTrips){
                unreadCommentTrip.setComments(commentRepository.findCommentsByTrip_IdOrderByCreatedAt(unreadCommentTrip.getId()));
            }
            viewModel.addAttribute("unreadCommentTrips", unreadCommentTrips);
            //will check to see if user is a groupMember, before allowing them to proceed to page.
            int counter=0;
            for (GroupMember groupMember: group.getGroupMembers()){
                if (groupMember.getMember().getId()==loggedInuser.getId()){
                    counter++;
                }
            }
            if (counter==0){
                viewModel.addAttribute("isGroupMember", false);
            }else{
                viewModel.addAttribute("isGroupMember", true);
            }
        }
        viewModel.addAttribute("groupImages", imageDao.getGroupImages(id));
        return "groups/view";
    }

    @GetMapping(path = "/groups/create")
    public String createGroup(Model viewModel){
        viewModel.addAttribute("group", new Group());
        if (SecurityContextHolder.getContext().getAuthentication().getName()==null || SecurityContextHolder.getContext().getAuthentication().getName().equalsIgnoreCase("anonymousUser")){
            List<FriendList> friendRequests= new ArrayList<>();
            viewModel.addAttribute("friendRequests", friendRequests);
            List<Trip> unreadCommentTrips = new ArrayList<>();
            viewModel.addAttribute("unreadCommentTrips", unreadCommentTrips);

        } else{
            User loggedInuser= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            viewModel.addAttribute("friendRequests", friendListRepository.findFriendListByFriendAndStatus(loggedInuser, FriendStatus.PENDING));
            List<Trip> unreadCommentTrips=tripRepository.getUnreadCommentTrips(loggedInuser);
            for (Trip unreadCommentTrip: unreadCommentTrips){
                unreadCommentTrip.setComments(commentRepository.findCommentsByTrip_IdOrderByCreatedAt(unreadCommentTrip.getId()));
            }
            viewModel.addAttribute("unreadCommentTrips", unreadCommentTrips);


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
        if (SecurityContextHolder.getContext().getAuthentication().getName()==null || SecurityContextHolder.getContext().getAuthentication().getName().equalsIgnoreCase("anonymousUser")){
            List<FriendList> friendRequests= new ArrayList<>();
            viewModel.addAttribute("friendRequests", friendRequests);
            List<Trip> unreadCommentTrips = new ArrayList<>();
            viewModel.addAttribute("unreadCommentTrips", unreadCommentTrips);
            return "/groups/"+id;

        } else{
            User loggedInuser= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            viewModel.addAttribute("friendRequests", friendListRepository.findFriendListByFriendAndStatus(loggedInuser, FriendStatus.PENDING));
            List<Trip> unreadCommentTrips=tripRepository.getUnreadCommentTrips(loggedInuser);
            for (Trip unreadCommentTrip: unreadCommentTrips){
                unreadCommentTrip.setComments(commentRepository.findCommentsByTrip_IdOrderByCreatedAt(unreadCommentTrip.getId()));
            }
            viewModel.addAttribute("unreadCommentTrips", unreadCommentTrips);

            //will check to see if user is a groupMember, before allowing them to proceed to page.
            int counter=0;
            for (GroupMember groupMember: oneGroup.getGroupMembers()){
                if (groupMember.getMember().getId()==loggedInuser.getId()){
                    counter++;
                }
            }
            if (counter==0){
                return "/groups/"+id;
            }
        }
        return "groups/edit-group";
    }
    @PostMapping(path = "/groups/{id}/update")
    public String editGroup(@ModelAttribute Group newGroup, @RequestParam(name = "groupMembersList", required = false) long[] groupMembers, @RequestParam(name = "profile_image", required = false) String profileImage) {

        User groupOwner = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        newGroup.setOwner(groupOwner);

        //if new profile image is uploaded, replace old one. If none is, attach original profile image
        if (profileImage!=null){
            newGroup.setProfile_image(profileImage);
        } else{
            newGroup.setProfile_image(groupDao.getOne(newGroup.getId()).getProfile_image());
        }

        Group createdGroup = groupDao.save(newGroup);

        //will loop through group member list, and save any new group members
        for (int i=0; i<groupMembers.length; i++){
            User user = userDao.getOne(groupMembers[i]);
            Boolean groupMemberExists=groupMemberDao.existsGroupMemberByMemberAndGroup(user, newGroup);
            if (!groupMemberExists){
                //if new, save the new group member
                groupMemberDao.save(new GroupMember(false, user, newGroup));
            }
        }






        return "redirect:/groups/"+createdGroup.getId();
    }

}
