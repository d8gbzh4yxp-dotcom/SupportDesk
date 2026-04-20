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
import it.uniroma2.dicii.ispw.supportdesk.dao.UserDAO;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.Role;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAODB implements UserDAO {

    private static final String USER_COLS =
        "id, name, surname, email, credential_hash, role, specialization";
    private static final String SELECT_USERS      = "SELECT " + USER_COLS + " FROM users";
    private static final String SQL_FIND_BY_EMAIL = SELECT_USERS + " WHERE email = ?";
    private static final String SQL_FIND_BY_ROLE  = SELECT_USERS + " WHERE role = ?";
    private static final String SQL_INSERT =
        "INSERT INTO users (name, surname, email, credential_hash, role, specialization) VALUES (?,?,?,?,?,?)";

    @Override
    public User findByEmail(String email) throws DAOException {
        try (Connection conn = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_EMAIL)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new DAOException("Errore findByEmail user", e);
        }
    }

    @Override
    public List<User> findByRole(Role role) throws DAOException {
        List<User> list = new ArrayList<>();
        try (Connection conn = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_ROLE)) {
            ps.setString(1, role.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DAOException("Errore findByRole user", e);
        }
        return list;
    }

    @Override
    public void insert(User user) throws DAOException {
        try (Connection conn = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {
            ps.setString(1, user.obtainName());
            ps.setString(2, user.obtainSurname());
            ps.setString(3, user.obtainEmail());
            ps.setString(4, user.obtainPasswordHash());
            ps.setString(5, user.obtainRole().name());
            ps.setString(6, user.obtainSpecialization());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Errore insert user", e);
        }
    }

    private User mapRow(ResultSet rs) throws SQLException {
        int id         = rs.getInt("id");
        String name    = rs.getString("name");
        String surname = rs.getString("surname");
        String email   = rs.getString("email");
        String hash    = rs.getString("credential_hash");
        Role role      = Role.valueOf(rs.getString("role"));
        User user      = new User(id, name, surname, email, hash, role);
        user.setSpecialization(rs.getString("specialization"));
        return user;
    }
}
