package com.trippyTravel.controllers;
import com.trippyTravel.models.FriendList;
import com.trippyTravel.models.FriendStatus;
import com.trippyTravel.models.Trip;
import com.trippyTravel.models.User;
import com.trippyTravel.services.EmailService;
import com.trippyTravel.services.UserService;
import org.apache.naming.factory.SendMailFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.trippyTravel.repositories.UsersRepository;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Fer on 1/10/17.
 */
@Controller
public class AuthenticationController {

    @Autowired
    private UserService usersService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsersRepository usersRepository;

  @Autowired
  private EmailService emailService;

    @GetMapping("/login")
    public String login(Model model){
        List<FriendList> friendRequests= new ArrayList<>();
        model.addAttribute("friendRequests", friendRequests);
        List<Trip> unreadCommentTrips = new ArrayList<>();
        model.addAttribute("unreadCommentTrips", unreadCommentTrips );
        return "login";
    }
    @GetMapping("/login/forgot_password")
    public String forgotPassword(Model m){
        List<FriendList> friendRequests= new ArrayList<>();
        m.addAttribute("friendRequests", friendRequests);
        List<Trip> unreadCommentTrips = new ArrayList<>();
        m.addAttribute("unreadCommentTrips", unreadCommentTrips);


        return "forgot_password";
    }

    @PostMapping("/forgot_username")
    public String forgotUsername(Model m ,@RequestParam(name = "email") String email,@RequestParam(name = "emailConfirm") String emailConfirm) {
        System.out.println(email);

        User existingEmail = usersRepository.findUserByEmail(email);
        if (email.equals(emailConfirm)) {
            if (existingEmail != null) {
                System.out.println("We got in here!");
                emailService.prepareAndSend1(existingEmail, email);
                List<FriendList> friendRequests= new ArrayList<>();
                m.addAttribute("friendRequests", friendRequests);
                List<Trip> unreadCommentTrips = new ArrayList<>();
                m.addAttribute("unreadCommentTrips", unreadCommentTrips);
                return "login";
            }
        }

         List<FriendList> friendRequests= new ArrayList<>();
      m.addAttribute("friendRequests", friendRequests);
      List<Trip> unreadCommentTrips = new ArrayList<>();
      m.addAttribute("unreadCommentTrips", unreadCommentTrips);
            return "forgot_password";
         }


    @GetMapping("/async-login")
    public String asyncLogin(){
        return "users/async-login";
    }

    @PostMapping("/async-login")
    @ResponseBody
    public HashMap<String, String> doLogin(@RequestParam(name = "username") String username, @RequestParam(name = "password") String password){
        HashMap<String, String> response = new HashMap<>();
        User dbUser = usersRepository.findByUsername(username);
        if(dbUser != null && passwordEncoder.matches(password, dbUser.getPassword())) {
            usersService.authenticate(dbUser);
            response.put("status", "ok");
        }else{
            response.put("status", "error");
        }
        return response;
    }

    @GetMapping("/register")
    public String showForm(Model m){
        m.addAttribute("user", new User());
        List<FriendList> friendRequests= new ArrayList<>();
        m.addAttribute("friendRequests", friendRequests);
        List<Trip> unreadCommentTrips = new ArrayList<>();
        m.addAttribute("unreadCommentTrips", unreadCommentTrips);
        return "users/create";
    }


}
