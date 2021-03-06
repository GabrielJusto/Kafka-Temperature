CREATE TABLE client
(
    clientId BIGINT PRIMARY KEY,
    pointId INTEGER,
    connector VARCHAR(255),
    maxprice FLOAT,
    walletKey VARCHAR(255),
    timeToCharge BIGINT,
    charging BIT
);


CREATE TABLE point
(
    pointId BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    lat FLOAT,
    lon FLOAT
);

CREATE TABLE station
(
    stationId BIGINT PRIMARY KEY,
    pointId INTEGER,
    connector VARCHAR(255),
    fastCharge BIT,
    brand VARCHAR(255),
    busy BIT,
    price FLOAT,
    busyTime BIGINT,
    timeToCharge BIGINT,
    temperature FLOAT
);