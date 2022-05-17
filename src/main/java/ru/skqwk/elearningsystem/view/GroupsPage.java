package ru.skqwk.elearningsystem.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.dialog.Dialog;
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
import ru.skqwk.elearningsystem.view.components.GroupForm;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;

@PageTitle("Классы")
@Route(value="groups", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class GroupsPage extends VerticalLayout {
    private final IELearningService service;

    Grid<Group> grid = new Grid<>(Group.class, false);
    TextField filterText = new TextField();
    GroupForm groupForm;
    Dialog addStudents = new Dialog();


    public GroupsPage(IELearningService service) {
        this.service = service;

        addClassName("list-view");
        setSizeFull();

        configureGrid();
        configureForm();
//        configureTreeGrid();

        add(
                getToolbar(),
                getContent()
        );

        updateList();
        closeEditor();
    }



    private void updateList() {
        grid.setItems(service.findAllGroups());
    }

    private void configureGrid() {
        grid.addClassName("group-grid");
        grid.setSizeFull();

        grid.addColumn(group -> String.
                format("%s %s", group.getNumber(), group.getLiteral()))
                .setHeader("Название");

        grid.addColumn(group -> group.getStudents().size())
                .setHeader("Количество студентов");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(e -> editGroup(e.getValue()));
    }

    private Component getToolbar() {

        Button addNewGroup = new Button("Добавить класс");
        addNewGroup.addClickListener(e -> addGroup());
        HorizontalLayout toolbar = new HorizontalLayout(addNewGroup);
        toolbar.addClassName("toolbar");

        return toolbar;
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, groupForm);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, groupForm);
        content.addClassName("content");
        content.setSizeFull();

        return content;
    }

    private void configureForm() {
        groupForm = new GroupForm(new Group());
        groupForm.setWidth("50em");

        groupForm.addListener(GroupForm.SaveEvent.class, this::saveGroup);
        groupForm.addListener(GroupForm.DeleteEvent.class, this::deleteGroup);
        groupForm.addListener(GroupForm.CloseEvent.class,  e -> closeEditor());
        groupForm.addListener(GroupForm.AddEvent.class, this::showStudents);
    }

    private void showStudents(GroupForm.AddEvent event) {
        VerticalLayout dialogLayout = new VerticalLayout();
        addStudents.removeAll();
        Grid<Student> studentGrid = new Grid<>(Student.class, false);
        studentGrid.setColumns("name", "surname", "patronymic");
        studentGrid.setSizeFull();
        studentGrid.getColumns().forEach(col -> col.setAutoWidth(true));

        CheckboxGroup<Student> listBox = new CheckboxGroup<>();
        listBox.setLabel("Ученики без класса");
        listBox.setItemLabelGenerator(student -> student.getSurname() + " " + student.getName() + " " + student.getPatronymic());

        listBox.setItems(service.findAllStudentsWithoutGroup());
        listBox.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
        dialogLayout.add(listBox);

        Button add = new Button("Добавить");
        add.addClickListener(e -> {
            Group group = event.getEntity();
            for (Student student : listBox.getSelectedItems()) {
                student.setGroup(group);
                service.saveStudent(student);
            }
            updateList();
            addStudents.close();});
        dialogLayout.add(add);
        addStudents.add(dialogLayout);
        addStudents.open();
    }

    private void closeEditor() {
        groupForm.setEntity(null);
        groupForm.setVisible(false);
        removeClassName("editing");
    }


    private void saveGroup(GroupForm.SaveEvent event) {
        service.saveGroup(event.getEntity());
        updateList();
        closeEditor();
    }

    private void deleteGroup(GroupForm.DeleteEvent event) {

        service.deleteGroup(event.getEntity());
        updateList();
        closeEditor();
    }

    private void addGroup() {
        grid.asSingleSelect().clear();
        editGroup(new Group());
    }

    private void editGroup(Group group) {
        if (group == null) {
            closeEditor();
        } else {
            groupForm.setEntity(group);
            groupForm.setVisible(true);
            addClassName("editing");
        }
    }
}
