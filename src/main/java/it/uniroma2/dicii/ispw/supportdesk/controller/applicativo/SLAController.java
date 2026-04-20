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
package it.uniroma2.dicii.ispw.supportdesk.controller.applicativo;

import it.uniroma2.dicii.ispw.supportdesk.dao.PersistenceLayer;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.TicketStatus;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.exception.SLAViolatedException;
import it.uniroma2.dicii.ispw.supportdesk.exception.TicketNotFoundException;
import it.uniroma2.dicii.ispw.supportdesk.model.Ticket;
import it.uniroma2.dicii.ispw.supportdesk.record.TicketRecord;
import it.uniroma2.dicii.ispw.supportdesk.utility.observer.EventType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

public class SLAController {

    private static final Logger log = LoggerFactory.getLogger(SLAController.class);

    private static final long SLA_WARNING_HOURS = 2;

    public boolean isSlaViolated(int ticketId) throws DAOException, TicketNotFoundException {
        Ticket ticket = PersistenceLayer.getInstance().findTicketById(ticketId);
        boolean violated = LocalDateTime.now().isAfter(ticket.getScadenzaSla());
        if (violated) {
            log.warn("SLA violato per ticket {}", ticketId);
        }
        return violated;
    }

    public void checkAndNotify(int ticketId, TicketController ticketController)
            throws DAOException, TicketNotFoundException, SLAViolatedException {
        Ticket ticket = PersistenceLayer.getInstance().findTicketById(ticketId);
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(ticket.getScadenzaSla())) {
            ticketController.notifyObservers(EventType.SLA_VIOLATO, ticket);
            throw new SLAViolatedException("SLA violato per ticket " + ticketId);
        }
        if (!now.isBefore(ticket.getScadenzaSla().minusHours(SLA_WARNING_HOURS))) {
            ticketController.notifyObservers(EventType.SLA_IN_SCADENZA, ticket);
        }
    }

    public List<TicketRecord> getTicketsWithSlaExpiringSoon() throws DAOException {
        LocalDateTime threshold = LocalDateTime.now().plusHours(SLA_WARNING_HOURS);
        return PersistenceLayer.getInstance().findAllTickets().stream()
                .filter(t -> !isTerminated(t) && !t.getScadenzaSla().isAfter(threshold))
                .map(TicketController::toRecord)
                .toList();
    }

    private boolean isTerminated(Ticket t) {
        return t.getStatus() == TicketStatus.RESOLVED || t.getStatus() == TicketStatus.CLOSED;
    }
}
