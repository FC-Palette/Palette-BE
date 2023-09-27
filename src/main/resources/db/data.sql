INSERT INTO building(name)
VALUES ('미왕빌딩'),
       ('그랑서울'),
       ('프롭테크빌리지'),
       ('서초프라자'),
       ('로이어즈타워'),
       ('대륭타워'),
       ('한승아스트라'),
       ('법률센터');


INSERT INTO tenant(name, building_id)
VALUES ('스타트23', '1');
INSERT INTO tenant(name, building_id)
VALUES ('스타트23', '2');
INSERT INTO tenant(name, building_id)
VALUES ('스타트23', '3');
INSERT INTO tenant(name, building_id)
VALUES ('아무건설', '1');
INSERT INTO tenant(name, building_id)
VALUES ('아무건설', '2');
INSERT INTO tenant(name, building_id)
VALUES ('아무건설', '3');
INSERT INTO tenant(name, building_id)
VALUES ('법무', '1');
INSERT INTO tenant(name, building_id)
VALUES ('법무', '2');
INSERT INTO tenant(name, building_id)
VALUES ('법무', '3');
insert into tenant (name, building_id)
values ('오세안무역', 1);
insert into tenant (name, building_id)
values ('법무법인 율천', 1);
insert into tenant (name, building_id)
values ('영광공인중개사사무소', 4);
insert into tenant (name, building_id)
values ('연세위드이비인후과', 5);
insert into tenant (name, building_id)
values ('8층약국', 5);
insert into tenant (name, building_id)
values ('삼성생명', 6);
insert into tenant (name, building_id)
values ('이경영 변호사사무소', 7);
insert into tenant (name, building_id)
values ('법무법인 흑룡', 8);



INSERT INTO office_room (room_number, floor, wing, tenant_id, building_id)
VALUES ('201호', 2, 'A동', 1, 1);
INSERT INTO office_room (room_number, floor, wing, tenant_id, building_id)
VALUES ('302호', 3, 'A동', 2, 2);
INSERT INTO office_room (room_number, floor, wing, tenant_id, building_id)
VALUES ('507호', 5, 'B동', 5, 2);
INSERT INTO office_room (room_number, floor, wing, tenant_id, building_id)
VALUES ('102호', 1, 'C동', 7, 3);
-- 호실
insert into office_room (room_number, floor, wing, building_id, tenant_id)
values ('101', 1, '-', 1, 11);
insert into office_room (room_number, floor, wing, building_id, tenant_id)
values ('807', 8, '-', 1, 12);
insert into office_room (room_number, floor, wing, building_id, tenant_id)
values ('b127', -1, '-', 4, 13);
insert into office_room (room_number, floor, wing, building_id, tenant_id)
values ('809', 8, '-', 5, 14);
insert into office_room (room_number, floor, wing, building_id, tenant_id)
values ('808', 8, '-', 6, 15);
insert into office_room (room_number, floor, wing, building_id, tenant_id)
values ('508', 5, '1동', 7, 16);
insert into office_room (room_number, floor, wing, building_id, tenant_id)
values ('208', 2, 'B동', 8, 17);

