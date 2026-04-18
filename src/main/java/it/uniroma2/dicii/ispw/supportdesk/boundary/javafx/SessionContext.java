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

import it.uniroma2.dicii.ispw.supportdesk.record.LoginRecord;

public final class SessionContext {

    private static LoginRecord currentUser;

    private SessionContext() {}

    public static void setCurrentUser(LoginRecord user) {
        currentUser = user;
    }

    public static LoginRecord getCurrentUser() {
        return currentUser;
    }

    public static void clear() {
        currentUser = null;
    }
}
