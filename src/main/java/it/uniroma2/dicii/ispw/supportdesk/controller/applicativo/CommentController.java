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

import it.uniroma2.dicii.ispw.supportdesk.dao.PersistenceLayer;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.exception.ValidationException;
import it.uniroma2.dicii.ispw.supportdesk.model.Comment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CommentController {

    private static final Logger log = LoggerFactory.getLogger(CommentController.class);

    public void addComment(int ticketId, String authorEmail, String text)
            throws ValidationException, DAOException {
        if (text == null || text.isBlank()) {
            throw new ValidationException("Il testo del commento non può essere vuoto");
        }
        int nextId = PersistenceLayer.getInstance().findAllComments()
                .stream().mapToInt(Comment::getId).max().orElse(0) + 1;
        Comment comment = new Comment(nextId, ticketId, authorEmail, text);
        PersistenceLayer.getInstance().saveComment(comment);
        if (log.isInfoEnabled()) {
            log.info("Commento {} aggiunto al ticket {} da {}", nextId, ticketId, authorEmail);
        }
    }

    public List<Comment> getCommentsForTicket(int ticketId) throws DAOException {
        return PersistenceLayer.getInstance().findCommentsByTicketId(ticketId);
    }
}
