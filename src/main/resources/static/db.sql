create database property_management;
use property_management;

CREATE TABLE user (
    id INT NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    email VARCHAR(200) NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    role VARCHAR(20) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (email)
);

update property set address='47, Ocean Drive' where id=2;

CREATE TABLE property (
    id INT NOT NULL AUTO_INCREMENT,
    owner_id INT NOT NULL,
    name varchar(255) not null,
    address VARCHAR(255) NOT NULL,
    state VARCHAR(100) NOT NULL,
    city VARCHAR(100) NOT NULL,
    pincode VARCHAR(6) NOT NULL,
    num_units INT NOT NULL,
    type VARCHAR(100) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (owner_id)
        REFERENCES user (id)
);

CREATE TABLE images (
    id INT NOT NULL AUTO_INCREMENT,
    property_id INT NOT NULL,
    url VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (property_id)
        REFERENCES property (id)
);

CREATE TABLE unit (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    property_id INT NOT NULL,
    availability VARCHAR(200),
    floor INT,
    square_footage FLOAT,
    bedrooms INT,
    bathrooms INT,
    sold_to INT,
    description TEXT,
    PRIMARY KEY (id),
    FOREIGN KEY (property_id)
        REFERENCES property (id),
    FOREIGN KEY (sold_to)
        REFERENCES user (id)
);

CREATE TABLE unit_availability (
    id INT NOT NULL AUTO_INCREMENT,
    unit_id INT NOT NULL,
    availability_type VARCHAR(200),
    amount INT NOT NULL,
    security_deposit INT,
    monthly_due INT,
    no_of_months INT,
    PRIMARY KEY (id),
    FOREIGN KEY (unit_id)
        REFERENCES unit (id),
    UNIQUE (unit_id , availability_type)
);

CREATE TABLE request (
    id INT NOT NULL AUTO_INCREMENT,
    unit_id INT NOT NULL,
    user_id INT NOT NULL,
    type VARCHAR(200) NOT NULL,
    message TEXT,
    status VARCHAR(100) DEFAULT 'PENDING',
    request_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    amount INT NOT NULL,
    security_deposit INT,
    monthly_due INT,
    no_of_months INT,
    PRIMARY KEY (id),
    FOREIGN KEY (unit_id)
        REFERENCES unit (id),
    FOREIGN KEY (user_id)
        REFERENCES user (id)
);

CREATE TABLE expense (
    id INT NOT NULL AUTO_INCREMENT,
    unit_id INT NOT NULL,
    amount INT NOT NULL,
    type VARCHAR(100),
    description TEXT,
    expense_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (unit_id)
        REFERENCES unit (id)
);

CREATE TABLE agreement (
    id INT NOT NULL AUTO_INCREMENT,
    request_id INT NOT NULL,
    start_date date,
    end_date date,
    number_of_years int,
    PRIMARY KEY (id),
    FOREIGN KEY (request_id)
        REFERENCES request(id)
);

CREATE TABLE transaction (
    id INT NOT NULL AUTO_INCREMENT,
    request_id INT NOT NULL,
    transaction_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(255) NOT NULL,
    payment_method VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (request_id)
        REFERENCES request (id)
);

CREATE TABLE notification (
    id INT NOT NULL AUTO_INCREMENT,
    agreement_id INT NOT NULL,
    notification_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (agreement_id)
        REFERENCES agreement (id)
);

select * from transaction t join request r on t.request_id=r.id join user u on u.id=r.user_id;
select * from unit u join property p on p.id=u.property_id join user on p.owner_id=user.id;

select u.id, t.transaction_time from transaction t join request r on r.id=t.request_id join agreement a on a.request_id=r.id join user u on r.user_id=u.id where a.end_date is null and u.id=4 order by t.transaction_time desc limit 1;

-- Insert and Select Queries

create database property_management;
use property_management;

CREATE TABLE user (
    id INT NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    email VARCHAR(200) NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE (email)
);


CREATE TABLE user_role (
    id INT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    role VARCHAR(100) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id)
        REFERENCES user (id),
    UNIQUE (user_id , role)
);

alter table user add column role varchar(20);

drop table user_role;

insert into user(first_name, last_name, phone_number, email, password) values("Dharshini", "S", 6345672345, "dharshini@gmail.com", "hello@123");

SELECT
    *
FROM
    property;
DELETE FROM property
WHERE
    id = 5;
drop database property_management;

alter table user_role add constraint unique(user_id, role);

insert into user_role(user_id, role) values(1, "Tenant");

SELECT
    *
FROM
    user_role;

drop table property;

