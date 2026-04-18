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

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class SupportDeskApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        SceneNavigator.init(stage);
        SceneNavigator.navigateTo("login.fxml", "SupportDesk — Login");
    }

    public static void main(String[] args) {
        launch();
    }
}
