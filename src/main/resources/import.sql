insert into authorities (authority, deleted, disabled) values ("register", false, false);
insert into authorities (authority, deleted, disabled) values ("add_user", false, false);

insert into user_role (id, deleted, disabled) values ("head_master", false, false);

insert into user_role_authorities (id, authorities, role) values (1, 1, "head_master");
insert into user_role_authorities (id, authorities, role) values (2, 2, "head_master");