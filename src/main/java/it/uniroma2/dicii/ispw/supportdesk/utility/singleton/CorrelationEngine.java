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
package it.uniroma2.dicii.ispw.supportdesk.utility.singleton;

import it.uniroma2.dicii.ispw.supportdesk.model.Ticket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public final class CorrelationEngine {

    private static final Logger log = LoggerFactory.getLogger(CorrelationEngine.class);
    private static final double DEFAULT_THRESHOLD = 0.30;

    private CorrelationEngine() {}

    private static final class Holder {
        private static final CorrelationEngine INSTANCE = new CorrelationEngine();
    }

    public static CorrelationEngine getInstance() {
        return Holder.INSTANCE;
    }

    public List<Ticket> findCorrelatedTickets(Ticket target, List<Ticket> candidates) {
        List<String> docs = prepareDocuments(target, candidates);
        Set<String> vocab = buildVocabulary(docs);
        Map<Integer, double[]> vecs = calculateTFIDFVectors(docs, vocab);
        return filterByThreshold(candidates, vecs);
    }

    private List<String> prepareDocuments(Ticket target, List<Ticket> candidates) {
        List<String> docs = new ArrayList<>();
        docs.add(normalize(target.getTitle() + " " + target.getDescription()));
        for (Ticket c : candidates) {
            docs.add(normalize(c.getTitle() + " " + c.getDescription()));
        }
        return docs;
    }

    private Set<String> buildVocabulary(List<String> docs) {
        Set<String> vocab = new LinkedHashSet<>();
        for (String doc : docs) {
            Collections.addAll(vocab, doc.split("\\s+"));
        }
        return vocab;
    }

    private Map<Integer, double[]> calculateTFIDFVectors(List<String> docs, Set<String> vocab) {
        String[] terms = vocab.toArray(new String[0]);
        Map<Integer, double[]> vecs = new LinkedHashMap<>();
        for (int i = 0; i < docs.size(); i++) {
            vecs.put(i, tfidfVector(docs.get(i), docs, terms));
        }
        return vecs;
    }

    private double[] tfidfVector(String doc, List<String> allDocs, String[] terms) {
        String[] words = doc.split("\\s+");
        double[] vec = new double[terms.length];
        for (int j = 0; j < terms.length; j++) {
            final int idx = j;
            long tf = Arrays.stream(words).filter(w -> w.equals(terms[idx])).count();
            long df = allDocs.stream().filter(d -> d.contains(terms[idx])).count();
            vec[j] = (tf > 0 && df > 0) ? ((double) tf / words.length) * Math.log((double) allDocs.size() / df) : 0;
        }
        return vec;
    }

    private List<Ticket> filterByThreshold(List<Ticket> candidates, Map<Integer, double[]> vecs) {
        double[] targetVec = vecs.get(0);
        List<Ticket> correlated = new ArrayList<>();
        for (int i = 0; i < candidates.size(); i++) {
            double sim = cosine(targetVec, vecs.get(i + 1));
            if (sim >= DEFAULT_THRESHOLD) {
                log.debug("Ticket {} correlato con similarita {}", candidates.get(i).getId(), sim);
                correlated.add(candidates.get(i));
            }
        }
        return correlated;
    }

    private double cosine(double[] a, double[] b) {
        double dot = 0;
        double na = 0;
        double nb = 0;
        for (int i = 0; i < a.length; i++) {
            dot += a[i] * b[i];
            na  += a[i] * a[i];
            nb  += b[i] * b[i];
        }
        return (na == 0 || nb == 0) ? 0 : dot / (Math.sqrt(na) * Math.sqrt(nb));
    }

    private String normalize(String text) {
        return text.toLowerCase(Locale.ITALIAN).replaceAll("[^a-z\u00e0\u00e8\u00e9\u00ec\u00f2\u00f9\\s]", " ").trim();
    }
}
