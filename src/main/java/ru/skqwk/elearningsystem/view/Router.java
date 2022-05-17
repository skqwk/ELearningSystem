package ru.skqwk.elearningsystem.view;

import com.vaadin.flow.component.Component;
import ru.skqwk.elearningsystem.model.enumeration.UserRole;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Router {

    private static final Map<UserRole, List<Route>> routes = new HashMap<>() {    };


    static {
        routes.put(UserRole.ADMIN, List.of(
                Route.builder()
                        .page(AcademicPlanPage.class)
                        .name("Учебный план")
                        .build(),

                Route.builder()
                        .page(DepartmentsPage.class)
                        .name("Кафедры")
                        .build(),

                Route.builder()
                        .page(GroupsPage.class)
                        .name("Классы")
                        .build(),

                Route.builder()
                        .page(CoursesPage.class)
                        .name("Дисциплины")
                        .build(),

                Route.builder()
                        .page(StudentsPage.class)
                        .name("Ученики")
                        .build(),

                Route.builder()
                        .page(TeachersPage.class)
                        .name("Учителя")
                        .build()
                ));

        routes.put(UserRole.STUDENT, List.of(
                Route.builder()
                        .page(StudentCoursesPage.class)
                        .name("Уроки")
                        .build()
        ));
        routes.put(UserRole.TEACHER, List.of(
                Route.builder()
                        .page(TeacherGroupsPage.class)
                        .name("Программа занятий")
                        .build()
        ));
    }

    public static List<Route> getByRole(UserRole role) {
        return routes.get(role);
    }
}
