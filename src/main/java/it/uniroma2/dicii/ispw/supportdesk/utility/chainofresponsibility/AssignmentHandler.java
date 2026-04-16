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
package it.uniroma2.dicii.ispw.supportdesk.utility.chainofresponsibility;

import it.uniroma2.dicii.ispw.supportdesk.exception.AssignmentException;
import it.uniroma2.dicii.ispw.supportdesk.model.Ticket;
import it.uniroma2.dicii.ispw.supportdesk.model.User;

import java.util.List;

public abstract class AssignmentHandler {

    private AssignmentHandler next;

    public AssignmentHandler setNext(AssignmentHandler next) {
        this.next = next;
        return next;
    }

    public final User handle(Ticket ticket, List<User> technicians) throws AssignmentException {
        User result = tryAssign(ticket, technicians);
        if (result != null) return result;
        if (next != null) return next.handle(ticket, technicians);
        throw new AssignmentException("Nessun handler in grado di assegnare il ticket: " + ticket.getId());
    }

    protected abstract User tryAssign(Ticket ticket, List<User> technicians);
}
