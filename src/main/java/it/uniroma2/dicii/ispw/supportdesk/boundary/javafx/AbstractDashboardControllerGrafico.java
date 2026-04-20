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

import it.uniroma2.dicii.ispw.supportdesk.record.TicketRecord;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractDashboardControllerGrafico {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @FXML protected VBox  detailPanel;
    @FXML protected Label detailId;
    @FXML protected Label detailTitle;
    @FXML protected Label detailDescription;
    @FXML protected Label detailCategory;
    @FXML protected Label detailPriority;
    @FXML protected Label detailStatus;
    @FXML protected Label detailDataApertura;
    @FXML protected Label detailSla;
    @FXML protected Label detailTechnician;

    protected void populateDetail(TicketRecord t) {
        detailId.setText(String.valueOf(t.id()));
        detailTitle.setText(t.title());
        detailDescription.setText(t.description());
        detailCategory.setText(t.getCategory());
        detailPriority.setText(t.getPriority());
        detailStatus.setText(t.getStatus());
        detailDataApertura.setText(t.getDataApertura());
        detailSla.setText(t.getScadenzaSla());
        detailTechnician.setText(t.getAssignedTechnicianName().isBlank()
                ? "Non assegnato" : t.getAssignedTechnicianName());
        detailPanel.setVisible(true);
        detailPanel.setManaged(true);
    }

    protected void hideDetail() {
        detailPanel.setVisible(false);
        detailPanel.setManaged(false);
    }

    protected void showError(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
