package com.trippyTravel.controllers;

import com.trippyTravel.repositories.TripRepository;
import com.trippyTravel.repositories.UsersRepository;
import org.springframework.stereotype.Controller;

@Controller
public class TripLikesController {
    private UsersRepository userDao;
    private TripRepository tripDao;

    public TripLikesController(UsersRepository userDao, TripRepository tripDao) {
        this.userDao = userDao;
        this.tripDao = tripDao;
    }
}
