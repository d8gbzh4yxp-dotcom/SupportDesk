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
 * Bean per il form di aggiunta commento. Validazione sintattica via isValid().
 * Nessuna logica di dominio: solo trasporto dati dalla boundary al controller.
 */
public class CommentBean {

    private int ticketId;
    private String authorEmail;
    private String text;

    public CommentBean() {
        // no-arg required by bean contract
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    /** Validazione sintattica: ticket valido e testo non vuoto. */
    public boolean isValid() {
        return ticketId > 0
                && authorEmail != null && !authorEmail.isBlank()
                && text != null && !text.isBlank();
    }
}
