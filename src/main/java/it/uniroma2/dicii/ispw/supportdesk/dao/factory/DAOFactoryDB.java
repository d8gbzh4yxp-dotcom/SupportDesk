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
package it.uniroma2.dicii.ispw.supportdesk.dao.factory;

import it.uniroma2.dicii.ispw.supportdesk.dao.TicketDAO;
import it.uniroma2.dicii.ispw.supportdesk.dao.UserDAO;
import it.uniroma2.dicii.ispw.supportdesk.dao.db.TicketDAODB;
import it.uniroma2.dicii.ispw.supportdesk.dao.db.UserDAODB;

public class DAOFactoryDB extends DAOAbstractFactory {

    @Override
    public TicketDAO createTicketDAO() {
        return new TicketDAODB();
    }

    @Override
    public UserDAO createUserDAO() {
        return new UserDAODB();
    }
}
