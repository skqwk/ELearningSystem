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
import ru.skqwk.elearningsystem.model.Group;
import ru.skqwk.elearningsystem.model.Student;
import ru.skqwk.elearningsystem.services.IELearningService;
import ru.skqwk.elearningsystem.view.components.StudentForm;

import javax.annotation.security.PermitAll;
import java.util.List;

@PageTitle("Students")
@Route(value="students", layout = MainLayout.class)
@PermitAll
public class StudentsPage extends VerticalLayout {

    private final IELearningService service;
    TextField filterText = new TextField();
    StudentForm studentForm;
    final Grid<Student> grid = new Grid<>(Student.class, false);

    public StudentsPage(IELearningService service) {
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



    private Component getToolbar() {
        filterText.setPlaceholder("Filter by name");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addNewStudent = new Button("Add new student");
        addNewStudent.addClickListener(e -> addStudent());
        HorizontalLayout toolbar = new HorizontalLayout(filterText, addNewStudent);
        toolbar.addClassName("toolbar");

        return toolbar;
    }

    private void configureForm() {
        studentForm = new StudentForm(service.findAllGroups(), new Student());
        studentForm.setWidth("25em");

        studentForm.addListener(StudentForm.SaveEvent.class, this::saveStudent);
        studentForm.addListener(StudentForm.DeleteEvent.class, this::deleteStudent);
        studentForm.addListener(StudentForm.CloseEvent.class,  e -> closeEditor());
    }

    private void closeEditor() {
        studentForm.setEntity(null);
        studentForm.setVisible(false);
        removeClassName("editing");
    }


    private void saveStudent(StudentForm.SaveEvent event) {
        service.saveStudent(event.getEntity());
        updateList();
        closeEditor();
    }

    private void deleteStudent(StudentForm.DeleteEvent event) {

        service.deleteStudent(event.getEntity());
        updateList();
        closeEditor();
    }

    private void addStudent() {
        grid.asSingleSelect().clear();
        editStudent(new Student());
    }

    private void editStudent(Student student) {
        if (student == null) {
            closeEditor();
        } else {
            System.out.println("Set visible");
            studentForm.setEntity(student);
            studentForm.setVisible(true);
            addClassName("editing");
        }
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, studentForm);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, studentForm);
        content.addClassName("content");
        content.setSizeFull();

        return content;
    }

    private void configureGrid() {
        grid.addClassName("student-grid");
        grid.setSizeFull();

        grid.setColumns("name", "surname", "patronymic");
        grid.addColumn(student -> {
            Group group = student.getGroup();
            if (group != null) {
                return group.getNumber() + " " + group.getLiteral();
            } else {
                return "";
            }

        }).setHeader("Класс");


        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(e -> editStudent(e.getValue()));
    }

    private void updateList() {
        List<Student> students = service.findAllStudents();
        System.out.println(students);
        grid.setItems(students);
    }

}
