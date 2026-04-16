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
package it.uniroma2.dicii.ispw.supportdesk.dao.db;

import it.uniroma2.dicii.ispw.supportdesk.dao.TicketDAO;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.exception.TicketNotFoundException;
import it.uniroma2.dicii.ispw.supportdesk.model.Ticket;

import java.util.List;

public class TicketDAODB implements TicketDAO {

    @Override
    public void insert(Ticket ticket) throws DAOException {
        throw new DAOException("TicketDAODB non ancora implementato");
    }

    @Override
    public Ticket findById(int id) throws DAOException, TicketNotFoundException {
        throw new DAOException("TicketDAODB non ancora implementato");
    }

    @Override
    public List<Ticket> findAll() throws DAOException {
        throw new DAOException("TicketDAODB non ancora implementato");
    }

    @Override
    public List<Ticket> findByUserEmail(String email) throws DAOException {
        throw new DAOException("TicketDAODB non ancora implementato");
    }

    @Override
    public void update(Ticket ticket) throws DAOException, TicketNotFoundException {
        throw new DAOException("TicketDAODB non ancora implementato");
    }

    @Override
    public void delete(int id) throws DAOException {
        throw new DAOException("TicketDAODB non ancora implementato");
    }
}
