package ru.skqwk.elearningsystem.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.function.SerializableBiConsumer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;
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
import ru.skqwk.elearningsystem.view.components.Dataset;
import ru.skqwk.elearningsystem.view.components.Row;

import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.skqwk.elearningsystem.model.enumeration.Status.*;

@PageTitle("Teacher Groups")
@Route(value="my-groups", layout = MainLayout.class)
@RolesAllowed("TEACHER")
@Slf4j
public class TeacherGroupsPage extends VerticalLayout {
    private final IELearningService service;

    private final Teacher teacher;

    private final VerticalLayout listGroups = new VerticalLayout();
    private final Grid<Row> table = new Grid<>();
    private final VerticalLayout tableForm = new VerticalLayout();
    private final Button addLesson = new Button("Добавить материал");
    private final Button close = new Button("Закрыть");
    private final Dialog addLessonDialog = new Dialog();
    private final Dialog homeworkDialog = new Dialog();
    private CourseTeacherGroup currentCTG;


    public TeacherGroupsPage(IELearningService service) {

        this.service = service;
        this.teacher = service.findTeacherByUserId(UserSingleton.getUser().getId());

        addClassName("list-view");
        setSizeFull();

        configureListGroups();
        configureTableForm();

        add(
                getContent()
        );
    }

    private void configureTableForm() {
        close.addClickListener(e -> {closeTableForm();});

        addLesson.addClickListener(e -> {
            renderNewDialog(currentCTG);
            System.out.println("CREATE NEW LESSON FOR " + currentCTG.getId());
        });
        tableForm.add(table);
        tableForm.add(addLesson);
        tableForm.add(close);
        tableForm.setVisible(false);
    }

    private void renderNewDialog(CourseTeacherGroup currentCTG) {
        addLessonDialog.removeAll();
        addLessonDialog.setSizeFull();
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

        TextField lessonField = new TextField("Название материала");
        lessonField.setSizeFull();

        TextArea contentField = new TextArea("Содержание материала");
        contentField.setSizeFull();

        ComboBox<MaterialType> type = new ComboBox<>("Тип материала");
        type.setItems(MaterialType.values());
        type.setItemLabelGenerator(MaterialType::getName);
        type.setSizeFull();

        Button saveLesson = new Button("Сохранить материал");
        saveLesson.addClickListener(e -> {
            service.saveMaterial(EducationalMaterial.builder()
                            .courseTeacherGroup(currentCTG)
                            .title(lessonField.getValue())
                            .content(contentField.getValue())
                            .type(type.getValue())
                    .build());

            closeTableForm();
            addLessonDialog.close();
        });
        layout.add(lessonField, type, contentField, saveLesson);

        addLessonDialog.open();
        addLessonDialog.add(layout);
    }

    private void configureListGroups() {
        List<CourseTeacherGroup> courseTeacherGroupList = service.findAllCourseTeacherGroupsByTeacher(teacher);


        Map<String, List<CourseTeacherGroup>> courseToGroups = new HashMap<>();
        log.info(courseTeacherGroupList.toString());
        for (CourseTeacherGroup courseTeacherGroup : courseTeacherGroupList) {
            Course course = courseTeacherGroup.getCourse();



            if (courseToGroups.containsKey(course.getName())) {
                courseToGroups.get(course.getName()).add(courseTeacherGroup);
            } else {
                courseToGroups.put(course.getName(),
                        new ArrayList<>(List.of(courseTeacherGroup)));
            }
        }

        for (Map.Entry<String, List<CourseTeacherGroup>> entry : courseToGroups.entrySet()) {
            VerticalLayout col = new VerticalLayout();
            List<CourseTeacherGroup> groups = entry.getValue();
            for (CourseTeacherGroup courseTeacherGroup : groups) {
                Group group = courseTeacherGroup.getGroup();
                HorizontalLayout row = new HorizontalLayout();
                Button buttonGroup = new Button(group.getNumber() + " " + group.getLiteral());
                buttonGroup.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
                buttonGroup.addClickListener(e -> {
                    log.info(group.getNumber() + " " + group.getLiteral());
                    fillTableForm(courseTeacherGroup);
                    openTableForm();
                });
                row.add(buttonGroup);

                col.add(row);
            }
            Accordion accordion = new Accordion();
            accordion.add(entry.getKey(), col);
            listGroups.add(accordion);
        }

    }

