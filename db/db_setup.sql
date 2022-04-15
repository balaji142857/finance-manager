
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
	last_modified_by varchar(100)
);
alter table EXP add constraint exp_asset_fk foreign key (asset_id) references ASSET(id);
alter table EXP add constraint exp_cat_fk foreign key (cat_id) references CATEGORY(id);
alter table EXP add constraint exp_subcat_fk foreign key (subcat_id) references SUBCATEGORY(id);

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

commit;