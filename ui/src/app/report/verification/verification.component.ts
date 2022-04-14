import { Component, OnInit } from '@angular/core';
import {animate, state, style, transition, trigger} from '@angular/animations';
import { ActivatedRoute, Router } from '@angular/router';
import { ExpenseModel } from 'src/app/models/expense.model';
import { SharedDataService } from 'src/app/services/shared-data.service';
import { UtilService } from 'src/common/util.service';

@Component({
  selector: 'app-verification',
  templateUrl: './verification.component.html',
  styleUrls: ['./verification.component.scss'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({height: '0px', minHeight: '0'})),
      state('expanded', style({height: '*'})),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ])
  ]
})
export class VerificationComponent implements OnInit {

  assets = [];
  categories = [];
  msg;
  columnsToDisplay = ['asset', 'category', 'amount', 'date','review']; // 'subCategory'
  // footerColumns = ['subCategory','amount']
  ELEMENT_DATA = { data: [] };
  dataSource = [];
  expandedElement: ExpenseModel | null;

  constructor(private route: ActivatedRoute,
    public sharedDataService: SharedDataService,
    public util: UtilService,) {
    this.assets = route.snapshot.data['assets'];
    this.categories = route.snapshot.data['categories'];
    this.ELEMENT_DATA.data = this.expenses;

  }

  ngOnInit(): void {
    this.dataSource = this.ELEMENT_DATA.data
  }

  get expenses() {
    return this.sharedDataService.expenseImportResponse;
  }

  set expenses(value) {
    this.sharedDataService.expenseImportResponse = value;
  }
  

}
