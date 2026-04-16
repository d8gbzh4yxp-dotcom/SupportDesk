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
package it.uniroma2.dicii.ispw.supportdesk.dao.file;

import java.io.File;

final class FilePathResolver {

    private static final String DATA_DIR_PROP = "supportdesk.data.dir";
    private static final String DEFAULT_DIR   = "data";

    private FilePathResolver() {}

    static String resolve(String filename) {
        String dir = System.getProperty(DATA_DIR_PROP, DEFAULT_DIR);
        File dataDir = new File(dir);
        if (!dataDir.exists()) dataDir.mkdirs();
        return dir + File.separator + filename;
    }
}
