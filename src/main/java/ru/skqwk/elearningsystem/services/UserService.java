package ru.skqwk.elearningsystem.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skqwk.elearningsystem.dao.UserDao;
import ru.skqwk.elearningsystem.model.User;

@Service
public class UserService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final UserDao userDao;

    public UserService(PasswordEncoder passwordEncoder,
                       UserDao userDao) {
        this.passwordEncoder = passwordEncoder;
        this.userDao = userDao;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findUserByLogin(username);
        if (user == null) {
            throw  new UsernameNotFoundException(String.format("Username: %s not found", username));
        }

        return user;
    }
}
