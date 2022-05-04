package ru.skqwk.elearningsystem.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.skqwk.elearningsystem.model.Group;
import ru.skqwk.elearningsystem.services.IELearningService;
import ru.skqwk.elearningsystem.view.components.GroupForm;

import javax.annotation.security.PermitAll;

@PageTitle("Groups")
@Route(value="groups", layout = MainLayout.class)
@PermitAll
public class GroupsPage extends VerticalLayout {
    private final IELearningService service;

    Grid<Group> grid = new Grid<>(Group.class, false);
//    TreeGrid<Group> treeGrid = new TreeGrid<>();
    TextField filterText = new TextField();
    GroupForm groupForm;

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
                format("%s%s", group.getNumber(), group.getLiteral()))
                .setHeader("Название");

        grid.addColumn(group -> group.getStudents().size())
                .setHeader("Количество студентов");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(e -> editGroup(e.getValue()));
    }

    private Component getToolbar() {
        filterText.setPlaceholder("Filter by name");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addNewGroup = new Button("Add new group");
        addNewGroup.addClickListener(e -> addGroup());
        HorizontalLayout toolbar = new HorizontalLayout(filterText, addNewGroup);
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
