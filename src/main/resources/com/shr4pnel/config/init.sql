DROP TABLE ProductTable;
CREATE TABLE ProductTable
(
    productNo   CHAR(4),
    description VARCHAR(40),
    picture     VARCHAR(80),
    price       FLOAT
);

INSERT INTO ProductTable
VALUES ('0001', '40 inch LED HD TV', 'images/pic0001.jpg', 269.00);
INSERT INTO ProductTable
VALUES ('0002', 'DAB Radio', 'images/pic0002.jpg', 29.99);
INSERT INTO ProductTable
VALUES ('0003', 'Toaster', 'images/pic0003.jpg', 19.99);
INSERT INTO ProductTable
VALUES ('0004', 'Watch', 'images/pic0004.jpg', 29.99);
INSERT INTO ProductTable
VALUES ('0005', 'Digital Camera', 'images/pic0005.jpg', 89.99);
INSERT INTO ProductTable
VALUES ('0006', 'MP3 player', 'images/pic0006.jpg', 7.99);
INSERT INTO ProductTable
VALUES ('0007', '32Gb USB2 drive', 'images/pic0007.jpg', 6.99);

DROP TABLE StockTable;
CREATE TABLE StockTable
(
    productNo  CHAR(4),
    stockLevel INTEGER
);

INSERT INTO StockTable
VALUES ('0001', 90);
INSERT INTO StockTable
VALUES ('0002', 20);
INSERT INTO StockTable
VALUES ('0003', 33);
INSERT INTO StockTable
VALUES ('0004', 10);
INSERT INTO StockTable
VALUES ('0005', 17);
INSERT INTO StockTable
VALUES ('0006', 15);
INSERT INTO StockTable
VALUES ('0007', 1);

SELECT *
FROM StockTable
         INNER JOIN ProductTable
                    ON StockTable.productNo = ProductTable.productNo;
