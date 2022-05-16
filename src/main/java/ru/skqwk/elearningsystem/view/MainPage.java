package ru.skqwk.elearningsystem.view;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;

@PageTitle("Main Page")
@Route(value="/", layout = MainLayout.class)
@PermitAll
public class MainPage extends VerticalLayout {
}
