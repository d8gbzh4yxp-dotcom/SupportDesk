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

import it.uniroma2.dicii.ispw.supportdesk.bean.TicketBean;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.Category;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.Priority;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.exception.SupportDeskException;
import it.uniroma2.dicii.ispw.supportdesk.fx.SceneNavigator;
import it.uniroma2.dicii.ispw.supportdesk.record.TicketRecord;
import it.uniroma2.dicii.ispw.supportdesk.utility.facade.SubmitTicketFacade;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Boundary JavaFX per il caso d'uso UC1 — Submit Ticket.
 * Corrisponde a SubmitTicketGUI nel VOPC e a OpenTicketGraphicController nel Sequence Diagram.
 */
public class OpenTicketControllerGrafico {


    @FXML private TextField           titleField;
    @FXML private TextArea            descriptionArea;
    @FXML private ComboBox<Category>  categoryBox;
    @FXML private ComboBox<Priority>  priorityBox;
    @FXML private Label               errorLabel;

    private static final Logger log = LoggerFactory.getLogger(OpenTicketControllerGrafico.class);

    private SubmitTicketFacade ticketFacade;

    @FXML
    public void initialize() {
        ticketFacade = SubmitTicketFacade.getInstance();   // step 3 SD: Initialize TicketFacade
        categoryBox.setItems(FXCollections.observableArrayList(Category.values()));
        priorityBox.setItems(FXCollections.observableArrayList(Priority.values()));
        showForm();
    }

    /** step VOPC: showForm() — prepara la vista per l'inserimento */
    public void showForm() {
        errorLabel.setText("");
        titleField.clear();
        descriptionArea.clear();
        categoryBox.setValue(null);
        priorityBox.setValue(null);
    }

    /** step 4 SD: utente preme "Invia" — step 5: boundary chiama ticketFacade.openTicketWithWorkflow() */
    @FXML
    public void onSubmitTicket() {
        errorLabel.setText("");
        String title       = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        Category category  = categoryBox.getValue();
        Priority priority  = priorityBox.getValue();
        submitTicket(title, description, category, priority);
    }

    /** VOPC: submitTicket(title, description, category, priority) */
    public void submitTicket(String title, String description, Category category, Priority priority) {
        TicketBean bean = new TicketBean();
        bean.setTitle(title);
        bean.setDescription(description);
        bean.setCategory(category);
        bean.setPriority(priority);

        try {
            TicketRecord result = ticketFacade.openTicketWithWorkflow(bean);
            showConfirmation(result.id());
        } catch (DAOException e) {
            log.error("Errore DAO apertura ticket", e);
            showError("Errore interno del sistema. Riprovare.");
        } catch (SupportDeskException e) {
            showError(e.getMessage());
        }
    }

    /** VOPC: showConfirmation(ticketId) */
    public void showConfirmation(int ticketId) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ticket aperto");
        alert.setContentText("Il tuo ticket #" + ticketId + " è stato aperto con successo.");
        alert.showAndWait();
        try {
            SceneNavigator.navigateTo("user-dashboard.fxml", "Dashboard Utente");
        } catch (IOException e) {
            log.error("Errore navigazione dopo conferma ticket", e);
        }
    }

    /** VOPC: showError(message) */
    public void showError(String message) {
        errorLabel.setText(message);
    }

    @FXML
    public void onBack() throws IOException {
        SceneNavigator.navigateTo("user-dashboard.fxml", "Dashboard Utente");
    }
}
