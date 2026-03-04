INSERT INTO users (id, username, display_name, type, password, enabled)
VALUES
  (1,  ${ADMIN_USERNAME},               'Admin User',           ${ADMIN_PASSWORD_HASH}, '{bcrypt}$2a$10$aoMSISl9oPz9L8uYPccV0eALCEA2ruTlsGXFi.vaj0HgUS7AtHRRa', true),
  (2,  'regular-user-1',         'Regular User 1',     'USER',  '{bcrypt}$2a$10$aoMSISl9oPz9L8uYPccV0eALCEA2ruTlsGXFi.vaj0HgUS7AtHRRa', true),
  (3,  'regular-user-2',           'Regular User 2',            'USER',  '{bcrypt}$2a$10$aoMSISl9oPz9L8uYPccV0eALCEA2ruTlsGXFi.vaj0HgUS7AtHRRa', true),
  (4,  'regular-user-3',       'Regular User 3',        'USER',  '{bcrypt}$2a$10$aoMSISl9oPz9L8uYPccV0eALCEA2ruTlsGXFi.vaj0HgUS7AtHRRa', true),
  (5,  'regular-user-4',  'Regular User 4',   'USER',  '{bcrypt}$2a$10$aoMSISl9oPz9L8uYPccV0eALCEA2ruTlsGXFi.vaj0HgUS7AtHRRa', true),
  (6,  'disabled-user-1',          'Disabled User 1',           'USER',  '{bcrypt}$2a$10$aoMSISl9oPz9L8uYPccV0eALCEA2ruTlsGXFi.vaj0HgUS7AtHRRa', false)
;

SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));
