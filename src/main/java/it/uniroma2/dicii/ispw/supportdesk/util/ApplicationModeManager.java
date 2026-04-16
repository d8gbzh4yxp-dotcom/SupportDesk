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
package it.uniroma2.dicii.ispw.supportdesk.util;

import it.uniroma2.dicii.ispw.supportdesk.enumerator.ApplicationMode;

public final class ApplicationModeManager {

    private volatile ApplicationMode mode = ApplicationMode.DEMO;

    private ApplicationModeManager() {}

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
}
