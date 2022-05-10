package ru.skqwk.elearningsystem.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.skqwk.elearningsystem.model.Course;
import ru.skqwk.elearningsystem.services.IELearningService;
import ru.skqwk.elearningsystem.view.components.CourseForm;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;

@PageTitle("Courses")
@Route(value="courses", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class CoursesPage extends VerticalLayout {

    private final IELearningService service;
    private final TextField filterText = new TextField();
    private final Grid<Course> grid = new Grid<>(Course.class);
    Dialog addGroups = new Dialog();

    CourseForm courseForm;

    public CoursesPage(IELearningService service) {
        this.service = service;
        addClassName("courses-list");
        setSizeFull();

        configureGrid();
        configureForm();

        add(
                getToolbar(),
                getContent()
        );

        updateList();
        closeEditor();
    }

    private void closeEditor() {
        courseForm.setEntity(null);
        courseForm.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(service.findAllCourses());
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, courseForm);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, courseForm);
        content.addClassName("content");
        content.setSizeFull();

        return content;
    }

    private void configureForm () {
        courseForm = new CourseForm(new Course(), service.findAllDepartments());
        courseForm.setWidth("25em");


        courseForm.addListener(CourseForm.SaveEvent.class, this::saveCourse);
        courseForm.addListener(CourseForm.DeleteEvent.class, this::deleteCourse);
        courseForm.addListener(CourseForm.CloseEvent.class, e -> closeEditor());
    }

    private void saveCourse(CourseForm.SaveEvent event) {
        service.saveCourse(event.getEntity());
        updateList();
        closeEditor();
    }

    private void deleteCourse (CourseForm.DeleteEvent event) {
        service.deleteCourse(event.getEntity());
        updateList();
        closeEditor();
    }

    private Component getToolbar () {
        filterText.setPlaceholder("Filter by name");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addNewCourse = new Button("Add new course");
        addNewCourse.addClickListener(e -> addCourse());
        HorizontalLayout toolbar = new HorizontalLayout(filterText, addNewCourse);
        toolbar.addClassName("toolbar");

        return toolbar;
    }

    private void addCourse() {
        grid.asSingleSelect().clear();
        editCourse(new Course());
    }

    private void configureGrid () {
        grid.addClassName("course-grid");
        grid.setSizeFull();
        grid.setColumns("name");

        grid.addColumn(course -> course.getDepartment().getName()).setHeader("Кафедра");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(e -> editCourse(e.getValue()));
    }

    private void editCourse(Course course){
        if (course == null) {
            closeEditor();
        } else {
            courseForm.setEntity(course);
            courseForm.setVisible(true);
            addClassName("editing");
        }
    }
}


