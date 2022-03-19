package com.uniovi.sdientrega132.controllers;
import com.uniovi.sdientrega132.entities.Friend;
import com.uniovi.sdientrega132.entities.User;
import com.uniovi.sdientrega132.services.RolesService;
import com.uniovi.sdientrega132.services.SecurityService;
import com.uniovi.sdientrega132.services.UsersService;
import com.uniovi.sdientrega132.validators.SignUpFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.*;

@Controller
public class UsersController {
    @Autowired
    private UsersService usersService;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private SignUpFormValidator signUpFormValidator;
    @Autowired
    private RolesService rolesService;

    @Autowired
    private LogsController logsController;

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String signup(@Validated User user, BindingResult result) {
        signUpFormValidator.validate(user, result);
        if (result.hasErrors()) {
            return "signup";
        }
        user.setRole(rolesService.getRoles()[0]);
        usersService.addUser(user);
        System.out.println("C");
        securityService.autoLogin(user.getEmail(), user.getPasswordConfirm());
        return "redirect:/user/list";
        //return "redirect:/home";
    }

    @RequestMapping("/user/list")
    public String getListado(Model model, Pageable pageable,
                             @RequestParam(value = "", required = false) String searchText) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User activeUser = usersService.getUserByEmail(email);
        Page<User> users;
        List<User> usersFriends = new ArrayList<>();
        if (searchText != null && !searchText.isEmpty())
            users = usersService.searchUserByEmailAndName(searchText, activeUser, pageable);
        else {
            if (activeUser.getRole().equals("ROLE_ADMIN")) {
                users = usersService.getUsers(pageable);
            } else {
                users = usersService.getStandardUsers(activeUser, pageable);
            }
        }
        System.out.println(activeUser.getId());
        for (Long f:activeUser.getFriends()) {
            System.out.println("2"+f.toString());
        }

        for (User u : users) {
                if(activeUser.isFriend(u.getId()) && !usersFriends.contains(u)) {
                    usersFriends.add(u);
                }
        }
        Page<User> aux = new PageImpl<>(usersFriends);
        model.addAttribute("actualUser", activeUser);
        model.addAttribute("usersList", users.getContent());
        model.addAttribute("usersListFriends", aux);
        model.addAttribute("page", users);
        return "user/list";
    }

    @RequestMapping("/user/list/update")
    public String updateList(Model model, Pageable pageable, Principal principal) {
        String email = principal.getName(); // Email es el name de la autenticación
        User user = usersService.getUserByEmail(email);
        model.addAttribute("usersList", usersService.getUsers(pageable));
        return "user/list :: tableUsers";
    }

    @GetMapping("/user/delete")
    public String delete(@RequestParam(value = "", required = false) List<Long> ids) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User activeUser = usersService.getUserByEmail(email);
        ids.remove(activeUser.getId());
        usersService.deleteUsers(ids);
        return "redirect:/user/list";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String signup(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, String error, String logout) {
        if (error != null) {
            model.addAttribute("error", "");
            logsController.LogInEr();
        }
        if (logout != null) {
            model.addAttribute("message", "");
            logsController.LogOut();
        }
        return "login";
    }

    @RequestMapping(value = {"/home"}, method = RequestMethod.GET)
    public String home(Model model) {
        return "home";
    }

}
