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
package it.uniroma2.dicii.ispw.supportdesk.utility.observer;

import it.uniroma2.dicii.ispw.supportdesk.enumerator.TicketStatus;
import it.uniroma2.dicii.ispw.supportdesk.model.Ticket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TechnicianNotificationObserver implements TicketObserver {

    private static final Logger log = LoggerFactory.getLogger(TechnicianNotificationObserver.class);

    @Override
    public void update(EventType event, Object payload) {
        if (!(payload instanceof Ticket ticket)) return;
        if (event == EventType.TICKET_CAMBIO_STATO
                && ticket.getStatus() == TicketStatus.ASSIGNED
                && ticket.getAssignedTechnician() != null) {
            String techName = ticket.getAssignedTechnician().obtainName()
                    + " " + ticket.getAssignedTechnician().obtainSurname();
            log.info("[NOTIFICA TECNICO] Ticket #{} assegnato a {} — {}", ticket.getId(), techName, ticket.getTitle());
        }
    }
}
