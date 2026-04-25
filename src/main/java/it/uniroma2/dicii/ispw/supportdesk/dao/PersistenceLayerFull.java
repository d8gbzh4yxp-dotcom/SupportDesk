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
import it.uniroma2.dicii.ispw.supportdesk.utility.singleton.ApplicationModeManager;

public class PersistenceLayerFull extends PersistenceLayer {

    PersistenceLayerFull() {
        DAOAbstractFactory factory = DAOAbstractFactory.getFactory(
            ApplicationModeManager.getInstance().getMode()
        );
        this.ticketDAO  = factory.createTicketDAO();
        this.userDAO    = factory.createUserDAO();
        this.commentDAO = factory.createCommentDAO();
    }
}
