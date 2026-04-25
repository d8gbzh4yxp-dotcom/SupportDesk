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

public class Comment {

    private final int id;
    private final int ticketId;
    private final String authorEmail;
    private final String text;
    private final LocalDateTime createdAt;

    public Comment(int id, int ticketId, String authorEmail, String text) {
        this.id          = id;
        this.ticketId    = ticketId;
        this.authorEmail = authorEmail;
        this.text        = text;
        this.createdAt   = LocalDateTime.now();
    }

    public Comment(int id, int ticketId, String authorEmail, String text, LocalDateTime createdAt) {
        this.id          = id;
        this.ticketId    = ticketId;
        this.authorEmail = authorEmail;
        this.text        = text;
        this.createdAt   = createdAt;
    }

    public int getId()            { return id; }
    public int getTicketId()      { return ticketId; }
    public String getAuthorEmail(){ return authorEmail; }
    public String getText()       { return text; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
