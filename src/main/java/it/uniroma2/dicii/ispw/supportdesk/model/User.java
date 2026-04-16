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
package it.uniroma2.dicii.ispw.supportdesk.model;

import it.uniroma2.dicii.ispw.supportdesk.enumerator.Role;

/**
 * Utente del sistema. Dumb data holder: nessuna logica di dominio.
 * Il campo passwordHash non contiene mai la password in chiaro.
 */
public class User {

    private final int id;
    private final String name;
    private final String surname;
    private final String email;
    private final String passwordHash;
    private final Role role;
    private String specialization;

    public User(int id, String name, String surname, String email, String passwordHash, Role role) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public int obtainId() {
        return id;
    }

    public String obtainName() {
        return name;
    }

    public String obtainSurname() {
        return surname;
    }

    public String obtainEmail() {
        return email;
    }

    public String obtainPasswordHash() {
        return passwordHash;
    }

    public Role obtainRole() {
        return role;
    }

    public String obtainSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}
