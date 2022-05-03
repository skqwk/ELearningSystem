package ru.skqwk.elearningsystem.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.skqwk.elearningsystem.dao.StudentDao;
import ru.skqwk.elearningsystem.model.Student;

import javax.annotation.security.PermitAll;

@PageTitle("Teachers")
@Route(value="students", layout = MainLayout.class)
@PermitAll
public class StudentsPage extends VerticalLayout {

    private final StudentDao repo;
    final Grid<Student> grid;

    public StudentsPage(StudentDao repo) {
        add(new H1("Hello world for anybody!"));
        Button button = new Button("Click me!");
        TextField name = new TextField("Name");

        HorizontalLayout hl = new HorizontalLayout(name, button);
        hl.setDefaultVerticalComponentAlignment(Alignment.BASELINE);

        button.addClickListener(click -> Notification.show("Hello, " + name.getValue()));

        add(hl);
        this.repo = repo;
        this.grid = new Grid<>(Student.class);
        add(grid);
        listCustomers();
    }

    private void listCustomers() {
        grid.setItems(repo.findAll());
    }

}
