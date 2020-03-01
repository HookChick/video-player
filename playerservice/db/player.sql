create database player default CHARACTER SET UTF8;
use player;

select * from users where uname='zhang' and password='123';
create table users(
uid int primary key auto_increment,
uname varchar(10) not null,
password varchar(10) not null,
role tinyint(1) not null default 0,
constraint uq_uname unique(uname)
);/*0��ʾ��ͨ�û���1��ʾ�������Ա*/

insert into users values(1,'zhang','123',0);
/*�����û����admin�˻�����*/
insert into users values(2,'admin','123',1);

create table account(
aid int primary key auto_increment,
userid int not null,
balance float default NULL
);
alter table account add constraint fk_userid_id foreign key(userid) references users(uid);
insert into account values(1,1,1000);
insert into account values(2,2,0);

create table payitem(
payid int primary key auto_increment,
userid int not null,
videoid varchar(15),
paytype boolean default false
);/*paytype false ��ʾû��ȫ��֧����true��ʾ����֧����ȫ��֧��*/
alter table payitem add constraint fk_userid_id foreign key(userid) references users(uid);

insert into payitem values(1,1,2,false);