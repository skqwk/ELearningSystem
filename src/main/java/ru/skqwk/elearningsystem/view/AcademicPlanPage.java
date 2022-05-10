package ru.skqwk.elearningsystem.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.skqwk.elearningsystem.model.Course;
import ru.skqwk.elearningsystem.model.Department;
import ru.skqwk.elearningsystem.model.Group;
import ru.skqwk.elearningsystem.model.Teacher;
import ru.skqwk.elearningsystem.model.dto.CourseTeacherGroup;
import ru.skqwk.elearningsystem.services.IELearningService;
import ru.skqwk.elearningsystem.view.components.AcademicPlanForm;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import java.util.List;

@PageTitle("Academic Plan")
@Route(value="academic-plan", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class AcademicPlanPage extends VerticalLayout {
    private final IELearningService service;

    Grid<Group> grid = new Grid<>(Group.class, false);
    Dialog editAcademicPlanForm = new Dialog();

    public AcademicPlanPage(IELearningService service) {
        this.service = service;

        addClassName("list-view");
        setSizeFull();

        configureGrid();
        grid.setItems(service.findAllGroups());
//        configureForm();
//
        add(
//                getToolbar(),
                getContent()
        );
//
        updateList();
    }

    private void updateList() {
        grid.setItems(service.findAllGroups());
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid
//                ,academicPlanForm
        );
        content.setFlexGrow(2, grid);
//        content.setFlexGrow(1, academicPlanForm);
        content.addClassName("content");
        content.setSizeFull();

        return content;
    }

    private void configureGrid() {
        grid.addClassName("group-grid");
        grid.setSizeFull();

        grid.addColumn(group -> String.
                        format("%s%s", group.getNumber(), group.getLiteral()))
                .setHeader("Название");

        grid.addColumn(group -> group.getStudents().size())
                .setHeader("Количество студентов");

        grid.addColumn((group) -> group.getCourseTeacherGroups().size()).setHeader("Количество предметов");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(e -> editAcademicPlan(e.getValue()));
    }

    private void editAcademicPlan(Group group) {
        if (group != null) {
            editAcademicPlanForm.removeAll();

            List<CourseTeacherGroup> courseTeacherGroupList = group.getCourseTeacherGroups();

            VerticalLayout academicPlanList = new VerticalLayout();

            Grid<CourseTeacherGroup> courseTeacherGroupGrid = new Grid<>(CourseTeacherGroup.class, false);
            courseTeacherGroupGrid.setItems(courseTeacherGroupList);
            courseTeacherGroupGrid.addColumn(c -> {
                Course course = c.getCourse();
                return course != null
                        ? course.getName()
                        : "";
            }).setHeader("Курс");

            courseTeacherGroupGrid.addColumn(c -> {
                Teacher teacher = c.getTeacher();
                return teacher != null
                        ? teacher.getSurname() + " " + teacher.getName() + " " + teacher.getPatronymic()
                        : "";
            }).setHeader("Преподаватель");

            for (CourseTeacherGroup courseTeacherGroup : courseTeacherGroupList) {
                HorizontalLayout academicPlanRow = new HorizontalLayout();
                academicPlanRow.setAlignItems(Alignment.BASELINE);

                TextField courseField = new TextField("Курс");
                courseField.setValue(courseTeacherGroup.getCourse().getName());
                courseField.setReadOnly(true);

                TextField teacherField = new TextField("Учитель");
                Teacher teacher = courseTeacherGroup.getTeacher();
                String fieldName = teacher.getSurname() + " " + teacher.getName() + " " + teacher.getPatronymic();
                teacherField.setValue(fieldName);
                teacherField.setReadOnly(true);

                Button deleteCourseTeacherGroup = new Button("Delete");
                deleteCourseTeacherGroup.addThemeVariants(ButtonVariant.LUMO_ERROR);
                deleteCourseTeacherGroup.addClickListener(e -> {
                    System.out.println(courseTeacherGroup.getId());
                    service.deleteCourseTeacherGroup(courseTeacherGroup);
                    academicPlanList.remove(academicPlanRow);
                });
                academicPlanRow.add(courseField, teacherField, deleteCourseTeacherGroup);
                academicPlanList.add(academicPlanRow);
            }
            editAcademicPlanForm.add(academicPlanList);
            Button add = new Button("Add");
            add.addClickListener(e -> addAcademicPlanRow(academicPlanList, group));
            editAcademicPlanForm.add(add);
            editAcademicPlanForm.addDialogCloseActionListener(e -> {
                editAcademicPlanForm.close();
                updateList();
            });
            editAcademicPlanForm.open();
        }

    }

    private void addAcademicPlanRow(VerticalLayout academicPlanList, Group group) {
        HorizontalLayout academicPlanRow = new HorizontalLayout();
        academicPlanRow.setAlignItems(Alignment.BASELINE);
            ComboBox<Course> courseComboBox = new ComboBox<>();
            ComboBox<Teacher> teacherComboBox = new ComboBox<>();

            List<Course> availableCourses = service.findAllCoursesWithoutGroup(group);
            courseComboBox.setItems(availableCourses);
            courseComboBox.setItemLabelGenerator(Course::getName);

            teacherComboBox.setItemLabelGenerator(t -> t.getSurname() + " " + t.getName() + " " + t.getPatronymic());
            courseComboBox.addValueChangeListener(e -> {
                teacherComboBox.setEnabled(true);
                Department department = e.getValue().getDepartment();
                teacherComboBox.setItems(service.findAllTeachersByDepartment(department));
            });

            courseComboBox.setRequired(true);

            teacherComboBox.setEnabled(false);


        Button saveCourseTeacherGroup = new Button("Save");
        saveCourseTeacherGroup.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveCourseTeacherGroup.addClickListener(e -> {
            CourseTeacherGroup courseTeacherGroup = service.saveCourseTeacherGroup(CourseTeacherGroup.builder()
                            .group(group)
                            .teacher(teacherComboBox.getValue())
                            .course(courseComboBox.getValue())
                    .build());
            academicPlanRow.removeAll();
            TextField courseField = new TextField("Курс");
            courseField.setValue(courseComboBox.getValue().getName());
            courseField.setReadOnly(true);

            TextField teacherField = new TextField("Учитель");
            Teacher teacher = teacherComboBox.getValue();
            String fieldName = teacher.getSurname() +  " " + teacher.getName() + " " + teacher.getPatronymic();
            teacherField.setValue(fieldName);
            teacherField.setReadOnly(true);
            Button deleteCourseTeacherGroup = new Button("Delete");
            deleteCourseTeacherGroup.addThemeVariants(ButtonVariant.LUMO_ERROR);
            deleteCourseTeacherGroup.addClickListener(deleteEvent -> {
                System.out.println(courseTeacherGroup.getId());
                service.deleteCourseTeacherGroup(courseTeacherGroup);
                academicPlanList.remove(academicPlanRow);
            });
            academicPlanRow.add(courseField, teacherField, deleteCourseTeacherGroup);
        });

        saveCourseTeacherGroup.setEnabled(false);
        teacherComboBox.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                saveCourseTeacherGroup.setEnabled(true);
            }
        });
        academicPlanRow.add(courseComboBox, teacherComboBox,  saveCourseTeacherGroup);
        academicPlanList.add(academicPlanRow);


    }
}
