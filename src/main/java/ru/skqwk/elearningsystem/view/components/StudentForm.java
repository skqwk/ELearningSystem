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
import ru.skqwk.elearningsystem.model.Group;
import ru.skqwk.elearningsystem.model.Student;

import java.util.List;

public class StudentForm extends FormLayout {
    Binder<Student> binder = new BeanValidationBinder<>(Student.class);

    TextField name = new TextField("name");
    TextField surname = new TextField("surname");
    TextField patronymic = new TextField("patronymic");
    ComboBox<Group> group = new ComboBox<>("group");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button cancel = new Button("Cancel");

    private Student student;

    public StudentForm(List<Group> groups, Student student) {
        this.student =  student;
        addClassName("student-form");
        binder.bindInstanceFields(this);
        group.setItems(groups);
        group.setItemLabelGenerator(g -> String.format("%s %s", g.getNumber(), g.getLiteral()));

        add(
                name,
                surname,
                patronymic,
                group,
                createButtonsLayout()
        );
    }

    public void setEntity(Student student) {
        this.student =  student;
        binder.readBean(student);
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new StudentForm.DeleteEvent(this, student)));
        cancel.addClickListener(event -> fireEvent(new StudentForm.CloseEvent(this)));

        save.addClickShortcut(Key.ENTER);
        cancel.addClickShortcut(Key.ESCAPE);

        return new HorizontalLayout(delete, cancel, save);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(student);
            fireEvent(new StudentForm.SaveEvent(this, student));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }


    public static abstract class StudentFormEvent extends ComponentEvent<StudentForm> {
        private Student student;

        protected StudentFormEvent(StudentForm source, Student student) {
            super(source, false);
            this.student =  student;
        }

        public Student getEntity() {
            return student;
        }
    }

    public static class SaveEvent extends StudentForm.StudentFormEvent {
        SaveEvent(StudentForm source, Student student) {
            super(source, student);
        }
    }

    public static class DeleteEvent extends StudentForm.StudentFormEvent {
        DeleteEvent(StudentForm source, Student student) {
            super(source, student);
        }

    }

    public static class CloseEvent extends StudentForm.StudentFormEvent {
        CloseEvent(StudentForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
