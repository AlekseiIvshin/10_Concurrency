drop database if exists payday;

create database payday CHARACTER SET utf8 COLLATE utf8_general_ci;
use payday;
create table payments(
	id int(11) AUTO_INCREMENT primary key,
	payer varchar(255) not null,
	payee varchar(255) not null,
	bank_bic varchar(15) not null,
	bank_name varchar(255) not null,
	cash decimal not null,
	date_of_create datetime not null,
	date_of_execute datetime not null,
	unique (payer, payee, date_of_create,bank_bic)
);
