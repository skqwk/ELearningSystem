package ru.skqwk.elearningsystem.view.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.shared.Registration;
import ru.skqwk.elearningsystem.model.Course;
import ru.skqwk.elearningsystem.model.Department;
import ru.skqwk.elearningsystem.model.Teacher;

public class DepartmentForm extends FormLayout {
    Binder<Department> binder = new BeanValidationBinder<>(Department.class);

    TextField name = new TextField("Название кафедры");
    Grid<Teacher> teachersGrid = new Grid<>(Teacher.class, false);
    Grid<Course> coursesGrid = new Grid<>(Course.class, false);

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button cancel = new Button("Cancel");

    private Department department;

    public DepartmentForm(Department department) {
        this.department =  department;
        addClassName("department-form");
        binder.bindInstanceFields(this);

        configureTeachersGrid();
        configureCoursesGrid();

        coursesGrid.setItems(DataProvider.ofCollection(department.getCourses()));
        teachersGrid.setItems(DataProvider.ofCollection(department.getTeachers()));

        add(
                name,
                teachersGrid,
                coursesGrid,
                createButtonsLayout()
        );
    }

    private void configureCoursesGrid() {
        coursesGrid.addColumn(Course::getName).setHeader("Название предмета");

        coursesGrid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private void configureTeachersGrid() {

        teachersGrid.addClassName("teacher-grid");

        teachersGrid.addColumn(student ->
                        String.format("%s %s %s",
                                student.getSurname(),
                                student.getName(),
                                student.getPatronymic()))
                .setHeader("ФИО");

        teachersGrid.getColumns().forEach(col -> col.setAutoWidth(true));

    }

    public void setEntity(Department department) {
        this.department =  department;
        binder.readBean(department);
        if (department != null) {

            coursesGrid.setItems(DataProvider.ofCollection(department.getCourses()));
            teachersGrid.setItems(DataProvider.ofCollection(department.getTeachers()));
        }
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DepartmentForm.DeleteEvent(this, department)));
        cancel.addClickListener(event -> fireEvent(new DepartmentForm.CloseEvent(this)));

        save.addClickShortcut(Key.ENTER);
        cancel.addClickShortcut(Key.ESCAPE);

        return new HorizontalLayout(delete, cancel, save);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(department);
            fireEvent(new DepartmentForm.SaveEvent(this, department));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }


    public static abstract class DepartmentFormEvent extends ComponentEvent<DepartmentForm> {
        private Department department;

        protected DepartmentFormEvent(DepartmentForm source, Department department) {
            super(source, false);
            this.department =  department;
        }

        public Department getEntity() {
            return department;
        }
    }

    public static class SaveEvent extends DepartmentForm.DepartmentFormEvent {
        SaveEvent(DepartmentForm source, Department department) {
            super(source, department);
        }


    }

    public static class DeleteEvent extends DepartmentForm.DepartmentFormEvent {
        DeleteEvent(DepartmentForm source, Department department) {
            super(source, department);
        }

    }

    public static class CloseEvent extends DepartmentForm.DepartmentFormEvent {
        CloseEvent(DepartmentForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
