
CREATE SEQUENCE seq_asset_id
  MINVALUE 0
  START WITH 1
  INCREMENT BY 1
  CACHE 20;

CREATE table ASSET (
	id INTEGER PRIMARY KEY,
	name varchar(50),
	created  DATE,
	version INTEGER,
	user_comment varchar(100),
	created_by varchar(100),
	usage real,
	last_modified DATE,
	last_modified_by varchar(100)
);


CREATE table HIST_ASSET_AUD (
	id INTEGER,
	name varchar(50),
	created  DATE,
	version INTEGER,
	user_comment varchar(100),
	created_by varchar(100),
	usage real,
	last_modified DATE,
	last_modified_by varchar(100),
	rev smallint,
	revtype smallint
);


CREATE SEQUENCE seq_cat_id
  MINVALUE 0
  START WITH 1
  INCREMENT BY 1
  CACHE 20;

CREATE table CATEGORY (
	id INTEGER PRIMARY KEY,
	name varchar(50),
	created  DATE,
	comment varchar(100),
	version INTEGER,
	created_by varchar(100),
	last_modified DATE,
	last_modified_by varchar(100)
);

CREATE table HIST_CATEGORY_AUD (
	id INTEGER,
	name varchar(50),
	created  DATE,
	comment varchar(100),
	version INTEGER,
	created_by varchar(100),
	last_modified DATE,
	last_modified_by varchar(100),
	rev smallint,
	revtype smallint
);


CREATE SEQUENCE seq_subcat_id
  MINVALUE 0
  START WITH 1
  INCREMENT BY 1
  CACHE 20;


CREATE table SUBCATEGORY (
	id INTEGER PRIMARY KEY,
	name varchar(50),
	cat_id INTEGER,
	created  DATE,
	comment varchar(100),
	version INTEGER,
	created_by varchar(100),
	last_modified DATE,
	last_modified_by varchar(100)
);
alter table SUBCATEGORY add constraint subcat_cat_fk foreign key (cat_id) references CATEGORY(id);

CREATE table HIST_SUBCATEGORY_AUD (
	id INTEGER,
	name varchar(50),
	cat_id INTEGER,
	created  DATE,
	comment varchar(100),
	version INTEGER,
	created_by varchar(100),
	last_modified DATE,
	last_modified_by varchar(100),
	rev smallint,
	revtype smallint
);

CREATE SEQUENCE seq_tag_id
  MINVALUE 0
  START WITH 1
  INCREMENT BY 1
  CACHE 20;


CREATE table TAG (
	id INTEGER PRIMARY KEY,
	name varchar(50),
	created  DATE,
	version INTEGER,
	created_by varchar(100),
	last_modified DATE,
	last_modified_by varchar(100)
);

CREATE table HIST_TAG_AUD (
	id INTEGER,
	name varchar(50),
	created  DATE,
	version INTEGER,
	created_by varchar(100),
	last_modified DATE,
	last_modified_by varchar(100),
	rev smallint,
	revtype smallint
);

CREATE SEQUENCE seq_file_imports_id
  MINVALUE 0
  START WITH 1
  INCREMENT BY 1
  CACHE 20;

CREATE table BANK_STATEMENT_FILE_IMPORTS (  
	id INTEGER PRIMARY KEY,	
	asset_id INTEGER,
	file_name varchar(100),
	import_time DATE,
	verified_time DATE,
	temp_storage varchar(100),
	is_data_imported varchar(1),
	storage_location varchar(500)
);
alter table BANK_STATEMENT_FILE_IMPORTS add constraint imports_file_asset_fk foreign key (asset_id) references ASSET(id);


CREATE SEQUENCE seq_exp_id
  MINVALUE 0
  START WITH 1
  INCREMENT BY 1
  CACHE 20;



