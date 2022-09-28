import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import * as env from '../environments/environment';
import { Asset } from './models/asset.model';
import { CategoryModel } from './models/category.model';
import { ExpenseModel } from './models/expense.model';
import { ChartDataModel } from './models/chart-data.model';
import { FileModel } from 'src/common/file-upload/file.model';
import { ExpenseFilterModel } from './models/expense-filter.model';
import { SearchModel } from './models/search.model';

@Injectable({
  providedIn: 'root'
})
export class RestService {

  basePath = env.environment.url;

  constructor(private http: HttpClient) { }

  getAssets(): Observable<Asset[]> {
    return this.http.get<Asset[]>(this.basePath+'assets/');
  }

  getImportFormats(): Observable<Asset[]> {
    return this.http.get<Asset[]>(this.basePath+'static/import-formats');
  }

  saveAsset(asset: Asset): Observable<Asset> {
    return this.http.post<Asset>(this.basePath+'assets/', asset);
  }

  deleteAsset(id: number) {
    return this.http.post(this.basePath+'assets/'+id,null);
  }

  getExpenseCategories(): Observable<CategoryModel[]> {
    return this.http.get<CategoryModel[]>(this.basePath+'categories/');
  }

  saveCategory(cat: CategoryModel) {
    return this.http.post(this.basePath+'categories/', cat);
  }

  deleteCategory(id: number) {
    return this.http.post(this.basePath+'categories/delete/'+id,null);
  }

  listExpenses(): Observable<ExpenseModel[]> {
    return this.http.get<ExpenseModel[]>(this.basePath+'expenses');
  }

  saveExpense(exp: ExpenseModel) {
    return this.http.post(this.basePath+'expenses/',[exp]);
  }

  saveImportVerifiedExpenses(expenses) {
    return this.http.post(this.basePath+'expenses/import/save',expenses)
  }

  deleteExpense(expId: number) {
    return this.http.post(this.basePath+'expenses/delete/'+expId, null);
  }

  filterExpense(searchModel: SearchModel<ExpenseFilterModel>) {
    return this.http.post(this.basePath+'expenses/filter', searchModel );
  }

  importExpenses(files: FileModel[]) {
      const formData: FormData = new FormData();
      if (files) {
        for (let i = 0; i < files.length; i++) {
          formData.append('files', files[i].content, files[i].name);
        }
        return this.http.post(this.basePath+'expenses/import',formData);
      }
  }

  getDailyExpenses(obj): Observable<ChartDataModel> {
    return this.http.post<ChartDataModel>(this.basePath+'dashboard/'+'getExpenseByMonthDay', obj);
  }

  getMonthlyExpenses(obj): Observable<ChartDataModel> {
    return this.http.post<ChartDataModel>(this.basePath+'dashboard/'+'getExpenseByYearMonth', obj);
  }

  getExpensesByCategory(obj): Observable<ChartDataModel>  {
    return this.http.post<ChartDataModel>(this.basePath+'dashboard/'+'getExpenseByCategories', obj);
  }

  getAssetUsage(obj: any): Observable<ChartDataModel>  {
    return this.http.post<ChartDataModel>(this.basePath+'dashboard/'+'getAssetUsage', obj);
  }

  downloadExpenses(): Observable<Blob> {
    return this.http.post(this.basePath+'expenses/download', null,  {
      responseType: 'blob'
    })
}

  getDefaultExpenseFilter() {
    return {
      options: {
        pageSize: 10,
        pageIndex: 0
      },
      data: {
        asset: [],
        category: [],
        subCategory: [],
        fromDate: null,
        toDate: null,
        minAmount: null,
        maxAmount: null,
        txDetail: null,
        comment: null
      }
    }
  }

  downloadFile(data: any) {
    const blob = new Blob(data , { type: 'text/csv' });
    const url= window.URL.createObjectURL(blob);
    window.open(url);
  }

}

