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
package it.uniroma2.dicii.ispw.supportdesk.bean;

/**
 * Bean per il form di login. Validazione sintattica via isValid().
 * Nessuna logica di dominio: solo trasporto dati dalla boundary al controller.
 */
public class LoginBean {

    private String email;
    private String password;

    public LoginBean() {
        // no-arg required by bean contract
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private static final String EMAIL_REGEX = "^[\\w.+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$";

    /** Validazione sintattica: formato email corretto e password non vuota. */
    public boolean isValid() {
        return email != null && email.matches(EMAIL_REGEX)
                && password != null && !password.isBlank();
    }
}
