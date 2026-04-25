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
package it.uniroma2.dicii.ispw.supportdesk.dao.file;

import it.uniroma2.dicii.ispw.supportdesk.dao.CommentDAO;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.model.Comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommentDAOFile implements CommentDAO {

    private static final String SEP = "|";
    private static final int FIELDS = 5;
    private static final String DATA_FILE = FilePathResolver.resolve("comments.csv");

    @Override
    public void insert(Comment comment) throws DAOException {
        CsvFileStore.appendLine(DATA_FILE, buildLine(comment));
    }

    @Override
    public List<Comment> findByTicketId(int ticketId) throws DAOException {
        List<Comment> result = new ArrayList<>();
        for (Comment c : findAll()) {
            if (c.getTicketId() == ticketId) result.add(c);
        }
        return result;
    }

    @Override
    public List<Comment> findAll() throws DAOException {
        List<Comment> list = new ArrayList<>();
        for (String line : CsvFileStore.readLines(DATA_FILE)) {
            list.add(parseLine(line));
        }
        return list;
    }

    private String buildLine(Comment c) {
        // text goes last so split limit keeps any | inside text intact
        return c.getId() + SEP + c.getTicketId() + SEP + c.getAuthorEmail()
            + SEP + c.getCreatedAt() + SEP + c.getText();
    }

    private Comment parseLine(String line) throws DAOException {
        String[] p = line.split("\\|", FIELDS);
        try {
            int id             = Integer.parseInt(p[0]);
            int ticketId       = Integer.parseInt(p[1]);
            String authorEmail = p[2];
            LocalDateTime createdAt = LocalDateTime.parse(p[3]);
            String text        = p[4];
            return new Comment(id, ticketId, authorEmail, text, createdAt);
        } catch (Exception e) {
            throw new DAOException("Errore parsing riga commento: " + line, e);
        }
    }
}
