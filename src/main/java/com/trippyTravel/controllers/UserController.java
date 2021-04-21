package com.trippyTravel.controllers;



import com.trippyTravel.models.*;
import com.trippyTravel.repositories.*;
import com.trippyTravel.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private GroupsRepository groupsRepository;

    @Autowired
    private GroupMembersRepository groupMembersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRoles userRoles;

    @Autowired
    private UserService usersService;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private FriendListRepository friendListRepository;


    @Value("${mapBoxToken}")
    private String mapBoxToken;

    @Value("${fileStackApiKey}")
    private String fileStackApiKey;

    @PostMapping("/users/create")
    public String saveUser(@Valid User user, Errors validation, Model m){

        String username = user.getUsername();
        User existingUsername = usersRepository.findByUsername(username);
        User existingEmail = usersRepository.findByEmail(user.getEmail());


        if(existingUsername != null){

            validation.rejectValue("username", "user.username", "Duplicated username " + username);
        }

        if(existingEmail != null){

            validation.rejectValue("email", "user.email", "Duplicated email " + user.getEmail());

        }

        if (validation.hasErrors()) {
            m.addAttribute("errors", validation);
            m.addAttribute("user", user);
            return "users/create";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Custom validation if the username is taken

        User newUser = usersRepository.save(user);
        Group newGroup = new Group(newUser.getUsername(), newUser);
        groupsRepository.save(newGroup);
        groupMembersRepository.save(new GroupMember(true, user, newGroup));

        UserRole ur = new UserRole();
        ur.setRole("ROLE_USER");
        ur.setUserId(newUser.getId());
        userRoles.save(ur);

        // Programmatic login after registering a user
        usersService.authenticate(user);

        m.addAttribute("user", user);
        return "redirect:/";

    }

    @GetMapping("/users/{id}")
    public String showUser(@PathVariable Long id, Model viewModel){
        User user = usersRepository.getOne(id);
        viewModel.addAttribute("user", user);
        List<GroupMember> groupMembers=groupMembersRepository.findGroupMembersByMember(user);
        List<Group> groups = new ArrayList<>();
        for (int i=0; i<groupMembers.size(); i++){
            groups.add(groupMembers.get(i).getGroup());
        }
        System.out.println(groups.size());
        viewModel.addAttribute("groups", groups);

        List<Trip> trips = new ArrayList<>();
        for (int i=0; i<groups.size(); i++){
            List<Trip> groupTrips = groups.get(i).getTrip();
            for (int j=0; j<groupTrips.size(); j++){
                trips.add(groupTrips.get(j));
            }
        }
        for (int i=0; i<trips.size(); i++){
            if (trips.get(i).getName()!=null) {
                System.out.println(trips.get(i).getName());
            }
        }

        System.out.println(trips.size());
        viewModel.addAttribute("trips", trips);
        if (SecurityContextHolder.getContext().getAuthentication().getName()==null || SecurityContextHolder.getContext().getAuthentication().getName().equalsIgnoreCase("anonymousUser")){
            List<FriendList> friendRequests= new ArrayList<>();
            viewModel.addAttribute("friendRequests", friendRequests);
            List<Trip> unreadCommentTrips = new ArrayList<>();
            viewModel.addAttribute("unreadCommentTrips", unreadCommentTrips);

        } else{
            User loggedInuser= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            viewModel.addAttribute("friendRequests", friendListRepository.findFriendListByFriendAndStatus(loggedInuser, FriendStatus.PENDING));
            viewModel.addAttribute("unreadCommentTrips", tripRepository.getUnreadCommentTrips(loggedInuser) );
        }
        viewModel.addAttribute("sessionUser", usersService.loggedInUser());
        viewModel.addAttribute("showEditControls", usersService.canEditProfile(user));

        viewModel.addAttribute("plannedTrips", tripRepository.findTripsByStatus(id, "future"));

        return "users/show";
    }

    @GetMapping("/users/profile")
    public String showProfile(Model viewModel){
        User logUser = usersService.loggedInUser();

        if(logUser == null){
            viewModel.addAttribute("msg", "You need to be logged in to be able to see this page");
            return "error/custom";
        }

        return "redirect:/users/" + usersService.loggedInUser().getId();
    }

    @GetMapping("/users/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model viewModel){
        User user = usersRepository.getOne(id);
        viewModel.addAttribute("user", user);
        viewModel.addAttribute("showEditControls", usersService.canEditProfile(user));
        if (SecurityContextHolder.getContext().getAuthentication().getName()==null || SecurityContextHolder.getContext().getAuthentication().getName().equalsIgnoreCase("anonymousUser")){
            List<FriendList> friendRequests= new ArrayList<>();
            viewModel.addAttribute("friendRequests", friendRequests);
            List<Trip> unreadCommentTrips = new ArrayList<>();
            viewModel.addAttribute("unreadCommentTrips", unreadCommentTrips);

        } else{
            User loggedInuser= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            viewModel.addAttribute("friendRequests", friendListRepository.findFriendListByFriendAndStatus(loggedInuser, FriendStatus.PENDING));
            viewModel.addAttribute("unreadCommentTrips", tripRepository.getUnreadCommentTrips(loggedInuser) );
        }
        return "users/edit";
    }

    @PostMapping("/users/{id}/edit")
    public String editUser(@PathVariable Long id, @Valid User editedUser, Errors validation, Model m){

        editedUser.setId(id);

        if (validation.hasErrors()) {
            m.addAttribute("errors", validation);
            m.addAttribute("user", editedUser);
            m.addAttribute("showEditControls", checkEditAuth(editedUser));
            return "users/edit";
        }
        editedUser.setPassword(passwordEncoder.encode(editedUser.getPassword()));
        usersRepository.save(editedUser);
        return "redirect:/users/"+id;
    }

    // Edit controls are being showed up if the user is logged in and it's the same user viewing the file
    public Boolean checkEditAuth(User user){
        return usersService.isLoggedIn() && (user.getId() == usersService.loggedInUser().getId());
    }

    @GetMapping("/users/friends")
    public String showFriends(Model vModel){
        User user = usersService.loggedInUser();
        vModel.addAttribute("friendsList", user.getFriends());
        return "users/friends";
    }

    @RequestMapping(value="/users/{id}/friend-request", method=RequestMethod.GET, produces="application/json")
    public @ResponseBody FriendList sendFriendRequest(@PathVariable long id ) {
        FriendList friendRequest=friendListRepository.save(new FriendList(usersService.loggedInUser(), usersRepository.getOne(id), FriendStatus.PENDING));
        return friendRequest;
    }

    @RequestMapping(value="/users/friend-requests/{id}", method=RequestMethod.GET, produces="application/json")
    public @ResponseBody FriendList sendFriendRequestResponse(@PathVariable long id, @RequestParam("response") String response ) {
        FriendList friendRequestResponse=friendListRepository.getOne(id);
        if (response.equalsIgnoreCase("approve")){
            friendRequestResponse.setStatus(FriendStatus.ACCEPTED);
            friendListRepository.save(new FriendList(friendRequestResponse.getFriend(), friendRequestResponse.getUser(), FriendStatus.ACCEPTED));
        } else{
            System.out.println("rejecting friend request");
            friendRequestResponse.setStatus(FriendStatus.REJECTED);
            friendListRepository.save(friendRequestResponse);
        }
        return friendRequestResponse;
    }

//    @GetMapping("/users.json")
//    public @ResponseBody List<User> viewAllUsersWithAjax() {
//        return usersRepository.findAll();
//    }

    @RequestMapping(value="/users.json", method=RequestMethod.GET, produces="application/json")
    public @ResponseBody List<User> viewSearchedUsersWithAjax(@RequestParam("name") String name) {
        User loggedInUser = usersService.loggedInUser();

        //grabbing all users in database that match searched term
        List<User> users= usersRepository.findByFirstNameContainingOrLastNameContainingOrUsernameContaining(name, name, name);
        System.out.println(users.size());
        //creating filtered list to pass to page (not including you if you searched)
        List<User> filteredUsers = new ArrayList<>();

        //will loop through users list created above
        for (User user: users){

            //will not add signed in user to list
            if (user.getId()!=loggedInUser.getId()) {

                //default status
            String status="not friends";

            //checking to see if user is a friend in database, or if friend request is pending
            Boolean friendExists = friendListRepository.existsFriendListByFriend_IdAndUser_Id(user.getId(), loggedInUser.getId());
                System.out.println("friendExists boolean: "+friendExists);
            //if friend does exist, will pass the friend request status to page
            if (friendExists){
                System.out.println("finding friends");
                FriendList friend=friendListRepository.findFriendListByFriend_IdAndUser_Id(user.getId(), loggedInUser.getId());
                status=friend.getStatus().name();
            }

                //will set status to user and pass to page
                 user.setFriendStatus(status);

                filteredUsers.add(user);
            }
        }
        return filteredUsers;
    }
}
