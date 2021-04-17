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

    public ActivityController(EmailService emailService, TripRepository tripRepository, GroupsRepository groupsRepository, ImageRepository imagesRepository, CommentRepository commentRepository, ActivityRepository activityRepository) {
        this.emailService = emailService;
        this.tripRepository = tripRepository;
        this.groupsRepository = groupsRepository;
        this.imagesRepository = imagesRepository;
        this.commentRepository = commentRepository;
        this.activityRepository = activityRepository;
    }

    @GetMapping(path = "/trip/{id}/activities")
    public String tripActivities(@PathVariable Long id ,Model model){
        Trip trip=tripRepository.getOne(id);
        model.addAttribute("trip", trip);
        model.addAttribute("activity", new Activity());
        return "Trip/activities_google";
    }



    @PostMapping(path = "/trip/{id}/activities")
    public void addActivity(@ModelAttribute Activity activity, @PathVariable Long id) {
        activity.setTrip(tripRepository.getOne(id));
        activityRepository.save(activity);

    }
}