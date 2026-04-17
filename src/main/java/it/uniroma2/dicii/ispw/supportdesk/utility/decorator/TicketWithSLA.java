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

import it.uniroma2.dicii.ispw.supportdesk.model.Ticket;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class TicketWithSLA extends TicketDecorator {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final String LABEL_SCADUTO = " [SLA SCADUTO]";

    public TicketWithSLA(Ticket ticket) {
        super(ticket);
    }

    @Override
    public String getDisplaySummary() {
        String slaFormatted = ticket.getScadenzaSla() != null
                ? FMT.format(ticket.getScadenzaSla())
                : "N/A";
        String scadutoTag = isScaduto() ? LABEL_SCADUTO : "";
        return String.format("[#%d] %s — SLA: %s%s",
                ticket.getId(), ticket.getTitle(), slaFormatted, scadutoTag);
    }

    private boolean isScaduto() {
        return ticket.getScadenzaSla() != null
                && LocalDateTime.now().isAfter(ticket.getScadenzaSla());
    }
}
