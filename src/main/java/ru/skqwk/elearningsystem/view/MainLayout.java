package ru.skqwk.elearningsystem.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import ru.skqwk.elearningsystem.model.User;
import ru.skqwk.elearningsystem.model.enumeration.UserRole;
import ru.skqwk.elearningsystem.security.SecurityService;
import ru.skqwk.elearningsystem.security.UserSingleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@JsModule("@vaadin/vaadin-lumo-styles/utility.js")
//@CssImport(value="some-stylesheet.css", include="lumo-utility")
public class MainLayout extends AppLayout {

    private final SecurityService securityService;
    private final UserRole role;


    public MainLayout(SecurityService securityService) {
        this.securityService = securityService;
        this.role = UserSingleton.getUser().getRole();
        createHeader();
        createDrawer();
    }



    private void createHeader() {
        H1 logo = new H1("ELearning System");
        logo.addClassNames("text-l", "m-m");

        H1 role = new H1(UserSingleton.getUser().getRole().name());
        role.addClassNames("text-l", "m-m");

        Button logout = new Button("Log out", e -> securityService.logout());


        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        System.out.println(principal.getClass());

        if (principal instanceof User) {
            User currentUser = (User) principal;
            System.out.println(currentUser);
        } else {
            String username = principal.toString();
            System.out.println(username);
        }


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

        List<Class<? extends Component>> pages = Routes.getByRole(role);
        VerticalLayout links = new VerticalLayout();
        for (Class<? extends Component> page : pages) {
            RouterLink link = new RouterLink(page.getSimpleName(), page);
            link.setHighlightCondition(HighlightConditions.sameLocation());
            links.add(link);
        }


        RouterLink teachersPage = new RouterLink("Teachers", TeachersPage.class);
        teachersPage.setHighlightCondition(HighlightConditions.sameLocation());

        RouterLink studentsPage = new RouterLink("Students", StudentsPage.class);
        studentsPage.setHighlightCondition(HighlightConditions.sameLocation());

        RouterLink coursesPage = new RouterLink("Courses", CoursesPage.class);
        coursesPage.setHighlightCondition(HighlightConditions.sameLocation());

        RouterLink groupsPage = new RouterLink("Groups", GroupsPage.class);
        groupsPage.setHighlightCondition(HighlightConditions.sameLocation());

        RouterLink departmentsPage = new RouterLink("Departments", DepartmentsPage.class);
        departmentsPage.setHighlightCondition(HighlightConditions.sameLocation());

        RouterLink academicPlanPage = new RouterLink("Academic Plan", AcademicPlanPage.class);
        academicPlanPage.setHighlightCondition(HighlightConditions.sameLocation());


        addToDrawer(links);
    }
}
