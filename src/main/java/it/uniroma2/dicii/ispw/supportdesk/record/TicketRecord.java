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
package it.uniroma2.dicii.ispw.supportdesk.record;

import it.uniroma2.dicii.ispw.supportdesk.enumerator.Category;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.Priority;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.TicketStatus;

import java.time.LocalDateTime;

/**
 * Snapshot immutabile di un Ticket restituito dal TicketController alla boundary.
 * Non espone mai l'entità model Ticket.
 */
public record TicketRecord(
        int id,
        String title,
        String description,
        Category category,
        Priority priority,
        TicketStatus status,
        LocalDateTime dataApertura,
        LocalDateTime scadenzaSla,
        String assignedTechnicianName
) {
    public int getId()                        { return id; }
    public String getTitle()                  { return title; }
    public String getDescription()            { return description; }
    public String getCategory()               { return category != null ? category.name() : ""; }
    public String getPriority()               { return priority != null ? priority.name() : ""; }
    public String getStatus()                 { return status != null ? status.name() : ""; }
    public String getDataApertura()           { return dataApertura != null ? dataApertura.toString() : ""; }
    public String getScadenzaSla()            { return scadenzaSla != null ? scadenzaSla.toString() : "N/A"; }
    public String getAssignedTechnicianName() { return assignedTechnicianName != null ? assignedTechnicianName : ""; }
}
