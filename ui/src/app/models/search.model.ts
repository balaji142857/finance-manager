export interface SearchModel<T> {
  options: {
    pageSize?: number;
    pageIndex?: number;
    sort?: string;
    sortDir?:  'asc' | 'desc' | '';
  },
  data: T
}
