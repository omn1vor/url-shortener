insert into users (id, email) values (1, 'hermione@thatschool.com')
insert into users (id, email) values (2, 'severus@thatschool.com')

insert into urls (short_url, url, author_id, created, status) values ('herm', 'https://www.google.com/', 1, {ts '2023-02-09 12:03:15'}, 'INACTIVE')
insert into urls (short_url, url, author_id, created, status) values ('herm1', 'https://www.google.com/', 1, {ts '2023-02-10 08:00:00'}, 'ACTIVE')
insert into urls (short_url, url, author_id, created, status) values ('seve', 'https://www.google.com/', 2, {ts '2023-02-10 08:00:15'}, 'ACTIVE')
