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
package it.uniroma2.dicii.ispw.supportdesk.utility.facade;

import it.uniroma2.dicii.ispw.supportdesk.bean.CommentBean;
import it.uniroma2.dicii.ispw.supportdesk.controller.applicativo.CommentController;
import it.uniroma2.dicii.ispw.supportdesk.controller.applicativo.TicketController;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.TicketStatus;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.exception.InvalidTransitionException;
import it.uniroma2.dicii.ispw.supportdesk.exception.TicketNotFoundException;
import it.uniroma2.dicii.ispw.supportdesk.exception.ValidationException;
import it.uniroma2.dicii.ispw.supportdesk.record.TicketRecord;

import java.util.List;

@SuppressWarnings("java:S6548")
public final class ViewTicketsFacade {

    private final TicketController ticketController = new TicketController();
    private final CommentController commentController = new CommentController();

    private ViewTicketsFacade() {}

    private static final class Holder {
        private static final ViewTicketsFacade INSTANCE = new ViewTicketsFacade();
    }

    public static ViewTicketsFacade getInstance() {
        return Holder.INSTANCE;
    }

    public List<TicketRecord> getAllTickets() throws DAOException {
        return ticketController.getAllTickets();
    }

    public List<TicketRecord> getTicketsByUser(String email) throws DAOException {
        return ticketController.getTicketsByUser(email);
    }

    public TicketRecord changeStatus(int ticketId, TicketStatus newStatus)
            throws DAOException, TicketNotFoundException, InvalidTransitionException {
        return ticketController.changeStatus(ticketId, newStatus);
    }

    public void addComment(CommentBean bean)
            throws ValidationException, DAOException {
        commentController.addComment(bean.getTicketId(), bean.getAuthorEmail(), bean.getText());
    }
}
