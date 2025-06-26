package org.yearup.controllers;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.data.ProfileDao;
import org.yearup.data.UserDao;
import org.yearup.models.Profile;
import org.yearup.models.User;

import java.security.Principal;

@RestController
@RequestMapping("/profile")
@CrossOrigin
@PreAuthorize("isAuthenticated()")
public class ProfileController
{
    private final ProfileDao profileDao;
    private final UserDao userDao;

    @Autowired
    public ProfileController(ProfileDao profileDao, UserDao userDao)
    {
        this.profileDao = profileDao;
        this.userDao = userDao;
    }

    @GetMapping
    public Profile getProfile(Principal principal)
    {
        String username = principal.getName();
        int userId = userDao.getIdByUsername(username);
        return profileDao.getByUserId(userId);
    }

    @PutMapping
    public void updateProfile(@RequestBody Profile profile, Principal principal)
    {
        String username = principal.getName();
        int userId = userDao.getIdByUsername(username);
        profileDao.update(userId, profile);
    }
}
