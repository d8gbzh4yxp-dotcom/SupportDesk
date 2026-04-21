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

import it.uniroma2.dicii.ispw.supportdesk.model.Ticket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManagerNotificationObserver implements TicketObserver {

    private static final Logger log = LoggerFactory.getLogger(ManagerNotificationObserver.class);


    @Override
    public void onTicketEvent(EventType eventType, Ticket ticket) {
        switch (eventType) {
            case TICKET_CAMBIO_STATO ->
                    log.info("[NOTIFICA MANAGER] Ticket #{} cambiato stato → {}", ticket.getId(), ticket.getStatus());
            case SLA_VIOLATO ->
                    log.warn("[NOTIFICA MANAGER] SLA VIOLATO — Ticket #{} {}", ticket.getId(), ticket.getTitle());
            case SLA_IN_SCADENZA ->
                    log.warn("[NOTIFICA MANAGER] SLA IN SCADENZA — Ticket #{} {}", ticket.getId(), ticket.getTitle());
            case ASSEGNAZIONE_MANUALE ->
                    log.warn("[NOTIFICA MANAGER] Nessun tecnico disponibile — Ticket #{} richiede assegnazione manuale", ticket.getId());
            default -> { /* altri eventi non di competenza del manager */ }
        }
    }
}
