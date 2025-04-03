CREATE TABLE IF NOT EXISTS users (
                                     id SERIAL PRIMARY KEY,
                                     name VARCHAR(255) NOT NULL,
    surname VARCHAR(255) NOT NULL,
    username VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    password_salt VARCHAR(255),
    password_hash VARCHAR(255),
    telephone_number VARCHAR(50),
    gender VARCHAR(10),
    registration_date TIMESTAMP,
    active BOOLEAN NOT NULL
    );

INSERT INTO users (id, name, surname, username, email, password_hash, password_salt, telephone_number, gender, active) VALUES
                                                                                                                           (1, 'Admin', 'Admin', 'admin', 'admin@edu.fit.ba', 'UbzzxOGag4pPmBhguTkyKnpEZw4=', 'qYk4OxryQgplthbzFlS0yQ==', '061-111-111', 'Male', true),
                                                                                                                           (2, 'Admin2', 'Admin2', 'admin2', 'admin2@edu.fit.ba', 'UbzzxOGag4pPmBhguTkyKnpEZw4=', 'qYk4OxryQgplthbzFlS0yQ==', '061-111-112', 'Male', true),
                                                                                                                           (3, 'Client1', 'Client1', 'client1', 'client1@edu.fit.ba', 'UbzzxOGag4pPmBhguTkyKnpEZw4=', 'qYk4OxryQgplthbzFlS0yQ==', '061-111-113', 'Male', true),
                                                                                                                           (4, 'Client2', 'Client2', 'client2', 'client2@edu.fit.ba', 'UbzzxOGag4pPmBhguTkyKnpEZw4=', 'qYk4OxryQgplthbzFlS0yQ==', '061-111-114', 'Female', true),
                                                                                                                           (5, 'Driver1', 'Driver1', 'driver1', 'driver1@edu.fit.ba', 'UbzzxOGag4pPmBhguTkyKnpEZw4=', 'qYk4OxryQgplthbzFlS0yQ==', '061-111-117', 'Male', true),
                                                                                                                           (6, 'Driver2', 'Driver2', 'driver2', 'driver2@edu.fit.ba', 'UbzzxOGag4pPmBhguTkyKnpEZw4=', 'qYk4OxryQgplthbzFlS0yQ==', '061-111-212', 'Male', true);
INSERT INTO admin (id, user_id) VALUES
                                    (1, 1),
                                    (2, 2);

INSERT INTO client (id, user_id) VALUES
                                     (1, 3),
                                     (2, 4);
INSERT INTO driver (id, user_id,number_of_hours_amount,number_of_clients_amount) VALUES
                                                                                     (1, 5,11,23),
                                                                                     (2, 6,15,29);
INSERT INTO vehicle (id, available, name, average_fuel_consumption, price) VALUES
                                                                               (1, true, 'Mercedes C180 2011', 6.8, 51.0),
                                                                               (2, true, 'Mercedes GLA 200d', 8.5, 55.0),
                                                                               (3, true, 'Peugeot 3007', 6.75, 45.0),
                                                                               (4, true, 'Range Rover', 10.0, 60.0),
                                                                               (5, true, 'Golf 5', 5.0, 45.0);
INSERT INTO company_price (id, price_per_kilometer, adding_date) VALUES
                                                                     (1, 3.01, NOW() - INTERVAL '2 months'),
                                                                     (2, 3.045, NOW() - INTERVAL '1 month'),
                                                                     (3, 3.06, NOW());
INSERT INTO route (
    id, source_point_lat, source_point_lon, destination_point_lat, destination_point_lon,duration,paid,
    status, client_id, company_price_id, driver_id, number_of_kilometers, full_price
) VALUES
      (1, 38.592187, -121.782024, 37.910617, -121.793017,0,false, 'wait', 1, 1, 1, 75.787, 3.01 * 75.787),
      (2, 38.523458, -121.463208, 37.519529, -120.935512,0,false, 'wait', 2, 2, 1, 120.823, 3.045 * 120.823),
      (3, 37.893279, -121.221347, 37.641422, -122.375726,0,false, 'wait', 2, 3, 2, 105.263, 3.06 * 105.263),
      (4, 38.557831, -121.485239, 36.600094, -119.745856,0,false ,'wait', 1, 3, 2, 266.227, 3.06 * 266.227),
      (5, 37.321893, -121.913084, 37.344823, -122.051879, 0,false,'wait', 1, 3, 1, 12.533, 3.06 * 12.533);
INSERT INTO rent (
    id, rent_date, end_date,paid, number_of_days, vehicle_id, client_id, full_price, status
) VALUES
      (1, NOW() + INTERVAL '10 days', NOW() + INTERVAL '13 days',false, 3, 1, 1, 120, 'wait'),
      (2, NOW() + INTERVAL '13 days', NOW() + INTERVAL '23 days',false ,10, 2, 1, 550, 'wait'),
      (3, NOW() + INTERVAL '10 days', NOW() + INTERVAL '15 days',false, 5, 5, 2, 225, 'wait'),
      (4, NOW() + INTERVAL '3 days', NOW() + INTERVAL '7 days', false,4, 4, 2, 196, 'wait'),
      (5, NOW() - INTERVAL '3 days', NOW() + INTERVAL '7 days', false,10, 1, 2, 400, 'active'),
      (6, NOW() - INTERVAL '2 days', NOW() + INTERVAL '4 days', false,6, 3, 1, 306, 'active'),
      (7, NOW() - INTERVAL '20 days', NOW() - INTERVAL '11 days', false,9, 2, 1, 495, 'finished');

INSERT INTO review (
    id, value, description, adding_date, reviews_id,reviewed_id, route_id
) VALUES
      (1, 5, 'This is a test review', NOW() - INTERVAL '2 days', 1, 1, 5),
      (2, 4, 'This is a test review 2', NOW() - INTERVAL '5 days', 2, 2, 3),
      (3, 3, 'This is a test review 3', NOW() - INTERVAL '1 day', 1, 1, 1);

INSERT INTO notification (
    id, adding_date, title, content, for_client
) VALUES
      (1, NOW() - INTERVAL '2 days', 'Welcome to eCar application.',
       'We would like to inform you that the eCar application is in preparation. We hope it will be operational as soon as possible. Currently, we are working on the admin part of the application.Your eCar Team',
       false),
      (2, NOW() - INTERVAL '5 days', 'Application in preparation.',
       'Dear valued clients and eCar application users, our development team is working on establishing the application, and we hope you will soon be able to enjoy the benefits of the eCar app.\nYour eCar Team',
       true);
SELECT setval('users_id_seq', (SELECT MAX(id) FROM users) + 1);

SELECT setval('admin_id_seq', (SELECT MAX(id) FROM admin) + 1);

SELECT setval('client_id_seq', (SELECT MAX(id) FROM client) + 1);

SELECT setval('driver_id_seq', (SELECT MAX(id) FROM driver) + 1);

SELECT setval('vehicle_id_seq', (SELECT MAX(id) FROM vehicle) + 1);

SELECT setval('company_price_id_seq', (SELECT MAX(id) FROM company_price) + 1);

SELECT setval('route_id_seq', (SELECT MAX(id) FROM route) + 1);

SELECT setval('rent_id_seq', (SELECT MAX(id) FROM rent) + 1);

SELECT setval('review_id_seq', (SELECT MAX(id) FROM review) + 1);

SELECT setval('notification_id_seq', (SELECT MAX(id) FROM notification) + 1);
