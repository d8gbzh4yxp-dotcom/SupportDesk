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
import it.uniroma2.dicii.ispw.supportdesk.enumerator.Role;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.TicketStatus;
import it.uniroma2.dicii.ispw.supportdesk.exception.AssignmentException;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.exception.InvalidTransitionException;
import it.uniroma2.dicii.ispw.supportdesk.exception.TicketNotFoundException;
import it.uniroma2.dicii.ispw.supportdesk.model.Ticket;
import it.uniroma2.dicii.ispw.supportdesk.model.User;
import it.uniroma2.dicii.ispw.supportdesk.utility.chainofresponsibility.AssignmentHandler;
import it.uniroma2.dicii.ispw.supportdesk.utility.chainofresponsibility.DefaultHandler;
import it.uniroma2.dicii.ispw.supportdesk.utility.chainofresponsibility.ExpertiseHandler;
import it.uniroma2.dicii.ispw.supportdesk.utility.chainofresponsibility.WorkloadHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssignmentController {

    private static final Logger log = LoggerFactory.getLogger(AssignmentController.class);


    public void assign(Ticket ticket)
            throws DAOException, AssignmentException, TicketNotFoundException, InvalidTransitionException {
        List<User> technicians = PersistenceLayer.getInstance().findUsersByRole(Role.TECHNICIAN);
        List<Ticket> allTickets = PersistenceLayer.getInstance().findAllTickets();
        Map<String, Integer> workloadMap = computeWorkloadMap(allTickets);
        AssignmentHandler chain = buildChain(workloadMap);
        User technician = chain.handle(ticket, technicians);
        ticket.setAssignedTechnician(technician);
        ticket.cambiaStato(TicketStatus.ASSIGNED);
        PersistenceLayer.getInstance().updateTicket(ticket);
        if (log.isInfoEnabled()) {
            log.info("Ticket {} assegnato a {}", ticket.getId(), technician.obtainEmail());
        }
    }

    private AssignmentHandler buildChain(Map<String, Integer> workloadMap) {
        AssignmentHandler expertise = new ExpertiseHandler();
        AssignmentHandler workload  = new WorkloadHandler(workloadMap);
        AssignmentHandler fallback  = new DefaultHandler();
        expertise.setNext(workload);
        workload.setNext(fallback);
        return expertise;
    }

    private Map<String, Integer> computeWorkloadMap(List<Ticket> allTickets) {
        Map<String, Integer> map = new HashMap<>();
        for (Ticket t : allTickets) {
            if (t.getAssignedTechnician() != null && t.getStatus() != TicketStatus.RESOLVED
                    && t.getStatus() != TicketStatus.CLOSED) {
                String email = t.getAssignedTechnician().obtainEmail();
                map.merge(email, 1, Integer::sum);
            }
        }
        return map;
    }
}
