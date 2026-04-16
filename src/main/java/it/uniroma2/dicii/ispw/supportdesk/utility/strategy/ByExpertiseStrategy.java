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
package it.uniroma2.dicii.ispw.supportdesk.utility.strategy;

import it.uniroma2.dicii.ispw.supportdesk.exception.AssignmentException;
import it.uniroma2.dicii.ispw.supportdesk.model.Ticket;
import it.uniroma2.dicii.ispw.supportdesk.model.User;

import java.util.List;

public class ByExpertiseStrategy implements AssignmentStrategy {

    @Override
    public User assign(Ticket ticket, List<User> technicians) throws AssignmentException {
        if (technicians.isEmpty()) throw new AssignmentException("Nessun tecnico disponibile");
        String category = ticket.getCategory().name();
        for (User tech : technicians) {
            if (category.equalsIgnoreCase(tech.obtainSpecialization())) return tech;
        }
        return technicians.get(0);
    }
}