-- password : abc1234#
INSERT INTO member (email, password, name, nickname, phone_number, bio, birthday, sex, position, job, created_at, updated_at, office_room_id, building_id, tenant_id )
VALUES('yohan@gmail.com', '$2a$12$iTiQm3XDjKfbu3boF1j3DO4S.udgMy4wKPMyb3aQMScnw9YaBYdMq', '김요한', '요한이', '01020003000', '안녕하세요', '20000214', 'MALE', 'EMPLOYEE', 'DESIGN', now(), now(), 1, 1, 1);
INSERT INTO member (email, password, name, nickname, phone_number, bio, birthday, sex, position, job, created_at, updated_at, office_room_id, building_id, tenant_id )
VALUES('watcha@naver.com', '$2a$12$iTiQm3XDjKfbu3boF1j3DO4S.udgMy4wKPMyb3aQMScnw9YaBYdMq', '김사원', '사원킴', '01011113117', '안녕하세요', '20000714', 'FEMALE', 'ASSISTANTMANAGER', 'BUSINESS', now(), now(), 2, 2 , 2);
INSERT INTO member (email, password, name, nickname, phone_number, bio, birthday, sex, position, job, created_at, updated_at, office_room_id, building_id, tenant_id )
VALUES('yumi@daum.net', '$2a$12$iTiQm3XDjKfbu3boF1j3DO4S.udgMy4wKPMyb3aQMScnw9YaBYdMq', '이유미', '유미', '01021113123', '하이하이', '19970214', 'FEMALE', 'SENIORMANAGER', 'HR', now(), now(), 4, 3, 7);
INSERT INTO member (email, password, name, nickname, phone_number, bio, birthday, sex, position, job, created_at, updated_at, office_room_id, building_id, tenant_id )
VALUES('naza@daum.net', '$2a$12$iTiQm3XDjKfbu3boF1j3DO4S.udgMy4wKPMyb3aQMScnw9YaBYdMq', '우철수', '철수', '01022376025', '법률쪽맡은사람입니다.', '19921225', 'MALE', 'GENERALMANAGER', 'LAW', now(), now(), 3, 2, 5);
INSERT INTO member (email, password, name, nickname, phone_number, bio, birthday, sex, position, job, created_at, updated_at, office_room_id, building_id, tenant_id )
VALUES('work99@gmail.com', '$2a$12$iTiQm3XDjKfbu3boF1j3DO4S.udgMy4wKPMyb3aQMScnw9YaBYdMq', '도배만', '도베르만', '01033328888', '안녕하세요', '19930707', 'MALE', 'ASSISTANTMANAGER', 'RESEARCH', now(), now(), 1, 1, 1 );
INSERT INTO member (email, password, name, nickname, phone_number, bio, birthday, sex, position, job, created_at, updated_at, office_room_id, building_id, tenant_id )
VALUES('pakka9@gmail.com', '$2a$12$iTiQm3XDjKfbu3boF1j3DO4S.udgMy4wKPMyb3aQMScnw9YaBYdMq', '한아름', NULL, NULL, NULL, NULL, NULL, NULL, NULL, now(), now(), 2, 2, 2 );
INSERT INTO member (email, password, name, nickname, phone_number, bio, birthday, sex, position, job, created_at, updated_at, office_room_id, building_id, tenant_id )
VALUES('jigubon9@gmail.com', '$2a$12$iTiQm3XDjKfbu3boF1j3DO4S.udgMy4wKPMyb3aQMScnw9YaBYdMq', '지구본', NULL, NULL, NULL, NULL, NULL, NULL, NULL, now(), now(), 4, 3, 7 );
INSERT INTO member (email, password, name, nickname, phone_number, bio, birthday, sex, position, job, created_at, updated_at, office_room_id, building_id, tenant_id )
VALUES('bakgu@gmail.com', '$2a$12$iTiQm3XDjKfbu3boF1j3DO4S.udgMy4wKPMyb3aQMScnw9YaBYdMq', '백구식', NULL, NULL, NULL, NULL, NULL, NULL, NULL, now(), now(), 3, 2, 5 );
INSERT INTO member (email, password, name, nickname, phone_number, bio, birthday, sex, position, job, created_at, updated_at, office_room_id, building_id, tenant_id )
VALUES('host77@gmail.com', '$2a$12$iTiQm3XDjKfbu3boF1j3DO4S.udgMy4wKPMyb3aQMScnw9YaBYdMq', '유호식', NULL, NULL, NULL, NULL, NULL, NULL, NULL, now(), now(), 1, 1, 1 );
INSERT INTO member (email, password, name, nickname, phone_number, bio, birthday, sex, position, job, created_at, updated_at, office_room_id, building_id, tenant_id )
VALUES('dklss8@gmail.com', '$2a$12$iTiQm3XDjKfbu3boF1j3DO4S.udgMy4wKPMyb3aQMScnw9YaBYdMq', '남궁민', NULL, NULL, NULL, NULL, NULL, NULL, NULL, now(), now(), 2, 2, 2 );


