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
package it.uniroma2.dicii.ispw.supportdesk.boundary.javafx;

import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.fx.SceneNavigator;
import it.uniroma2.dicii.ispw.supportdesk.record.LoginRecord;
import it.uniroma2.dicii.ispw.supportdesk.record.TicketRecord;
import it.uniroma2.dicii.ispw.supportdesk.utility.facade.ViewTicketsFacade;
import it.uniroma2.dicii.ispw.supportdesk.utility.singleton.UserSession;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class UserDashboardControllerGrafico {

    private static final Logger log = LoggerFactory.getLogger(UserDashboardControllerGrafico.class);

    @FXML private Label     welcomeLabel;

    @FXML private TableView<TicketRecord>          ticketTable;
    @FXML private TableColumn<TicketRecord, Integer>    colId;
    @FXML private TableColumn<TicketRecord, String>     colTitle;
    @FXML private TableColumn<TicketRecord, String>     colCategory;
    @FXML private TableColumn<TicketRecord, String>     colPriority;
    @FXML private TableColumn<TicketRecord, String>     colStatus;
    @FXML private TableColumn<TicketRecord, String>     colSla;

    @FXML
    public void initialize() {
        LoginRecord user = SessionContext.getCurrentUser();
        welcomeLabel.setText("Benvenuto, " + user.name() + " " + user.surname());

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colPriority.setCellValueFactory(new PropertyValueFactory<>("priority"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colSla.setCellValueFactory(new PropertyValueFactory<>("scadenzaSla"));

        loadMyTickets();
    }

    @FXML
    public void onOpenTicket() throws IOException {
        SceneNavigator.navigateTo("open-ticket.fxml", "Apri Ticket");
    }

    @FXML
    public void onRefresh() {
        loadMyTickets();
    }

    @FXML
    public void onLogout() throws IOException {
        UserSession.getInstance().logout();
        SessionContext.clear();
        SceneNavigator.navigateTo("login.fxml", "Login");
    }

    private void loadMyTickets() {
        try {
            List<TicketRecord> tickets = ViewTicketsFacade.getInstance()
                    .getTicketsByUser(SessionContext.getCurrentUser().email());
            ticketTable.setItems(FXCollections.observableArrayList(tickets));
        } catch (DAOException e) {
            log.error("Errore caricamento ticket", e);
            showError("Errore", "Impossibile caricare i ticket.");
        }
    }

    private void showError(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }

}
