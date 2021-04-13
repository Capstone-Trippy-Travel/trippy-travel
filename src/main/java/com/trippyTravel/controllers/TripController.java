package com.trippyTravel.controllers;

import com.trippyTravel.models.Group;
import com.trippyTravel.models.Trip;
import com.trippyTravel.models.User;
import com.trippyTravel.repositories.GroupsRepository;
import com.trippyTravel.repositories.TripRepository;
import com.trippyTravel.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class TripController {
    @Autowired
    private final EmailService emailService;
    private final TripRepository tripRepository;
    private final GroupsRepository groupsRepository;

    public TripController(EmailService emailService, TripRepository tripRepository, GroupsRepository groupsRepository) {
        this.emailService = emailService;
        this.tripRepository = tripRepository;
        this.groupsRepository=groupsRepository;

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

    @GetMapping("/trip/{id}")
    public String showOneTrip(@PathVariable Long id, Model vModel){
        vModel.addAttribute("trips", tripRepository.getOne(id));
        return "Trip/show";
    }

    @GetMapping("/trip/create")
    public String createTrip(Model model){
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("trip", new Trip());
        model.addAttribute("groups", groupsRepository.findByOwner(user));
        return "Trip/create";
    }

    @PostMapping("/trip/create")

    public String createTripForm(@ModelAttribute Trip trips) {
//        Group groups=(Group) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

//        trips.setGroup(groups);
//        Image imagesToSave= new Image(image0);
//        Image image1ToSave= new Image(image1);
//        imagesToSave.setPost(post);
//        image1ToSave.setPost(post);
//      post.setImages(imagesToSave);

        Trip saveTrip= tripRepository.save(trips);
//        imageRepo.save(imagesToSave);
//        imageRepo.save(image1ToSave);
//        emailService.prepareAndSend(saveTrip, "new trip","hey where you wanna go");

        return "redirect:/trip/"+saveTrip.getId();
    }
    @GetMapping(path = "/trip/{id}/edit")
    public String updateTrip(@PathVariable Long id ,Model model){

        model.addAttribute("trip",tripRepository.getOne(id));
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
}
