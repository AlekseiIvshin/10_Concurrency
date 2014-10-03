drop database if exists payday;

create database payday CHARACTER SET utf8 COLLATE utf8_general_ci;
use payday;

create table participant_types(
	id int(2) AUTO_INCREMENT primary key,
	name varchar(16)
);


create table paymentparticipant(
	account varchar(20) primary key,
	surname varchar(255),
	name varchar(255),
	patronymic varchar(255),
	organization_name varchar(255),
	phone varchar(20),
	address varchar(255),
	document_series varchar(10),
	document_number varchar(20),
	document_type varchar(10) not null,
	part_type int(2) not null,
	foreign key(part_type) references participant_types(id)
);

create table payments(
	id int(11) AUTO_INCREMENT primary key,
	payer_account varchar(20),
	payee_account varchar(20),
	bank_bic varchar(15) not null,
	bank_name varchar(255) not null,
	cash decimal not null,
	date_of_create datetime not null,
	date_of_execute datetime not null,
	unique key (payer_account, payee_account, date_of_create,bank_bic),
	foreign key (payer_account) references paymentparticipant(account),
	foreign key (payee_account) references paymentparticipant(account)	
);


insert into participant_types(name) values("INDIVIDUAL"),("LEGALENTITY");

insert into paymentparticipant(account,surname,name,patronymic,document_series, document_number, document_type,phone,address,organization_name,part_type) 
values 	("10000000000000000000", "Surname","Name","Patronymic","0990","123123","passport","1231123123","Town",null,1),
		("10000000000000000001", "Surname","Name","Patronymic","0990","123123","passport","1231123123","Town",null,1),
		("10000000000000000002", "Surname","Name","Patronymic","0990","123123","passport","1231123123","Town",null,1),
		("10000000000000000003", "Surname","Name","Patronymic","0990","123123","passport","1231123123","Town",null,1),
		("10000000000000000004", "Surname","Name","Patronymic","0990","123123","passport","1231123123","Town",null,1),
		("10000000000000000005", "Surname","Name","Patronymic","0990","123123","passport","1231123123","Town",null,1),
		("10000000000000000006", "Surname","Name","Patronymic","0990","123123","passport","1231123123","Town",null,1),
		("10000000000000000007", "Surname","Name","Patronymic","0990","123123","passport","1231123123","Town",null,1),
		("10000000000000000008", null,null,null,null,"123312421123","inn","1231123123","Town","Organization",2),
		("10000000000000000009", null,null,null,null,"123312421123","inn","1231123123","Town","Organization",2),
		("10000000000000000010", null,null,null,null,"123312421123","inn","1231123123","Town","Organization",2),
		("10000000000000000011", null,null,null,null,"123312421123","inn","1231123123","Town","Organization",2),
		("10000000000000000012", null,null,null,null,"123312421123","inn","1231123123","Town","Organization",2),
		("10000000000000000013", null,null,null,null,"123312421123","inn","1231123123","Town","Organization",2),
		("10000000000000000014", null,null,null,null,"123312421123","inn","1231123123","Town","Organization",2),
		("10000000000000000015", null,null,null,null,"123312421123","inn","1231123123","Town","Organization",2),
		("10000000000000000016", null,null,null,null,"123312421123","inn","1231123123","Town","Organization",2);
