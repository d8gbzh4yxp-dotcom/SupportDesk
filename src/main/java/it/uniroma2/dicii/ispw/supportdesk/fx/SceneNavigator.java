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

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public final class SceneNavigator {

    private static final int SCENE_WIDTH  = 1100;
    private static final int SCENE_HEIGHT = 700;

    private static Stage stage;

    private SceneNavigator() {}

    public static void init(Stage s) {
        stage = s;
        stage.setResizable(true);
        stage.setMinWidth(900);
        stage.setMinHeight(600);
    }

    public static void navigateTo(String fxml, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                SceneNavigator.class.getResource(fxml));
        Scene scene = new Scene(loader.load(), SCENE_WIDTH, SCENE_HEIGHT);
        stage.setTitle("SupportDesk — " + title);
        stage.setScene(scene);
        stage.show();
    }
}
