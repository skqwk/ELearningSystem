package ru.skqwk.elearningsystem.view;

import com.vaadin.flow.component.Component;
import ru.skqwk.elearningsystem.model.enumeration.UserRole;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Routes {

    private static final Map<UserRole, List<Class<? extends Component>>> routes = new HashMap<>() {    };


    static {
        routes.put(UserRole.ADMIN, List.of(
                AcademicPlanPage.class,
                DepartmentsPage.class,
                GroupsPage.class,
                CoursesPage.class,
                StudentsPage.class,
                TeachersPage.class
                ));

        routes.put(UserRole.STUDENT, List.of(StudentCoursesPage.class));
        routes.put(UserRole.TEACHER, List.of(TeacherGroupsPage.class));
    }

    public static List<Class<? extends Component>> getByRole(UserRole role) {
        return routes.get(role);
    }
}