INSERT INTO meeting (member_id, category, type, sex, title, description, head_count, start_date, end_date, on_off, place, week, time, progress_time, accept_type, is_closing, hits, likes, recruited_personnel, created_at)
VALUES
    (1, 'CAREER', 'STUDY', 'FEMALE', '제목', '성장해요', 20, '2023-10-01', '2023-10-15', true, '장소', 'EVERY_WEEK', '3:00 PM', '30', 'APPROVAL', false, 10, 20, 0, now()),
    (1, 'CAREER', 'PROJECT', 'FEMALE', '제목', '성장해요', 20, '2023-10-02', '2023-10-15', true, '장소', 'EVERY_WEEK', '3:00 PM', '30', 'APPROVAL', false, 20, 10, 0, now()),
    (1, 'CAREER', 'AMITY', 'FEMALE', '제목', '성장해요', 20, '2023-10-03', '2023-10-15', true, '장소', 'EVERY_WEEK', '3:00 PM', '30', 'APPROVAL', false, 110, 40, 0, now()),
    (1, 'CAREER', 'STUDY', 'MALE', '제목', '성장해요', 20, '2023-10-04', '2023-10-15', true, '장소', 'EVERY_WEEK', '3:00 PM', '30', 'APPROVAL', false, 14, 15, 0, now()),
    (1, 'CAREER', 'PROJECT', 'MALE', '제목', '성장해요', 20, '2023-10-05', '2023-10-15', true, '장소', 'EVERY_WEEK', '3:00 PM', '30', 'APPROVAL', false, 15, 17, 0, now()),
    (1, 'CAREER', 'AMITY', 'MALE', '제목', '성장해요', 20, '2023-10-06', '2023-10-15', false, '장소', 'EVERY_WEEK', '3:00 PM', '50', 'APPROVAL', false, 16, 19, 0, now()),
    (1, 'CAREER', 'STUDY', 'IRRELEVANT', '제목', '성장해요', 20, '2023-10-07', '2023-10-15', false, '장소', 'EVERY_WEEK', '3:00 PM', '50', 'APPROVAL', false, 17, 20, 0, now()),
    (1, 'CAREER', 'PROJECT', 'IRRELEVANT', '제목', '성장해요', 20, '2023-10-08', '2023-10-15', false, '장소', 'EVERY_WEEK', '3:00 PM', '50', 'APPROVAL', false, 10, 50, 0, now()),
    (1, 'CAREER', 'AMITY', 'IRRELEVANT', '제목', '성장해요', 20, '2023-10-09', '2023-10-15', false, '장소', 'EVERY_WEEK', '3:00 PM', '50', 'APPROVAL', false, 20, 60, 0, now()),
    (1, 'CAREER', 'STUDY', 'IRRELEVANT', '제목', '성장해요', 20, '2023-10-10', '2023-10-15', false, '장소', 'EVERY_WEEK', '3:00 PM', '50', 'APPROVAL', false, 30, 120, 0, now()),
    (2, 'CAREER', 'STUDY', 'FEMALE', '제목', '성장해요', 20, '2023-10-01', '2023-10-15', true, '장소', 'EVERY_WEEK', '3:00 PM', '50', 'APPROVAL', false, 40, 80, 0, now()),
    (2, 'CAREER', 'PROJECT', 'FEMALE', '제목', '성장해요', 20, '2023-10-02', '2023-10-15', true, '장소', 'EVERY_WEEK', '3:00 PM', '50', 'APPROVAL', false, 50, 90, 0, now()),
    (2, 'CAREER', 'AMITY', 'FEMALE', '제목', '성장해요', 20, '2023-10-03', '2023-10-15', true, '장소', 'EVERY_WEEK', '3:00 PM', '50', 'APPROVAL', false, 10, 10, 0, now()),
    (2, 'CAREER', 'STUDY', 'MALE', '제목', '성장해요', 20, '2023-10-04', '2023-10-15', true, '장소', 'EVERY_WEEK', '3:00 PM', '50', 'APPROVAL', false, 150, 12, 0, now()),
    (2, 'CAREER', 'PROJECT', 'MALE', '제목', '성장해요', 20, '2023-10-05', '2023-10-15', true, '장소', 'EVERY_WEEK', '3:00 PM', '50', 'APPROVAL', false, 160, 22, 0, now()),
    (2, 'CAREER', 'AMITY', 'MALE', '제목', '성장해요', 20, '2023-10-06', '2023-10-15', false, '장소', 'EVERY_WEEK', '3:00 PM', '100', 'APPROVAL', false, 110, 17, 0, now()),
    (2, 'CAREER', 'STUDY', 'IRRELEVANT', '제목', '성장해요', 20, '2023-10-07', '2023-10-15', false, '장소', 'EVERY_WEEK', '3:00 PM', '100', 'APPROVAL', false, 10, 111, 0, now()),
    (2, 'CAREER', 'PROJECT', 'IRRELEVANT', '제목', '성장해요', 20, '2023-10-08', '2023-10-15', false, '장소', 'EVERY_WEEK', '3:00 PM', '100', 'APPROVAL', false, 0, 0, 0, now()),
    (2, 'CAREER', 'AMITY', 'IRRELEVANT', '제목', '성장해요', 20, '2023-10-09', '2023-10-15', false, '장소', 'EVERY_WEEK', '3:00 PM', '100', 'APPROVAL', false, 0, 0, 0, now()),
    (2, 'CAREER', 'STUDY', 'IRRELEVANT', '제목', '성장해요', 20, '2023-10-10', '2023-10-15', false, '장소', 'EVERY_WEEK', '3:00 PM', '100', 'APPROVAL', false, 0, 0, 0, now()),
    (3, 'CAREER', 'STUDY', 'FEMALE', '제목', '성장해요', 20, '2023-10-01', '2023-10-15', true, '장소', 'EVERY_WEEK', '3:00 PM', '100', 'APPROVAL', false, 0, 0, 0, now()),
    (3, 'CAREER', 'PROJECT', 'FEMALE', '제목', '성장해요', 20, '2023-10-02', '2023-10-15', true, '장소', 'EVERY_WEEK', '3:00 PM', '100', 'APPROVAL', false, 0, 0, 0, now()),
    (3, 'CAREER', 'AMITY', 'FEMALE', '제목', '성장해요', 20, '2023-10-03', '2023-10-15', true, '장소', 'EVERY_WEEK', '3:00 PM', '100', 'APPROVAL', false, 0, 0, 0, now()),
    (3, 'CAREER', 'STUDY', 'MALE', '제목', '성장해요', 20, '2023-10-04', '2023-10-15', true, '장소', 'EVERY_WEEK', '3:00 PM', '100', 'APPROVAL', false, 0, 0, 0, now()),
    (3, 'CAREER', 'PROJECT', 'MALE', '제목', '성장해요', 20, '2023-10-05', '2023-10-15', true, '장소', 'EVERY_WEEK', '3:00 PM', '100', 'APPROVAL', false, 0, 0, 0, now()),
    (3, 'CAREER', 'AMITY', 'MALE', '제목', '성장해요', 20, '2023-10-06', '2023-10-15', false, '장소', 'EVERY_WEEK', '3:00 PM', '130', 'APPROVAL', false, 0, 0, 0, now()),
    (3, 'CAREER', 'STUDY', 'IRRELEVANT', '제목', '성장해요', 20, '2023-10-07', '2023-10-15', false, '장소', 'EVERY_WEEK', '3:00 PM', '130', 'APPROVAL', false, 0, 0, 0, now()),
    (3, 'CAREER', 'PROJECT', 'IRRELEVANT', '제목', '성장해요', 20, '2023-10-08', '2023-10-15', false, '장소', 'EVERY_WEEK', '3:00 PM', '130', 'APPROVAL', false, 0, 0, 0, now()),
    (3, 'CAREER', 'AMITY', 'IRRELEVANT', '제목', '성장해요', 20, '2023-10-09', '2023-10-15', false, '장소', 'EVERY_WEEK', '3:00 PM', '130', 'APPROVAL', false, 0, 0, 0, now()),
    (3, 'CAREER', 'STUDY', 'IRRELEVANT', '제목', '성장해요', 20, '2023-10-10', '2023-10-15', false, '장소', 'EVERY_WEEK', '3:00 PM', '130', 'APPROVAL', false, 0, 0, 0, now()),
    (4, 'CAREER', 'STUDY', 'FEMALE', '제목', '성장해요', 20, '2023-10-01', '2023-10-15', true, '장소', 'EVERY_WEEK', '3:00 PM', '130', 'APPROVAL', false, 0, 0, 0, now()),
    (4, 'CAREER', 'PROJECT', 'FEMALE', '제목', '성장해요', 20, '2023-10-02', '2023-10-15', true, '장소', 'EVERY_WEEK', '3:00 PM', '150', 'APPROVAL', false, 0, 0, 0, now()),
    (4, 'CAREER', 'AMITY', 'FEMALE', '제목', '성장해요', 20, '2023-10-03', '2023-10-15', true, '장소', 'EVERY_WEEK', '3:00 PM', '150', 'APPROVAL', false, 0, 0, 0, now()),
    (4, 'CAREER', 'STUDY', 'MALE', '제목', '성장해요', 20, '2023-10-04', '2023-10-15', true, '장소', 'EVERY_WEEK', '3:00 PM', '150', 'APPROVAL', false, 0, 0, 0, now()),
    (4, 'CAREER', 'PROJECT', 'MALE', '제목', '성장해요', 20, '2023-10-05', '2023-10-15', true, '장소', 'EVERY_WEEK', '3:00 PM', '150', 'APPROVAL', false, 0, 0, 0,now()),
    (4, 'CAREER', 'AMITY', 'MALE', '제목', '성장해요', 20, '2023-10-06', '2023-10-15', false, '장소', 'EVERY_WEEK', '3:00 PM', '150', 'APPROVAL', false, 0, 0, 0, now()),
    (4, 'CAREER', 'STUDY', 'IRRELEVANT', '제목', '성장해요', 20, '2023-10-07', '2023-10-15', false, '장소', 'EVERY_WEEK', '3:00 PM', '150', 'APPROVAL', false, 0, 0, 0, now()),
    (4, 'CAREER', 'PROJECT', 'IRRELEVANT', '제목', '성장해요', 20, '2023-10-08', '2023-10-15', false, '장소', 'EVERY_WEEK', '3:00 PM', '150', 'APPROVAL', true, 0, 0, 0, now()),
    (4, 'CAREER', 'AMITY', 'IRRELEVANT', '제목', '성장해요', 20, '2023-10-09', '2023-10-15', false, '장소', 'EVERY_WEEK', '3:00 PM', '150', 'APPROVAL', true, 0, 0, 0, now()),
    (4, 'CAREER', 'STUDY', 'IRRELEVANT', '제목', '성장해요', 20, '2023-10-10', '2023-10-15', false, '장소', 'EVERY_WEEK', '3:00 PM', '150', 'APPROVAL', true, 0, 0, 0, now());

