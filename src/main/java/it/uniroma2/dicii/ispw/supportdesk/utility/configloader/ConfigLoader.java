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
package it.uniroma2.dicii.ispw.supportdesk.utility.configloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigLoader {

    private static final Logger log = LoggerFactory.getLogger(ConfigLoader.class);
    private static final String DB_PROPERTIES = "db.properties";

    private final Properties properties = new Properties();

    private ConfigLoader() {
        load();
    }

    private static final class Holder {
        private static final ConfigLoader INSTANCE = new ConfigLoader();
    }

    public static ConfigLoader getInstance() {
        return Holder.INSTANCE;
    }

    private void load() {
        try (InputStream in = ConfigLoader.class.getClassLoader().getResourceAsStream(DB_PROPERTIES)) {
            if (in != null) {
                properties.load(in);
            } else {
                log.warn("File {} non trovato nel classpath — uso valori di default", DB_PROPERTIES);
            }
        } catch (IOException e) {
            log.error("Errore caricamento {}", DB_PROPERTIES, e);
        }
    }

    public String get(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}
