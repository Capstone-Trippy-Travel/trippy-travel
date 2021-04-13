package com.trippyTravel.controllers;

import com.trippyTravel.models.Groups;
import com.trippyTravel.models.Trips;
import com.trippyTravel.models.User;
import com.trippyTravel.repositories.TripRepository;
import com.trippyTravel.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TripController {
    @Autowired
private final EmailService emailService;
    private final TripRepository tripRepository;

    public TripController(EmailService emailService, TripRepository tripRepository) {
        this.emailService = emailService;
        this.tripRepository = tripRepository;
    }

    @GetMapping("/trip/create")
    public String createTrip(Model model){
        model.addAttribute("Trips", new Trips());
        return "Trip/create";
    }
    @PostMapping("/Trip/create")

    public String createPostForm(@ModelAttribute Trips trips) {
        Groups groups=(Groups) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        trips.setGroup(groups);
//        Image imagesToSave= new Image(image0);
//        Image image1ToSave= new Image(image1);
//        imagesToSave.setPost(post);
//        image1ToSave.setPost(post);
//      post.setImages(imagesToSave);

        Trips saveTrip= tripRepository.save(trips);
//        imageRepo.save(imagesToSave);
//        imageRepo.save(image1ToSave);
        emailService.prepareAndSend(saveTrip, "new trip","hey where you wanna go");
        return "redirect:/Trips";
    }
}