insert Into meeting_job (meeting_id, job)
values
    (1, 'BUSINESS'), (1, 'SERVICE'), (1, 'DEVELOPMENT'),
    (2, 'BUSINESS'), (2, 'SERVICE'), (2, 'DATA'),
    (3, 'MARKETING'), (3, 'ECOMMERCE'), (3, 'DATA'),
    (4, 'MARKETING'), (4, 'DESIGN'), (4, 'DATA'),
    (5, 'MARKETING'), (5, 'EDUCATION'), (5, 'FINANCE'),
    (6, 'ECOMMERCE'), (6, 'FINANCE'), (6, 'ACCOUNTING'),
    (7, 'CUSTOMER'), (7, 'HR'), (7, 'GAMEDEV'),
    (8, 'DISTRIBUTION'), (8, 'MEDICAL'), (8, 'GAMEDEV'),
    (9, 'MEDICAL'), (9, 'RESEARCH'), (9, 'LAW'),
    (10, 'ENGINEERING'), (10, 'PRODUCTION'), (10, 'LAW'),
    (11, 'BUSINESS'), (11, 'SERVICE'), (11, 'DEVELOPMENT'),
    (12, 'BUSINESS'), (12, 'SERVICE'), (12, 'DATA'),
    (13, 'MARKETING'), (13, 'ECOMMERCE'), (13, 'DATA'),
    (14, 'MARKETING'), (14, 'DESIGN'), (14, 'DATA'),
    (15, 'MARKETING'), (15, 'EDUCATION'), (15, 'FINANCE'),
    (16, 'ECOMMERCE'), (16, 'FINANCE'), (16, 'ACCOUNTING'),
    (17, 'CUSTOMER'), (17, 'HR'), (17, 'GAMEDEV'),
    (18, 'DISTRIBUTION'), (18, 'MEDICAL'), (18, 'GAMEDEV'),
    (19, 'MEDICAL'), (19, 'RESEARCH'), (19, 'LAW'),
    (20, 'ENGINEERING'), (20, 'PRODUCTION'), (20, 'LAW'),
    (21, 'BUSINESS'), (21, 'SERVICE'), (21, 'DEVELOPMENT'),
    (22, 'BUSINESS'), (22, 'SERVICE'), (22, 'DATA'),
    (23, 'MARKETING'), (23, 'ECOMMERCE'), (23, 'DATA'),
    (24, 'MARKETING'), (24, 'DESIGN'), (24, 'DATA'),
    (25, 'MARKETING'), (25, 'EDUCATION'), (25, 'FINANCE'),
    (26, 'ECOMMERCE'), (26, 'FINANCE'), (26, 'ACCOUNTING'),
    (27, 'CUSTOMER'), (27, 'HR'), (27, 'GAMEDEV'),
    (28, 'DISTRIBUTION'), (28, 'MEDICAL'), (28, 'GAMEDEV'),
    (29, 'MEDICAL'), (29, 'RESEARCH'), (29, 'LAW'),
    (30, 'ENGINEERING'), (30, 'PRODUCTION'), (30, 'LAW'),
    (31, 'BUSINESS'), (31, 'SERVICE'), (31, 'DEVELOPMENT'),
    (32, 'BUSINESS'), (32, 'SERVICE'), (32, 'DATA'),
    (33, 'MARKETING'), (33, 'ECOMMERCE'), (33, 'DATA'),
    (34, 'MARKETING'), (34, 'DESIGN'), (34, 'DATA'),
    (35, 'MARKETING'), (35, 'EDUCATION'), (35, 'FINANCE'),
    (36, 'ECOMMERCE'), (36, 'FINANCE'), (36, 'ACCOUNTING'),
    (37, 'CUSTOMER'), (37, 'HR'), (37, 'GAMEDEV'),
    (38, 'DISTRIBUTION'), (38, 'MEDICAL'), (38, 'GAMEDEV'),
    (39, 'MEDICAL'), (39, 'RESEARCH'), (39, 'LAW'),
    (40, 'ENGINEERING'), (40, 'PRODUCTION'), (40, 'LAW');

