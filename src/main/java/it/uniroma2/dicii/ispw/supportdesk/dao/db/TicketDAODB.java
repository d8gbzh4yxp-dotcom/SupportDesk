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
package it.uniroma2.dicii.ispw.supportdesk.dao.db;

import it.uniroma2.dicii.ispw.supportdesk.dao.ConnectionManager;
import it.uniroma2.dicii.ispw.supportdesk.dao.TicketDAO;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.Category;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.Priority;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.TicketStatus;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.exception.TicketNotFoundException;
import it.uniroma2.dicii.ispw.supportdesk.model.Ticket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TicketDAODB implements TicketDAO {

    private static final Logger LOG = LoggerFactory.getLogger(TicketDAODB.class);

    private static final String SQL_INSERT =
        "INSERT INTO tickets (title, description, category, priority, status, data_apertura, scadenza_sla) VALUES (?,?,?,?,?,?,?)";
    private static final String TICKET_COLS =
        "id, title, description, category, priority, status, data_apertura, assigned_technician_email";
    private static final String SQL_FIND_BY_ID =
        "SELECT " + TICKET_COLS + " FROM tickets WHERE id = ?";
    private static final String SQL_FIND_ALL =
        "SELECT " + TICKET_COLS + " FROM tickets";
    private static final String SQL_FIND_BY_EMAIL =
        "SELECT " + TICKET_COLS + " FROM tickets WHERE assigned_technician_email = ?";
    private static final String SQL_UPDATE =
        "UPDATE tickets SET status = ?, assigned_technician_email = ? WHERE id = ?";
    private static final String SQL_DELETE =
        "DELETE FROM tickets WHERE id = ?";

    @Override
    public void insert(Ticket ticket) throws DAOException {
        try (Connection conn = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {
            ps.setString(1, ticket.getTitle());
            ps.setString(2, ticket.getDescription());
            ps.setString(3, ticket.getCategory().name());
            ps.setString(4, ticket.getPriority().name());
            ps.setString(5, ticket.getStatus().name());
            ps.setTimestamp(6, Timestamp.valueOf(ticket.getDataApertura()));
            ps.setTimestamp(7, Timestamp.valueOf(ticket.getScadenzaSla()));
            ps.executeUpdate();
            if (LOG.isDebugEnabled()) LOG.debug("Ticket inserito: {}", ticket.getTitle());
        } catch (SQLException e) {
            throw new DAOException("Errore insert ticket", e);
        }
    }

    @Override
    public Ticket findById(int id) throws DAOException, TicketNotFoundException {
        try (Connection conn = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new TicketNotFoundException("Ticket non trovato: " + id);
                return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new DAOException("Errore findById ticket", e);
        }
    }

    @Override
    public List<Ticket> findAll() throws DAOException {
        List<Ticket> list = new ArrayList<>();
        try (Connection conn = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new DAOException("Errore findAll ticket", e);
        }
        return list;
    }

    @Override
    public List<Ticket> findByUserEmail(String email) throws DAOException {
        List<Ticket> list = new ArrayList<>();
        try (Connection conn = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_EMAIL)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DAOException("Errore findByUserEmail ticket", e);
        }
        return list;
    }

    @Override
    public void update(Ticket ticket) throws DAOException, TicketNotFoundException {
        try (Connection conn = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {
            ps.setString(1, ticket.getStatus().name());
            String techEmail = ticket.getAssignedTechnician() != null
                ? ticket.getAssignedTechnician().obtainEmail() : null;
            ps.setString(2, techEmail);
            ps.setInt(3, ticket.getId());
            int rows = ps.executeUpdate();
            if (rows == 0) throw new TicketNotFoundException("Ticket non trovato: " + ticket.getId());
        } catch (SQLException e) {
            throw new DAOException("Errore update ticket", e);
        }
    }

    @Override
    public void delete(int id) throws DAOException {
        try (Connection conn = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            if (LOG.isDebugEnabled()) LOG.debug("Ticket eliminato id={}", id);
        } catch (SQLException e) {
            throw new DAOException("Errore delete ticket", e);
        }
    }

    private Ticket mapRow(ResultSet rs) throws SQLException {
        int id              = rs.getInt("id");
        String title        = rs.getString("title");
        String description  = rs.getString("description");
        Category category   = Category.valueOf(rs.getString("category"));
        Priority priority   = Priority.valueOf(rs.getString("priority"));
        TicketStatus status = TicketStatus.valueOf(rs.getString("status"));
        LocalDateTime data  = rs.getTimestamp("data_apertura").toLocalDateTime();
        return new Ticket(id, title, description, category, priority, data, status);
    }
}
