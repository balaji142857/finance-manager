import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { RestService } from 'src/app/rest.service';
import { ExpenseModel } from 'src/app/models/expense.model';
import { CategoryModel } from 'src/app/models/category.model';
import { Asset } from 'src/app/models/asset.model';

@Component({
  selector: 'app-expense-dialog',
  templateUrl: './expense-dialog.component.html',
  styleUrls: ['./expense-dialog.component.scss']
})
export class ExpenseDialogComponent implements OnInit {

  exp: ExpenseModel;
  categories: CategoryModel[] = [];
  assets: Asset[] = [];
  constructor(@Inject(MAT_DIALOG_DATA) public data,
  private service: RestService,
  private dialog: MatDialogRef<ExpenseDialogComponent>) {
    this.exp = data.model ? data.model  : this.getDefaultExpenseModel();
    this.categories = data.categories;
    this.assets = data.assets;
  }

  ngOnInit(): void {
  }

  cancel() {
    this.dialog.close();
  }

  confirm() {
    this.service.saveExpense(this.exp).subscribe(data =>
      this.dialog.close({ confirmed: true }), err =>  this.dialog.close({}));
    }

  getDefaultExpenseModel(): ExpenseModel {
    return {
      id: null,
      amount: null,
      asset: null,
      comment: null,
      category: null,
      subCategory: null,
      transactionDate: null
    };
  }

}
