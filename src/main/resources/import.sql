insert into users (email) values ('hermione@thatschool.com')
insert into users (email) values ('severus@thatschool.com')

insert into urls (code, url, author_id, created, status) values ('herm', 'https://www.google.com/', 1, {ts '2022-02-09 12:03:15'}, 'INACTIVE')
insert into urls (code, url, author_id, created, status) values ('herm1', 'https://www.google.com/', 1, {ts '2022-02-10 08:00:00'}, 'ACTIVE')
insert into urls (code, url, author_id, created, status) values ('severus', 'https://www.google.com/', 2, {ts '2022-02-10 08:00:15'}, 'ACTIVE')
