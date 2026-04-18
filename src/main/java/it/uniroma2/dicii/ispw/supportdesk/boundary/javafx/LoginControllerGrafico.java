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

import it.uniroma2.dicii.ispw.supportdesk.bean.LoginBean;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.Role;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.exception.SupportDeskException;
import it.uniroma2.dicii.ispw.supportdesk.fx.SceneNavigator;
import it.uniroma2.dicii.ispw.supportdesk.record.LoginRecord;
import it.uniroma2.dicii.ispw.supportdesk.utility.facade.LoginFacade;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class LoginControllerGrafico {

    private static final Logger log = LoggerFactory.getLogger(LoginControllerGrafico.class);

    @FXML private TextField     emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label         errorLabel;

    @FXML
    public void onLogin() {
        errorLabel.setText("");
        LoginBean bean = new LoginBean();
        bean.setEmail(emailField.getText().trim());
        bean.setPassword(passwordField.getText());

        try {
            LoginRecord loginRecord = LoginFacade.getInstance().login(bean);
            navigateToDashboard(loginRecord);
        } catch (DAOException e) {
            log.error("Errore DAO durante il login", e);
            showError("Errore", "Errore interno del sistema. Riprovare.");
        } catch (SupportDeskException e) {
            errorLabel.setText(e.getMessage());
        } catch (IOException e) {
            log.error("Errore navigazione post-login", e);
            showError("Errore", "Impossibile aprire la dashboard.");
        }
    }

    private void navigateToDashboard(LoginRecord loginRecord) throws IOException {
        SessionContext.setCurrentUser(loginRecord);
        if (loginRecord.role() == Role.USER) {
            SceneNavigator.navigateTo("user-dashboard.fxml", "Dashboard Utente");
        } else if (loginRecord.role() == Role.TECHNICIAN) {
            SceneNavigator.navigateTo("tech-dashboard.fxml", "Dashboard Tecnico");
        } else {
            SceneNavigator.navigateTo("manager-dashboard.fxml", "Dashboard Manager");
        }
    }

    private void showError(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
