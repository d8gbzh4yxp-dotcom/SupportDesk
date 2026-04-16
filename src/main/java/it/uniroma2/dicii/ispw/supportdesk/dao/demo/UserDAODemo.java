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

import it.uniroma2.dicii.ispw.supportdesk.dao.UserDAO;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.Role;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.model.User;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UserDAODemo implements UserDAO {

    // SHA-256 di "demo1234" — placeholder per modalità demo
    private static final String DEMO_PASSWORD_HASH = "DEMO_HASH_PLACEHOLDER";

    private static final Map<String, User> STORE = new LinkedHashMap<>();

    static {
        User admin = new User(1, "Admin", "Sistema", "admin@supportdesk.it", DEMO_PASSWORD_HASH, Role.ADMIN);
        User tech1 = new User(2, "Marco", "Bianchi", "marco.bianchi@supportdesk.it", DEMO_PASSWORD_HASH, Role.TECHNICIAN);
        User tech2 = new User(3, "Laura", "Verdi", "laura.verdi@supportdesk.it", DEMO_PASSWORD_HASH, Role.TECHNICIAN);
        tech1.setSpecialization("Email e Posta Elettronica");
        tech2.setSpecialization("Reti e Connettività");
        User user1 = new User(4, "Giovanni", "Rossi", "giovanni.rossi@azienda.it", DEMO_PASSWORD_HASH, Role.USER);
        User user2 = new User(5, "Sara", "Neri", "sara.neri@azienda.it", DEMO_PASSWORD_HASH, Role.USER);

        STORE.put(admin.obtainEmail(), admin);
        STORE.put(tech1.obtainEmail(), tech1);
        STORE.put(tech2.obtainEmail(), tech2);
        STORE.put(user1.obtainEmail(), user1);
        STORE.put(user2.obtainEmail(), user2);
    }

    @Override
    public User findByEmail(String email) throws DAOException {
        if (email == null) {
            throw new DAOException("Email non può essere null");
        }
        return STORE.get(email);
    }

    @Override
    public List<User> findByRole(Role role) throws DAOException {
        if (role == null) {
            throw new DAOException("Role non può essere null");
        }
        List<User> result = new ArrayList<>();
        for (User u : STORE.values()) {
            if (u.obtainRole() == role) {
                result.add(u);
            }
        }
        return result;
    }

    @Override
    public void insert(User user) throws DAOException {
        if (user == null) {
            throw new DAOException("User non può essere null");
        }
        if (STORE.containsKey(user.obtainEmail())) {
            throw new DAOException("Utente già esistente: " + user.obtainEmail());
        }
        STORE.put(user.obtainEmail(), user);
    }
}
