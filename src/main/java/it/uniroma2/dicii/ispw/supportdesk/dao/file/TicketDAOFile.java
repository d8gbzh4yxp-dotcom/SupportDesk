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

import it.uniroma2.dicii.ispw.supportdesk.dao.TicketDAO;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.Category;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.Priority;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.TicketStatus;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.exception.TicketNotFoundException;
import it.uniroma2.dicii.ispw.supportdesk.model.Ticket;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TicketDAOFile implements TicketDAO {
    private static final String SEP = "|";
    private static final String NULL_TOKEN = "null";
    private static final int FIELDS = 10;
    private static final String DATA_FILE = FilePathResolver.resolve("tickets.csv");

    @Override
    public void insert(Ticket ticket) throws DAOException {
        int nextId = computeNextId();
        CsvFileStore.appendLine(DATA_FILE, buildLine(nextId, ticket));
        }

    @Override
    public Ticket findById(int id) throws DAOException, TicketNotFoundException {
        for (Ticket t : findAll()) {
            if (t.getId() == id) return t;
        }
        throw new TicketNotFoundException("Ticket non trovato: " + id);
    }

    @Override
    public List<Ticket> findAll() throws DAOException {
        List<Ticket> list = new ArrayList<>();
        for (String line : CsvFileStore.readLines(DATA_FILE)) list.add(parseLine(line));
        return list;
    }

    @Override
    public List<Ticket> findByUserEmail(String email) throws DAOException {
        List<Ticket> result = new ArrayList<>();
        for (Ticket t : findAll()) {
            if (email.equals(t.getAuthorEmail())) {
                result.add(t);
            }
        }
        return result;
    }

    @Override
    public void update(Ticket ticket) throws DAOException, TicketNotFoundException {
        List<String> raw = CsvFileStore.readLines(DATA_FILE);
        boolean found = false;
        List<String> updated = new ArrayList<>();
        for (String line : raw) {
            if (Integer.parseInt(line.split("\\|", 2)[0]) == ticket.getId()) {
                updated.add(buildLine(ticket.getId(), ticket));
                found = true;
            } else {
                updated.add(line);
            }
        }
        if (!found) throw new TicketNotFoundException("Ticket non trovato: " + ticket.getId());
        CsvFileStore.writeLines(DATA_FILE, updated);
    }

    @Override
    public void delete(int id) throws DAOException {
        List<String> raw = CsvFileStore.readLines(DATA_FILE);
        List<String> filtered = new ArrayList<>();
        for (String line : raw) {
            if (Integer.parseInt(line.split("\\|", 2)[0]) != id) filtered.add(line);
        }
        CsvFileStore.writeLines(DATA_FILE, filtered);
        }

    private Ticket parseLine(String line) throws DAOException {
        String[] p = line.split("\\|", FIELDS);
        try {
            int id              = Integer.parseInt(p[0]);
            String title        = p[1];
            String description  = p[2];
            Category category   = Category.valueOf(p[3]);
            Priority priority   = Priority.valueOf(p[4]);
            TicketStatus status = TicketStatus.valueOf(p[5]);
            LocalDateTime data  = LocalDateTime.parse(p[6]);
            // p[7] = scadenzaSla stored for reference; recalculated in constructor
            String authorEmail = p[8];
            return new Ticket.Builder(id, title, description, category, priority)
                    .dataApertura(data)
                    .status(status)
                    .authorEmail(authorEmail)
                    .build();
        } catch (Exception e) {
            throw new DAOException("Errore parsing riga ticket: " + line, e);
        }
    }

    private String buildLine(int id, Ticket t) {
        String techEmail = t.getAssignedTechnician() != null
            ? t.getAssignedTechnician().obtainEmail() : NULL_TOKEN;
        String authorEmail = t.getAuthorEmail() != null ? t.getAuthorEmail() : NULL_TOKEN;
        return id + SEP + t.getTitle() + SEP + t.getDescription() + SEP
            + t.getCategory().name() + SEP + t.getPriority().name() + SEP
            + t.getStatus().name() + SEP + t.getDataApertura() + SEP
            + t.getScadenzaSla() + SEP + authorEmail + SEP + techEmail;
    }

    private int computeNextId() throws DAOException {
        int max = 0;
        for (String line : CsvFileStore.readLines(DATA_FILE)) {
            int id = Integer.parseInt(line.split("\\|", 2)[0]);
            if (id > max) max = id;
        }
        return max + 1;
    }
}
