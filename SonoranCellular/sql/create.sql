CREATE TABLE Account(
	AccountNumber NUMBER(10),
	Name VARCHAR(40) NOT NULL,
	PRIMARY KEY (AccountNumber)
	);

CREATE TABLE Phone(
	IMEI NUMBER(10),
	MobileNumber VARCHAR(14),
	Manufacturer VARCHAR(40) NOT NULL,
	Model VARCHAR(40) NOT NULL,
	PRIMARY KEY (IMEI)
	);

CREATE TABLE Plan(
	PlanName VARCHAR(40),
	PricePerMonth NUMERIC(10, 2) NOT NULL,
	AllowedDataUsage NUMBER(10) NOT NULL,
	Type VARCHAR(40) NOT NULL,
	PRIMARY KEY (PlanName)
	);

CREATE TABLE Subscribe(
	IMEI NUMBER(10),
	AccountNumber NUMBER(10) NOT NULL,
	PlanName VARCHAR(40),
	PRIMARY KEY (IMEI),
	FOREIGN KEY (IMEI) REFERENCES Phone,
	FOREIGN KEY (PlanName) REFERENCES Plan,
	FOREIGN KEY (AccountNumber) REFERENCES Account
	);

CREATE TABLE Owns(
	MasterAccountNumber NUMBER(10),
	DependentAccountNumber NUMBER(10),
	PRIMARY KEY (DependentAccountNumber),
	FOREIGN KEY (DependentAccountNumber) REFERENCES Account,
	FOREIGN KEY (MasterAccountNumber) REFERENCES Account
);

CREATE TABLE Bill(
	AccountNumber NUMBER(10),
	EndDate DATE,
	StartDate DATE,
	DueDate DATE,
	PRIMARY KEY (AccountNumber, EndDate),
	FOREIGN KEY (AccountNumber) REFERENCES Account
);

CREATE TABLE Item(
	AccountNumber NUMBER(10),
	EndDate DATE,
	ItemNumber NUMBER(10),
	Amount NUMERIC(10,2),
	PRIMARY KEY (AccountNumber, EndDate, ItemNumber),
	FOREIGN KEY (AccountNumber, EndDate) REFERENCES Bill
);
