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
//        List<Activity> activityList=activityRepository.findAll();
//        model.addAttribute("activities",activityList);
//        System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());

//        List<Trip> tripsFromDb= tripRepository.findTripsByVisibilityOrderByIdDesc("public");
//        List<List<Trip>> tripFromDbLists = new LinkedList<List<Trip>>();
//        List<Trip> tempList = new LinkedList<Trip>();
//        int listSize = tripsFromDb.size();
//        for ( int i = 0; i < 3; i++ ) {
//            tempList.add( tripsFromDb.get( i ) );
//            if ( listSize == ( i+1 ) || tempList.size() == 3 ) {
//                tripFromDbLists.add( tempList );
//                tempList = new LinkedList<Trip>();
//            }
//        }
//        model.addAttribute( "generalTripList", tripFromDbLists );

        //User Trips
//        User logUser = usersService.loggedInUser();
//        List<Trip> userTrip= tripRepository.findUserTrips(logUser, "public");
//        List<List<Trip>> userTripsList = new LinkedList<List<Trip>>();
//        List<Trip> userTempList = new LinkedList<Trip>();
//        int listSize2 = userTrip.size();
//        for ( int i = 0; i < 3; i++ ) {
//            userTempList.add( userTrip.get( i ) );
//            if ( listSize2 == ( i+1 ) || userTempList.size() == 3 ) {
//                userTripsList.add( userTempList );
//                userTempList = new LinkedList<Trip>();
//            }
//        }
//        model.addAttribute( "userTripList", userTripsList );


        if (SecurityContextHolder.getContext().getAuthentication().getName()==null || SecurityContextHolder.getContext().getAuthentication().getName().equalsIgnoreCase("anonymousUser")){
            List<FriendList> friendRequests= new ArrayList<>();
            model.addAttribute("friendRequests", friendRequests);
            List<Trip> unreadCommentTrips = new ArrayList<>();
            model.addAttribute("unreadCommentTrips", unreadCommentTrips);


            List<Trip> tripsFromDb= tripRepository.findTripsByVisibilityOrderByIdDesc("public");
            List<List<Trip>> tripFromDbLists = new LinkedList<List<Trip>>();
            List<Trip> tempList = new LinkedList<Trip>();
            int listSize = tripsFromDb.size();
            for ( int i = 0; i < 3; i++ ) {
                tempList.add( tripsFromDb.get( i ) );
                if ( listSize == ( i+1 ) || tempList.size() == 3 ) {
                    tripFromDbLists.add( tempList );
                    tempList = new LinkedList<Trip>();
                }
            }
            model.addAttribute( "generalTripList", tripFromDbLists );


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
            List<List<Trip>> userTripsList = new LinkedList<List<Trip>>();
            List<Trip> userTempList = new LinkedList<Trip>();
            int listSize2 = userTrip.size();
            for ( int i = 0; i < 3; i++ ) {
                userTempList.add( userTrip.get( i ) );
                if ( listSize2 == ( i+1 ) || userTempList.size() == 3 ) {
                    userTripsList.add( userTempList );
                    userTempList = new LinkedList<Trip>();
                }
            }
            model.addAttribute( "userTripList", userTripsList );
        }
        return "index";
    }
//    @GetMapping("/userTrips")
//    public String showUserTrip(Model viewModel){
//        List<Trip> tripsFromDb= tripRepository.findTripsByVisibilityOrderByIdDesc("public");
//        List<List<Trip>> tripFromDbLists = new LinkedList<List<Trip>>();
//        List<Trip> tempList = new LinkedList<Trip>();
//
//        int listSize = tripsFromDb.size();
//        for ( int i = 0; i < 3; i++ ) {
//            tempList.add( tripsFromDb.get( i ) );
//            if ( listSize == ( i+1 ) || tempList.size() == 3 ) {
//                tripFromDbLists.add( tempList );
//                tempList = new LinkedList<Trip>();
//            }
//        }
//        viewModel.addAttribute( "generalTripList", tripFromDbLists );
//
//        if (SecurityContextHolder.getContext().getAuthentication().getName()==null || SecurityContextHolder.getContext().getAuthentication().getName().equalsIgnoreCase("anonymousUser")){
//            List<FriendList> friendRequests= new ArrayList<>();
//            viewModel.addAttribute("friendRequests", friendRequests);
//            List<Trip> unreadCommentTrips = new ArrayList<>();
//            viewModel.addAttribute("unreadCommentTrips", unreadCommentTrips);
//
//        } else{
//            User loggedInuser= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//            viewModel.addAttribute("friendRequests", friendListRepository.findFriendListByFriendAndStatus(loggedInuser, FriendStatus.PENDING));
//            List<Trip> unreadCommentTrips=tripRepository.getUnreadCommentTrips(loggedInuser);
//            for (Trip unreadCommentTrip: unreadCommentTrips){
//                unreadCommentTrip.setComments(commentRepository.findCommentsByTrip_IdOrderByCreatedAt(unreadCommentTrip.getId()));
//            }
//            viewModel.addAttribute("unreadCommentTrips", unreadCommentTrips);
//        }
//        return "index";
//    }

}
