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
package it.uniroma2.dicii.ispw.supportdesk.dao.demo;

import it.uniroma2.dicii.ispw.supportdesk.dao.CommentDAO;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.model.Comment;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CommentDAODemo implements CommentDAO {

    private static final Map<Integer, Comment> STORE = new LinkedHashMap<>();

    @Override
    public void insert(Comment comment) throws DAOException {
        if (comment == null) throw new DAOException("Commento non può essere null");
        STORE.put(comment.getId(), comment);
    }

    @Override
    public List<Comment> findByTicketId(int ticketId) {
        List<Comment> result = new ArrayList<>();
        for (Comment c : STORE.values()) {
            if (c.getTicketId() == ticketId) result.add(c);
        }
        return result;
    }

    @Override
    public List<Comment> findAll() {
        return new ArrayList<>(STORE.values());
    }
}
