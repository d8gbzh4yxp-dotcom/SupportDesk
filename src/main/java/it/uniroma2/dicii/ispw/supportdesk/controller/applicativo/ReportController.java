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
package it.uniroma2.dicii.ispw.supportdesk.controller.applicativo;

import it.uniroma2.dicii.ispw.supportdesk.dao.PersistenceLayer;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.exception.ReportException;
import it.uniroma2.dicii.ispw.supportdesk.model.Ticket;
import it.uniroma2.dicii.ispw.supportdesk.utility.builder.ReportBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

public class ReportController {

    private static final Logger log = LoggerFactory.getLogger(ReportController.class);


    public ReportBuilder.Report generateReport(String title, LocalDateTime start, LocalDateTime end)
            throws ReportException, DAOException {
        if (title == null || title.isBlank()) {
            throw new ReportException("Titolo del report è obbligatorio");
        }
        List<Ticket> all = PersistenceLayer.getInstance().findAllTickets();
        List<Ticket> filtered = filterByPeriod(all, start, end);
        log.info("Report generato: {} ticket", filtered.size());
        return new ReportBuilder()
                .withTitle(title)
                .withPeriod(start, end)
                .withTickets(filtered)
                .build();
    }

    private List<Ticket> filterByPeriod(List<Ticket> tickets, LocalDateTime start, LocalDateTime end) {
        return tickets.stream()
                .filter(t -> isInPeriod(t.getDataApertura(), start, end))
                .toList();
    }

    private boolean isInPeriod(LocalDateTime date, LocalDateTime start, LocalDateTime end) {
        boolean afterStart = start == null || !date.isBefore(start);
        boolean beforeEnd  = end   == null || !date.isAfter(end);
        return afterStart && beforeEnd;
    }
}
