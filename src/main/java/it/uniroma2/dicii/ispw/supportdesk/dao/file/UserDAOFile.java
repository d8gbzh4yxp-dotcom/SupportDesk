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

import it.uniroma2.dicii.ispw.supportdesk.dao.UserDAO;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.Role;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserDAOFile implements UserDAO {
    private static final String SEP = "|";
    private static final String NULL_TOKEN = "null";
    private static final int FIELDS = 7;
    private static final String DATA_FILE = FilePathResolver.resolve("users.csv");

    @Override
    public User findByEmail(String email) throws DAOException {
        for (User u : readAll()) {
            if (email.equals(u.obtainEmail())) return u;
        }
        return null;
    }

    @Override
    public List<User> findByRole(Role role) throws DAOException {
        List<User> result = new ArrayList<>();
        for (User u : readAll()) {
            if (u.obtainRole() == role) result.add(u);
        }
        return result;
    }

    @Override
    public void insert(User user) throws DAOException {
        int nextId = computeNextId();
        CsvFileStore.appendLine(DATA_FILE, buildLine(nextId, user));
        }

    private List<User> readAll() throws DAOException {
        List<User> list = new ArrayList<>();
        for (String line : CsvFileStore.readLines(DATA_FILE)) list.add(parseLine(line));
        return list;
    }

    private User parseLine(String line) throws DAOException {
        String[] p = line.split("\\|", FIELDS);
        try {
            int id         = Integer.parseInt(p[0]);
            String name    = p[1];
            String surname = p[2];
            String email   = p[3];
            String hash    = p[4];
            Role role      = Role.valueOf(p[5]);
            String spec    = NULL_TOKEN.equals(p[6]) ? null : p[6];
            User user      = new User(id, name, surname, email, hash, role);
            user.setSpecialization(spec);
            return user;
        } catch (Exception e) {
            throw new DAOException("Errore parsing riga user: " + line, e);
        }
    }

    private String buildLine(int id, User u) {
        String spec = u.obtainSpecialization() != null ? u.obtainSpecialization() : NULL_TOKEN;
        return id + SEP + u.obtainName() + SEP + u.obtainSurname() + SEP
            + u.obtainEmail() + SEP + u.obtainPasswordHash() + SEP
            + u.obtainRole().name() + SEP + spec;
    }

    private int computeNextId() throws DAOException {
        int max = 0;
        for (User u : readAll()) {
            if (u.obtainId() > max) max = u.obtainId();
        }
        return max + 1;
    }
}