insert Into meeting_position (meeting_id, position)
values
    (1, 'INTERN'), (1, 'EMPLOYEE'), (1, 'ASSISTANTMANAGER'),
    (2, 'INTERN'), (2, 'SENIORMANAGER'), (2, 'DEPUTYMANAGER'),
    (3, 'EMPLOYEE'), (3, 'ASSISTANTMANAGER'), (3, 'SENIORMANAGER'),
    (4, 'EMPLOYEE'), (4, 'DEPUTYMANAGER'), (4, 'GENERALMANAGER'),
    (5, 'BOSS'), (5, 'MANAGER'), (5, 'ETC'),
    (6, 'BOSS'), (6, 'DEPUTYMANAGER'), (6, 'ETC'),
    (7, 'BOSS'), (7, 'EMPLOYEE'), (7, 'ETC'),
    (8, 'INTERN'), (8, 'EMPLOYEE'), (8, 'GENERALMANAGER'),
    (9, 'GENERALMANAGER'), (9, 'BOSS'), (9, 'ASSISTANTMANAGER'),
    (10, 'INTERN'), (10, 'GENERALMANAGER'), (10, 'BOSS'),
    (11, 'INTERN'), (11, 'EMPLOYEE'), (11, 'ASSISTANTMANAGER'),
    (12, 'INTERN'), (12, 'SENIORMANAGER'), (12, 'DEPUTYMANAGER'),
    (13, 'EMPLOYEE'), (13, 'ASSISTANTMANAGER'), (13, 'SENIORMANAGER'),
    (14, 'EMPLOYEE'), (14, 'DEPUTYMANAGER'), (14, 'GENERALMANAGER'),
    (15, 'BOSS'), (15, 'MANAGER'), (15, 'ETC'),
    (16, 'BOSS'), (16, 'DEPUTYMANAGER'), (16, 'ETC'),
    (17, 'BOSS'), (17, 'DEPUTYMANAGER'), (17, 'ETC'),
    (18, 'INTERN'), (18, 'GENERALMANAGER'), (18, 'MANAGER'),
    (19, 'MANAGER'), (19, 'INTERN'), (19, 'GENERALMANAGER'),
    (20, 'GENERALMANAGER'), (20, 'EMPLOYEE'), (20, 'ASSISTANTMANAGER'),
    (21, 'INTERN'), (21, 'EMPLOYEE'), (21, 'ASSISTANTMANAGER'),
    (22, 'INTERN'), (22, 'SENIORMANAGER'), (22, 'DEPUTYMANAGER'),
    (23, 'EMPLOYEE'), (23, 'ASSISTANTMANAGER'), (23, 'SENIORMANAGER'),
    (24, 'EMPLOYEE'), (24, 'DEPUTYMANAGER'), (24, 'GENERALMANAGER'),
    (25, 'BOSS'), (25, 'MANAGER'), (25, 'ETC'),
    (26, 'BOSS'), (26, 'DEPUTYMANAGER'), (26, 'ETC'),
    (27, 'BOSS'), (27, 'DEPUTYMANAGER'), (27, 'ETC'),
    (28, 'INTERN'), (28, 'GENERALMANAGER'), (28, 'MANAGER'),
    (29, 'MANAGER'), (29, 'INTERN'), (29, 'GENERALMANAGER'),
    (30, 'GENERALMANAGER'), (30, 'EMPLOYEE'), (30, 'ASSISTANTMANAGER'),
    (31, 'INTERN'), (31, 'EMPLOYEE'), (31, 'ASSISTANTMANAGER'),
    (32, 'INTERN'), (32, 'SENIORMANAGER'), (32, 'DEPUTYMANAGER'),
    (33, 'EMPLOYEE'), (33, 'ASSISTANTMANAGER'), (33, 'SENIORMANAGER'),
    (34, 'EMPLOYEE'), (34, 'DEPUTYMANAGER'), (34, 'GENERALMANAGER'),
    (35, 'BOSS'), (35, 'MANAGER'), (35, 'ETC'),
    (36, 'BOSS'), (36, 'DEPUTYMANAGER'), (36, 'ETC'),
    (37, 'BOSS'), (37, 'DEPUTYMANAGER'), (37, 'ETC'),
    (38, 'INTERN'), (38, 'GENERALMANAGER'), (38, 'MANAGER'),
    (39, 'MANAGER'), (39, 'INTERN'), (39, 'GENERALMANAGER'),
    (40, 'GENERALMANAGER'), (40, 'EMPLOYEE'), (40, 'ASSISTANTMANAGER');

