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

import it.uniroma2.dicii.ispw.supportdesk.dao.CommentDAO;
import it.uniroma2.dicii.ispw.supportdesk.dao.TicketDAO;
import it.uniroma2.dicii.ispw.supportdesk.dao.UserDAO;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.ApplicationMode;

public abstract class DAOAbstractFactory {

    public static DAOAbstractFactory getFactory(ApplicationMode mode) {
        return switch (mode) {
            case DEMO      -> new DAOFactoryDemo();
            case FULL_DB   -> new DAOFactoryDB();
            case FULL_FILE -> new DAOFactoryFile();
        };
    }

    public abstract TicketDAO createTicketDAO();

    public abstract UserDAO createUserDAO();

    public abstract CommentDAO createCommentDAO();
}
