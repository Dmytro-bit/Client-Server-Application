DROP DATABASE IF EXISTS `restaurant_simulator`;
CREATE DATABASE `restaurant_simulator`;
USE `restaurant_simulator`;

DROP TABLE IF EXISTS `Booking`, `Customer`, `RestaurantTable`;

CREATE TABLE `RestaurantTable`
(
    id       INT AUTO_INCREMENT PRIMARY KEY,
    capacity INT NOT NULL
);

CREATE TABLE `Customer`
(
    id    INT AUTO_INCREMENT PRIMARY KEY,
    name  VARCHAR(255) NOT NULL,
    phone VARCHAR(20)  NOT NULL,
    email VARCHAR(255) NOT NULL
);

CREATE TABLE `Booking`
(
    id           INT AUTO_INCREMENT PRIMARY KEY,
    customer_id  INT,
    table_id     INT,
    booking_date DATE                                       NOT NULL,
    start_time   TIME                                       NOT NULL,
    end_time     TIME                                       NOT NULL,
    status       ENUM ('PENDING', 'CONFIRMED', 'CANCELLED') NOT NULL DEFAULT 'PENDING',
    CONSTRAINT fk_customer
        FOREIGN KEY (customer_id)
            REFERENCES `Customer` (id)
            ON DELETE SET NULL,
    CONSTRAINT fk_table
        FOREIGN KEY (table_id)
            REFERENCES `RestaurantTable` (id)
            ON DELETE CASCADE
);

INSERT INTO `RestaurantTable` (capacity)
VALUES (2),
       (4),
       (6),
       (4),
       (8),
       (2),
       (10),
       (6),
       (4),
       (8);

INSERT INTO `Customer` (name, phone, email)
VALUES ('Alice Johnson', '+54343532534', 'alice@example.com'),
       ('Bob Smith', '+543436544334', 'bob@example.com'),
       ('Charlie Davis', '+543433325334', 'charlie@example.com'),
       ('David Wilson', '+23443532534', 'david@example.com'),
       ('Emily Brown', '+54343532534', 'emily@example.com'),
       ('Frank Miller', '+54343532534', 'frank@example.com'),
       ('Grace Adams', '+54343532534', 'grace@example.com'),
       ('Hannah White', '+54343532534', 'hannah@example.com'),
       ('Ian Thomas', '+54343532534', 'ian@example.com'),
       ('Jessica Taylor', '+54343532534', 'jessica@example.com');

INSERT INTO `Booking` (customer_id, table_id, booking_date, start_time, end_time, status)
VALUES (1, 1, '2025-03-10', '19:00:00', '21:00:00', 'PENDING'),
       (2, 2, '2025-03-11', '20:00:00', '22:00:00', 'CONFIRMED'),
       (3, 3, '2025-03-12', '18:30:00', '20:30:00', 'CANCELLED'),
       (4, 4, '2025-03-13', '19:00:00', '21:00:00', 'PENDING'),
       (5, 5, '2025-03-14', '20:00:00', '22:00:00', 'CONFIRMED'),
       (6, 6, '2025-03-15', '18:00:00', '20:00:00', 'PENDING'),
       (7, 7, '2025-03-16', '19:30:00', '21:30:00', 'CONFIRMED'),
       (8, 8, '2025-03-17', '20:15:00', '22:15:00', 'CANCELLED'),
       (9, 9, '2025-03-18', '18:45:00', '20:45:00', 'PENDING'),
       (10, 10, '2025-03-19', '19:10:00', '21:10:00', 'CONFIRMED');