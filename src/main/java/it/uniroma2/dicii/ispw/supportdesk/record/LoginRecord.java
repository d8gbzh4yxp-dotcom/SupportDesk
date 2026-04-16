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
package it.uniroma2.dicii.ispw.supportdesk.record;

import it.uniroma2.dicii.ispw.supportdesk.enumerator.Role;

/**
 * Risposta immutabile restituita dal LoginController alla boundary dopo autenticazione.
 */
public record LoginRecord(
        int userId,
        String name,
        String surname,
        String email,
        Role role
) {}
