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
package it.uniroma2.dicii.ispw.supportdesk.dao;

import it.uniroma2.dicii.ispw.supportdesk.enumerator.ApplicationMode;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.Role;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.exception.TicketNotFoundException;
import it.uniroma2.dicii.ispw.supportdesk.model.Ticket;
import it.uniroma2.dicii.ispw.supportdesk.model.User;
import it.uniroma2.dicii.ispw.supportdesk.util.ApplicationModeManager;

import java.util.List;

public abstract class PersistenceLayer {

    protected TicketDAO ticketDAO;
    protected UserDAO   userDAO;

    public static PersistenceLayer getInstance() {
        ApplicationMode mode = ApplicationModeManager.getInstance().getMode();
        if (mode == ApplicationMode.DEMO) {
            return new PersistenceLayerDemo();
        }
        return new PersistenceLayerFull();
    }

    // ── Ticket ────────────────────────────────────────────────────────────────

    public void insertTicket(Ticket ticket) throws DAOException {
        ticketDAO.insert(ticket);
    }

    public Ticket findTicketById(int id) throws DAOException, TicketNotFoundException {
        return ticketDAO.findById(id);
    }

    public List<Ticket> findAllTickets() throws DAOException {
        return ticketDAO.findAll();
    }

    public List<Ticket> findTicketsByUserEmail(String email) throws DAOException {
        return ticketDAO.findByUserEmail(email);
    }

    public void updateTicket(Ticket ticket) throws DAOException, TicketNotFoundException {
        ticketDAO.update(ticket);
    }

    public void deleteTicket(int id) throws DAOException {
        ticketDAO.delete(id);
    }

    // ── User ──────────────────────────────────────────────────────────────────

    public User findUserByEmail(String email) throws DAOException {
        return userDAO.findByEmail(email);
    }

    public List<User> findUsersByRole(Role role) throws DAOException {
        return userDAO.findByRole(role);
    }

    public void insertUser(User user) throws DAOException {
        userDAO.insert(user);
    }
}
