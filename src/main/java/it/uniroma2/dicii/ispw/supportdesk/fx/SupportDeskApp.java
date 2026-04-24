/*
 * SupportDesk — ISPW Project
 * Copyright (C) 2026  Alexandro Daniliuc
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */
package it.uniroma2.dicii.ispw.supportdesk.fx;

import it.uniroma2.dicii.ispw.supportdesk.controller.applicativo.TicketController;
import it.uniroma2.dicii.ispw.supportdesk.dao.PersistenceLayer;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.ApplicationMode;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.TicketStatus;
import it.uniroma2.dicii.ispw.supportdesk.utility.observer.ManagerNotificationObserver;
import it.uniroma2.dicii.ispw.supportdesk.utility.singleton.ApplicationModeManager;
import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SupportDeskApp extends Application {

    private static final Logger log = LoggerFactory.getLogger(SupportDeskApp.class);

    @Override
    public void start(Stage stage) throws IOException {
        SceneNavigator.init(stage);
        if (ApplicationModeManager.getInstance().getMode() != ApplicationMode.DEMO) {
            rescheduleSlaTimers();
        }
        SceneNavigator.navigateTo("login.fxml", "SupportDesk — Login");
    }

    private void rescheduleSlaTimers() {
        try {
            TicketController tc = new TicketController();
            tc.attach(new ManagerNotificationObserver());
            PersistenceLayer.getInstance().findAllTickets().stream()
                    .filter(t -> t.getStatus() != TicketStatus.RESOLVED && t.getStatus() != TicketStatus.CLOSED)
                    .forEach(tc::schedulaSlaTimer);
        } catch (Exception e) {
            log.warn("Impossibile ricaricare timer SLA al boot: {}", e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