-- 멤버
INSERT INTO member (email, password, name, nickname, phone_number, bio, birthday, sex, position, job, created_at,
                    updated_at, office_room_id, building_id, tenant_id)
VALUES ('glory@gmail.com', '$2a$12$iTiQm3XDjKfbu3boF1j3DO4S.udgMy4wKPMyb3aQMScnw9YaBYdMq', '정성민', '정성민123',
        '01066789982', '정성민입니다.', '19890812', 'MALE',
        'SENIORMANAGER',
        'CUSTOMER', now(),
        now(), 7, 4, 12);

INSERT INTO member (email, password, name, nickname, phone_number, bio, birthday, sex, position, job, created_at,
                    updated_at, office_room_id, building_id, tenant_id)
VALUES ('doctorchoi@yonseiwith.com', '$2a$12$iTiQm3XDjKfbu3boF1j3DO4S.udgMy4wKPMyb3aQMScnw9YaBYdMq', '최원장', 'dr.choi',
        '01012311233', '연세위드 이비인후과 원장 최원장입니다.', '19800101',
        'MALE', 'ETC', 'MEDICAL', now(),
        now(), 8, 5, 14);

INSERT INTO member (email, password, name, nickname, phone_number, bio, birthday, sex, position, job, created_at,
                    updated_at, office_room_id, building_id, tenant_id)
VALUES ('acd@gmail.com', '$2a$12$iTiQm3XDjKfbu3boF1j3DO4S.udgMy4wKPMyb3aQMScnw9YaBYdMq', '김민정', 'min_vely',
        '01063329182', '', '19901112', 'FEMALE', 'BOSS', 'MEDICAL',
        now(),
        now(), 9, 6, 15);


--공동구매
insert into purchase (member_id, title, category, shop_url, start_date, end_date, head_count, price, description,
                      closing_type, is_closing, hits, bank, account_number, account_owner)
values (1, '코드제로 A9', 'HOUSEHOLD', 'https://www.lge.co.kr/vacuum-cleaners/as9202wd', '2023-10-01', '2023-10-31', 5,
        500000, '이거 같이 삽시다 넘 좋아요 ㅜ',
        'DATETIME',
        false, 25, '신한은행', '110351316910',
        '김요한');
