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
package it.uniroma2.dicii.ispw.supportdesk.enumerator;

/**
 * Priorità di un ticket con SLA massimo incorporato.
 * Le ore SLA sono parte del dominio: appartengono qui, non in un file di config.
 */
public enum Priority {

    LOW(72),
    MEDIUM(24),
    HIGH(8),
    CRITICAL(4);

    private final int maxSlaHours;

    Priority(int maxSlaHours) {
        this.maxSlaHours = maxSlaHours;
    }

    /**
     * Ore massime entro cui il ticket deve essere risolto.
     */
    public int getMaxSlaHours() {
        return maxSlaHours;
    }
}
