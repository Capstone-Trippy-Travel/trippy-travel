package com.trippyTravel.controllers;

import com.trippyTravel.models.*;
import com.trippyTravel.repositories.*;
import com.trippyTravel.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


@Controller
public class HomeController {
    private final TripRepository tripRepository;
    private final GroupsRepository groupsRepository;
    private final ImageRepository imagesRepository;
    private final CommentRepository commentRepository;
    private final FriendListRepository friendListRepository;
    private final ActivityRepository activityRepository;
    private final UsersRepository usersRepository;
    @Autowired
    private UserService usersService;
    @Autowired
    private GroupMembersRepository groupMembersRepository;

    public HomeController(TripRepository tripRepository, GroupsRepository groupsRepository, ImageRepository imagesRepository, CommentRepository commentRepository, FriendListRepository friendListRepository, ActivityRepository activityRepository, UsersRepository usersRepository) {
        this.tripRepository = tripRepository;
        this.groupsRepository = groupsRepository;
        this.imagesRepository = imagesRepository;
        this.commentRepository = commentRepository;
        this.friendListRepository = friendListRepository;
        this.activityRepository = activityRepository;
        this.usersRepository = usersRepository;
    }

    @GetMapping("/")
    public String SeeAllTripsHome(@ModelAttribute User user, Model model) {

        if (SecurityContextHolder.getContext().getAuthentication().getName()==null || SecurityContextHolder.getContext().getAuthentication().getName().equalsIgnoreCase("anonymousUser")){
            List<FriendList> friendRequests= new ArrayList<>();
            model.addAttribute("friendRequests", friendRequests);
            List<Trip> unreadCommentTrips = new ArrayList<>();
            model.addAttribute("unreadCommentTrips", unreadCommentTrips);

            System.out.println("returning public trips");
            model.addAttribute( "generalTripList", tripRepository.findTripsByVisibilityOrderByIdDesc("public").subList(0,3) );


        } else{
            User loggedInuser= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("friendRequests", friendListRepository.findFriendListByFriendAndStatus(loggedInuser, FriendStatus.PENDING));
            List <Trip> unreadCommentTrips=tripRepository.getUnreadCommentTrips(loggedInuser);
            for (Trip trip: unreadCommentTrips){
                System.out.println(trip.getName());
            }
            model.addAttribute("unreadCommentTrips", unreadCommentTrips );

            User logUser = usersService.loggedInUser();
            List<Trip> userTrip= tripRepository.findUserTrips(logUser, "public");
            List<Trip> userTripSub= new ArrayList<>();
            for (int i=0; i<userTrip.size() && i<3; i++){
                System.out.println(userTrip.get(i).getName());
                userTripSub.add(userTrip.get(i));
            }

            model.addAttribute( "userTripList", userTripSub );
        }
        System.out.println("returning users trips");
        return "index";
    }
    @GetMapping("/team")
    public String meetTeam(@ModelAttribute User user, Model model) {
        if (SecurityContextHolder.getContext().getAuthentication().getName()==null || SecurityContextHolder.getContext().getAuthentication().getName().equalsIgnoreCase("anonymousUser")){
            List<FriendList> friendRequests= new ArrayList<>();
            model.addAttribute("friendRequests", friendRequests);
            List<Trip> unreadCommentTrips = new ArrayList<>();
            model.addAttribute("unreadCommentTrips", unreadCommentTrips);

        } else{
            User loggedInuser= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("friendRequests", friendListRepository.findFriendListByFriendAndStatus(loggedInuser, FriendStatus.PENDING));
            List <Trip> unreadCommentTrips=tripRepository.getUnreadCommentTrips(loggedInuser);
            for (Trip trip: unreadCommentTrips){
                System.out.println(trip.getName());
            }
            model.addAttribute("unreadCommentTrips", unreadCommentTrips );
        }
        return "team";
    }

}
