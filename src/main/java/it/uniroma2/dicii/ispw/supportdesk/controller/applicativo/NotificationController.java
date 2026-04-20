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

import it.uniroma2.dicii.ispw.supportdesk.enumerator.Role;
import it.uniroma2.dicii.ispw.supportdesk.model.Notification;
import it.uniroma2.dicii.ispw.supportdesk.record.NotificationRecord;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class NotificationController {


    private final List<Notification> notifications = new CopyOnWriteArrayList<>();
    private final AtomicInteger idGen = new AtomicInteger(1);

    public NotificationRecord createNotification(String message, Role targetRole, int ticketId) {
        Notification n = new Notification(idGen.getAndIncrement(), message, targetRole, ticketId);
        notifications.add(n);
        System.out.println("Notifica creata per ticket " + ticketId);
        return toRecord(n);
    }

    public List<NotificationRecord> getNotificationsForRole(Role targetRole) {
        return notifications.stream()
                .filter(n -> n.getTargetRole() == targetRole)
                .map(this::toRecord)
                .toList();
    }

    public void markAsRead(int notificationId) {
        notifications.stream()
                .filter(n -> n.getId() == notificationId)
                .findFirst()
                .ifPresent(Notification::markAsRead);
    }

    private NotificationRecord toRecord(Notification n) {
        return new NotificationRecord(n.getId(), n.getMessage(), n.getTargetRole(),
                n.getTicketId(), n.getCreatedAt(), n.isRead());
    }
}
