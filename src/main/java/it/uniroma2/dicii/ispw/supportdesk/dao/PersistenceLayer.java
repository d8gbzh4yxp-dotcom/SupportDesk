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

    public static PersistenceLayer getInstance() {
        ApplicationMode mode = ApplicationModeManager.getInstance().getMode();
        if (mode == ApplicationMode.DEMO) {
            return new PersistenceLayerDemo();
        }
        return new PersistenceLayerFull();
    }

    // ── Ticket ────────────────────────────────────────────────────────────────

    public abstract void insertTicket(Ticket ticket) throws DAOException;

    public abstract Ticket findTicketById(int id) throws DAOException, TicketNotFoundException;

    public abstract List<Ticket> findAllTickets() throws DAOException;

    public abstract List<Ticket> findTicketsByUserEmail(String email) throws DAOException;

    public abstract void updateTicket(Ticket ticket) throws DAOException, TicketNotFoundException;

    public abstract void deleteTicket(int id) throws DAOException;

    // ── User ──────────────────────────────────────────────────────────────────

    public abstract User findUserByEmail(String email) throws DAOException;

    public abstract List<User> findUsersByRole(Role role) throws DAOException;

    public abstract void insertUser(User user) throws DAOException;
}
