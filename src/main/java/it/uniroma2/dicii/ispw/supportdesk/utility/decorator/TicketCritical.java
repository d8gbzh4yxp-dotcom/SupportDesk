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
package it.uniroma2.dicii.ispw.supportdesk.utility.decorator;

import it.uniroma2.dicii.ispw.supportdesk.enumerator.Priority;
import it.uniroma2.dicii.ispw.supportdesk.model.Ticket;

public final class TicketCritical extends TicketDecorator {

    private static final String CRITICAL_TAG = "[CRITICO] ";

    public TicketCritical(Ticket ticket) {
        super(ticket);
    }

    @Override
    public String getDisplaySummary() {
        String tag = Priority.CRITICAL.equals(ticket.getPriority()) ? CRITICAL_TAG : "";
        return String.format("%s[#%d] %s (%s)",
                tag, ticket.getId(), ticket.getTitle(), ticket.getStatus().name());
    }
}
