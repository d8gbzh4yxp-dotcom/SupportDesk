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
package it.uniroma2.dicii.ispw.supportdesk.utility.factory;

import it.uniroma2.dicii.ispw.supportdesk.enumerator.Role;
import it.uniroma2.dicii.ispw.supportdesk.model.Notification;

public final class NotificationFactory {

    private static final String MSG_TICKET_OPEN      = "Nuovo ticket #%d aperto";
    private static final String MSG_SLA_VIOLATION    = "SLA violato per ticket #%d";
    private static final String MSG_TICKET_ASSIGNED  = "Ticket #%d assegnato al tecnico";
    private static final String MSG_STATE_CHANGED    = "Ticket #%d: cambio stato";

    private NotificationFactory() {}

    public static Notification forTicketOpen(int ticketId) {
        return new Notification(0, String.format(MSG_TICKET_OPEN, ticketId), Role.MANAGER, ticketId);
    }

    public static Notification forSlaViolation(int ticketId) {
        return new Notification(0, String.format(MSG_SLA_VIOLATION, ticketId), Role.MANAGER, ticketId);
    }

    public static Notification forTicketAssigned(int ticketId, Role targetRole) {
        return new Notification(0, String.format(MSG_TICKET_ASSIGNED, ticketId), targetRole, ticketId);
    }

    public static Notification forStateChanged(int ticketId, Role targetRole) {
        return new Notification(0, String.format(MSG_STATE_CHANGED, ticketId), targetRole, ticketId);
    }
}
