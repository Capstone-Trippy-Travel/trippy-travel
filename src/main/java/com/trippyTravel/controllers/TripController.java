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
public class TripController {
    @Autowired
    private final EmailService emailService;
    private final TripRepository tripRepository;
    private final GroupsRepository groupsRepository;
    private final ImageRepository imagesRepository;
    private final CommentRepository commentRepository;
    private final FriendListRepository friendListRepository;

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
    public TripController(EmailService emailService, TripRepository tripRepository, GroupsRepository groupsRepository, ImageRepository imagesRepository, CommentRepository commentRepository, ActivityRepository activityRepository, FriendListRepository friendListRepository) {
        this.emailService = emailService;
        this.tripRepository = tripRepository;
        this.groupsRepository = groupsRepository;
        this.imagesRepository = imagesRepository;
        this.commentRepository = commentRepository;
        this.activityRepository = activityRepository;
        this.friendListRepository=friendListRepository;
    }
    @GetMapping("/trip/page/{pageNumber}")
    public String SeeAllTripsPage(Model model, @PathVariable int pageNumber) {
        int numberOfTrips=tripRepository.findAll().size();
        List<Trip> tripFromDb= tripRepository.findAll().subList((18*(-1+pageNumber)), numberOfTrips);
        model.addAttribute("trips",tripFromDb);
        if (SecurityContextHolder.getContext().getAuthentication().getName()==null || SecurityContextHolder.getContext().getAuthentication().getName().equalsIgnoreCase("anonymousUser")){
            List<FriendList> friendRequests= new ArrayList<>();
            model.addAttribute("friendRequests", friendRequests);
            List<Trip> unreadCommentTrips = new ArrayList<>();
            model.addAttribute("unreadCommentTrips", unreadCommentTrips);

        } else{
            User loggedInuser= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("friendRequests", friendListRepository.findFriendListByFriendAndStatus(loggedInuser, FriendStatus.PENDING));
            model.addAttribute("unreadCommentTrips", tripRepository.getUnreadCommentTrips(loggedInuser) );
        }
        System.out.println();
        model.addAttribute("publicTrips", tripRepository.findTripsByVisibility());
        model.addAttribute("pageNumber", pageNumber);
        double numberOfTripsDouble=(double) numberOfTrips;
        System.out.println("number of trips: "+numberOfTrips);
        Double numPagesDouble= Math.ceil(numberOfTripsDouble/18);
        Long numPages=Math.round(numPagesDouble);
        System.out.println("number of pages:"+numPages);
        model.addAttribute("numPages", Math.ceil(numberOfTripsDouble/18));
        return "Trip/index";
    }
    @PostMapping("/trip")
    public String index(Model model, @PathVariable Long id) {
        List<Trip> tripFromDb= tripRepository.findAll();
        if (SecurityContextHolder.getContext().getAuthentication().getName()==null || SecurityContextHolder.getContext().getAuthentication().getName().equalsIgnoreCase("anonymousUser")){
            List<FriendList> friendRequests= new ArrayList<>();
            model.addAttribute("friendRequests", friendRequests);
            List<Trip> unreadCommentTrips = new ArrayList<>();
            model.addAttribute("unreadCommentTrips", unreadCommentTrips);
        } else{
            User loggedInuser= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("friendRequests", friendListRepository.findFriendListByFriendAndStatus(loggedInuser, FriendStatus.PENDING));
            model.addAttribute("unreadCommentTrips", tripRepository.getUnreadCommentTrips(loggedInuser) );
        }
        model.addAttribute("trip",tripFromDb);
        return "Trip/index";
    }
    @RequestMapping(path = "/keys.js", produces = "application/javascript")
    @ResponseBody
    public String apikey(){
        return "const FileStackApiKey = `" + fileStackApiKey +"`\n" +"const mapBoxToken = `" + mapBoxToken+"`\n"+"const fourSquareId = `" + fourSquareId+"`\n"+ "const fourSquarePassword = `" + fourSquarePassword+"`\n"+ "const googleMapsKey = `" + googleMapsKey+"`";
    }
    @GetMapping("/trip/{id}")
    public String showOneTrip(@PathVariable Long id, Model vModel ){
        System.out.println("numbe of images for trip: "+ tripRepository.getOne(id).getImages().size());
        vModel.addAttribute("trips", tripRepository.getOne(id));
        if (SecurityContextHolder.getContext().getAuthentication().getName()==null || SecurityContextHolder.getContext().getAuthentication().getName().equalsIgnoreCase("anonymousUser")){
            List<FriendList> friendRequests= new ArrayList<>();
            vModel.addAttribute("friendRequests", friendRequests);
            List<Trip> unreadCommentTrips = new ArrayList<>();
            vModel.addAttribute("unreadCommentTrips", unreadCommentTrips);
        } else{
            User loggedInuser= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            vModel.addAttribute("friendRequests", friendListRepository.findFriendListByFriendAndStatus(loggedInuser, FriendStatus.PENDING));
            vModel.addAttribute("unreadCommentTrips", tripRepository.getUnreadCommentTrips(loggedInuser) );
        }
//        vModel.addAttribute("comments", commentRepository.getOne(id));
//        vModel.addAttribute("activity", activityRepository.getOne(1L));

//        List<Comment> commentsList = new ArrayList<>();
//        ListIterator<Comment> commentListIterator = commentsList.listIterator();
//        while (commentListIterator.hasNext()) {
//            System.out.println(commentListIterator.next());
//        }
        return "Trip/show";
    }
    @PostMapping(path = "/trip/{id}")
    public String addPicture(@PathVariable Long id,@RequestParam(name = "image_url",  required = false) String ImgUrl, @RequestParam(name = "activity_id",  required = false) String activityId  ) {
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        System.out.println(ImgUrl);
        Image imageToSave = new Image(ImgUrl, user, tripRepository.getOne(id));

        if (activityId!=null){
            imageToSave.setActivity(activityRepository.getOne(Long.parseLong(activityId)));
        }
        System.out.println(imageToSave.getImage_url());
        System.out.println(imageToSave.getUser().getUsername());
        System.out.println("about to save image");
        imagesRepository.save(imageToSave);
        System.out.println("saved image");



        return "redirect:/trip/"+id;
    }
    //    @GetMapping(path = "/trip/{id}/edit")
//    public String updateTrip(@PathVariable Long id ,Model model){
//        Trip trip=tripRepository.getOne(id);
//        System.out.println(trip.getName());
//        model.addAttribute("trip", trip);
//        return "Trip/edit";
//    }
    @GetMapping("/trip/create")
    public String createTrip(Model model){
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("trip", new Trip());
        model.addAttribute("groups", groupsRepository.findByOwner(user));
        if (SecurityContextHolder.getContext().getAuthentication().getName()==null || SecurityContextHolder.getContext().getAuthentication().getName().equalsIgnoreCase("anonymousUser")){
            List<FriendList> friendRequests= new ArrayList<>();
            model.addAttribute("friendRequests", friendRequests);
            List<Trip> unreadCommentTrips = new ArrayList<>();
            model.addAttribute("unreadCommentTrips", unreadCommentTrips);

        } else{
            User loggedInuser= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("friendRequests", friendListRepository.findFriendListByFriendAndStatus(loggedInuser, FriendStatus.PENDING));
            model.addAttribute("unreadCommentTrips", tripRepository.getUnreadCommentTrips(loggedInuser) );
        }
        return "Trip/create";
    }
    @PostMapping("/trip/create")
    public String createTripForm(@ModelAttribute Trip trip,@RequestParam(name = "image_url",  required = false) String ImgUrl, @RequestParam(name="groupId")String groupId
    ) {
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Group group = groupsRepository.getOne(Long.parseLong(groupId));
        trip.setGroup(group);
        Trip saveTrip= tripRepository.save(trip);
        System.out.println();
//        Image imageToSave = new Image(ImgUrl, user, saveTrip);
//        System.out.println(imageToSave.getImage_url());
//        System.out.println(imageToSave.getUser().getUsername());
//        System.out.println("about to save image");
//        imagesRepository.save(imageToSave);
//        System.out.println("saved image");
//        Group groups=(Group) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        trips.setGroup(groups);
//        Image imagesToSave= new Image(image0);
//        Image image1ToSave= new Image(image1);
//        imagesToSave.setPost(post);
//        image1ToSave.setPost(post);
//      post.setImages(imagesToSave);
//        imageRepo.save(imagesToSave);
//        imageRepo.save(image1ToSave);
//        emailService.prepareAndSend(saveTrip, "new trip","hey where you wanna go");
        return "redirect:/trip/"+saveTrip.getId();
    }
    @GetMapping(path = "/trip/{id}/edit")
    public String updateTrip(@PathVariable Long id ,Model model){
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("groups", groupsRepository.findByOwner(user));
        Trip trips=tripRepository.getOne(id);
        System.out.println(trips.getName());
        model.addAttribute("trip", trips);
        if (SecurityContextHolder.getContext().getAuthentication().getName()==null || SecurityContextHolder.getContext().getAuthentication().getName().equalsIgnoreCase("anonymousUser")){
            List<FriendList> friendRequests= new ArrayList<>();
            model.addAttribute("friendRequests", friendRequests);
            List<Trip> unreadCommentTrips = new ArrayList<>();
            model.addAttribute("unreadCommentTrips", unreadCommentTrips);

        } else{
            User loggedInuser= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("friendRequests", friendListRepository.findFriendListByFriendAndStatus(loggedInuser, FriendStatus.PENDING));
            model.addAttribute("unreadCommentTrips", tripRepository.getUnreadCommentTrips(loggedInuser) );
        }
        return "Trip/edit";
    }
    @PostMapping(path = "/trip/{id}/edit")
    public String updateTripForm(@PathVariable Long id ,@ModelAttribute Trip trips, @RequestParam(name="groupId")String groupId, @RequestParam(name = "image_url",  required = false) String ImgUrl) {
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Group groups = groupsRepository.getOne(Long.parseLong(groupId));
//        trips.setGroup(group);
//        Trip saveTrip= tripRepository.save(trips);
//        System.out.println();
//        Image imageToSave = new Image(ImgUrl, user, saveTrip);
//        Group groups=(Group) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        trips.setId(id);
        trips.setGroup(groups);
        Trip savedTrip = tripRepository.save(trips);
        return "redirect:/trip/"+savedTrip.getId();
    }
    @PostMapping("/trip/{id}/delete")
    public String DeleteTrip(@PathVariable Long id) {
        tripRepository.deleteById(id);
        return "redirect:/trip";
    }

