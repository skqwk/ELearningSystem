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
import ru.skqwk.elearningsystem.security.SecurityService;

//@JsModule("@vaadin/vaadin-lumo-styles/utility.js")
//@CssImport(value="some-stylesheet.css", include="lumo-utility")
public class MainLayout extends AppLayout {

    private final SecurityService securityService;

    public MainLayout(SecurityService securityService) {
        this.securityService = securityService;
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("ELearning System");
        logo.addClassNames("text-l", "m-m");

        Button logout = new Button("Log out", e -> securityService.logout());

        HorizontalLayout header = new HorizontalLayout(
                new DrawerToggle(),
                logo,
                logout
                );
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo);
        header.setWidthFull();
        header.addClassNames("py-0", "px-m");

        addToNavbar(header);
    }

    private void createDrawer() {
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





        addToDrawer(new VerticalLayout(
                teachersPage,
                studentsPage,
                coursesPage,
                groupsPage,
                departmentsPage,
                academicPlanPage
        ));
    }
}
