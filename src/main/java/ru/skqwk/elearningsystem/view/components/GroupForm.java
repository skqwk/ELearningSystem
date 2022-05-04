package ru.skqwk.elearningsystem.view.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.shared.Registration;
import ru.skqwk.elearningsystem.model.Group;
import ru.skqwk.elearningsystem.model.Department;
import ru.skqwk.elearningsystem.model.Student;

import java.util.List;

public class GroupForm extends FormLayout {
    Binder<Group> binder = new BeanValidationBinder<>(Group.class);

    TextField literal = new TextField("literal");
    TextField number = new TextField("number");
    Grid<Student> students = new Grid<>(Student.class, false);

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button cancel = new Button("Cancel");

    private Group group;

    public GroupForm(Group group) {
        this.group =  group;
        addClassName("Group-form");
        binder.forField(number)
                .withConverter(new StringToIntegerConverter(""))
                .bind(Group::getNumber, Group::setNumber);
        binder.bindInstanceFields(this);

        configureStudentsGrid();
        students.setItems(group.getStudents());

//        department.setItems(departments);
//        department.setItemLabelGenerator(Department::getName);

        add(
                literal,
                number,
                students,
                createButtonsLayout()
        );
    }

    private void configureStudentsGrid() {

        students.addClassName("teacher-grid");

        students.addColumn(student ->
                String.format("%s %s %s",
                student.getSurname(),
                student.getName(),
                student.getPatronymic()))
                .setHeader("ФИО");

        students.getColumns().forEach(col -> col.setAutoWidth(true));

    }

    public void setEntity(Group group) {
        this.group =  group;
        binder.readBean(group);
        if (group != null) {
            students.setItems(group.getStudents());
        }
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new GroupForm.DeleteEvent(this, group)));
        cancel.addClickListener(event -> fireEvent(new GroupForm.CloseEvent(this)));

        save.addClickShortcut(Key.ENTER);
        cancel.addClickShortcut(Key.ESCAPE);

        return new HorizontalLayout(delete, cancel, save);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(group);
            fireEvent(new GroupForm.SaveEvent(this, group));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }


    public static abstract class GroupFormEvent extends ComponentEvent<GroupForm> {
        private Group group;

        protected GroupFormEvent(GroupForm source, Group group) {
            super(source, false);
            this.group =  group;
        }

        public Group getEntity() {
            return group;
        }
    }

    public static class SaveEvent extends GroupForm.GroupFormEvent {
        SaveEvent(GroupForm source, Group group) {
            super(source, group);
        }


    }

    public static class DeleteEvent extends GroupForm.GroupFormEvent {
        DeleteEvent(GroupForm source, Group group) {
            super(source, group);
        }

    }

    public static class CloseEvent extends GroupForm.GroupFormEvent {
        CloseEvent(GroupForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