    private void fillTableForm(CourseTeacherGroup courseTeacherGroup) {
        this.currentCTG = courseTeacherGroup;
        fillTable(courseTeacherGroup);

    }

    private void fillTable(CourseTeacherGroup courseTeacherGroup) {
        table.removeAllColumns();
        table.setVisible(true);
        table.setSizeFull();

        // Формируем колонки
        List<String> cols = new ArrayList<>(List.of("Ученик"));

        List<EducationalMaterial> materials = service.findAllMaterialsByCourseTeacherGroup(courseTeacherGroup);


        cols.addAll(materials.stream()
                .map(EducationalMaterial::getTitle)
                .collect(Collectors.toList()));


        // Заполняем строку для каждого ученика
        List<Row> rows = new ArrayList<>();
        Group group = courseTeacherGroup.getGroup();
        for (Student student : group.getStudents()) {
            Row row = new Row();
            List<Object> values = new ArrayList<>();
            String fio = student.getSurname()
                    + " " +
                    student.getName()
                    + " " +
                    student.getPatronymic();
            values.add(fio);

//            List<String> statuses = service.getStudentStudyStatusesString(student, materials);

            List<StudyStatus> statuses = service.getStudentStudyStatuses(student, materials);

            values.addAll(statuses);

            for (int i = 0; i < values.size(); i++) {
                row.setValue(cols.get(i), values.get(i));
            }
            rows.add(row);
        }

        Dataset dataset = new Dataset(cols, rows);

        for (String column : dataset.getColumns()) {

            table.addComponentColumn(row ->

                    {
                        Object val = row.getValue(column);
                        if (val instanceof StudyStatus) {
                            StudyStatus studyStatus = (StudyStatus) val;
                            Status status = studyStatus.getStatus();

                            Span badge = new Span(status.getName());
                            badge.getElement().getThemeList().add(status.getStyle());

                            if (studyStatus.getContent() != null) {
                                badge.addClickListener(e -> {
                                    System.out.println("OPEN HOMEWORK");
                                    renderHomeworkDialog(studyStatus);
                                });
                            }
                            return badge;
                        }


                        return new Span(val.toString());
                    }


                ).setHeader(column);
        }
        table.setItems(dataset.getRows());

    }

    private void renderHomeworkDialog(StudyStatus studyStatus) {
        homeworkDialog.removeAll();

        VerticalLayout layout = new VerticalLayout();


        Student student = studyStatus.getStudent();
        TextField group = new TextField("Класс");
        group.setReadOnly(true);
        group.setValue(student.getGroup().getNumber() + " " + student.getGroup().getLiteral());
        group.setSizeFull();

        TextField studentField = new TextField("Ученик");
        studentField.setValue(
                student.getSurname()
                        + " " +
                        student.getName()
                        + " " +
                        student.getPatronymic());
        studentField.setReadOnly(true);
        studentField.setSizeFull();

        TextArea content = new TextArea("Выполненное задание");
        content.setReadOnly(true);
        content.setValue(studyStatus.getContent());
        content.setSizeFull();

        ComboBox<Status> mark = new ComboBox<>("Оценка");
        mark.setItems(List.of(EXCELLENT, GOOD, NOT_BAD));
        mark.setItemLabelGenerator(Status::getName);


        mark.addValueChangeListener(e -> {
            studyStatus.setStatus(mark.getValue());
            service.saveStudyStatus(studyStatus);
        });
        mark.setSizeFull();

        Button close = new Button("Закрыть");
        close.addClickListener(e -> {
            closeTableForm();
            homeworkDialog.close();
        });
        close.addThemeVariants(ButtonVariant.LUMO_ERROR);


        layout.add(group, studentField, content, mark, close);
        homeworkDialog.add(layout);
        homeworkDialog.setSizeFull();
        homeworkDialog.open();
    }

    private void openTableForm() {
        tableForm.setVisible(true);
    }

    private void closeTableForm() {
        tableForm.setVisible(false);
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(listGroups, tableForm);
        content.setFlexGrow(1, listGroups);
        content.setFlexGrow(3, tableForm);
        content.addClassName("content");
        content.setSizeFull();

        return content;
    }


}
