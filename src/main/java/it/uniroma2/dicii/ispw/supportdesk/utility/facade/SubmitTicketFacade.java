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
import it.uniroma2.dicii.ispw.supportdesk.controller.applicativo.TicketController;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.exception.ValidationException;
import it.uniroma2.dicii.ispw.supportdesk.record.TicketRecord;
import it.uniroma2.dicii.ispw.supportdesk.utility.observer.ManagerNotificationObserver;
import it.uniroma2.dicii.ispw.supportdesk.utility.observer.TechnicianNotificationObserver;
import it.uniroma2.dicii.ispw.supportdesk.utility.singleton.UserSession;

@SuppressWarnings("java:S6548")
public final class SubmitTicketFacade {

    private SubmitTicketFacade() {}

    private static final class Holder {
        private static final SubmitTicketFacade INSTANCE = new SubmitTicketFacade();
    }

    public static SubmitTicketFacade getInstance() {
        return Holder.INSTANCE;
    }

    public TicketRecord openTicketWithWorkflow(TicketBean bean) throws ValidationException, DAOException {
        if (bean == null || !bean.isValid()) {
            throw new ValidationException("TicketBean non valido o incompleto");
        }
        TicketController ctrl = new TicketController();
        ctrl.attach(new TechnicianNotificationObserver());
        ctrl.attach(new ManagerNotificationObserver());
        String authorEmail = UserSession.getInstance().getCurrentUser() != null
                ? UserSession.getInstance().getCurrentUser().obtainEmail()
                : "unknown";
        return ctrl.openTicket(bean, authorEmail);
    }
}
