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

import it.uniroma2.dicii.ispw.supportdesk.controller.applicativo.CorrelationController;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.exception.SupportDeskException;
import it.uniroma2.dicii.ispw.supportdesk.fx.SceneNavigator;
import it.uniroma2.dicii.ispw.supportdesk.record.TicketRecord;
import it.uniroma2.dicii.ispw.supportdesk.utility.facade.SlaFacade;
import it.uniroma2.dicii.ispw.supportdesk.utility.facade.ViewTicketsFacade;
import it.uniroma2.dicii.ispw.supportdesk.utility.singleton.UserSession;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.List;

public class ManagerDashboardControllerGrafico extends AbstractDashboardControllerGrafico {

    private static final String COL_TITLE = "title";
    private static final String COL_STATUS = "status";
    private static final String COL_SCADENZA_SLA = "scadenzaSla";
    private static final String ERR_TITLE = "Errore";

    private final CorrelationController correlationController = new CorrelationController();

    @FXML private Label welcomeLabel;

    // Tab 1 — Tutti i ticket
    @FXML private TableView<TicketRecord>       allTicketsTable;
    @FXML private TableColumn<TicketRecord, Integer> colId;
    @FXML private TableColumn<TicketRecord, String>  colTitle;
    @FXML private TableColumn<TicketRecord, String>  colCategory;
    @FXML private TableColumn<TicketRecord, String>  colPriority;
    @FXML private TableColumn<TicketRecord, String>  colStatus;
    @FXML private TableColumn<TicketRecord, String>  colTech;
    @FXML private TableColumn<TicketRecord, String>  colSla;

    // Tab 2 — SLA
    @FXML private TableView<TicketRecord>       slaTable;
    @FXML private TableColumn<TicketRecord, Integer> slaColId;
    @FXML private TableColumn<TicketRecord, String>  slaColTitle;
    @FXML private TableColumn<TicketRecord, String>  slaColPriority;
    @FXML private TableColumn<TicketRecord, String>  slaColStatus;
    @FXML private TableColumn<TicketRecord, String>  slaColSla;

    // Tab 3 — Correlati
    @FXML private TextField                     correlationIdField;
    @FXML private Label                         correlationErrorLabel;
    @FXML private TableView<TicketRecord>       correlatedTable;
    @FXML private TableColumn<TicketRecord, Integer> corrColId;
    @FXML private TableColumn<TicketRecord, String>  corrColTitle;
    @FXML private TableColumn<TicketRecord, String>  corrColCategory;
    @FXML private TableColumn<TicketRecord, String>  corrColStatus;
    @FXML private TableColumn<TicketRecord, String>  corrColSla;

    @FXML
    public void initialize() {
        welcomeLabel.setText("Benvenuto, " + SessionContext.getCurrentUser().name());
        bindAllTicketsTable();
        bindSlaTable();
        bindCorrelatedTable();

        allTicketsTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, o, n) -> { if (n != null) populateDetail(n); else hideDetail(); });
        slaTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, o, n) -> { if (n != null) populateDetail(n); else hideDetail(); });
        correlatedTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, o, n) -> { if (n != null) populateDetail(n); else hideDetail(); });

        loadAllTickets();
    }

    @FXML
    public void onRefreshAll() { loadAllTickets(); }

    @FXML
    public void onCheckSla() {
        try {
            List<TicketRecord> expiring = SlaFacade.getInstance().getTicketsWithSlaExpiringSoon();
            slaTable.setItems(FXCollections.observableArrayList(expiring));
        } catch (DAOException e) {
            log.error("Errore controllo SLA", e);
            showError(ERR_TITLE, "Errore interno del sistema.");
        }
    }

    @FXML
    public void onFindCorrelated() {
        correlationErrorLabel.setText("");
        String raw = correlationIdField.getText().trim();
        int ticketId;
        try {
            ticketId = Integer.parseInt(raw);
        } catch (NumberFormatException e) {
            correlationErrorLabel.setText("Inserisci un ID numerico valido.");
            return;
        }
        try {
            TicketRecord target = ViewTicketsFacade.getInstance().getAllTickets()
                    .stream().filter(t -> t.id() == ticketId).findFirst().orElse(null);
            if (target == null) {
                correlationErrorLabel.setText("Ticket ID " + ticketId + " non trovato.");
                return;
            }
            List<TicketRecord> correlated = correlationController.analyzeCorrelations(
                    new it.uniroma2.dicii.ispw.supportdesk.model.Ticket(
                            target.id(), target.title(), target.description(),
                            target.category(), target.priority()));
            correlatedTable.setItems(FXCollections.observableArrayList(correlated));
        } catch (DAOException e) {
            log.error("Errore ricerca ticket correlati", e);
            showError(ERR_TITLE, "Errore interno del sistema.");
        } catch (SupportDeskException e) {
            correlationErrorLabel.setText(e.getMessage());
        }
    }

    @FXML
    public void onLogout() throws IOException {
        UserSession.getInstance().logout();
        SessionContext.clear();
        SceneNavigator.navigateTo("login.fxml", "Login");
    }

    private void loadAllTickets() {
        try {
            allTicketsTable.setItems(FXCollections.observableArrayList(
                    ViewTicketsFacade.getInstance().getAllTickets()));
        } catch (DAOException e) {
            log.error("Errore caricamento tutti i ticket", e);
            showError(ERR_TITLE, "Impossibile caricare i ticket.");
        }
    }

    private void bindAllTicketsTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>(COL_TITLE));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colPriority.setCellValueFactory(new PropertyValueFactory<>("priority"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>(COL_STATUS));
        colTech.setCellValueFactory(new PropertyValueFactory<>("assignedTechnicianName"));
        colSla.setCellValueFactory(new PropertyValueFactory<>(COL_SCADENZA_SLA));
    }

    private void bindSlaTable() {
        slaColId.setCellValueFactory(new PropertyValueFactory<>("id"));
        slaColTitle.setCellValueFactory(new PropertyValueFactory<>(COL_TITLE));
        slaColPriority.setCellValueFactory(new PropertyValueFactory<>("priority"));
        slaColStatus.setCellValueFactory(new PropertyValueFactory<>(COL_STATUS));
        slaColSla.setCellValueFactory(new PropertyValueFactory<>(COL_SCADENZA_SLA));
    }

    private void bindCorrelatedTable() {
        corrColId.setCellValueFactory(new PropertyValueFactory<>("id"));
        corrColTitle.setCellValueFactory(new PropertyValueFactory<>(COL_TITLE));
        corrColCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        corrColStatus.setCellValueFactory(new PropertyValueFactory<>(COL_STATUS));
        corrColSla.setCellValueFactory(new PropertyValueFactory<>(COL_SCADENZA_SLA));
    }

    @FXML
    public void onCloseDetail() {
        hideDetail();
        allTicketsTable.getSelectionModel().clearSelection();
        slaTable.getSelectionModel().clearSelection();
        correlatedTable.getSelectionModel().clearSelection();
    }
}
