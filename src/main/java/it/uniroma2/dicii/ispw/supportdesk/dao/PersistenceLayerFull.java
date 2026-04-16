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

import it.uniroma2.dicii.ispw.supportdesk.dao.factory.DAOAbstractFactory;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.Role;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.exception.TicketNotFoundException;
import it.uniroma2.dicii.ispw.supportdesk.model.Ticket;
import it.uniroma2.dicii.ispw.supportdesk.model.User;
import it.uniroma2.dicii.ispw.supportdesk.util.ApplicationModeManager;

import java.util.List;

public class PersistenceLayerFull extends PersistenceLayer {

    private final TicketDAO ticketDAO;
    private final UserDAO   userDAO;

    PersistenceLayerFull() {
        DAOAbstractFactory factory = DAOAbstractFactory.getFactory(
            ApplicationModeManager.getInstance().getMode()
        );
        this.ticketDAO = factory.createTicketDAO();
        this.userDAO   = factory.createUserDAO();
    }

    @Override
    public void insertTicket(Ticket ticket) throws DAOException {
        ticketDAO.insert(ticket);
    }

    @Override
    public Ticket findTicketById(int id) throws DAOException, TicketNotFoundException {
        return ticketDAO.findById(id);
    }

    @Override
    public List<Ticket> findAllTickets() throws DAOException {
        return ticketDAO.findAll();
    }

    @Override
    public List<Ticket> findTicketsByUserEmail(String email) throws DAOException {
        return ticketDAO.findByUserEmail(email);
    }

    @Override
    public void updateTicket(Ticket ticket) throws DAOException, TicketNotFoundException {
        ticketDAO.update(ticket);
    }

    @Override
    public void deleteTicket(int id) throws DAOException {
        ticketDAO.delete(id);
    }

    @Override
    public User findUserByEmail(String email) throws DAOException {
        return userDAO.findByEmail(email);
    }

    @Override
    public List<User> findUsersByRole(Role role) throws DAOException {
        return userDAO.findByRole(role);
    }

    @Override
    public void insertUser(User user) throws DAOException {
        userDAO.insert(user);
    }
}
