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

import java.time.LocalDateTime;

/**
 * Commento aggiunto da un utente su un ticket. Dumb data holder.
 */
public class Comment {

    private final int id;
    private final int ticketId;
    private final User author;
    private final String text;
    private final LocalDateTime createdAt;

    public Comment(int id, int ticketId, User author, String text) {
        this.id = id;
        this.ticketId = ticketId;
        this.author = author;
        this.text = text;
        this.createdAt = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public int getTicketId() {
        return ticketId;
    }

    public User getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
