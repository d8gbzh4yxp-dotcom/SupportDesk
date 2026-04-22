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

import it.uniroma2.dicii.ispw.supportdesk.bean.TicketBean;
import it.uniroma2.dicii.ispw.supportdesk.dao.PersistenceLayer;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.TicketStatus;
import it.uniroma2.dicii.ispw.supportdesk.exception.AssignmentException;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.exception.InvalidTransitionException;
import it.uniroma2.dicii.ispw.supportdesk.exception.TicketNotFoundException;
import it.uniroma2.dicii.ispw.supportdesk.exception.ValidationException;
import it.uniroma2.dicii.ispw.supportdesk.model.Ticket;
import it.uniroma2.dicii.ispw.supportdesk.record.TicketRecord;
import it.uniroma2.dicii.ispw.supportdesk.utility.observer.EventType;
import it.uniroma2.dicii.ispw.supportdesk.utility.observer.TicketObserver;
import it.uniroma2.dicii.ispw.supportdesk.utility.observer.TicketSubject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class TicketController implements TicketSubject {

    private static final Logger log = LoggerFactory.getLogger(TicketController.class);

    private final List<TicketObserver> observers = new CopyOnWriteArrayList<>();

    @Override
    public void attach(TicketObserver observer) {
        observers.add(observer);
    }

    @Override
    public void detach(TicketObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(EventType eventType, Object payload) {
        for (TicketObserver o : observers) {
            o.update(eventType, payload);
        }
    }

    public TicketRecord openTicket(TicketBean bean, String authorEmail)
            throws ValidationException, DAOException {
        if (!bean.isValid()) {
            throw new ValidationException("Dati ticket non validi");
        }
        List<Ticket> all = PersistenceLayer.getInstance().findAllTickets();
        int nextId = all.stream().mapToInt(Ticket::getId).max().orElse(0) + 1;
        Ticket ticket = new Ticket.Builder(nextId, bean.getTitle(), bean.getDescription(),
                bean.getCategory(), bean.getPriority())
                .authorEmail(authorEmail)
                .build();
        notifyObservers(EventType.TICKET_OPEN, ticket);
        PersistenceLayer.getInstance().saveTicket(ticket);
        launchBackgroundTasks(ticket);
        log.info("Ticket {} aperto da {}", ticket.getId(), authorEmail);
        return toRecord(ticket);
    }

    public TicketRecord getTicket(int id) throws DAOException, TicketNotFoundException {
        return toRecord(PersistenceLayer.getInstance().getTicketById(id));
    }

    public List<TicketRecord> getAllTickets() throws DAOException {
        return PersistenceLayer.getInstance().findAllTickets()
                .stream().map(TicketController::toRecord).toList();
    }

    public List<TicketRecord> getTicketsByUser(String email) throws DAOException {
        return PersistenceLayer.getInstance().getTicketsByUser(email)
                .stream().map(TicketController::toRecord).toList();
    }

    public TicketRecord changeStatus(int id, TicketStatus newStatus)
            throws DAOException, TicketNotFoundException, InvalidTransitionException {
        Ticket ticket = PersistenceLayer.getInstance().getTicketById(id);
        ticket.cambiaStato(newStatus);
        PersistenceLayer.getInstance().updateTicket(ticket);
        notifyObservers(EventType.TICKET_CAMBIO_STATO, ticket);
        log.info("Ticket {} passato a stato {}", id, newStatus);
        return toRecord(ticket);
    }

    private void launchBackgroundTasks(Ticket ticket) {
        int id = ticket.getId();

        Thread sla = new Thread(() -> {
            try {
                new SLAController().checkSLA(ticket, this);
            } catch (Exception e) {
                log.info("SLA monitoring non disponibile per ticket {}", id);
            }
        });

        Thread correlation = new Thread(() -> {
            try {
                new CorrelationController().findCorrelations(ticket);
            } catch (Exception e) {
                log.info("Correlazione non disponibile per ticket {}", id);
            }
        });

        Thread coordinator = new Thread(() -> {
            sla.start();
            correlation.start();
            try {
                sla.join();
                correlation.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
            AssignmentController ac = new AssignmentController();
            try {
                ac.assign(ticket);
                notifyObservers(EventType.TICKET_CAMBIO_STATO, ticket);
            } catch (AssignmentException e) {
                log.info("Nessun tecnico disponibile per ticket {} — richiesta assegnazione manuale", id);
                notifyObservers(EventType.ASSEGNAZIONE_MANUALE, ticket);
            } catch (Exception e) {
                log.info("Assegnazione non disponibile per ticket {}", id);
            }
        });

        coordinator.start();
    }

    static TicketRecord toRecord(Ticket t) {
        String techName = t.getAssignedTechnician() != null
                ? t.getAssignedTechnician().obtainName() + " " + t.getAssignedTechnician().obtainSurname()
                : null;
        return new TicketRecord(t.getId(), t.getTitle(), t.getDescription(),
                t.getCategory(), t.getPriority(), t.getStatus(),
                t.getDataApertura(), t.getScadenzaSla(), techName);
    }
}