CREATE table EXP (
	id INTEGER PRIMARY KEY,
	cat_id INTEGER,
	subcat_id INTEGER,
	asset_id INTEGER,
	amount real,
	tx_date DATE,
	created  DATE,
	version INTEGER,
	transaction_detail varchar(500),
	user_comment varchar(100),
	created_by varchar(100),
	last_modified DATE,
	last_modified_by varchar(100),
	source varchar(2),
	file_import_id INTEGER
);
alter table EXP add constraint exp_asset_fk foreign key (asset_id) references ASSET(id);
alter table EXP add constraint exp_cat_fk foreign key (cat_id) references CATEGORY(id);
alter table EXP add constraint exp_subcat_fk foreign key (subcat_id) references SUBCATEGORY(id);
alter table EXP add constraint exp_file_fk foreign key (file_import_id) references BANK_STATEMENT_FILE_IMPORTS(id);

CREATE table HIST_EXP_AUD (
	id INTEGER,
	cat_id INTEGER,
	subcat_id INTEGER,
	asset_id INTEGER,
	amount real,
	tx_date DATE,
	created  DATE,
	version INTEGER,
	transaction_detail varchar(500),
	user_comment varchar(100),
	created_by varchar(100),
	last_modified DATE,
	last_modified_by varchar(100),
	rev smallint,
	revtype smallint
);

CREATE table TAG_EXP (
	tag_id INTEGER,
	exp_id INTEGER
);
alter table TAG_EXP add constraint jt_te_t_fk foreign key (tag_id) references TAG(id);
alter table TAG_EXP add constraint jt_te_e_fk foreign key (exp_id) references EXP(id);



CREATE table DASHBOARD_CONFIG (
	id INTEGER PRIMARY KEY,
	chart_type varchar(200),
	date_grouping_oracle_format varchar(100),
	date_grouping_java_format varchar(100),
	date_label_format varchar(100),
	chart_label varchar(500)
);
alter table  DASHBOARD_CONFIG add constraint dashboard_chart_type_unique UNIQUE(chart_type);

insert into DASHBOARD_CONFIG values (1, 'getExpenseByYearMonth', 'YYYYMM', 'yyyyMM', 'MMM YY', 'Expenses by Month');
insert into DASHBOARD_CONFIG values (2, 'getExpenseByMonthDay', 'YYYYMMDD', 'yyyyMMdd', 'dd MMM', 'Expenses by Day');
insert into DASHBOARD_CONFIG values (3, 'getExpenseByCategories', null, null, null, 'Expenses by Category');
insert into DASHBOARD_CONFIG values (4, 'getAssetUsage', null, null, null, 'Expenses by asset');

	
insert into ASSET(id, name, created, version, user_comment, created_by, usage, last_modified, last_modified_by) 
	values(nextval('seq_asset_id'), 'AXIS', CURRENT_DATE, 0, 'AUTO CREATED', 'SYSTEM',0,null, null);
insert into ASSET(id, name, created, version, user_comment, created_by, usage, last_modified, last_modified_by) 
 	values(nextval('seq_asset_id'), 'ICICI', CURRENT_DATE, 0, 'AUTO CREATED', 'SYSTEM',0,null,null);
insert into ASSET(id, name, created, version, user_comment, created_by, usage, last_modified, last_modified_by) 
 	values(nextval('seq_asset_id'), 'HDFC', CURRENT_DATE, 0, 'AUTO CREATED', 'SYSTEM',0,null,null);
insert into ASSET(id, name, created, version, user_comment, created_by, usage, last_modified, last_modified_by) 
 	values(nextval('seq_asset_id'), 'CASH', CURRENT_DATE, 0, 'AUTO CREATED', 'SYSTEM',0,null,null);		


insert into CATEGORY (id, name, created, comment, version, created_by, last_modified, last_modified_by) 
	values (nextval('seq_cat_id'),'Entertainment',CURRENT_DATE, null, 0, 'SYSTEM',null, null); 
insert into SUBCATEGORY ( id, name, cat_id, created, comment, version, created_by, last_modified, last_modified_by)
 values (nextval('seq_subcat_id'), 'Netflix', (select id from CATEGORY where name='Entertainment'), CURRENT_DATE, null, 0, 'SYSTEM', null, null);
