package ru.skqwk.elearningsystem.config;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;

@Theme(value="elearningsystem")
@PWA(name = "ELearning System", shortName = "ELS", offlineResources = {"images/logo.png"})
public class Config implements AppShellConfigurator {
}
