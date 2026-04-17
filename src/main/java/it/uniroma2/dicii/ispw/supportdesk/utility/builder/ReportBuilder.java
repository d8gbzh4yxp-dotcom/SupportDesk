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
package it.uniroma2.dicii.ispw.supportdesk.utility.builder;

import it.uniroma2.dicii.ispw.supportdesk.model.Ticket;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public final class ReportBuilder {

    private String title = "";
    private LocalDateTime periodStart;
    private LocalDateTime periodEnd;
    private final List<Ticket> tickets = new ArrayList<>();

    public ReportBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public ReportBuilder withPeriod(LocalDateTime start, LocalDateTime end) {
        this.periodStart = start;
        this.periodEnd   = end;
        return this;
    }

    public ReportBuilder withTickets(List<Ticket> tickets) {
        this.tickets.addAll(tickets);
        return this;
    }

    public Report build() {
        return new Report(title, periodStart, periodEnd, List.copyOf(tickets));
    }

    public record Report(
            String title,
            LocalDateTime periodStart,
            LocalDateTime periodEnd,
            List<Ticket> tickets
    ) {
        public int totalTickets() { return tickets.size(); }
    }
}
