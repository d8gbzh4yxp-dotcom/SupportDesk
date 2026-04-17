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

import it.uniroma2.dicii.ispw.supportdesk.enumerator.Category;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.Priority;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.TicketStatus;
import it.uniroma2.dicii.ispw.supportdesk.model.Ticket;

import java.time.LocalDateTime;

public abstract class TicketDecorator {

    protected final Ticket ticket;

    protected TicketDecorator(Ticket ticket) {
        this.ticket = ticket;
    }

    public int getId()                    { return ticket.getId(); }
    public String getTitle()              { return ticket.getTitle(); }
    public String getDescription()        { return ticket.getDescription(); }
    public Category getCategory()         { return ticket.getCategory(); }
    public Priority getPriority()         { return ticket.getPriority(); }
    public TicketStatus getStatus()       { return ticket.getStatus(); }
    public LocalDateTime getDataApertura(){ return ticket.getDataApertura(); }
    public LocalDateTime getScadenzaSla() { return ticket.getScadenzaSla(); }

    public abstract String getDisplaySummary();
}
