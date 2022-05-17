package ru.skqwk.elearningsystem.view;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import ru.skqwk.elearningsystem.model.Student;
import ru.skqwk.elearningsystem.model.Teacher;
import ru.skqwk.elearningsystem.model.User;
import ru.skqwk.elearningsystem.model.enumeration.UserRole;
import ru.skqwk.elearningsystem.security.SecurityService;
import ru.skqwk.elearningsystem.security.UserSingleton;
import ru.skqwk.elearningsystem.services.ELearningService;

import java.util.List;

public class MainLayout extends AppLayout {

    private final SecurityService securityService;
    private final UserRole role;
    private final ELearningService service;


    public MainLayout(SecurityService securityService, ELearningService service) {
        this.securityService = securityService;
        this.service = service;
        this.role = UserSingleton.getUser().getRole();
        createHeader();
        createDrawer();
    }



    private void createHeader() {
        H1 logo = new H1("ELearning System");
        logo.addClassNames("text-l", "m-m");


        User user = UserSingleton.getUser();
        String userHeader = "";

        if (user.getRole().equals(UserRole.ADMIN)) {
            userHeader = "Сотрудник УО";
        } else if (user.getRole().equals(UserRole.STUDENT)) {
            Student student = service.findStudentByUserId(user.getId());
            userHeader = student.getSurname() + " " +
                    student.getName().charAt(0) + ". " +
                    student.getPatronymic().charAt(0) + ".";
        } else if (user.getRole().equals(UserRole.TEACHER)) {
            Teacher teacher = service.findTeacherByUserId(user.getId());
            userHeader = teacher.getSurname() + " " +
                    teacher.getName().charAt(0) + ". " +
                    teacher.getPatronymic().charAt(0) + ".";
        }

        H1 role = new H1(userHeader);
        role.addClassNames("text-l", "m-m");

        Button logout = new Button("Выйти", e -> securityService.logout());


        HorizontalLayout header = new HorizontalLayout(
                new DrawerToggle(),
                logo,
                role,
                logout
                );
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo);
        header.setWidthFull();
        header.addClassNames("py-0", "px-m");

        addToNavbar(header);
    }

    private void createDrawer() {

        List<Route> routes = Router.getByRole(role);
        VerticalLayout links = new VerticalLayout();
        for (Route route : routes) {
            RouterLink link = new RouterLink(route.getName(), route.getPage());
            link.setHighlightCondition(HighlightConditions.sameLocation());
            links.add(link);
        }


        addToDrawer(links);
    }
}
