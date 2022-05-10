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
import com.vaadin.flow.shared.Registration;
import ru.skqwk.elearningsystem.model.Course;
import ru.skqwk.elearningsystem.model.Teacher;
import ru.skqwk.elearningsystem.model.dto.CourseTeacherGroup;

public class AcademicPlanForm extends FormLayout {
    Binder<CourseTeacherGroup> binder = new BeanValidationBinder<>(CourseTeacherGroup.class);

    TextField literal = new TextField("literal");
    TextField number = new TextField("number");
    Grid<CourseTeacherGroup> courseTeacherGroupGrid = new Grid<>(CourseTeacherGroup.class, false);

    ComboBox<Teacher> teachers = new ComboBox<Teacher>();
    ComboBox<Course> courses = new ComboBox<Course>();
    
    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button cancel = new Button("Cancel");
    Button add = new Button("Add");

    private CourseTeacherGroup courseTeacherGroup;

    public AcademicPlanForm(CourseTeacherGroup courseTeacherGroup) {
        this.courseTeacherGroup =  courseTeacherGroup;
        addClassName("CourseTeacherGroup-form");
//        binder.forField(number)
//                .withConverter(new StringToIntegerConverter(""))
//                .bind(CourseTeacherGroup::getNumber, CourseTeacherGroup::setNumber);
        binder.bindInstanceFields(this);



        add(
                literal,
                number,
                createButtonsLayout()
        );
    }

    public void setEntity(CourseTeacherGroup courseTeacherGroup) {
        this.courseTeacherGroup =  courseTeacherGroup;
        binder.readBean(courseTeacherGroup);
//        if (courseTeacherGroup != null) {
//            students.setItems(courseTeacherGroup.getStudents());
//        }
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        add.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new AcademicPlanForm.DeleteEvent(this, courseTeacherGroup)));
        cancel.addClickListener(event -> fireEvent(new AcademicPlanForm.CloseEvent(this)));
        add.addClickListener(event -> fireEvent(new AcademicPlanForm.AddEvent(this, courseTeacherGroup)));

        save.addClickShortcut(Key.ENTER);
        cancel.addClickShortcut(Key.ESCAPE);

        return new HorizontalLayout(delete, cancel, save, add);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(courseTeacherGroup);
            fireEvent(new AcademicPlanForm.SaveEvent(this, courseTeacherGroup));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }


    public static abstract class AcademicPlanFormEvent extends ComponentEvent<AcademicPlanForm> {
        private CourseTeacherGroup courseTeacherGroup;

        protected AcademicPlanFormEvent(AcademicPlanForm source, CourseTeacherGroup courseTeacherGroup) {
            super(source, false);
            this.courseTeacherGroup =  courseTeacherGroup;
        }

        public CourseTeacherGroup getEntity() {
            return courseTeacherGroup;
        }
    }

    public static class SaveEvent extends AcademicPlanForm.AcademicPlanFormEvent {
        SaveEvent(AcademicPlanForm source, CourseTeacherGroup courseTeacherGroup) {
            super(source, courseTeacherGroup);
        }


    }

    public static class DeleteEvent extends AcademicPlanForm.AcademicPlanFormEvent {
        DeleteEvent(AcademicPlanForm source, CourseTeacherGroup courseTeacherGroup) {
            super(source, courseTeacherGroup);
        }

    }
    public static class AddEvent extends AcademicPlanForm.AcademicPlanFormEvent {
        AddEvent(AcademicPlanForm source, CourseTeacherGroup courseTeacherGroup) {
            super(source, courseTeacherGroup);
        }

    }

    public static class CloseEvent extends AcademicPlanForm.AcademicPlanFormEvent {
        CloseEvent(AcademicPlanForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
