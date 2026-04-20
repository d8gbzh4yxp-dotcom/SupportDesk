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
package it.uniroma2.dicii.ispw.supportdesk.utility.singleton;

import it.uniroma2.dicii.ispw.supportdesk.enumerator.ApplicationMode;
import it.uniroma2.dicii.ispw.supportdesk.utility.configloader.ConfigLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("java:S6548")
public final class ApplicationModeManager {

    private static final String MODE_KEY = "app.mode";

    private static final Logger log = LoggerFactory.getLogger(ApplicationModeManager.class);

    private volatile ApplicationMode mode;

    private ApplicationModeManager() {
        String raw = ConfigLoader.getInstance().get(MODE_KEY, ApplicationMode.DEMO.name());
        mode = parseMode(raw);
    }

    private static final class Holder {
        private static final ApplicationModeManager INSTANCE = new ApplicationModeManager();
    }

    public static ApplicationModeManager getInstance() {
        return Holder.INSTANCE;
    }

    public ApplicationMode getMode() {
        return mode;
    }

    public void setMode(ApplicationMode mode) {
        this.mode = mode;
    }

    private ApplicationMode parseMode(String raw) {
        try {
            return ApplicationMode.valueOf(raw.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.warn("Valore app.mode non riconosciuto: {} — uso DEMO", raw);
            return ApplicationMode.DEMO;
        }
    }
}
