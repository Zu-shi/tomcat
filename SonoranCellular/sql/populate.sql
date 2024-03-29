INSERT INTO Phone VALUES(11678, '(520) 234-8582', 'Google', 'GalaxyS2');
INSERT INTO Phone VALUES(55012, '(912) 856-1111', 'Apple', 'IPhone');
INSERT INTO Phone VALUES(35451, '(223) 857-9045', 'Bender', 'Hooker');
INSERT INTO Phone VALUES(87621, '(234) 574-3437', 'BlastCo', 'Dynamite');
INSERT INTO Phone VALUES(11111, '(111) 111-1111', 'AAA', 'AAA');
INSERT INTO Phone VALUES(22222, '(222) 222-2222', 'AAA', 'AAA');
INSERT INTO Phone VALUES(12345, '(111) 555-0123', 'TheCompany', 'ShadeyPhone');
INSERT INTO Phone VALUES(12346, '(111) 555-0124', 'TheCompany', 'PenPhone');

INSERT INTO Plan VALUES('El Plano', 25.50, 500, 'Mobile');
INSERT INTO Plan VALUES('Plan 2', 34.45, 100, 'Hmm');
INSERT INTO Plan VALUES('The BIG One', 100.99, 10000, 'OhYeah');
INSERT INTO Plan VALUES('MiniMe', 5.99, 20, 'Tiny');
INSERT INTO Plan VALUES('AAA', 11, 1111, 'AAA');
INSERT INTO PLAN VALUES('BBB', 11, 1111, 'AAA');

INSERT INTO Account VALUES(1234, 'Mike');
INSERT INTO Account VALUES(6789, 'Jane');
INSERT INTO Account VALUES(1111, 'AAA');
INSERT INTO Account VALUES(11111, 'AAA');
INSERT INTO Account VALUES(54321, 'No Plan Man');
INSERT INTO Account VALUES(9999, 'CCC');

INSERT INTO Subscribe VALUES(11111, 1111, 'AAA');
INSERT INTO Subscribe VALUES(11678, 1111, 'BBB');
INSERT INTO Subscribe VALUES(55012, 1234, 'The BIG One');
INSERT INTO Subscribe VALUES(35451, 9999, 'BBB');
INSERT INTO Subscribe VALUES(87621, 6789, 'MiniMe');
INSERT INTO Subscribe VALUES(12345, 11111, 'Plan 2');
INSERT INTO Subscribe VALUES(12346, 11111, 'Plan 2');

INSERT INTO Bill VALUES(
	1111,
	TO_DATE(20141229, 'YYYYMMDD'),
	TO_DATE(20131229, 'YYYYMMDD'),
	TO_DATE(20111111, 'YYYYMMDD')
);

INSERT INTO Bill VALUES(
	6789,
	TO_DATE(20200101, 'YYYYMMDD'),
	TO_DATE(20000101, 'YYYYMMDD'), 
	TO_DATE(20000102, 'YYYYMMDD')
);

INSERT INTO Bill VALUES(
	1111,
	TO_DATE(20150101, 'YYYYMMDD'),
	TO_DATE(20141230, 'YYYYMMDD'),
	TO_DATE(20111111, 'YYYYMMDD')
);

INSERT INTO Bill VALUES(
	1111,
	TO_DATE(20150201, 'YYYYMMDD'),
	TO_DATE(20150102, 'YYYYMMDD'),
	TO_DATE(20111112, 'YYYYMMDD')
);

INSERT INTO Bill VALUES(
	1111,
	TO_DATE(20150301, 'YYYYMMDD'),
	TO_DATE(20150202, 'YYYYMMDD'),
	TO_DATE(20111111, 'YYYYMMDD')
);

INSERT INTO Bill VALUES(
	1111,
	TO_DATE(20150401, 'YYYYMMDD'),
	TO_DATE(20150302, 'YYYYMMDD'),
	TO_DATE(20111111, 'YYYYMMDD')
);

INSERT INTO Bill VALUES(
	1234,
	TO_DATE(30000201, 'YYYYMMDD'),
	TO_DATE(19800201, 'YYYYMMDD'),
	TO_DATE(40000201, 'YYYYMMDD')
);

INSERT INTO Bill VALUES(
	9999,
	TO_DATE(20150101, 'YYYYMMDD'),
	TO_DATE(20140101, 'YYYYMMDD'),
	TO_DATE(20140101, 'YYYYMMDD')
);

INSERT INTO Bill VALUES(
	1234,
	TO_DATE(20160101, 'YYYYMMDD'),
	TO_DATE(20140101, 'YYYYMMDD'),
	TO_DATE(20150101, 'YYYYMMDD')
);

INSERT INTO Owns VALUES(1234, 6789);
INSERT INTO Owns VALUES(1234, 1111);
INSERT INTO Owns VALUES(1234, 9999);

INSERT INTO Item VALUES(
	1234,
	TO_DATE(20160101, 'YYYYMMDD'),
	1,
	200.01
);

INSERT INTO Item VALUES(
	1234,
	TO_DATE(30000201, 'YYYYMMDD'),
	2,
	499.99
);

INSERT INTO Item VALUES(
	6789,
	TO_DATE(20200101, 'YYYYMMDD'),
	1,
	30.50
);

INSERT INTO Item VALUES(
	9999,
	TO_DATE(20150101, 'YYYYMMDD'),
	1,
	44.73
);

INSERT INTO Item VALUES(
	1111,
	TO_DATE(20141229, 'YYYYMMDD'),
	1,
	120.59
);

INSERT INTO Item VALUES(
	1111,
	TO_DATE(20141229, 'YYYYMMDD'),
	2,
	59.99
);

INSERT INTO Item VALUES(
	1111,
	TO_DATE(20141229, 'YYYYMMDD'),
	3,
	99.99
);

INSERT INTO Item VALUES(
	1111,
	TO_DATE(20150101, 'YYYYMMDD'),
	1,
	14.99
);
