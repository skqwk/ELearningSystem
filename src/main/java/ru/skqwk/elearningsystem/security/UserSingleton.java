package ru.skqwk.elearningsystem.security;

import org.springframework.security.core.context.SecurityContextHolder;
import ru.skqwk.elearningsystem.model.Teacher;
import ru.skqwk.elearningsystem.model.User;
import ru.skqwk.elearningsystem.services.ELearningService;
import ru.skqwk.elearningsystem.services.IELearningService;

public class UserSingleton {
    static private User user;


    public static User getUser() {
        if (user == null) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            user = (User) principal;
            System.out.println(user);
        }
        return user;
    }

    public static void logout() {
        user = null;
    }
}
