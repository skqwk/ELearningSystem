package ru.skqwk.elearningsystem.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.skqwk.elearningsystem.model.Teacher;
import ru.skqwk.elearningsystem.services.ELearningService;
import ru.skqwk.elearningsystem.view.components.TeacherForm;

import javax.annotation.security.PermitAll;

@PageTitle("Teachers")
@Route(value="teachers", layout = MainLayout.class)
@PermitAll
public class TeachersPage extends VerticalLayout {


    private final ELearningService service;

    Grid<Teacher> grid = new Grid<>(Teacher.class);
    TextField filterText = new TextField();
    TeacherForm teacherForm;


    public TeachersPage(ELearningService service) {
        this.service = service;

        addClassName("list-view");
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
        teacherForm.setTeacher(null);
        teacherForm.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(service.findAllTeachersByFilter(filterText.getValue()));
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, teacherForm);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, teacherForm);
        content.addClassName("content");
        content.setSizeFull();

        return content;
    }

    private void configureForm() {
        teacherForm = new TeacherForm(service.findAllDepartments(), new Teacher());
        teacherForm.setWidth("25em");

        teacherForm.addListener(TeacherForm.SaveEvent.class, this::saveTeacher);
        teacherForm.addListener(TeacherForm.DeleteEvent.class, this::deleteTeacher);
        teacherForm.addListener(TeacherForm.CloseEvent.class,  e -> closeEditor());
    }

    private void saveTeacher(TeacherForm.SaveEvent event) {
        service.saveTeacher(event.getTeacher());
        updateList();
        closeEditor();
    }

    private void deleteTeacher(TeacherForm.DeleteEvent event) {
        service.deleteTeacher(event.getTeacher());
        updateList();
        closeEditor();
    }

    private Component getToolbar() {
        filterText.setPlaceholder("Filter by name");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addNewTeacher = new Button("Add new teacher");
        addNewTeacher.addClickListener(e -> addTeacher());
        HorizontalLayout toolbar = new HorizontalLayout(filterText, addNewTeacher);
        toolbar.addClassName("toolbar");

        return toolbar;
    }

    private void addTeacher() {
        grid.asSingleSelect().clear();
        editTeacher(new Teacher());
    }

    private void configureGrid() {
        grid.addClassName("teacher-grid");
        grid.setSizeFull();
        grid.setColumns("name", "surname", "patronymic", "workExperience");

        grid.addColumn(teacher -> teacher.getDepartment().getName()).setHeader("Кафедра");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(e -> editTeacher(e.getValue()));
    }

    private void editTeacher(Teacher teacher) {
        if (teacher == null) {
            closeEditor();
        } else {
            teacherForm.setTeacher(teacher);
            teacherForm.setVisible(true);
            addClassName("editing");
        }
    }
}
