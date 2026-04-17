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
package it.uniroma2.dicii.ispw.supportdesk.dao;

import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@SuppressWarnings("java:S6548")
public final class ConnectionManager {

    private static final Logger LOG = LoggerFactory.getLogger(ConnectionManager.class);
    private static final String DB_PROPS_FILE = "db.properties";
    private static final String KEY_URL  = "db.url";
    private static final String KEY_USER = "db.user";
    private static final String KEY_CRED = "db.credential";

    private final Properties props;

    private ConnectionManager() {
        props = new Properties();
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(DB_PROPS_FILE)) {
            if (in != null) props.load(in);
        } catch (IOException e) {
            LOG.warn("db.properties non trovato, uso system properties");
        }
    }

    private static final class Holder {
        private static final ConnectionManager INSTANCE = new ConnectionManager();
    }

    public static ConnectionManager getInstance() {
        return Holder.INSTANCE;
    }

    public Connection getConnection() throws DAOException {
        String url  = props.getProperty(KEY_URL,  System.getProperty(KEY_URL));
        String user = props.getProperty(KEY_USER, System.getProperty(KEY_USER));
        String cred = props.getProperty(KEY_CRED, System.getProperty(KEY_CRED, ""));
        try {
            return DriverManager.getConnection(url, user, cred);
        } catch (SQLException e) {
            LOG.error("Connessione DB fallita: {}", e.getMessage());
            throw new DAOException("Impossibile connettersi al database", e);
        }
    }
}
