insert into authorities (authority, deleted, disabled) values ("register", false, false);
insert into authorities (authority, deleted, disabled) values ("add_user", false, false);

insert into user_role (id, deleted, disabled) values ("coordinator", false, false);

insert into user_role_authorities (id, authorities, role) values (1, 1, "coordinator");
insert into user_role_authorities (id, authorities, role) values (2, 2, "coordinator");