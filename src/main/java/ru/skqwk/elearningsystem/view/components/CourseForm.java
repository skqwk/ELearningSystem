package ru.skqwk.elearningsystem.view.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import ru.skqwk.elearningsystem.model.Course;
import ru.skqwk.elearningsystem.model.Department;

import java.util.List;

public class CourseForm extends FormLayout {
    Binder<Course> binder = new BeanValidationBinder<>(Course.class);

    TextField name = new TextField("name");
    ComboBox<Department> department = new ComboBox<>("department");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button cancel = new Button("Cancel");

    private Course course;

    public CourseForm(List<Department> departments, Course course) {
        this.course =  course;
        addClassName("Course-form");
        binder.bindInstanceFields(this);
        department.setItems(departments);
        department.setItemLabelGenerator(Department::getName);

        add(
                name,
                department,
                createButtonsLayout()
        );
    }

    public void setEntity(Course course) {
        this.course =  course;
        binder.readBean(course);
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new CourseForm.DeleteEvent(this, course)));
        cancel.addClickListener(event -> fireEvent(new CourseForm.CloseEvent(this)));

        save.addClickShortcut(Key.ENTER);
        cancel.addClickShortcut(Key.ESCAPE);

        return new HorizontalLayout(delete, cancel, save);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(course);
            fireEvent(new CourseForm.SaveEvent(this, course));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }


    public static abstract class CourseFormEvent extends ComponentEvent<CourseForm> {
        private Course course;

        protected CourseFormEvent(CourseForm source, Course course) {
            super(source, false);
            this.course =  course;
        }

        public Course getEntity() {
            return course;
        }
    }

    public static class SaveEvent extends CourseForm.CourseFormEvent {
        SaveEvent(CourseForm source, Course course) {
            super(source, course);
        }


    }

    public static class DeleteEvent extends CourseForm.CourseFormEvent {
        DeleteEvent(CourseForm source, Course course) {
            super(source, course);
        }

    }

    public static class CloseEvent extends CourseForm.CourseFormEvent {
        CloseEvent(CourseForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
