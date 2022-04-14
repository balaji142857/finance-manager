
CREATE SEQUENCE seq_asset_id
  MINVALUE 0
  START WITH 1
  INCREMENT BY 1
  CACHE 20;

CREATE table ASSET (
	id NUMBER(8) PRIMARY KEY,
	name VARCHAR2(50),
	created  DATE,
	version NUMBER(5),
	user_comment varchar2(100),
	created_by varchar2(100),
	usage NUMBER(30,2),
	last_modified DATE,
	last_modified_by varchar2(100)
);


CREATE table HIST_ASSET_AUD (
	id NUMBER(8),
	name VARCHAR2(50),
	created  DATE,
	version NUMBER(5),
	user_comment varchar2(100),
	created_by varchar2(100),
	usage NUMBER(30,2),
	last_modified DATE,
	last_modified_by varchar2(100),
	rev number(12),
	revtype number(2)
);


CREATE SEQUENCE seq_cat_id
  MINVALUE 0
  START WITH 1
  INCREMENT BY 1
  CACHE 20;

CREATE table CATEGORY (
	id NUMBER(8) PRIMARY KEY,
	name VARCHAR2(50),
	created  DATE,
	comment VARCHAR2(100),
	version NUMBER(5),
	created_by varchar2(100),
	last_modified DATE,
	last_modified_by varchar2(100)
);

CREATE table HIST_CATEGORY_AUD (
	id NUMBER(8),
	name VARCHAR2(50),
	created  DATE,
	comment VARCHAR2(100),
	version NUMBER(5),
	created_by varchar2(100),
	last_modified DATE,
	last_modified_by varchar2(100),
	rev number(12),
	revtype number(2)
);


CREATE SEQUENCE seq_subcat_id
  MINVALUE 0
  START WITH 1
  INCREMENT BY 1
  CACHE 20;


CREATE table SUBCATEGORY (
	id NUMBER(8) PRIMARY KEY,
	name VARCHAR2(50),
	cat_id NUMBER(8),
	created  DATE,
	comment VARCHAR2(100),
	version NUMBER(5),
	created_by varchar2(100),
	last_modified DATE,
	last_modified_by varchar2(100)
);
alter table SUBCATEGORY add constraint subcat_cat_fk foreign key (cat_id) references CATEGORY(id);

CREATE table HIST_SUBCATEGORY_AUD (
	id NUMBER(8),
	name VARCHAR2(50),
	cat_id NUMBER(8),
	created  DATE,
	comment VARCHAR2(100),
	version NUMBER(5),
	created_by varchar2(100),
	last_modified DATE,
	last_modified_by varchar2(100),
	rev number(12),
	revtype number(2)
);

CREATE SEQUENCE seq_tag_id
  MINVALUE 0
  START WITH 1
  INCREMENT BY 1
  CACHE 20;


CREATE table TAG (
	id NUMBER(8) PRIMARY KEY,
	name VARCHAR2(50),
	created  DATE,
	version NUMBER(5),
	created_by varchar2(100),
	last_modified DATE,
	last_modified_by varchar2(100)
);

CREATE table HIST_TAG_AUD (
	id NUMBER(8),
	name VARCHAR2(50),
	created  DATE,
	version NUMBER(5),
	created_by varchar2(100),
	last_modified DATE,
	last_modified_by varchar2(100),
	rev number(12),
	revtype number(2)
);


CREATE SEQUENCE seq_exp_id
  MINVALUE 0
  START WITH 1
  INCREMENT BY 1
  CACHE 20;



CREATE table EXP (
	id NUMBER(12) PRIMARY KEY,
	cat_id NUMBER(8),
	subcat_id NUMBER(8),
	asset_id NUMBER(8),
	amount NUMBER(12,2),
	tx_date DATE,
	created  DATE,
	version NUMBER(5),
	transaction_detail varchar2(500),
	user_comment varchar2(100),
	created_by varchar2(100),
	last_modified DATE,
	last_modified_by varchar2(100)
);
alter table EXP add constraint exp_asset_fk foreign key (asset_id) references ASSET(id);
alter table EXP add constraint exp_cat_fk foreign key (cat_id) references CATEGORY(id);
alter table EXP add constraint exp_subcat_fk foreign key (subcat_id) references SUBCATEGORY(id);

CREATE table HIST_EXP_AUD (
	id NUMBER(12),
	cat_id NUMBER(8),
	subcat_id NUMBER(8),
	asset_id NUMBER(8),
	amount NUMBER(12,2),
	tx_date DATE,
	created  DATE,
	version NUMBER(5),
	transaction_detail varchar2(500),
	user_comment varchar2(100),
	created_by varchar2(100),
	last_modified DATE,
	last_modified_by varchar2(100),
	rev number(12),
	revtype number(2)
);

CREATE table TAG_EXP (
	tag_id NUMBER(8),
	exp_id NUMBER(8)
);
alter table TAG_EXP add constraint jt_te_t_fk foreign key (tag_id) references TAG(id);
alter table TAG_EXP add constraint jt_te_e_fk foreign key (exp_id) references EXP(id);



CREATE table DASHBOARD_CONFIG (
	id NUMBER(12) PRIMARY KEY,
	chart_type varchar2(200),
	date_grouping_oracle_format varchar2(100),
	date_grouping_java_format varchar2(100),
	date_label_format varchar2(100),
	chart_label varchar2(500)
);
alter table  DASHBOARD_CONFIG add constraint dashboard_chart_type_unique UNIQUE(chart_type);

insert into DASHBOARD_CONFIG values (1, 'getExpenseByYearMonth', 'YYYYMM', 'yyyyMM', 'MMM YY', 'Expenses by Month');
insert into DASHBOARD_CONFIG values (2, 'getExpenseByMonthDay', 'YYYYMMDD', 'yyyyMMdd', 'dd MMM', 'Expenses by Day');
insert into DASHBOARD_CONFIG values (3, 'getExpenseByCategories', null, null, null, 'Expenses by Category');
insert into DASHBOARD_CONFIG values (4, 'getAssetUsage', null, null, null, 'Expenses by asset');

commit;