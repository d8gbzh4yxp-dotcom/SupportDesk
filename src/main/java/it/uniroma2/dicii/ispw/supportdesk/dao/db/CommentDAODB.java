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

import it.uniroma2.dicii.ispw.supportdesk.dao.CommentDAO;
import it.uniroma2.dicii.ispw.supportdesk.dao.ConnectionManager;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.model.Comment;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommentDAODB implements CommentDAO {

    private static final String SQL_INSERT =
        "INSERT INTO comments (id, ticket_id, author_email, text, created_at) VALUES (?,?,?,?,?)";
    private static final String SQL_FIND_BY_TICKET =
        "SELECT id, ticket_id, author_email, text, created_at FROM comments WHERE ticket_id = ?";
    private static final String SQL_FIND_ALL =
        "SELECT id, ticket_id, author_email, text, created_at FROM comments";

    @Override
    public void insert(Comment comment) throws DAOException {
        try (Connection conn = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {
            ps.setInt(1, comment.getId());
            ps.setInt(2, comment.getTicketId());
            ps.setString(3, comment.getAuthorEmail());
            ps.setString(4, comment.getText());
            ps.setTimestamp(5, Timestamp.valueOf(comment.getCreatedAt()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Errore insert commento", e);
        }
    }

    @Override
    public List<Comment> findByTicketId(int ticketId) throws DAOException {
        List<Comment> list = new ArrayList<>();
        try (Connection conn = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_TICKET)) {
            ps.setInt(1, ticketId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DAOException("Errore findByTicketId commenti", e);
        }
        return list;
    }

    @Override
    public List<Comment> findAll() throws DAOException {
        List<Comment> list = new ArrayList<>();
        try (Connection conn = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new DAOException("Errore findAll commenti", e);
        }
        return list;
    }

    private Comment mapRow(ResultSet rs) throws SQLException {
        int id            = rs.getInt("id");
        int ticketId      = rs.getInt("ticket_id");
        String authorEmail = rs.getString("author_email");
        String text       = rs.getString("text");
        LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
        return new Comment(id, ticketId, authorEmail, text, createdAt);
    }
}
