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
import it.uniroma2.dicii.ispw.supportdesk.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class CommentController {

    private static final Logger log = LoggerFactory.getLogger(CommentController.class);


    private final List<Comment> comments = new CopyOnWriteArrayList<>();
    private final AtomicInteger idGen = new AtomicInteger(1);

    public void addComment(int ticketId, String authorEmail, String text)
            throws ValidationException, DAOException {
        if (text == null || text.isBlank()) {
            throw new ValidationException("Il testo del commento non può essere vuoto");
        }
        User author = PersistenceLayer.getInstance().findUserByEmail(authorEmail);
        if (author == null) {
            throw new ValidationException("Autore non trovato: " + authorEmail);
        }
        Comment comment = new Comment(idGen.getAndIncrement(), ticketId, author, text);
        comments.add(comment);
        log.info("Commento aggiunto al ticket {} da {}", ticketId, authorEmail);
    }

    public List<Comment> getCommentsForTicket(int ticketId) {
        return comments.stream()
                .filter(c -> c.getTicketId() == ticketId)
                .toList();
    }
}
