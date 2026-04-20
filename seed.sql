-- SupportDesk — utenti di test
-- Credenziali:
--   manager : admin@supportdesk.it  / admin123
--   tech 1  : marco@supportdesk.it  / tech123
--   tech 2  : laura@supportdesk.it  / tech123
--   user 1  : giovanni@azienda.it   / user123
--   user 2  : sara@azienda.it       / user123

INSERT INTO users (name, surname, email, credential_hash, role, specialization) VALUES
  ('Admin',    'Sistema', 'admin@supportdesk.it', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'MANAGER',    NULL),
  ('Marco',    'Bianchi', 'marco@supportdesk.it', '3ac40463b419a7de590185c7121f0bfbe411d6168699e8014f521b050b1d6653', 'TECHNICIAN', 'NETWORK'),
  ('Laura',    'Verdi',   'laura@supportdesk.it', '3ac40463b419a7de590185c7121f0bfbe411d6168699e8014f521b050b1d6653', 'TECHNICIAN', 'SOFTWARE'),
  ('Giovanni', 'Rossi',   'giovanni@azienda.it',  'e606e38b0d8c19b24cf0ee3808183162ea7cd63ff7912dbb22b5e803286b4446', 'USER',       NULL),
  ('Sara',     'Neri',    'sara@azienda.it',       'e606e38b0d8c19b24cf0ee3808183162ea7cd63ff7912dbb22b5e803286b4446', 'USER',       NULL);