insert into SUBCATEGORY ( id, name, cat_id, created, comment, version, created_by, last_modified, last_modified_by)
 values (nextval('seq_subcat_id'), 'Disney Hotstar', (select id from CATEGORY where name='Entertainment'), CURRENT_DATE, null, 0, 'SYSTEM', null, null);
insert into SUBCATEGORY ( id, name, cat_id, created, comment, version, created_by, last_modified, last_modified_by)
 values (nextval('seq_subcat_id'), 'Amazon Prime', (select id from CATEGORY where name='Entertainment'), CURRENT_DATE, null, 0, 'SYSTEM', null, null);


insert into CATEGORY (id, name, created, comment, version, created_by, last_modified, last_modified_by) 
	values (nextval('seq_cat_id'),'Travel',CURRENT_DATE, null, 0, 'SYSTEM',null, null);
insert into SUBCATEGORY ( id, name, cat_id, created, comment, version, created_by, last_modified, last_modified_by)
 values (nextval('seq_subcat_id'), 'RedBus', (select id from CATEGORY where name='Travel'), CURRENT_DATE, null, 0, 'SYSTEM', null, null);
insert into SUBCATEGORY ( id, name, cat_id, created, comment, version, created_by, last_modified, last_modified_by)
 values (nextval('seq_subcat_id'), 'Irctc', (select id from CATEGORY where name='Travel'), CURRENT_DATE, null, 0, 'SYSTEM', null, null);
insert into SUBCATEGORY ( id, name, cat_id, created, comment, version, created_by, last_modified, last_modified_by)
 values (nextval('seq_subcat_id'), 'OLA', (select id from CATEGORY where name='Travel'), CURRENT_DATE, null, 0, 'SYSTEM', null, null);
insert into SUBCATEGORY ( id, name, cat_id, created, comment, version, created_by, last_modified, last_modified_by)
 values (nextval('seq_subcat_id'), 'Uber', (select id from CATEGORY where name='Travel'), CURRENT_DATE, null, 0, 'SYSTEM', null, null);

insert into CATEGORY (id, name, created, comment, version, created_by, last_modified, last_modified_by) 
	values (nextval('seq_cat_id'),'Medicine',CURRENT_DATE, null, 0, 'SYSTEM',null, null);
insert into SUBCATEGORY ( id, name, cat_id, created, comment, version, created_by, last_modified, last_modified_by)
 values (nextval('seq_subcat_id'), 'NetMeds', (select id from CATEGORY where name='Medicine'), CURRENT_DATE, null, 0, 'SYSTEM', null, null);
insert into SUBCATEGORY ( id, name, cat_id, created, comment, version, created_by, last_modified, last_modified_by)
 values (nextval('seq_subcat_id'), 'Local Shop', (select id from CATEGORY where name='Medicine'), CURRENT_DATE, null, 0, 'SYSTEM', null, null);
insert into SUBCATEGORY ( id, name, cat_id, created, comment, version, created_by, last_modified, last_modified_by)
 values (nextval('seq_subcat_id'), 'Amazon Pharmacy', (select id from CATEGORY where name='Medicine'), CURRENT_DATE, null, 0, 'SYSTEM', null, null);


insert into CATEGORY (id, name, created, comment, version, created_by, last_modified, last_modified_by) 
	values (nextval('seq_cat_id'),'Insurance',CURRENT_DATE, null, 0, 'SYSTEM',null, null);
insert into SUBCATEGORY ( id, name, cat_id, created, comment, version, created_by, last_modified, last_modified_by)
 values (nextval('seq_subcat_id'), 'LIC', (select id from CATEGORY where name='Insurance'), CURRENT_DATE, null, 0, 'SYSTEM', null, null);
insert into SUBCATEGORY ( id, name, cat_id, created, comment, version, created_by, last_modified, last_modified_by)
 values (nextval('seq_subcat_id'), 'ICICI Lombard', (select id from CATEGORY where name='Insurance'), CURRENT_DATE, null, 0, 'SYSTEM', null, null);