CREATE TABLE property (
    id INT NOT NULL AUTO_INCREMENT,
    owner_id INT NOT NULL,
    address VARCHAR(255) NOT NULL,
    state VARCHAR(100) NOT NULL,
    city VARCHAR(100) NOT NULL,
    pincode VARCHAR(6) NOT NULL,
    num_units INT NOT NULL,
    type VARCHAR(100) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (owner_id)
        REFERENCES user (id)
);

alter table unit add column name varchar(255);

insert into property(owner_id, address, city, state, pincode, num_units, type) values(1, "No. 48, Gandhi Street", "Bangalore", "Karnataka", "600089", 8, "Apartment");

SELECT
    *
FROM
    property p
        JOIN
    user u ON u.id = p.owner_id
WHERE
    email = 'dharshini@gmail.com';

-- Listing properties
-- select * from property p
-- join user_role r on p.owner_id=r.id
-- join user u on u.id=r.user_id
-- where u.email='gsangeetha@gmail.com';

CREATE TABLE images (
    id INT NOT NULL AUTO_INCREMENT,
    property_id INT NOT NULL,
    url VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (property_id)
        REFERENCES property (id)
);

drop table unit;

CREATE TABLE unit (
    id INT NOT NULL AUTO_INCREMENT,
    property_id INT NOT NULL,
    availability VARCHAR(200),
    floor INT,
    square_footage FLOAT,
    bedrooms INT,
    bathrooms INT,
    description TEXT,
    PRIMARY KEY (id),
    FOREIGN KEY (property_id)
        REFERENCES property (id)
);

drop table unit_availability;

CREATE TABLE unit_availability (
    id INT NOT NULL AUTO_INCREMENT,
    unit_id INT NOT NULL,
    availability_type VARCHAR(200),
    amount INT NOT NULL,
    security_deposit INT,
    PRIMARY KEY (id),
    FOREIGN KEY (unit_id)
        REFERENCES unit (id)
);

drop table request;

delete from user where id=10;

select * from user;

CREATE TABLE request (
    id INT NOT NULL AUTO_INCREMENT,
    unit_id INT NOT NULL,
    user_id INT NOT NULL,
    type VARCHAR(200) NOT NULL,
    message TEXT,
    status VARCHAR(100) DEFAULT 'PENDING',
    request_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (unit_id)
        REFERENCES unit (id),
    FOREIGN KEY (user_id)
        REFERENCES user (id)
);

alter table request modify column status varchar(100) default 'PENDING';

CREATE TABLE expense (
    id INT NOT NULL AUTO_INCREMENT,
    unit_id INT NOT NULL,
    amount INT NOT NULL,
    type VARCHAR(100),
    description TEXT,
    expense_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (unit_id)
        REFERENCES unit (id)
);

delete from request where id=1;

-- rename table unit_type to unit_availability;

UPDATE user
SET
    email = 'madhulika@gmail.com'
WHERE
    email = 'madhulikan@gmail.com';

CREATE TABLE agreement (
    id INT NOT NULL AUTO_INCREMENT,
    unit_avail_id INT NOT NULL,
    tenant_id INT NOT NULL,
    document_url VARCHAR(255) NOT NULL,
    monthly_due_date INT,
    PRIMARY KEY (id),
    FOREIGN KEY (unit_avail_id)
        REFERENCES unit_availability (id)
);

CREATE TABLE agreement_line_items (
    id INT NOT NULL AUTO_INCREMENT,
    agreement_id INT NOT NULL,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    amount INT NOT NULL,
    deposit INT,
    PRIMARY KEY (id),
    FOREIGN KEY (agreement_id)
        REFERENCES agreement (id)
);

CREATE TABLE transaction (
    id INT NOT NULL AUTO_INCREMENT,
    agreement_line_id INT NOT NULL,
    transaction_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(255) NOT NULL,
    payment_method VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (agreement_line_id)
        REFERENCES agreement_line_items (id)
);

alter table agreement add constraint foreign key(tenant_id) references user(id);

drop database property_management;

select * from agreement;

update request set status='EXPIRED' where id=2;


select * from unit;
delete from property where id=2;
--
select * from user;
update unit set description="A cozy retreat bathed in starlight, offering a peaceful escape from the hustle and bustle of everyday life." where id>=4 and id<=5;
update agreement set end_date="2024-08-09" where id=2;
select * from unit;
update unit set availability='AVAILABLE' where id=1;
select * from agreement;
select * from agreement a join request r ON r.id = a.request_id WHERE r.user_id = 2 AND r.type <> 'BUY' AND a.end_date IS NULL ORDER BY a.start_date DESC LIMIT 1;

alter table request add column validity boolean default true;
delete from agreement;
delete from request;

select * from agreement join request on agreement.request_id=request.id join unit on unit.id=request.unit_id join property on unit.property_id=property.id;
update unit set availability="AVAILABLE" where id=1;
update agreement set start_date="2019-07-30" where request_id=1;