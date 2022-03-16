package com.uniovi.sdientrega132.services;

import com.uniovi.sdientrega132.entities.User;
import com.uniovi.sdientrega132.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class UsersService {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${spring.data.web.pageable.page-parameter}")
    private int page;

    @Value("${spring.data.web.pageable.search-page-size}")
    private int searchSize;

    public Page<User> getUsers(Pageable pageable) {
        Page<User> users = usersRepository.findAll(pageable);
        return users;
    }

    public Page<User> getStandardUsers(User user, Pageable pageable) {
        Page<User> users = usersRepository.findAllStandard(pageable, user);
        return users;
    }

    public User getUserByEmail(String email){
        return usersRepository.findByEmail(email);
    }

    public void addUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        usersRepository.save(user);
    }

    public Page<User> searchUserByEmailAndName(String searchText, User user) {
        Page<User> users = new PageImpl<User>(new LinkedList<User>());
        searchText  = "%"+searchText+"%";
        Pageable pageable = PageRequest.of(page,searchSize);
        if (user.getRole().equals("ROLE_USER")) {
            users = usersRepository.searchByEmailAndName(pageable, searchText);
        }
        if (user.getRole().equals("ROLE_ADMIN")) {
            users = usersRepository.searchByEmailNameAndSurnames(pageable, searchText);
        }
        return users;
    }

    public void deleteUsers(List<Long> ids) {
        usersRepository.deleteAllById(ids);
    }

}
