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
import it.uniroma2.dicii.ispw.supportdesk.exception.CorrelationEngineException;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.model.Ticket;
import it.uniroma2.dicii.ispw.supportdesk.record.TicketRecord;
import it.uniroma2.dicii.ispw.supportdesk.utility.singleton.CorrelationEngine;

import java.util.List;

public class CorrelationController {


    public List<TicketRecord> analyzeCorrelations(Ticket target)
            throws CorrelationEngineException, DAOException {
        List<Ticket> all = PersistenceLayer.getInstance().findAllTickets();
        List<Ticket> candidates = all.stream()
                .filter(t -> t.getId() != target.getId())
                .toList();
        try {
            List<Ticket> correlated = CorrelationEngine.getInstance()
                    .findCorrelatedTickets(target, candidates);
            return correlated.stream().map(TicketController::toRecord).toList();
        } catch (Exception e) {
            throw new CorrelationEngineException(
                    "Errore nell'analisi di correlazione per ticket " + target.getId(), e);
        }
    }
}
