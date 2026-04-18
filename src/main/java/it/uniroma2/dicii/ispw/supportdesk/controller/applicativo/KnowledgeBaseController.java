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
import it.uniroma2.dicii.ispw.supportdesk.exception.KnowledgeBaseException;
import it.uniroma2.dicii.ispw.supportdesk.model.KnowledgeEntry;
import it.uniroma2.dicii.ispw.supportdesk.model.User;
import it.uniroma2.dicii.ispw.supportdesk.record.KnowledgeEntryRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class KnowledgeBaseController {

    private static final Logger log = LoggerFactory.getLogger(KnowledgeBaseController.class);

    private final List<KnowledgeEntry> entries = new CopyOnWriteArrayList<>();
    private final AtomicInteger idGen = new AtomicInteger(1);

    public KnowledgeEntryRecord addEntry(String title, String content, String authorEmail)
            throws KnowledgeBaseException, DAOException {
        if (title == null || title.isBlank() || content == null || content.isBlank()) {
            throw new KnowledgeBaseException("Titolo e contenuto sono obbligatori");
        }
        User author = PersistenceLayer.getInstance().findUserByEmail(authorEmail);
        if (author == null) {
            throw new KnowledgeBaseException("Autore non trovato: " + authorEmail);
        }
        KnowledgeEntry entry = new KnowledgeEntry(idGen.getAndIncrement(), title, content, author);
        entries.add(entry);
        log.info("Knowledge entry '{}' aggiunta da {}", title, authorEmail);
        return toRecord(entry);
    }

    public List<KnowledgeEntryRecord> searchEntries(String keyword) throws KnowledgeBaseException {
        if (keyword == null || keyword.isBlank()) {
            throw new KnowledgeBaseException("Keyword di ricerca non valida");
        }
        String kw = keyword.toLowerCase(Locale.ITALIAN);
        return entries.stream()
                .filter(e -> matchesKeyword(e, kw))
                .map(this::toRecord)
                .toList();
    }

    public List<KnowledgeEntryRecord> getAllEntries() {
        return entries.stream().map(this::toRecord).toList();
    }

    private boolean matchesKeyword(KnowledgeEntry e, String kw) {
        return e.getTitle().toLowerCase(Locale.ITALIAN).contains(kw)
                || e.getContent().toLowerCase(Locale.ITALIAN).contains(kw);
    }

    private KnowledgeEntryRecord toRecord(KnowledgeEntry e) {
        String authorName = e.getAuthor().obtainName() + " " + e.getAuthor().obtainSurname();
        return new KnowledgeEntryRecord(e.getId(), e.getTitle(), e.getContent(), authorName, e.getCreatedAt());
    }
}
