
import { Component, ViewChild, OnInit, AfterViewInit } from '@angular/core';
import {SelectionModel} from '@angular/cdk/collections';
import {animate, state, style, transition, trigger} from '@angular/animations';
import { MatPaginator } from '@angular/material/paginator';
import { Asset } from '../models/asset.model';
import { ActivatedRoute } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { AssetDialogComponent } from '../dialogs/asset-dialog/asset-dialog.component';
import { GenericDialogComponent } from '../dialogs/generic-dialog/generic-dialog.component';
import { RestService } from '../rest.service';
import { MatSort } from '@angular/material/sort';
import { merge, of } from 'rxjs';
import { catchError,map, startWith, switchMap } from 'rxjs/operators';
import { UtilService } from 'src/common/util.service';
import * as config from '../../common/config';

@Component({
  selector: 'app-asset',
  templateUrl: './asset.component.html',
  styleUrls: ['./asset.component.scss'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({height: '0px', minHeight: '0'})),
      state('expanded', style({height: '*'})),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ])
  ],
})

export class AssetComponent implements OnInit, AfterViewInit {

  dataSource = [];
  ELEMENT_DATA = {
    data:[]
  };
  constructor(private route: ActivatedRoute,
    private service: RestService,
    private util: UtilService,
    private dialog: MatDialog) {
    this.ELEMENT_DATA.data = this.route.snapshot.data['assets'];
  }
  columnsToDisplay = ['id', 'name', 'usage', 'comment','actions'];
  expandedElement: Asset | null;
  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  ngOnInit() {
    this.dataSource = this.ELEMENT_DATA.data
  }

  ngAfterViewInit() {
    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);
    merge(this.sort.sortChange, this.paginator.page)
    .pipe(
        startWith({}),
        switchMap(() =>  this.service.getAssets() ),
        map(data =>  data ),
        catchError(() =>   of([]) )
    ).subscribe(data => this.dataSource = data);
  }

  openAsset(asset: Asset) {
		const dialogRef = this.dialog.open(AssetDialogComponent, {
			width: '100vw',
			maxWidth: '100vw',
			position: {
				top: '64px'
			},
			data: {
				asset: asset
			}
		});
		dialogRef.afterClosed().subscribe(result => {
			if (result && result.asset) {
				this.paginator._changePageSize(this.paginator.pageSize);
			}
		});
  }

  editAsset(asset: Asset, event) {
    this.openAsset(JSON.parse(JSON.stringify(asset)));
    this.suppressEvent(event);
  }

  deleteAsset(asset: Asset) {
    const ref = this.dialog.open(GenericDialogComponent, {
      width: '50vw',
      height: '40vh',
      data: {
        confirmationMessage: 'Do you want to delete this asset ?',
        cancelText: 'CANCEL',
        confirmText: 'CONFIRM'
      }
    });
    ref.afterClosed().subscribe(data => {
      if (data && data.confirmed) {
        this.service.deleteAsset(asset.id).subscribe(
          data => {
            this.paginator._changePageSize(this.paginator.pageSize)
            this.util.openSnackBar(config.default.messages.assetDeleted);
          },
          err => this.util.openSnackBar(config.default.messages.assetDeleteErr))
      }
    });
  }

  suppressEvent(event) {
    event.stopPropagation();
    event.preventDefault();
  }



}

