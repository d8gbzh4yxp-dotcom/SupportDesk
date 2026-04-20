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

import it.uniroma2.dicii.ispw.supportdesk.controller.applicativo.KnowledgeBaseController;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.TicketStatus;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.exception.KnowledgeBaseException;
import it.uniroma2.dicii.ispw.supportdesk.exception.SupportDeskException;
import it.uniroma2.dicii.ispw.supportdesk.fx.SceneNavigator;
import it.uniroma2.dicii.ispw.supportdesk.record.KnowledgeEntryRecord;
import it.uniroma2.dicii.ispw.supportdesk.record.TicketRecord;
import it.uniroma2.dicii.ispw.supportdesk.utility.facade.ViewTicketsFacade;
import it.uniroma2.dicii.ispw.supportdesk.utility.singleton.UserSession;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.List;

public class TechDashboardControllerGrafico extends AbstractDashboardControllerGrafico {

    private final KnowledgeBaseController kbController = new KnowledgeBaseController();

    @FXML private Label welcomeLabel;
    @FXML private Label actionErrorLabel;

    @FXML private TableView<TicketRecord>               ticketTable;
    @FXML private TableColumn<TicketRecord, Integer>    colId;
    @FXML private TableColumn<TicketRecord, String>     colTitle;
    @FXML private TableColumn<TicketRecord, String>     colCategory;
    @FXML private TableColumn<TicketRecord, String>     colPriority;
    @FXML private TableColumn<TicketRecord, String>     colStatus;
    @FXML private TableColumn<TicketRecord, String>     colSla;

    @FXML private TextField         kbSearchField;
    @FXML private ListView<String>  kbResultsList;

    @FXML
    public void initialize() {
        welcomeLabel.setText("Benvenuto, " + SessionContext.getCurrentUser().name());

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colPriority.setCellValueFactory(new PropertyValueFactory<>("priority"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colSla.setCellValueFactory(new PropertyValueFactory<>("scadenzaSla"));

        ticketTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    if (newVal != null) populateDetail(newVal);
                    else hideDetail();
                });

        loadAssignedTickets();
    }

    @FXML
    public void onRefresh() {
        loadAssignedTickets();
    }

    @FXML
    public void onTakeCharge() {
        TicketRecord selected = ticketTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            actionErrorLabel.setText("Seleziona un ticket dalla lista.");
            return;
        }
        changeStatus(selected.id(), TicketStatus.IN_PROGRESS);
    }

    @FXML
    public void onResolve() {
        TicketRecord selected = ticketTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            actionErrorLabel.setText("Seleziona un ticket dalla lista.");
            return;
        }
        changeStatus(selected.id(), TicketStatus.RESOLVED);
    }

    @FXML
    public void onKbSearch() {
        String keyword = kbSearchField.getText().trim();
        if (keyword.isBlank()) {
            kbResultsList.setItems(FXCollections.emptyObservableList());
            return;
        }
        try {
            List<KnowledgeEntryRecord> results = kbController.searchEntries(keyword);
            List<String> display = results.stream()
                    .map(r -> "[" + r.authorName() + "] " + r.title() + " — " + r.content())
                    .toList();
            kbResultsList.setItems(FXCollections.observableArrayList(display));
        } catch (KnowledgeBaseException e) {
            showError("Knowledge Base", e.getMessage());
        }
    }

    @FXML
    public void onLogout() throws IOException {
        UserSession.getInstance().logout();
        SessionContext.clear();
        SceneNavigator.navigateTo("login.fxml", "Login");
    }

    private void loadAssignedTickets() {
        actionErrorLabel.setText("");
        try {
            List<TicketRecord> tickets = ViewTicketsFacade.getInstance()
                    .getTicketsByUser(SessionContext.getCurrentUser().email());
            ticketTable.setItems(FXCollections.observableArrayList(tickets));
        } catch (DAOException e) {
            log.error("Errore caricamento ticket tecnico", e);
            showError("Errore", "Impossibile caricare i ticket assegnati.");
        }
    }

    private void changeStatus(int ticketId, TicketStatus newStatus) {
        actionErrorLabel.setText("");
        try {
            ViewTicketsFacade.getInstance().changeStatus(ticketId, newStatus);
            loadAssignedTickets();
        } catch (DAOException e) {
            log.error("Errore cambio stato ticket {}", ticketId, e);
            showError("Errore", "Errore interno del sistema.");
        } catch (SupportDeskException e) {
            actionErrorLabel.setText(e.getMessage());
        }
    }

    @FXML
    public void onCloseDetail() {
        hideDetail();
        ticketTable.getSelectionModel().clearSelection();
    }
}
