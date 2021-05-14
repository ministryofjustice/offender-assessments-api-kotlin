-- Users
INSERT INTO OASYS_USER (OASYS_USER_UK, OASYS_USER_CODE, USER_FORENAME_1, USER_FORENAME_2, USER_FORENAME_3, USER_FAMILY_NAME, USER_STATUS_ELM, USER_STATUS_CAT, EMAIL_ADDRESS,CREATE_DATE, CREATE_USER, LASTUPD_DATE, LASTUPD_USER, CT_AREA_EST_CODE)
VALUES (100, 'USER1', 'JOHN', 'Middle', 'Middle 2', 'SMITH', 'ACTIVE', 'USER_STATUS', 'test@test.com', TO_DATE('2019-06-18 16:06:28', 'YYYY-MM-DD HH24:MI:SS'), 'SYS', TO_DATE('2019-07-17 14:58:41', 'YYYY-MM-DD HH24:MI:SS'), 'SYS', '1650'),
(101, 'USER2', 'JOHN', 'Middle', 'Middle 2', 'SMITH', 'ACTIVE', 'USER_STATUS', 'testemail@test.com', TO_DATE('2019-06-18 16:06:28', 'YYYY-MM-DD HH24:MI:SS'), 'SYS', TO_DATE('2019-07-17 14:58:41', 'YYYY-MM-DD HH24:MI:SS'), 'SYS', '1650');

-- User areas
INSERT INTO USER_AREA (USER_AREA_UK, CT_AREA_EST_CODE, OASYS_USER_CODE, START_DATE, END_DATE, EMAIL_ADDRESS, DEFAULT_TEAM_AREA_EST_CODE, CREATE_DATE, CREATE_USER, LASTUPD_DATE, LASTUPD_USER)
VALUES (100, '1650', 'USER1', TO_DATE('2019-06-18 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), null, 'test@test.com', '1650', TO_DATE('2019-06-18 16:06:31', 'YYYY-MM-DD HH24:MI:SS'), 'SYS', TO_DATE('2019-06-18 16:06:31', 'YYYY-MM-DD HH24:MI:SS'), 'SYS'),
       (101, '1650', 'USER2', TO_DATE('2019-06-18 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), null, 'testemail@test.com', '1650', TO_DATE('2019-06-18 16:06:31', 'YYYY-MM-DD HH24:MI:SS'), 'SYS', TO_DATE('2019-06-18 16:06:31', 'YYYY-MM-DD HH24:MI:SS'), 'SYS');

-- User area roles
INSERT INTO AREA_EST_USER_ROLE (AREA_EST_USER_ROLE_UK, OASYS_USER_CODE, REF_ROLE_CODE, CT_AREA_EST_CODE, START_DATE, END_DATE, CREATE_DATE, CREATE_USER, LASTUPD_DATE, LASTUPD_USER)
VALUES (100, 'USER1', '2065', '1650', TO_DATE('2011-07-19 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), null, TO_DATE('2012-10-16 10:16:29', 'YYYY-MM-DD HH24:MI:SS'), 'SYS', TO_DATE('2012-10-16 10:16:29', 'YYYY-MM-DD HH24:MI:SS'), 'SYS'),
       (101, 'USER2', '2065', '1650', TO_DATE('2011-07-19 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), null, TO_DATE('2012-10-16 10:16:29', 'YYYY-MM-DD HH24:MI:SS'), 'SYS', TO_DATE('2012-10-16 10:16:29', 'YYYY-MM-DD HH24:MI:SS'), 'SYS');


-- Area
INSERT INTO CT_AREA_EST(CT_AREA_EST_CODE, CT_AREA_EST_UK, AREA_EST_NAME, START_DATE, CREATE_USER, LASTUPD_USER)
VALUES ('1651', 129, 'Lancashire', TO_DATE('2011-07-19 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'EOR_RETAIN_DATA', TO_DATE('2011-07-19 00:00:00', 'YYYY-MM-DD HH24:MI:SS')),
       ('1652', 130, 'Kent Surrey and Sussex CRC', TO_DATE('2011-07-19 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'EOR_RETAIN_DATA', TO_DATE('2011-07-19 00:00:00', 'YYYY-MM-DD HH24:MI:SS')),
       ('1653', 131, 'Thames Valley CRC', TO_DATE('2011-07-19 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'EOR_RETAIN_DATA', TO_DATE('2011-07-19 00:00:00', 'YYYY-MM-DD HH24:MI:SS'));
