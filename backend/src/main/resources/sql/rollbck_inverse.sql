
DROP SEQUENCE seq_asset_id;
  

DROP table ASSET;


DROP table HIST_ASSET_AUD ;


DROP SEQUENCE seq_cat_id;

DROP table CATEGORY;

DROP table HIST_CATEGORY_AUD;


DROP SEQUENCE seq_subcat_id;


alter table SUBCATEGORY drop constraint subcat_cat_fk;
DROP table SUBCATEGORY;

DROP table HIST_SUBCATEGORY_AUD;

DROP SEQUENCE seq_tag_id;


DROP table TAG;

DROP table HIST_TAG_AUD;

alter table TAG_EXP drop constraint jt_te_t_fk;
alter table TAG_EXP drop constraint jt_te_e_fk;
DROP table TAG_EXP;


DROP SEQUENCE seq_exp_id;



alter table EXP drop constraint exp_asset_fk;
alter table EXP drop constraint exp_cat_fk;
alter table EXP drop constraint exp_subcat_fk;
DROP table EXP;

DROP table HIST_EXP_AUD;