import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { RestService } from '../rest.service';
import { UtilService } from 'src/common/util.service';

import { SharedDataService } from '../services/shared-data.service';

@Component({
  selector: 'app-report',
  templateUrl: './report.component.html',
  styleUrls: ['./report.component.scss']
})
export class ReportComponent implements OnInit {

  expenseFiles = [];
  importFormats = [];

  constructor(private service: RestService,
    private util: UtilService,
    private route: ActivatedRoute,
    private sharedDataService: SharedDataService,
    private router: Router) {
      this.importFormats = route.snapshot.data['importFormats']; 
    }

  ngOnInit(): void {
  }

  import() {
    if (this.expenseFiles) {
      this.service.importExpenses(this.expenseFiles).subscribe(
        resp => this.showVerificationPage(resp),
        err => console.log(err)
      );
    }
  }

  showVerificationPage(response) {
    this.sharedDataService.expenseImportResponse = response.data;
    this.sharedDataService.expenseImportResponseMessage = response.message;
    this.router.navigate(['/verify']);
  }

  // openVerificationDialog(resp) {
  //   console.log('opening dialog with data', resp.data);
  //   const dialogRef = this.dialog.open(ImportVerificationDialogComponent, {
  //     width: '100vw',
  //     height: '70vh',
  //     data: {
  //       model: resp,
  //       categories: this.categories,
  //       assets: this.assets
  //     },
  //     hasBackdrop: false,
  //     disableClose: true
  //   });
  // }

}
