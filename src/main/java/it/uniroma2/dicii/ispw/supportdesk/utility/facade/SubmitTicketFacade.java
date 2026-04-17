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
package it.uniroma2.dicii.ispw.supportdesk.utility.facade;

import it.uniroma2.dicii.ispw.supportdesk.bean.TicketBean;
import it.uniroma2.dicii.ispw.supportdesk.dao.PersistenceLayer;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.exception.ValidationException;
import it.uniroma2.dicii.ispw.supportdesk.model.Ticket;
import it.uniroma2.dicii.ispw.supportdesk.model.User;
import it.uniroma2.dicii.ispw.supportdesk.record.TicketRecord;
import it.uniroma2.dicii.ispw.supportdesk.utility.singleton.UserSession;

public final class SubmitTicketFacade {

    private SubmitTicketFacade() {}

    private static final class Holder {
        private static final SubmitTicketFacade INSTANCE = new SubmitTicketFacade();
    }

    public static SubmitTicketFacade getInstance() {
        return Holder.INSTANCE;
    }

    public TicketRecord submitTicket(TicketBean bean) throws ValidationException, DAOException {
        if (bean == null || !bean.isValid()) {
            throw new ValidationException("TicketBean non valido o incompleto");
        }
        Ticket ticket = new Ticket(0, bean.getTitle(), bean.getDescription(),
                bean.getCategory(), bean.getPriority());
        PersistenceLayer.getInstance().insertTicket(ticket);
        User tech = ticket.getAssignedTechnician();
        String techName = tech != null ? tech.obtainName() : null;
        return new TicketRecord(ticket.getId(), ticket.getTitle(), ticket.getDescription(),
                ticket.getCategory(), ticket.getPriority(), ticket.getStatus(),
                ticket.getDataApertura(), ticket.getScadenzaSla(), techName);
    }
}
