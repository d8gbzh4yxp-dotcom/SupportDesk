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

import it.uniroma2.dicii.ispw.supportdesk.bean.CommentBean;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.TicketStatus;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.exception.SupportDeskException;
import it.uniroma2.dicii.ispw.supportdesk.fx.SceneNavigator;
import it.uniroma2.dicii.ispw.supportdesk.record.LoginRecord;
import it.uniroma2.dicii.ispw.supportdesk.record.TicketRecord;
import it.uniroma2.dicii.ispw.supportdesk.utility.facade.ViewTicketsFacade;
import it.uniroma2.dicii.ispw.supportdesk.utility.singleton.UserSession;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.List;

public class UserDashboardControllerGrafico extends AbstractDashboardControllerGrafico {

    @FXML private Label    welcomeLabel;
    @FXML private Label    actionErrorLabel;
    @FXML private Button   btnRiapri;
    @FXML private TextArea commentField;

    @FXML private TableView<TicketRecord>               ticketTable;
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

        ticketTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    if (newVal != null) populateDetail(newVal);
                    else hideDetail();
                });

        loadMyTickets();
    }

    @Override
    protected void populateDetail(TicketRecord t) {
        super.populateDetail(t);
        actionErrorLabel.setText("");
        boolean isResolved = t.status() == TicketStatus.RESOLVED;
        btnRiapri.setVisible(isResolved);
        btnRiapri.setManaged(isResolved);
        if (commentField != null) commentField.clear();
    }

    @FXML
    public void onAddComment() {
        TicketRecord selected = ticketTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            actionErrorLabel.setText("Seleziona un ticket dalla lista.");
            return;
        }
        String text = commentField.getText();
        CommentBean bean = new CommentBean();
        bean.setTicketId(selected.id());
        bean.setAuthorEmail(SessionContext.getCurrentUser().email());
        bean.setText(text);
        if (!bean.isValid()) {
            actionErrorLabel.setText("Il commento non può essere vuoto.");
            return;
        }
        actionErrorLabel.setText("");
        try {
            ViewTicketsFacade.getInstance().addComment(bean);
            commentField.clear();
        } catch (DAOException e) {
            log.error("Errore aggiunta commento al ticket {}", selected.id(), e);
            showError("Errore", "Impossibile salvare il commento.");
        } catch (SupportDeskException e) {
            actionErrorLabel.setText(e.getMessage());
        }
    }

    @FXML
    public void onReopenTicket() {
        TicketRecord selected = ticketTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            actionErrorLabel.setText("Seleziona un ticket dalla lista.");
            return;
        }
        actionErrorLabel.setText("");
        try {
            ViewTicketsFacade.getInstance().changeStatus(selected.id(), TicketStatus.REOPENED);
            loadMyTickets();
            hideDetail();
            ticketTable.getSelectionModel().clearSelection();
        } catch (DAOException e) {
            log.error("Errore riapertura ticket {}", selected.id(), e);
            showError("Errore", "Errore interno del sistema.");
        } catch (SupportDeskException e) {
            actionErrorLabel.setText(e.getMessage());
        }
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
            log.error("Errore caricamento ticket utente", e);
            showError("Errore", "Impossibile caricare i ticket.");
        }
    }

    @FXML
    public void onCloseDetail() {
        hideDetail();
        ticketTable.getSelectionModel().clearSelection();
        actionErrorLabel.setText("");
        commentField.clear();
        btnRiapri.setVisible(false);
        btnRiapri.setManaged(false);
    }
}
