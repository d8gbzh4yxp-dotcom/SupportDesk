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

import it.uniroma2.dicii.ispw.supportdesk.dao.UserDAO;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.Role;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.model.User;

import java.util.List;

public class UserDAODB implements UserDAO {

    @Override
    public User findByEmail(String email) throws DAOException {
        throw new DAOException("UserDAODB non ancora implementato");
    }

    @Override
    public List<User> findByRole(Role role) throws DAOException {
        throw new DAOException("UserDAODB non ancora implementato");
    }

    @Override
    public void insert(User user) throws DAOException {
        throw new DAOException("UserDAODB non ancora implementato");
    }
}