insert into SUBCATEGORY ( id, name, cat_id, created, comment, version, created_by, last_modified, last_modified_by)
 values (nextval('seq_subcat_id'), 'HDFC Life', (select id from CATEGORY where name='Insurance'), CURRENT_DATE, null, 0, 'SYSTEM', null, null);


insert into CATEGORY (id, name, created, comment, version, created_by, last_modified, last_modified_by) 
	values (nextval('seq_cat_id'),'Shopping',CURRENT_DATE, null, 0, 'SYSTEM',null, null);
insert into SUBCATEGORY ( id, name, cat_id, created, comment, version, created_by, last_modified, last_modified_by)
 values (nextval('seq_subcat_id'), 'Flipkart', (select id from CATEGORY where name='Shopping'), CURRENT_DATE, null, 0, 'SYSTEM', null, null);
insert into SUBCATEGORY ( id, name, cat_id, created, comment, version, created_by, last_modified, last_modified_by)
 values (nextval('seq_subcat_id'), 'Amazon', (select id from CATEGORY where name='Shopping'), CURRENT_DATE, null, 0, 'SYSTEM', null, null);
insert into SUBCATEGORY ( id, name, cat_id, created, comment, version, created_by, last_modified, last_modified_by)
 values (nextval('seq_subcat_id'), 'CLIQ', (select id from CATEGORY where name='Shopping'), CURRENT_DATE, null, 0, 'SYSTEM', null, null);
insert into SUBCATEGORY ( id, name, cat_id, created, comment, version, created_by, last_modified, last_modified_by)
 values (nextval('seq_subcat_id'), 'Decathalon', (select id from CATEGORY where name='Shopping'), CURRENT_DATE, null, 0, 'SYSTEM', null, null);   


insert into CATEGORY (id, name, created, comment, version, created_by, last_modified, last_modified_by) 
	values (nextval('seq_cat_id'),'Mobile recharge',CURRENT_DATE, null, 0, 'SYSTEM',null, null);
insert into SUBCATEGORY ( id, name, cat_id, created, comment, version, created_by, last_modified, last_modified_by)
 values (nextval('seq_subcat_id'), 'Vodafone', (select id from CATEGORY where name='Mobile recharge'), CURRENT_DATE, null, 0, 'SYSTEM', null, null);   
insert into SUBCATEGORY ( id, name, cat_id, created, comment, version, created_by, last_modified, last_modified_by)
 values (nextval('seq_subcat_id'), 'Airtel', (select id from CATEGORY where name='Mobile recharge'), CURRENT_DATE, null, 0, 'SYSTEM', null, null);   
insert into SUBCATEGORY ( id, name, cat_id, created, comment, version, created_by, last_modified, last_modified_by)
 values (nextval('seq_subcat_id'), 'Jio', (select id from CATEGORY where name='Mobile recharge'), CURRENT_DATE, null, 0, 'SYSTEM', null, null);   
insert into SUBCATEGORY ( id, name, cat_id, created, comment, version, created_by, last_modified, last_modified_by)
 values (nextval('seq_subcat_id'), 'BSNL', (select id from CATEGORY where name='Mobile recharge'), CURRENT_DATE, null, 0, 'SYSTEM', null, null);      	


insert into CATEGORY (id, name, created, comment, version, created_by, last_modified, last_modified_by) 
	values (nextval('seq_cat_id'),'Groceries',CURRENT_DATE, null, 0, 'SYSTEM',null, null);
insert into SUBCATEGORY ( id, name, cat_id, created, comment, version, created_by, last_modified, last_modified_by)
 values (nextval('seq_subcat_id'), 'Yellow Mart', (select id from CATEGORY where name='Groceries'), CURRENT_DATE, null, 0, 'SYSTEM', null, null); 
insert into SUBCATEGORY ( id, name, cat_id, created, comment, version, created_by, last_modified, last_modified_by)
 values (nextval('seq_subcat_id'), 'BigBasket', (select id from CATEGORY where name='Groceries'), CURRENT_DATE, null, 0, 'SYSTEM', null, null); 
