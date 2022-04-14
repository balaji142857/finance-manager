import { BaseModel } from './base.model';

export interface ExpenseModel extends BaseModel {

  asset: number;
  amount: number;
  transactionDate?: number;
  category?: number;
  subCategory?: number;
  comment?: string

}
