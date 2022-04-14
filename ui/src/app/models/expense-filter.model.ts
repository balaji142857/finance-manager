export interface ExpenseFilterModel {
  asset?: number[];
  category?: number[];
  subCategory?: number[];
  fromDate?: string;
  toDate?: string;
  minAmount?: number;
  maxAmount?: number;
  txDetail?: string;
  comment?: string;
}