insert into purchase (member_id, title, category, shop_url, start_date, end_date, head_count, price, description,
                      closing_type, is_closing, hits, bank, account_number, account_owner)
values (2, '로지텍 mx master 3s', 'OFFICE_SUPPLIES', 'https://www.funshop.co.kr/goods/detail/183875?t=sc', '2023-10-01',
        '2023-09-19', 10, 110000,
        '가성비 너무 좋아요! 같이 삽시다',
        'HEAD_COUNT', false, 100, '신한은행', '111352316910',
        '김사원');
insert into purchase (member_id, title, category, shop_url, start_date, end_date, head_count, price, description,
                      closing_type, is_closing, hits, bank, account_number, account_owner)
values (3, 'WH-1000XM5', 'ELECTRONICS', 'https://www.musinsa.com/app/goods/2608456', '2023-10-01', '2023-10-11', 10,
        390000, '소니 wh-1000xm5 공구합니다',
        'HEAD_COUNT', false, 150, '국민은행', '03192619100',
        '이유미');
insert into purchase (member_id, title, category, shop_url, start_date, end_date, head_count, price, description,
                      closing_type, is_closing, hits, bank, account_number, account_owner)
values (5, '맥북에어 m3 15인치 같이사용', 'ELECTRONICS', 'https://www.coupang.com/vp/products/7400323975', '2023-10-15',
        '2023-10-31', 30, 1690000,
        '맥북에어 15인치 m3',
        'HEAD_COUNT', false, 300, '카카오뱅크', '123512310901',
        '도배만');
insert into purchase (member_id, title, category, shop_url, start_date, end_date, head_count, price, description,
                      closing_type, is_closing, hits, bank, account_number, account_owner)
values (10, '믹서기마냥 갈아버리는 블렌더 보틀 같이 사실분?', 'HOUSEHOLD', 'https://www.coupang.com/vp/products/7075531344', '2023-10-01',
        '2023-10-11', 10, 15000,
        '블렌더 보틀 텀블러 같이사요~~',
        'HEAD_COUNT', false, 123, '우리은행', '100399721231',
        '김민정');

--공동구매 미디어
insert into purchase_media (url, purchase_id)
values ('https://fc-palette.s3.ap-northeast-2.amazonaws.com/purchase/23415.png', 1);
insert into purchase_media (url, purchase_id)
values ('https://fc-palette.s3.ap-northeast-2.amazonaws.com/purchase/vs_image800.jpg', 2);
insert into purchase_media (url, purchase_id)
values ('https://fc-palette.s3.ap-northeast-2.amazonaws.com/purchase/2608456_16908576712947_500.jpg', 3);
insert into purchase_media (url, purchase_id)
values ('https://fc-palette.s3.ap-northeast-2.amazonaws.com/purchase/images.jpeg', 3);
insert into purchase_media(url,purchase_id)
values ('https://fc-palette.s3.ap-northeast-2.amazonaws.com/purchase/vs_image800.jpg', 4);
insert into purchase_media(url,purchase_id)
values ('https://fc-palette.s3.ap-northeast-2.amazonaws.com/purchase/air15.jpeg', 4);
insert into purchase_media(url,purchase_id)
values ('https://fc-palette.s3.ap-northeast-2.amazonaws.com/purchase/air152.jpeg', 4);
insert into purchase_media (url, purchase_id)
values ('https://fc-palette.s3.ap-northeast-2.amazonaws.com/purchase/a123.png', 5);

-- 공동구매 북마크
insert into purchase_bookmark (member_id, purchase_id, created_at, updated_at)
values (1, 2, now(), now());
insert into purchase_bookmark (member_id, purchase_id, created_at, updated_at)
values (2, 1, now(), now());
insert into purchase_bookmark (member_id, purchase_id, created_at, updated_at)
values (10, 5, now(), now());
insert into purchase_bookmark (member_id, purchase_id, created_at, updated_at)
values (10, 1, now(), now());
insert into purchase_bookmark (member_id, purchase_id, created_at, updated_at)
values (5, 4, now(), now());

insert into group_purchase_participant (purchase_id)
values (1);
insert into group_purchase_participant (purchase_id)
values (1);
insert into group_purchase_participant (purchase_id)
values (1);
insert into group_purchase_participant (purchase_id)
values (2);
insert into group_purchase_participant (purchase_id)
values (2);
insert into group_purchase_participant (purchase_id)
values (3);
insert into group_purchase_participant (purchase_id)
values (4);
insert into group_purchase_participant (purchase_id)
values (4);
insert into group_purchase_participant (purchase_id)
values (4);
insert into group_purchase_participant (purchase_id)
values (5);
insert into group_purchase_participant (purchase_id)
values (5);

