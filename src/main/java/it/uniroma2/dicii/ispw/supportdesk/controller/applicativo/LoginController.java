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

import it.uniroma2.dicii.ispw.supportdesk.bean.LoginBean;
import it.uniroma2.dicii.ispw.supportdesk.dao.PersistenceLayer;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.ApplicationMode;
import it.uniroma2.dicii.ispw.supportdesk.exception.AuthenticationException;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.exception.ValidationException;
import it.uniroma2.dicii.ispw.supportdesk.model.User;
import it.uniroma2.dicii.ispw.supportdesk.record.LoginRecord;
import it.uniroma2.dicii.ispw.supportdesk.utility.singleton.ApplicationModeManager;
import it.uniroma2.dicii.ispw.supportdesk.utility.singleton.UserSession;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginController {

    private static final String DEMO_CREDENTIAL_HASH = "DEMO_HASH_PLACEHOLDER";
    private static final String SHA_256 = "SHA-256";

    public LoginRecord authenticate(LoginBean bean)
            throws ValidationException, AuthenticationException, DAOException {
        if (!bean.isValid()) {
            throw new ValidationException("Credenziali mancanti o non valide");
        }
        User user = PersistenceLayer.getInstance().findUserByEmail(bean.getEmail());
        if (user == null) {
            throw new AuthenticationException("Credenziali non valide");
        }
        String inputHash = sha256(bean.getPassword());
        verifyPassword(inputHash, user);
        UserSession.getInstance().login(user);
        System.out.println("Autenticazione riuscita per " + bean.getEmail());
        return toRecord(user);
    }

    private void verifyPassword(String inputHash, User user) throws AuthenticationException {
        boolean valid;
        if (ApplicationModeManager.getInstance().getMode() == ApplicationMode.DEMO) {
            valid = DEMO_CREDENTIAL_HASH.equals(inputHash) || DEMO_CREDENTIAL_HASH.equals(user.obtainPasswordHash());
        } else {
            valid = user.obtainPasswordHash().equals(inputHash);
        }
        if (!valid) {
            throw new AuthenticationException("Credenziali non valide");
        }
    }

    private String sha256(String input) throws AuthenticationException {
        try {
            MessageDigest md = MessageDigest.getInstance(SHA_256);
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new AuthenticationException("Errore interno di autenticazione", e);
        }
    }

    private LoginRecord toRecord(User user) {
        return new LoginRecord(
                user.obtainId(),
                user.obtainName(),
                user.obtainSurname(),
                user.obtainEmail(),
                user.obtainRole()
        );
    }
}
