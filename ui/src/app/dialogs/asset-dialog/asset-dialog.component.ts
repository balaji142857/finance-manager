import { Component, OnInit, Inject } from '@angular/core';
import { RestService } from 'src/app/rest.service';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Asset } from 'src/app/models/asset.model';
import { UtilService } from 'src/common/util.service';
import * as config from '../../../common/config';

@Component({
  selector: 'app-asset-dialog',
  templateUrl: './asset-dialog.component.html',
  styleUrls: ['./asset-dialog.component.scss']
})
export class AssetDialogComponent {

  constructor(public dialogRef: MatDialogRef<AssetDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private util: UtilService,
      private service: RestService) {
        if (!data.asset) {
          data.asset = this.getDefaultAsset();
        }
      }

  save(asset: Asset) {
    this.service.saveAsset(asset).subscribe(
      data => {
        this.util.openSnackBar(config.default.messages.assetCreated);
        this.dialogRef.close({asset: data});
      },
      err =>  this.util.showError(err, config.default.messages.assetCreateErr)
    )
  }

  getDefaultAsset(): Asset {
    return {
      id: null,
      name: null,
      comment: null,
      createdAt: null,createdBy: null,
      usage: null
    }
  }

  cancel() {
    this.dialogRef.close();
  }

  confirm() {
    this.save(this.data.asset);
  }

}
