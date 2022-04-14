import { BaseModel } from './base.model';


export interface CategoryModel extends BaseModel{
  name: string;
  comment?: string;
  subCategories:SubCategoryModel[];
}

export interface SubCategoryModel extends BaseModel {
  name: string;
  comment?: string;
  isEdited?: boolean;
}
