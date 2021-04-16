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

    public TripController(EmailService emailService, TripRepository tripRepository, GroupsRepository groupsRepository, ImageRepository imagesRepository, CommentRepository commentRepository, ActivityRepository activityRepository) {
        this.emailService = emailService;
        this.tripRepository = tripRepository;
        this.groupsRepository = groupsRepository;
        this.imagesRepository = imagesRepository;
        this.commentRepository = commentRepository;
        this.activityRepository = activityRepository;
    }

    @GetMapping("/trip")
    public String SeeAllTripsPage(Model model) {
        List<Trip> tripFromDb= tripRepository.findAll();
        model.addAttribute("posts",tripFromDb);

        return "Trip/index";
    }

    @PostMapping("/trip")
    public String index(Model model) {
        List<Trip> tripFromDb= tripRepository.findAll();
        model.addAttribute("trips",tripFromDb);

        return "Trip/index";
    }
    @RequestMapping(path = "/keys.js", produces = "application/javascript")
    @ResponseBody
    public String apikey(){

        return "const FileStackApiKey = `" + fileStackApiKey +"`\n" +"const mapBoxToken = `" + mapBoxToken+"`\n"+"const fourSquareId = `" + fourSquareId+"`\n"+ "const fourSquarePassword = `" + fourSquarePassword+"`\n"+ "const googleMapsKey = `" + googleMapsKey+"`";
    }
    @GetMapping("/trip/{id}")
    public String showOneTrip(@PathVariable Long id, Model vModel){
        System.out.println("numbe of images for trip: "+ tripRepository.getOne(id).getImages().size());
        vModel.addAttribute("trips", tripRepository.getOne(id));
        vModel.addAttribute("comments", commentRepository.findAll());
//        vModel.addAttribute("activity", activityRepository.getOne(1L));
        List<Comment> commentsList = new ArrayList<>();
        ListIterator<Comment> commentListIterator = commentsList.listIterator();
//        while (commentListIterator.hasNext()) {
//            System.out.println(commentListIterator.next());
//        }
        return "Trip/show";
    }
    @PostMapping(path = "/trip/{id}")
    public String addPicture(@ModelAttribute Trip trip,@RequestParam(name = "image_url",  required = false) String ImgUrl
    ) {
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();


        System.out.println(ImgUrl);
        Image imageToSave = new Image(ImgUrl, user, trip);
        System.out.println(imageToSave.getImage_url());
        System.out.println(imageToSave.getUser().getUsername());
        System.out.println("about to save image");
        imagesRepository.save(imageToSave);
        System.out.println("saved image");

//
        return "redirect:/trip/"+trip.getId();
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
        Image imageToSave = new Image(ImgUrl, user, saveTrip);
        System.out.println(imageToSave.getImage_url());
        System.out.println(imageToSave.getUser().getUsername());
        System.out.println("about to save image");
        imagesRepository.save(imageToSave);
        System.out.println("saved image");

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
        Trip trip=tripRepository.getOne(id);
        System.out.println(trip.getName());
        model.addAttribute("trip", trip);
        return "Trip/edit";
    }
    @PostMapping(path = "/trip/{id}/edit")

    public String updateTripForm(@PathVariable Long id ,@ModelAttribute Trip trips) {
        Group groups=(Group) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        trips.setId(id);
        trips.setGroup(groups);
        tripRepository.save(trips);
        return "redirect:/trip";
    }



    @PostMapping("/trip/{id}/delete")

    public String DeleteTrip(@PathVariable Long id) {
        tripRepository.deleteById(id);
        return "redirect:/trip";

    }

    @GetMapping(path = "/trip/{id}/activities")
    public String tripActivities(@PathVariable Long id ,Model model){
        Trip trip=tripRepository.getOne(id);
        model.addAttribute("trip", trip);
        return "Trip/activities_google";
    }
}