DELETE FROM user_roles;
DELETE FROM meals;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
  ('User', 'user@yandex.ru', 'password'),
  ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO meals (description, calories, dateTime, user_id) VALUES
  ('Завтрак',1000, TIMESTAMP '2018-05-16 10:36:38', 100000),
  ('Обед', 750, TIMESTAMP '2018-05-16 15:36:38', 100000),
  ('Ужин', 150, TIMESTAMP '2018-05-16 20:36:38', 100000),
  ('Завтрак',1000, TIMESTAMP '2018-05-17 10:30:00', 100000),
  ('Обед', 750, TIMESTAMP '2018-05-17 15:30:00', 100000),
  ('Ужин', 500, TIMESTAMP '2018-05-17 20:30:00', 100000),
  ('Завтрак1',1000, TIMESTAMP '2018-06-16 10:36:38', 100001),
  ('Обед1', 750, TIMESTAMP '2018-06-16 15:36:38', 100001),
  ('Ужин1', 150, TIMESTAMP '2018-06-16 20:36:38', 100001),
  ('Завтрак1',1000, TIMESTAMP '2018-06-17 10:30:00', 100001),
  ('Обед1', 750, TIMESTAMP '2018-06-17 15:30:00', 100001),
  ('Ужин1', 500, TIMESTAMP '2018-06-17 20:30:00', 100001);

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);
