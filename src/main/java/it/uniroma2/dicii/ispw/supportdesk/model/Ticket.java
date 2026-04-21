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
package it.uniroma2.dicii.ispw.supportdesk.model;

import it.uniroma2.dicii.ispw.supportdesk.enumerator.Category;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.Priority;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.TicketStatus;
import it.uniroma2.dicii.ispw.supportdesk.exception.InvalidTransitionException;

import java.time.LocalDateTime;

/**
 * Entità centrale del sistema. Contiene la logica della state machine
 * e il calcolo automatico della scadenza SLA.
 */
public class Ticket {

    private final int id;
    private final String title;
    private final String description;
    private final Category category;
    private final Priority priority;
    private final LocalDateTime dataApertura;
    private final LocalDateTime scadenzaSla;
    private final String authorEmail;
    private TicketStatus status;
    private User assignedTechnician;

    public Ticket(int id, String title, String description, Category category, Priority priority,
                  String authorEmail) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.priority = priority;
        this.authorEmail = authorEmail;
        this.dataApertura = LocalDateTime.now();
        this.scadenzaSla = dataApertura.plusHours(priority.getMaxSlaHours());
        this.status = TicketStatus.OPEN;
    }

    public Ticket(int id, String title, String description, Category category, Priority priority,
                  LocalDateTime dataApertura, TicketStatus status, String authorEmail) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.priority = priority;
        this.authorEmail = authorEmail;
        this.dataApertura = dataApertura;
        this.scadenzaSla = dataApertura.plusHours(priority.getMaxSlaHours());
        this.status = status;
    }

    /**
     * Unico metodo business del model: cambia stato rispettando la state machine.
     *
     * @throws InvalidTransitionException se la transizione non è consentita
     */
    public void cambiaStato(TicketStatus nuovoStato) throws InvalidTransitionException {
        if (!this.status.canTransitionTo(nuovoStato)) {
            throw new InvalidTransitionException(this.status, nuovoStato);
        }
        this.status = nuovoStato;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public Priority getPriority() {
        return priority;
    }

    public LocalDateTime getDataApertura() {
        return dataApertura;
    }

    public LocalDateTime getScadenzaSla() {
        return scadenzaSla;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public User getAssignedTechnician() {
        return assignedTechnician;
    }

    public void setAssignedTechnician(User technician) {
        this.assignedTechnician = technician;
    }
}
