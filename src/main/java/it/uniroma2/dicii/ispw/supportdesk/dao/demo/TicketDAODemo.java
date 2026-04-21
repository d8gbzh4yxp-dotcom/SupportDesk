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
package it.uniroma2.dicii.ispw.supportdesk.dao.demo;

import it.uniroma2.dicii.ispw.supportdesk.dao.TicketDAO;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.Category;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.Priority;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.exception.TicketNotFoundException;
import it.uniroma2.dicii.ispw.supportdesk.model.Ticket;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class TicketDAODemo implements TicketDAO {

    private static final Map<Integer, Ticket> STORE = new LinkedHashMap<>();
    private static final AtomicInteger nextId = new AtomicInteger(11);

    private static final String USER1 = "giovanni.rossi@azienda.it";
    private static final String USER2 = "sara.neri@azienda.it";

    static {
        // cluster email (4 ticket) — giovanni
        STORE.put(1, new Ticket(1, "Outlook non si apre",                "Outlook non risponde all'avvio",             Category.SOFTWARE, Priority.HIGH,     USER1));
        STORE.put(2, new Ticket(2, "Outlook non funziona su laptop",     "Crash all'apertura della casella di posta",  Category.SOFTWARE, Priority.HIGH,     USER1));
        STORE.put(3, new Ticket(3, "Impossibile inviare email",          "Errore SMTP 550 durante l'invio",            Category.SOFTWARE, Priority.MEDIUM,   USER1));
        STORE.put(4, new Ticket(4, "Configurazione account email",       "Account IMAP da riconfigurare dopo migrazione", Category.SOFTWARE, Priority.LOW,  USER1));

        // cluster network (3 ticket) — giovanni + sara
        STORE.put(5, new Ticket(5, "Connessione di rete assente",        "PC non ottiene indirizzo IP dal DHCP",       Category.NETWORK,  Priority.CRITICAL, USER1));
        STORE.put(6, new Ticket(6, "VPN non si connette",                "Timeout durante l'handshake VPN aziendale",  Category.NETWORK,  Priority.HIGH,     USER2));
        STORE.put(7, new Ticket(7, "Rete WiFi lenta",                    "Throughput inferiore a 1 Mbit/s in ufficio", Category.NETWORK,  Priority.MEDIUM,   USER2));

        // ticket singoli (3 ticket) — sara
        STORE.put(8,  new Ticket(8,  "Stampante non rilevata",           "Driver stampante da reinstallare",           Category.HARDWARE, Priority.MEDIUM,   USER2));
        STORE.put(9,  new Ticket(9,  "Aggiornamento Windows bloccato",   "Aggiornamento bloccato al 30% da 48 ore",    Category.SOFTWARE, Priority.LOW,      USER2));
        STORE.put(10, new Ticket(10, "Accesso negato alla cartella condivisa", "Permessi NTFS da ripristinare",         Category.SOFTWARE, Priority.HIGH,     USER2));
    }

    @Override
    public void insert(Ticket ticket) throws DAOException {
        if (ticket == null) {
            throw new DAOException("Ticket non può essere null");
        }
        STORE.put(nextId.getAndIncrement(), ticket);
    }

    @Override
    public Ticket findById(int id) throws DAOException, TicketNotFoundException {
        Ticket t = STORE.get(id);
        if (t == null) {
            throw new TicketNotFoundException("Ticket non trovato con id: " + id);
        }
        return t;
    }

    @Override
    public List<Ticket> findAll() throws DAOException {
        return new ArrayList<>(STORE.values());
    }

    @Override
    public List<Ticket> findByUserEmail(String email) throws DAOException {
        List<Ticket> result = new ArrayList<>();
        for (Ticket t : STORE.values()) {
            if (email.equals(t.getAuthorEmail())) result.add(t);
        }
        return result;
    }

    @Override
    public void update(Ticket ticket) throws DAOException, TicketNotFoundException {
        if (ticket == null) {
            throw new DAOException("Ticket non può essere null");
        }
        if (!STORE.containsKey(ticket.getId())) {
            throw new TicketNotFoundException("Ticket non trovato con id: " + ticket.getId());
        }
        STORE.put(ticket.getId(), ticket);
    }

    @Override
    public void delete(int id) throws DAOException {
        STORE.remove(id);
    }
}