    @RequestMapping(value="/trip.json", method=RequestMethod.GET, produces="application/json")
    public @ResponseBody List<Activity> retrieveActivitiesWithAjax(@RequestParam("tripId") String tripId) {
        System.out.println(tripId);
        List<Activity> activities= tripRepository.getOne(Long.parseLong(tripId)).getActivities();
        for (Activity activity: activities){
            List<ActivityVote> activityVotes = activity.getActivityVotes();

            //will set an initial vote setting on none, then will check to see if user voted and modify accordingly.
            activity.setUsersPreviousVote("none");

            //will grab all comments from this activity, and pass the quantity to the object to be passed to the page.
            activity.setCommentCount(commentRepository.findCommentsByActivity_Id(activity.getId()).size());
            int voteCount=0;
            for (ActivityVote activityVote: activityVotes){
                if (activityVote.isVote()){
                    voteCount++;
                    if (SecurityContextHolder.getContext().getAuthentication().getName()==null || SecurityContextHolder.getContext().getAuthentication().getName().equalsIgnoreCase("anonymousUser")){
                        //doing nothing if you are not logged in
                    } else{
                        //if you are logged in, will check to see if you voted
                        User loggedInUser= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                        if (activityVote.getUser().getId()==loggedInUser.getId()){
                            activity.setUsersPreviousVote("like");
                        }
                    }
                } else{
                    voteCount--;
                    if (SecurityContextHolder.getContext().getAuthentication().getName()==null || SecurityContextHolder.getContext().getAuthentication().getName().equalsIgnoreCase("anonymousUser")){
                        //doing nothing if you are not logged in
                    } else{
                        //if you are logged in, will check to see if you voted
                        User loggedInUser= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                        if (activityVote.getUser().getId()==loggedInUser.getId()){
                            activity.setUsersPreviousVote("dislike");
                        }
                    }
                }
            }
            activity.setVoteCount(voteCount);
        }
        System.out.println("about to return activities");
        return activities;
    }

    @GetMapping("/")
    public String SeeAllTripsHome(Model model) {
        List<Trip> tripFromDb= tripRepository.findAll();
        model.addAttribute("trips",tripFromDb);
        List<Activity> activityList=activityRepository.findAll();
        model.addAttribute("activities",activityList);
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
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

        System.out.println();
        return "index";
    }

    @PostMapping(path = "/trip/search")
    public String searchTrip(Model viewModel, @RequestParam(name = "search") String term) {
        viewModel.addAttribute("tripResults", tripRepository.findAllByDescriptionContainingOrNameContainingOrLocationContaining(term, term, term));
        return "Trip/search";
    }

}