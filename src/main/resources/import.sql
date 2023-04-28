insert into authorities (authority, deleted, disabled) values ("register", false, false);
insert into authorities (authority, deleted, disabled) values ("add_user", false, false);
insert into authorities (authority, deleted, disabled) values ("update_user", false, false);
insert into authorities (authority, deleted, disabled) values ("get_all_users", false, false);

insert into user_role (id, deleted, disabled) values ("coordinator", false, false);

insert into user_role_authorities (id, authorities, role, institute)
values (1, "register", "coordinator", 1);
insert into user_role_authorities (id, authorities, role, institute)
values (2, "add_user", "coordinator", 1);
insert into user_role_authorities (id, authorities, role, institute)
values (3, "update_user", "coordinator", 1);
insert into user_role_authorities (id, authorities, role, institute)
values (4, "get_all_users", "coordinator", 1);