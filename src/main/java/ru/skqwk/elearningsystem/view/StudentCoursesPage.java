package ru.skqwk.elearningsystem.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.skqwk.elearningsystem.model.Course;
import ru.skqwk.elearningsystem.model.EducationalMaterial;
import ru.skqwk.elearningsystem.model.Group;
import ru.skqwk.elearningsystem.model.Student;
import ru.skqwk.elearningsystem.model.StudyStatus;
import ru.skqwk.elearningsystem.model.Teacher;
import ru.skqwk.elearningsystem.model.dto.CourseTeacherGroup;
import ru.skqwk.elearningsystem.model.enumeration.MaterialType;
import ru.skqwk.elearningsystem.model.enumeration.Status;
import ru.skqwk.elearningsystem.security.UserSingleton;
import ru.skqwk.elearningsystem.services.IELearningService;
import ru.skqwk.elearningsystem.view.components.Row;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@PageTitle("Уроки")
@Route(value="my-courses", layout = MainLayout.class)
@RolesAllowed("STUDENT")
public class StudentCoursesPage extends VerticalLayout {
    private final IELearningService service;

    private final Student student;

    private final VerticalLayout listCourses = new VerticalLayout();
    private final Grid<Row> table = new Grid<>();
    private final VerticalLayout materialForm = new VerticalLayout();
    private final Button close = new Button("Закрыть");
    private final Dialog addSolutionDialog = new Dialog();
//    private CourseTeacherGroup currentCTG;


    public StudentCoursesPage(IELearningService service) {

        this.service = service;
        this.student = service.findStudentByUserId(UserSingleton.getUser().getId());

        addClassName("list-view");
        setSizeFull();

        configureListCourses();

        add(
                getContent()
        );
    }

    private void configureListCourses() {
        Group group = student.getGroup();

        List<CourseTeacherGroup> courseTeacherGroupList = service.findAllCourseTeacherGroupsByGroup(group);

        for (CourseTeacherGroup ctg : courseTeacherGroupList) {
            Course course = ctg.getCourse();

            VerticalLayout lessons = new VerticalLayout();
            for (EducationalMaterial material : service.findAllMaterialsByCourseTeacherGroup(ctg)) {
                Button viewLesson = new Button(material.getTitle());
                viewLesson.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
                viewLesson.addClickListener(e -> {
                    openMaterial(material);
                });
                lessons.add(viewLesson);
            }

            Accordion accordion = new Accordion();
            accordion.add(course.getName(), lessons);
            listCourses.add(accordion);
        }



    }

    private void openMaterial(EducationalMaterial material) {
        materialForm.removeAll();

        materialForm.add(new H3(material.getTitle()));
        TextArea content = new TextArea("Содержание");
        content.setValue(material.getContent());
        content.setReadOnly(true);
        content.setSizeFull();
        materialForm.add(content);

        Status status = service.getStudentMaterialStatus(student, material);

        if (material.getType().equals(MaterialType.LESSON)) {
            Checkbox setLearned = new Checkbox();
            setLearned.setLabel("Отметить изученным");

            setLearned.setValue(status.equals(Status.LEARNED));

            setLearned.addClickListener(e -> {
                service.saveStudyStatus(StudyStatus.builder()
                                .status(Status.LEARNED)
                                .material(material)
                                .student(student)
                        .build());
            });

            materialForm.add(setLearned);
        } else {
            Button addSolution = new Button("Прикрепить решение");
            addSolution.addClickListener(e -> {
                renderNewDialog(material);
            });
            materialForm.add(addSolution);
        }

        Button close = new Button("Закрыть");
        close.addClickListener(e -> {
            closeMaterialForm();
        });

        materialForm.add(close);
        materialForm.setVisible(true);

    }



    private void renderNewDialog( EducationalMaterial material) {
        CourseTeacherGroup currentCTG = material.getCourseTeacherGroup();

        addSolutionDialog.removeAll();
        addSolutionDialog.setSizeFull();
        VerticalLayout layout = new VerticalLayout();

        TextField courseField = new TextField("Курс");
        courseField.setReadOnly(true);
        courseField.setValue(currentCTG.getCourse().getName());
        courseField.setSizeFull();

        TextField teacherField = new TextField("Учитель");
        teacherField.setReadOnly(true);
        Teacher teacher = currentCTG.getTeacher();
        teacherField.setValue(
                teacher.getSurname() + " " +
                        teacher.getName() + " " +
                        teacher.getPatronymic());
        teacherField.setSizeFull();

        TextField groupField = new TextField("Класс");
        groupField.setReadOnly(true);
        Group group =  currentCTG.getGroup();
        groupField.setValue(group.getNumber() + " " + group.getLiteral());
        groupField.setSizeFull();

        layout.add(teacherField, courseField, groupField);
        layout.setSizeFull();

        TextArea contentField = new TextArea("Содержание домашнего задания");
        contentField.setSizeFull();



        Button saveLesson = new Button("Сохранить домашнеее задание");
        saveLesson.addClickListener(e -> {
            service.saveStudyStatus(StudyStatus.builder()
                            .material(material)
                            .student(student)
                            .content(contentField.getValue())
                            .status(Status.COMPLETED)
                    .build());

            closeMaterialForm();
            addSolutionDialog.close();
        });
        layout.add(contentField, saveLesson);

        addSolutionDialog.open();
        addSolutionDialog.add(layout);
    }

    private void openMaterialForm() {
        materialForm.setVisible(true);
    }

    private void closeMaterialForm() {
        materialForm.setVisible(false);
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(listCourses, materialForm);
        content.setFlexGrow(1, listCourses);
        content.setFlexGrow(3, materialForm);
        content.addClassName("content");
        content.setSizeFull();

        return content;
    }

}
