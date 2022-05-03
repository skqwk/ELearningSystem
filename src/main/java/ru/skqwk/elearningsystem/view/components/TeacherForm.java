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
import ru.skqwk.elearningsystem.model.Department;
import ru.skqwk.elearningsystem.model.Teacher;

import java.util.List;

public class TeacherForm extends FormLayout {
    Binder<Teacher> binder = new BeanValidationBinder<>(Teacher.class);


    TextField name = new TextField("name");
    TextField surname = new TextField("surname");
    TextField patronymic = new TextField("patronymic");
    ComboBox<Department> department = new ComboBox<>("department");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button cancel = new Button("Cancel");

    private Teacher teacher;

    public TeacherForm(List<Department> departments, Teacher teacher) {
        this.teacher =  teacher;
        addClassName("teacher-form");
        binder.bindInstanceFields(this);
        department.setItems(departments);
        department.setItemLabelGenerator(Department::getName);

        add(
                name,
                surname,
                patronymic,
                department,
                createButtonsLayout()
        );
    }

    public void setTeacher(Teacher teacher) {
        this.teacher =  teacher;
        binder.readBean(teacher);
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, teacher)));
        cancel.addClickListener(event -> fireEvent(new CloseEvent(this)));

        save.addClickShortcut(Key.ENTER);
        cancel.addClickShortcut(Key.ESCAPE);

        return new HorizontalLayout(delete, cancel, save);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(teacher);
            fireEvent(new SaveEvent(this, teacher));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }


    public static abstract class TeacherFormEvent extends ComponentEvent<TeacherForm> {
        private Teacher teacher;

        protected TeacherFormEvent(TeacherForm source, Teacher teacher) {
            super(source, false);
            this.teacher =  teacher;
        }

        public Teacher getTeacher() {
            return teacher;
        }
    }

    public static class SaveEvent extends TeacherFormEvent {
        SaveEvent(TeacherForm source, Teacher teacher) {
            super(source, teacher);
        }
    }

    public static class DeleteEvent extends TeacherFormEvent {
        DeleteEvent(TeacherForm source, Teacher teacher) {
            super(source, teacher);
        }

    }

    public static class CloseEvent extends TeacherFormEvent {
        CloseEvent(TeacherForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
