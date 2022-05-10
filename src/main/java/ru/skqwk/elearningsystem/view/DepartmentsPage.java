package ru.skqwk.elearningsystem.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.skqwk.elearningsystem.model.Department;
import ru.skqwk.elearningsystem.services.IELearningService;
import ru.skqwk.elearningsystem.view.components.DepartmentForm;

import javax.annotation.security.RolesAllowed;

@PageTitle("Departments")
@Route(value="departments", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class DepartmentsPage extends VerticalLayout {
    private final IELearningService service;

    Grid<Department> grid = new Grid<>(Department.class, false);
    TextField filterText = new TextField();
    DepartmentForm departmentForm;

    public DepartmentsPage(IELearningService service) {
        this.service = service;

        addClassName("list-view");
        setSizeFull();

        configureGrid();
        configureForm();

        add(
                getToolbar(),
                getContent()
        );

        updateList();
        closeEditor();
    }



    private void updateList() {
        grid.setItems(service.findAllDepartments());
    }

    private void configureGrid() {
        grid.addClassName("department-grid");
        grid.setSizeFull();

        grid.addColumn(Department::getName).setHeader("Название кафедры");
        grid.addColumn(department -> department.getTeachers().size())
                .setHeader("Количество преподавателей");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(e -> editDepartment(e.getValue()));
    }

    private Component getToolbar() {
        filterText.setPlaceholder("Filter by name");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addNewDepartment = new Button("Add new department");
        addNewDepartment.addClickListener(e -> addDepartment());
        HorizontalLayout toolbar = new HorizontalLayout(filterText, addNewDepartment);
        toolbar.addClassName("toolbar");

        return toolbar;
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, departmentForm);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, departmentForm);
        content.addClassName("content");
        content.setSizeFull();

        return content;
    }

    private void configureForm() {
        departmentForm = new DepartmentForm(new Department());
        departmentForm.setWidth("50em");

        departmentForm.addListener(DepartmentForm.SaveEvent.class, this::saveDepartment);
        departmentForm.addListener(DepartmentForm.DeleteEvent.class, this::deleteDepartment);
        departmentForm.addListener(DepartmentForm.CloseEvent.class,  e -> closeEditor());
    }

    private void closeEditor() {
        departmentForm.setEntity(null);
        departmentForm.setVisible(false);
        removeClassName("editing");
    }


    private void saveDepartment(DepartmentForm.SaveEvent event) {
        Department department = event.getEntity();
        System.out.println(department);
        service.saveDepartment(department);
        updateList();
        closeEditor();
    }

    private void deleteDepartment(DepartmentForm.DeleteEvent event) {
        service.deleteDepartment(event.getEntity());
        updateList();
        closeEditor();
    }

    private void addDepartment() {
        grid.asSingleSelect().clear();
        editDepartment(new Department());
    }

    private void editDepartment(Department department) {
        if (department == null) {
            closeEditor();
        } else {
            departmentForm.setEntity(department);
            departmentForm.setVisible(true);
            addClassName("editing");
        }
    }
}