insert into SUBCATEGORY ( id, name, cat_id, created, comment, version, created_by, last_modified, last_modified_by)
 values (nextval('seq_subcat_id'), 'More', (select id from CATEGORY where name='Groceries'), CURRENT_DATE, null, 0, 'SYSTEM', null, null); 
insert into SUBCATEGORY ( id, name, cat_id, created, comment, version, created_by, last_modified, last_modified_by)
 values (nextval('seq_subcat_id'), 'Amazon Fresh', (select id from CATEGORY where name='Groceries'), CURRENT_DATE, null, 0, 'SYSTEM', null, null);    	
insert into SUBCATEGORY ( id, name, cat_id, created, comment, version, created_by, last_modified, last_modified_by)
 values (nextval('seq_subcat_id'), 'Pazhamudir', (select id from CATEGORY where name='Groceries'), CURRENT_DATE, null, 0, 'SYSTEM', null, null); 

insert into CATEGORY (id, name, created, comment, version, created_by, last_modified, last_modified_by) 
	values (nextval('seq_cat_id'),'Food',CURRENT_DATE, null, 0, 'SYSTEM',null, null);
insert into SUBCATEGORY ( id, name, cat_id, created, comment, version, created_by, last_modified, last_modified_by)
 values (nextval('seq_subcat_id'), 'Swiggy', (select id from CATEGORY where name='Food'), CURRENT_DATE, null, 0, 'SYSTEM', null, null); 
insert into SUBCATEGORY ( id, name, cat_id, created, comment, version, created_by, last_modified, last_modified_by)
 values (nextval('seq_subcat_id'), 'Zomato', (select id from CATEGORY where name='Food'), CURRENT_DATE, null, 0, 'SYSTEM', null, null); 
insert into SUBCATEGORY ( id, name, cat_id, created, comment, version, created_by, last_modified, last_modified_by)
 values (nextval('seq_subcat_id'), 'A2B', (select id from CATEGORY where name='Food'), CURRENT_DATE, null, 0, 'SYSTEM', null, null);
 


insert into CATEGORY (id, name, created, comment, version, created_by, last_modified, last_modified_by) 
	values (nextval('seq_cat_id'),'Fuel',CURRENT_DATE, null, 0, 'SYSTEM',null, null);
insert into SUBCATEGORY ( id, name, cat_id, created, comment, version, created_by, last_modified, last_modified_by)
 	values (nextval('seq_subcat_id'), 'My Car', (select id from CATEGORY where name='Fuel'), CURRENT_DATE, null, 0, 'SYSTEM', null, null); 

insert into CATEGORY (id, name, created, comment, version, created_by, last_modified, last_modified_by) 
	values (nextval('seq_cat_id'),'Vyoma',CURRENT_DATE, null, 0, 'SYSTEM',null, null);
insert into SUBCATEGORY ( id, name, cat_id, created, comment, version, created_by, last_modified, last_modified_by)
	values (nextval('seq_subcat_id'), 'Property tax', (select id from CATEGORY where name='Vyoma'), CURRENT_DATE, null, 0, 'SYSTEM', null, null);
insert into SUBCATEGORY ( id, name, cat_id, created, comment, version, created_by, last_modified, last_modified_by)
	values (nextval('seq_subcat_id'), 'Maintenance', (select id from CATEGORY where name='Vyoma'), CURRENT_DATE, null, 0, 'SYSTEM', null, null);	



insert into CATEGORY (id, name, created, comment, version, created_by, last_modified, last_modified_by) 
	values (nextval('seq_cat_id'),'MapleHeights',CURRENT_DATE, null, 0, 'SYSTEM',null, null);
insert into SUBCATEGORY ( id, name, cat_id, created, comment, version, created_by, last_modified, last_modified_by)
 	values (nextval('seq_subcat_id'), 'Rent', (select id from CATEGORY where name='MapleHeights'), CURRENT_DATE, null, 0, 'SYSTEM', null, null);	
