import { Component, OnInit, ViewChild } from '@angular/core';
import {animate, state, style, transition, trigger} from '@angular/animations';

import { ExpenseFilterModel } from '../models/expense-filter.model';
import { ExpenseModel } from '../models/expense.model';
import { MatPaginator } from '@angular/material/paginator';
import { MatDialog } from '@angular/material/dialog';
import { ExpenseDialogComponent } from '../dialogs/expense-dialog/expense-dialog.component';
import { CategoryModel } from '../models/category.model';
import { Asset as AssetModel } from '../models/asset.model';
import { ActivatedRoute } from '@angular/router';
import { UtilService } from 'src/common/util.service';
import { RestService } from '../rest.service';
import { GenericDialogComponent } from '../dialogs/generic-dialog/generic-dialog.component';
import { MatSort } from '@angular/material/sort';
import { merge, of } from 'rxjs';
import { catchError,map, startWith, switchMap } from 'rxjs/operators';
import * as config from '../../common/config';
import { saveAs } from 'file-saver';

@Component({
  selector: 'app-expense',
  templateUrl: './expense.component.html',
  styleUrls: ['./expense.component.scss'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({height: '0px', minHeight: '0'})),
      state('expanded', style({height: '*'})),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ])
  ]
})
export class ExpenseComponent implements OnInit {

  panelOpenState = true;
  categories: CategoryModel[] = [];
  assets: AssetModel[] = [];
  expenses: ExpenseModel[] = [];
  dataSource = [];
  ELEMENT_DATA = { data:[] };
  columnsToDisplay = ['asset','category','subCategory','amount','date','actions'];
  footerColumns = ['subCategory','amount']
  totalAmount =  0;
  appliedFilterObj: ExpenseFilterModel;
  appliedFilterResultsLength;
  expandedElement: ExpenseModel | null;

  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;

  constructor(private dialog: MatDialog,
    public util: UtilService,
    private service: RestService,
    private route: ActivatedRoute) {
      this.categories = route.snapshot.data['categories'];
      this.assets = route.snapshot.data['assets'];
      this.expenses= route.snapshot.data['expenses'];
      this.ELEMENT_DATA.data = this.expenses;
      this.appliedFilterObj = this.service.getDefaultExpenseFilter().data;
  }

  ngOnInit() {
    this.dataSource = this.ELEMENT_DATA.data
  }

  ngAfterViewInit() {
    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);
    merge(this.sort.sortChange, this.paginator.page)
    .pipe(
        startWith({}),
        switchMap(() =>  this.service.filterExpense({
          options: {
            pageIndex: this.paginator.pageIndex,
            pageSize: this.paginator.pageSize,
            sort: this.sort.active,
            sortDir: this.sort.direction
          },
          data: this.appliedFilterObj})),
        map(response =>  {
          this.appliedFilterResultsLength = (<any>response).overallCount;
          this.totalAmount = this.getTotalAmount((<any>response).data);
          return (<any>response).data;
        }),
        catchError(() =>   of([]))
    ).subscribe(result => this.dataSource = result);
  }


  applyFilter(filterObj) {
    this.appliedFilterObj = filterObj;
    this.paginator.pageIndex = 0
    this.paginator._changePageSize(this.paginator.pageSize);
  }

  openExpense(exp: ExpenseModel, event) {
    const dialogRef = this.dialog.open(ExpenseDialogComponent, {
      width: '100vw',
      height: '50vh',
      data: {
        model: exp,
        categories: this.categories,
        assets: this.assets
      }
    });
    dialogRef.afterClosed().subscribe(data => {
      if (data && data.confirmed) {
        this.util.openSnackBar(config.default.messages.expSaved);
        this.paginator._changePageSize(this.paginator.pageSize);
      }
    });
    this.stopEvent(event);
  }

  deleteExpense(exp: ExpenseModel, event) {
    const ref = this.dialog.open(GenericDialogComponent, {
      data: {
        confirmationMessage: 'Delete this Expense ?',
        confirmText: 'DELETE',
        cancelText: 'CANCEL'
      }
    });
    ref.afterClosed().subscribe(
      data => {
        if (data && data.confirmed) {
          this.service.deleteExpense(exp.id).subscribe(
            data => {
              this.util.openSnackBar(config.default.messages.expDeleted)
              this.paginator._changePageSize(this.paginator.pageSize)
            },
            err => this.util.showError(err, config.default.messages.expDeleteErr)
          );
        }
      }
    )
    this.stopEvent(event);
  }

  stopEvent(event) {
    event.preventDefault();
    event.stopPropagation();
  }

  downloadExpenses() {
    this.service.downloadExpenses().subscribe(data => saveAs(data, 'ApplicationExport.xls'));
  }

  getTotalAmount(values: any[]) {
    return values.map(t => t.amount).reduce((acc, value) => acc + value, 0);
  }



}
