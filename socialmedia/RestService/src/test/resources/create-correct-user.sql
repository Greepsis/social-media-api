insert into users(user_id, email, name, password) values
    ('ef6b50da-44f5-4330-8320-3e2f35da27a7','alexAlex@gmail.com','Alex','$2a$10$XqPmhhh7o9z1M88BQkZDwu9EUC9RC10hBojE5ZvRC2KZVkKyscmYC');
insert into roles(role_id,name) values ('d5086bd5-7537-47c8-82da-22a49ee18225', 'ROLE_USER');
insert into users_roles(user_user_id,roles_role_id) values ('ef6b50da-44f5-4330-8320-3e2f35da27a7','d5086bd5-7537-47c8-82da-22a49ee18225');