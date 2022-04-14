import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { RestService } from 'src/app/rest.service';
import { CategoryModel } from 'src/app/models/category.model';
import * as config from '../../../common/config';
import { UtilService } from 'src/common/util.service';
@Component({
  selector: 'app-category-dialog',
  templateUrl: './category-dialog.component.html',
  styleUrls: ['./category-dialog.component.scss']
})
export class CategoryDialogComponent{
  cat: CategoryModel;

  constructor(@Inject(MAT_DIALOG_DATA) public data: any,
    private restService: RestService,
    private util: UtilService,
    private dialog: MatDialogRef<CategoryDialogComponent>) {
      this.cat = data.category ? data.category : this.getDefaultCategory();
  }

  cancel() {
    this.dialog.close();
  }

  confirm() {
    this.restService.saveCategory(this.cat).subscribe(
      data => {
        this.util.openSnackBar(config.default.messages.catCreated);
        this.dialog.close({
          isChanged: true
        });
      },
      err => this.util.showError(err, config.default.messages.catCreateError)
    )
  }

  handleRemove(subCat) {
    const index = this.cat.subCategories.indexOf(subCat);
    if (index >= 0 ) {
      this.cat.subCategories.splice(index, 1);
    }
  }

  addSubCat() {
    if (!this.cat.subCategories || !this.cat.subCategories.length) {
      this.cat.subCategories = [];
    }
    this.cat.subCategories.push({
      id: null,
      name: null,
      comment: null
    })
  }

  getDefaultCategory(): CategoryModel {
    return {
      id: null,
      name: null,
      comment: null,
      subCategories: []
    }
  }

}