insert into SUBCATEGORY ( id, name, cat_id, created, comment, version, created_by, last_modified, last_modified_by)
	values (nextval('seq_subcat_id'), 'Electricity', (select id from CATEGORY where name='MapleHeights'), CURRENT_DATE, null, 0, 'SYSTEM', null, null);
insert into SUBCATEGORY ( id, name, cat_id, created, comment, version, created_by, last_modified, last_modified_by)
 	values (nextval('seq_subcat_id'), 'Milk', (select id from CATEGORY where name='MapleHeights'), CURRENT_DATE, null, 0, 'SYSTEM', null, null);
insert into SUBCATEGORY ( id, name, cat_id, created, comment, version, created_by, last_modified, last_modified_by)
 	values (nextval('seq_subcat_id'), 'Water', (select id from CATEGORY where name='MapleHeights'), CURRENT_DATE, null, 0, 'SYSTEM', null, null);
insert into SUBCATEGORY ( id, name, cat_id, created, comment, version, created_by, last_modified, last_modified_by)
 	values (nextval('seq_subcat_id'), 'Internet', (select id from CATEGORY where name='MapleHeights'), CURRENT_DATE, null, 0, 'SYSTEM', null, null);
insert into SUBCATEGORY ( id, name, cat_id, created, comment, version, created_by, last_modified, last_modified_by)
 	values (nextval('seq_subcat_id'), 'Maintenance', (select id from CATEGORY where name='MapleHeights'), CURRENT_DATE, null, 0, 'SYSTEM', null, null); 
insert into SUBCATEGORY ( id, name, cat_id, created, comment, version, created_by, last_modified, last_modified_by)
 	values (nextval('seq_subcat_id'), 'NewsPaper', (select id from CATEGORY where name='MapleHeights'), CURRENT_DATE, null, 0, 'SYSTEM', null, null); 	

insert into CATEGORY (id, name, created, comment, version, created_by, last_modified, last_modified_by) 
	values (nextval('seq_cat_id'),'Home',CURRENT_DATE, null, 0, 'SYSTEM',null, null);
insert into SUBCATEGORY ( id, name, cat_id, created, comment, version, created_by, last_modified, last_modified_by)
 	values (nextval('seq_subcat_id'), 'Rent', (select id from CATEGORY where name='Home'), CURRENT_DATE, null, 0, 'SYSTEM', null, null);	
insert into SUBCATEGORY ( id, name, cat_id, created, comment, version, created_by, last_modified, last_modified_by)
	values (nextval('seq_subcat_id'), 'Electricity', (select id from CATEGORY where name='Home'), CURRENT_DATE, null, 0, 'SYSTEM', null, null);
insert into SUBCATEGORY ( id, name, cat_id, created, comment, version, created_by, last_modified, last_modified_by)
 	values (nextval('seq_subcat_id'), 'Milk', (select id from CATEGORY where name='Home'), CURRENT_DATE, null, 0, 'SYSTEM', null, null);
insert into SUBCATEGORY ( id, name, cat_id, created, comment, version, created_by, last_modified, last_modified_by)
 	values (nextval('seq_subcat_id'), 'Water', (select id from CATEGORY where name='Home'), CURRENT_DATE, null, 0, 'SYSTEM', null, null);
insert into SUBCATEGORY ( id, name, cat_id, created, comment, version, created_by, last_modified, last_modified_by)
 	values (nextval('seq_subcat_id'), 'Internet', (select id from CATEGORY where name='Home'), CURRENT_DATE, null, 0, 'SYSTEM', null, null);
insert into SUBCATEGORY ( id, name, cat_id, created, comment, version, created_by, last_modified, last_modified_by)
 	values (nextval('seq_subcat_id'), 'Maintenance', (select id from CATEGORY where name='Home'), CURRENT_DATE, null, 0, 'SYSTEM', null, null); 
insert into SUBCATEGORY ( id, name, cat_id, created, comment, version, created_by, last_modified, last_modified_by)
 	values (nextval('seq_subcat_id'), 'NewsPaper', (select id from CATEGORY where name='Home'), CURRENT_DATE, null, 0, 'SYSTEM', null, null); 	 	

commit;