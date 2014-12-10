CREATE TABLE Account(
	AccountNumber NUMBER(10) NOT NULL,
	Name VARCHAR(40) NOT NULL,
	PRIMARY KEY (AccountNumber)
	);

CREATE TABLE Phone(
	IMEI NUMBER(10) NOT NULL,
	MobileNumber VARCHAR(14) NOT NULL,
	Manufacturer VARCHAR(40) NOT NULL,
	Model VARCHAR(40) NOT NULL,
	PRIMARY KEY (IMEI)
	);

CREATE TABLE Plan(
	PlanName VARCHAR(40) NOT NULL,
	PricePerMonth NUMERIC(8, 2) NOT NULL,
	AllowedDataUsage NUMBER(8) NOT NULL,
	Type VARCHAR(40) NOT NULL,
	PRIMARY KEY (PlanName)
	);

CREATE TABLE Subscribe(
	IMEI NUMBER(8),
	AccountNumber NUMBER(8) NOT NULL,
	PlanName VARCHAR(40) NOT NULL,
	PRIMARY KEY (IMEI),
	FOREIGN KEY (IMEI) REFERENCES Phone,
	FOREIGN KEY (PlanName) REFERENCES Plan,
	FOREIGN KEY (AccountNumber) REFERENCES Account
	);

CREATE TABLE Owns(
	MasterAccountNumber NUMBER(8) NOT NULL,
	DependentAccountNumber NUMBER(8) NOT NULL,
	PRIMARY KEY (DependentAccountNumber),
	FOREIGN KEY (DependentAccountNumber) REFERENCES Account,
	FOREIGN KEY (MasterAccountNumber) REFERENCES Account
);

CREATE TABLE Bill(
	AccountNumber NUMBER(8) NOT NULL,
	EndDate DATE,
	StartDate DATE,
	DueDate DATE,
	PRIMARY KEY (AccountNumber, EndDate),
	FOREIGN KEY (AccountNumber) REFERENCES Account
);

CREATE TABLE Item(
	AccountNumber NUMBER(8) NOT NULL,
	EndDate DATE,
	ItemNumber NUMBER(8) NOT NULL,
	Amount NUMERIC(8,2) NOT NULL,
	PRIMARY KEY (AccountNumber, EndDate, ItemNumber),
	FOREIGN KEY (AccountNumber, EndDate) REFERENCES Bill
);