insert into group_purchase_participant_member (member_id, purchase_participant_id)
values (1, 5);
insert into group_purchase_participant_member (member_id, purchase_participant_id)
values (1, 4);
insert into group_purchase_participant_member (member_id, purchase_participant_id)
values (2, 5);
insert into group_purchase_participant_member (member_id, purchase_participant_id)
values (2, 4);
insert into group_purchase_participant_member (member_id, purchase_participant_id)
values (3, 4);
insert into group_purchase_participant_member (member_id, purchase_participant_id)
values (4, 3);
insert into group_purchase_participant_member (member_id, purchase_participant_id)
values (7, 1);
insert into group_purchase_participant_member (member_id, purchase_participant_id)
values (8, 1);
insert into group_purchase_participant_member (member_id, purchase_participant_id)
values (9, 1);
insert into group_purchase_participant_member (member_id, purchase_participant_id)
values (10, 2);
insert into group_purchase_participant_member (member_id, purchase_participant_id)
values (5, 2);

-- 중고거래
insert into secondhand (member_id, title, category, transaction_start_time,
                        transaction_end_time, price, description,
                        hits, is_free, created_at, updated_at)
values (1, '메가커피 기프티콘 팝니다', 'TICKET', '00:00:00', '23:59:59', 1000, '메가커피 아메리카노 깊티입니다.', 25, false, now(), now());
insert into secondhand (member_id, title, category, transaction_start_time, transaction_end_time, price, description,
                        hits, is_free, created_at, updated_at)
values (1, '아이폰 13미니', 'ELECTRONICS', '00:00:00', '23:59:59', 500000, '아이폰 13미니 128gb 미드나잇 블루', 25, false, now(),
        now());
insert into secondhand (member_id, title, category, transaction_start_time,
                        transaction_end_time, price, description, hits,
                        is_free, created_at, updated_at)
values (3, '티쏘 젠틀맨 팝니다', 'FASHION', '00:00:00', '23:59:59', 300000, '넘예쁘네여', 39,
        false, now(), now());
insert into secondhand (member_id, title, category, transaction_start_time,
                        transaction_end_time, price, description, hits,
                        is_free, created_at, updated_at)
values (2, '몽블랑 머니클립', 'FASHION', '13:00:00', '20:00:00', 200000, '머니클립 너무 편해용', 19,
        false, now(), now());
insert into secondhand (member_id, title, category, transaction_start_time,
                        transaction_end_time, price, description, hits,
                        is_free, created_at, updated_at)
values (10, '셀린느 카드지갑', 'FASHION', '11:00:00', '20:00:00', 300000, '산지 얼마 되지 않아 상태 아주 좋습니다', 51,
        false, now(), now());
insert into secondhand (member_id, title, category, transaction_start_time,
                        transaction_end_time, price, description, hits,
                        is_free, created_at, updated_at)
values (7, '투퍼데이 멀티비타민', 'PROCESSED_WELLNESS_FOODS', '10:00:00', '22:00:00', 20000, '투퍼데이 멀티비타인 미개봉 판매해요', 11,
        false, now(), now());
insert into secondhand (member_id, title, category, transaction_start_time,
                        transaction_end_time, price, description, hits,
                        is_free, created_at, updated_at)
values (7, '홈키파 훈증기 무료나눔', 'HOUSEHOLD', '10:00:00', '22:00:00', 0, '액상형으로 교체해 안쓰는 훈증기 무료나눔합니다~~', 11,
        true, now(), now());

-- 중고거래 이미지
insert into secondhand_media (url, secondhand_id)
values ('https://fc-palette.s3.ap-northeast-2.amazonaws.com/secondhand/1000.png', 1);
insert into secondhand_media (url, secondhand_id)
values ('https://fc-palette.s3.ap-northeast-2.amazonaws.com/secondhand/13.jpeg', 2);
insert into secondhand_media (url, secondhand_id)
values ('https://fc-palette.s3.ap-northeast-2.amazonaws.com/secondhand/7899.jpeg', 3);
insert into secondhand_media (url, secondhand_id)
values ('https://fc-palette.s3.ap-northeast-2.amazonaws.com/secondhand/14121.jpeg', 4);
insert into secondhand_media (url, secondhand_id)
values ('https://fc-palette.s3.ap-northeast-2.amazonaws.com/secondhand/1362.png', 5);
insert into secondhand_media (url, secondhand_id)
values ('https://fc-palette.s3.ap-northeast-2.amazonaws.com/secondhand/120.jpeg', 6);
insert into secondhand_media (url, secondhand_id)
values ('https://fc-palette.s3.ap-northeast-2.amazonaws.com/secondhand/356.png', 7);

-- 중고거래 북마크
insert into secondhand_bookmark (member_id, secondhand_id, created_at, updated_at)
values (1, 2, now(), now());
insert into secondhand_bookmark (member_id, secondhand_id, created_at, updated_at)
values (2, 1, now(), now());
insert into secondhand_bookmark (member_id, secondhand_id, created_at, updated_at)
values (10, 5, now(), now());
insert into secondhand_bookmark (member_id, secondhand_id, created_at, updated_at)
values (10, 1, now(), now());
insert into secondhand_bookmark (member_id, secondhand_id, created_at, updated_at)
values (5, 4, now(), now());
