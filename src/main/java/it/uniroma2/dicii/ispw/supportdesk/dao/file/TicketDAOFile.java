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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TicketDAOFile implements TicketDAO {

    private static final Logger LOG = LoggerFactory.getLogger(TicketDAOFile.class);
    private static final String SEP = "|";
    private static final String NULL_TOKEN = "null";
    private static final int FIELDS = 9;
    private static final String DATA_FILE = FilePathResolver.resolve("tickets.csv");

    @Override
    public void insert(Ticket ticket) throws DAOException {
        int nextId = computeNextId();
        String line = buildLine(nextId, ticket, null);
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(DATA_FILE, true), StandardCharsets.UTF_8))) {
            bw.write(line);
            bw.newLine();
        } catch (IOException e) {
            throw new DAOException("Errore insert ticket su file", e);
        }
    }

    @Override
    public Ticket findById(int id) throws DAOException, TicketNotFoundException {
        for (Ticket t : readAll()) {
            if (t.getId() == id) return t;
        }
        throw new TicketNotFoundException("Ticket non trovato: " + id);
    }

    @Override
    public List<Ticket> findAll() throws DAOException {
        return readAll();
    }

    @Override
    public List<Ticket> findByUserEmail(String email) throws DAOException {
        List<Ticket> result = new ArrayList<>();
        for (Ticket t : readAll()) {
            if (t.getAssignedTechnician() != null
                    && email.equals(t.getAssignedTechnician().obtainEmail())) {
                result.add(t);
            }
        }
        return result;
    }

    @Override
    public void update(Ticket ticket) throws DAOException, TicketNotFoundException {
        List<String> lines = readRawLines();
        boolean found = false;
        List<String> updated = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split("\\|", FIELDS);
            if (Integer.parseInt(parts[0]) == ticket.getId()) {
                String techEmail = ticket.getAssignedTechnician() != null
                    ? ticket.getAssignedTechnician().obtainEmail() : NULL_TOKEN;
                updated.add(buildLine(ticket.getId(), ticket, techEmail));
                found = true;
            } else {
                updated.add(line);
            }
        }
        if (!found) throw new TicketNotFoundException("Ticket non trovato: " + ticket.getId());
        writeAllLines(updated);
    }

    @Override
    public void delete(int id) throws DAOException {
        List<String> lines = readRawLines();
        List<String> filtered = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split("\\|", FIELDS);
            if (Integer.parseInt(parts[0]) != id) filtered.add(line);
        }
        writeAllLines(filtered);
        LOG.debug("Ticket eliminato id={}", id);
    }

    private List<Ticket> readAll() throws DAOException {
        List<Ticket> list = new ArrayList<>();
        for (String line : readRawLines()) list.add(parseLine(line));
        return list;
    }

    private List<String> readRawLines() throws DAOException {
        List<String> lines = new ArrayList<>();
        File file = new File(DATA_FILE);
        if (!file.exists()) return lines;
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.isBlank()) lines.add(line);
            }
        } catch (IOException e) {
            throw new DAOException("Errore lettura tickets.csv", e);
        }
        return lines;
    }

    private void writeAllLines(List<String> lines) throws DAOException {
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(DATA_FILE, false), StandardCharsets.UTF_8))) {
            for (String line : lines) { bw.write(line); bw.newLine(); }
        } catch (IOException e) {
            throw new DAOException("Errore scrittura tickets.csv", e);
        }
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
            LocalDateTime sla   = LocalDateTime.parse(p[7]);
            return new Ticket(id, title, description, category, priority, data, sla, status);
        } catch (Exception e) {
            throw new DAOException("Errore parsing riga ticket: " + line, e);
        }
    }

    private String buildLine(int id, Ticket t, String techEmail) {
        String te = (techEmail == null || techEmail.isBlank()) ? NULL_TOKEN : techEmail;
        return id + SEP + t.getTitle() + SEP + t.getDescription() + SEP
            + t.getCategory().name() + SEP + t.getPriority().name() + SEP
            + t.getStatus().name() + SEP + t.getDataApertura() + SEP
            + t.getScadenzaSla() + SEP + te;
    }

    private int computeNextId() throws DAOException {
        int max = 0;
        for (String line : readRawLines()) {
            int id = Integer.parseInt(line.split("\\|", 2)[0]);
            if (id > max) max = id;
        }
        return max + 1;
    }
}
