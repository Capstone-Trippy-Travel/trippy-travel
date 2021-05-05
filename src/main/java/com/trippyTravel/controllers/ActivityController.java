package com.trippyTravel.controllers;

import com.trippyTravel.models.*;
import com.trippyTravel.repositories.*;
import com.trippyTravel.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

@Controller
public class ActivityController {
    @Autowired
    private final EmailService emailService;
    private final TripRepository tripRepository;
    private final GroupsRepository groupsRepository;
    private final ImageRepository imagesRepository;
    private final CommentRepository commentRepository;
    private final FriendListRepository friendListRepository;
    private final ActivityVoteRepository activityVoteRepository;
    private final GroupMembersRepository groupMembersRepository;

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


    private final ActivityRepository activityRepository;

    public ActivityController(EmailService emailService, TripRepository tripRepository, GroupsRepository groupsRepository, ImageRepository imagesRepository, CommentRepository commentRepository, ActivityRepository activityRepository, FriendListRepository friendListRepository, ActivityVoteRepository activityVoteRepository, GroupMembersRepository groupMembersRepository) {
        this.emailService = emailService;
        this.tripRepository = tripRepository;
        this.groupsRepository = groupsRepository;
        this.imagesRepository = imagesRepository;
        this.commentRepository = commentRepository;
        this.activityRepository = activityRepository;
        this.friendListRepository=friendListRepository;
        this.activityVoteRepository=activityVoteRepository;
        this.groupMembersRepository=groupMembersRepository;
    }

    @GetMapping(path = "/trip/{id}/activities")
    public String tripActivities(@PathVariable Long id, Model model) {
        Trip trip = tripRepository.getOne(id);



        model.addAttribute("trip", trip);
        model.addAttribute("activity", new Activity());
        if (SecurityContextHolder.getContext().getAuthentication().getName()==null || SecurityContextHolder.getContext().getAuthentication().getName().equalsIgnoreCase("anonymousUser")){
            List<FriendList> friendRequests= new ArrayList<>();
            model.addAttribute("friendRequests", friendRequests);
            List<Trip> unreadCommentTrips = new ArrayList<>();
            model.addAttribute("unreadCommentTrips", unreadCommentTrips);
            return "Trip/activities_google";

        } else{
            User loggedInuser= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("friendRequests", friendListRepository.findFriendListByFriendAndStatus(loggedInuser, FriendStatus.PENDING));
            List<Trip> unreadCommentTrips=tripRepository.getUnreadCommentTrips(loggedInuser);
            for (Trip unreadCommentTrip: unreadCommentTrips){
                unreadCommentTrip.setComments(commentRepository.findCommentsByTrip_IdOrderByCreatedAt(unreadCommentTrip.getId()));
            }
            model.addAttribute("unreadCommentTrips", unreadCommentTrips);

            //will check to see if user is a groupMember, and will re-route them away if not.
            int counter=0;
            for (GroupMember groupMember: trip.getGroup().getGroupMembers()){
                if(groupMember.getMember().getId()== loggedInuser.getId()){
                    counter++;
                }
            }
            if (counter==0){
                return "Trip/activities_google";
            }
        }
        return "Trip/activities_google";
    }


    @PostMapping(path = "/trip/{id}/activities")
    public @ResponseBody Activity addActivity(@PathVariable Long id, @RequestParam(name = "place", required = false) String place, @RequestParam(name = "address", required = false) String address, @RequestParam(name = "rating", required = false) double rating, @RequestParam(name = "reviews", required = false) int reviews, @RequestParam(name = "website", required = false) String website, @RequestParam(name = "phone", required = false) String phone, @RequestParam(name = "hours", required = false) String hours, @RequestParam(name = "placeId", required = false) String placeId, @RequestParam(name = "lat", required = false) double lat, @RequestParam(name = "lng", required = false) double lng, @RequestParam(name = "photoURL", required = false) String photoURL) {
        System.out.println("trying to add activity");
        Activity activity = new Activity();
        activity.setTrip(tripRepository.getOne(id));
        activity.setPlace(place);
        activity.setAddress(address);
        activity.setRating(rating);
        activity.setReviews(reviews);
        activity.setWebsite(website);
        activity.setPhone(phone);
        activity.setHours(hours);
        activity.setPlaceId(placeId);
        activity.setLat(lat);
        activity.setLng(lng);
        activity.setPhotoURL(photoURL);
        System.out.println("about to save new activity!!");
        Activity savedActivity=activityRepository.save(activity);
        System.out.println("successfully saved new activity!");
       return  savedActivity;


    }

    @RequestMapping(value="/activity/{id}/activityVote", method=RequestMethod.GET, produces="application/json")
    public @ResponseBody List<ActivityVote> retrieveActivitiesWithAjax(@PathVariable long id, @RequestParam("vote") Boolean vote) {
        System.out.println("about to save activity vote!!");
        User loggedInuser= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Activity activity = activityRepository.getOne(id);
        ActivityVote activityVote;
        if (activityVoteRepository.existsActivityVoteByActivityAndUser(activity, loggedInuser)){
            activityVote = activityVoteRepository.findActivityVoteByActivityAndUser(activity, loggedInuser);
            activityVote.setVote(vote);
        } else {
            activityVote = new ActivityVote(activity, vote, loggedInuser);
        }
        System.out.println("saving activity vote!!");

        activityVoteRepository.save(activityVote);
        List<ActivityVote> activityVotes= activityVoteRepository.findActivityVotesByActivity(activity);
        return activityVotes;
    }

    @RequestMapping(value="/trip/{id}/commentNotifications", method=RequestMethod.GET, produces="application/json")
    public @ResponseBody GroupMember updateCommentNotifications(@PathVariable long id) {
        System.out.println("about to update group member comment notifications");
        GroupMember savedGroupMember=new GroupMember();
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getName() == null);
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getName().equalsIgnoreCase("anonymousUser"));
        if (SecurityContextHolder.getContext().getAuthentication().getName() == null || SecurityContextHolder.getContext().getAuthentication().getName().equalsIgnoreCase("anonymousUser")) {
        }else{
            System.out.println("about to grab user");

            User loggedInUser= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (groupMembersRepository.existsGroupMemberByMemberAndUnreadCommentTripAndUnreadComment(loggedInUser, tripRepository.getOne(id), true)){
                System.out.println("about to update group member");
                GroupMember groupMember = groupMembersRepository.findGroupMemberByMemberAndUnreadCommentTripAndUnreadComment(loggedInUser, tripRepository.getOne(id), true);
               groupMember.setUnreadCommentTrip(null);
               groupMember.setUnreadComment(false);
               groupMembersRepository.save(groupMember);
               savedGroupMember=groupMember;
            }
        }
        return savedGroupMember;
    }
}

