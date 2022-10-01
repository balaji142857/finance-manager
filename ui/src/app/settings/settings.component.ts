import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { CategoryDialogComponent } from '../dialogs/category-dialog/category-dialog.component';
import { ActivatedRoute } from '@angular/router';
import { CategoryModel } from '../models/category.model';
import { GenericDialogComponent } from '../dialogs/generic-dialog/generic-dialog.component';
import { RestService } from '../rest.service';
import { UtilService } from 'src/common/util.service';
import config from '../../common/config';

@Component({
  selector: "app-settings",
  templateUrl: "./settings.component.html",
  styleUrls: ["./settings.component.scss"],
})
export class SettingsComponent implements OnInit {
  categories: CategoryModel[] = [];

  dataSource = [];
  ELEMENT_DATA = { data: [] };
  columnsToDisplay = ["category", "subCategory", "actions"];

  constructor(
    private dialog: MatDialog,
    private service: RestService,
    private util: UtilService,
    private route: ActivatedRoute
  ) {
    this.categories = route.snapshot.data["categories"];
    this.ELEMENT_DATA.data = this.categories;
  }

  ngOnInit() {
    this.dataSource = this.ELEMENT_DATA.data;
  }

  loadCategories() {
    this.service.getExpenseCategories().subscribe((data) => {
      (this.categories = data),
        (err) => this.util.showError(err, config.messages.catLoadErr);
    });
  }

  handleRemove(cat: CategoryModel) {
    const ref = this.dialog.open(GenericDialogComponent, {
      data: {
        confirmationMessage: "Delete this Category ? ",
        confirmText: "DELETE",
        cancelText: "CANCEL",
      },
    });
    ref.afterClosed().subscribe((data) => {
      if (data && data.confirmed) {
        this.service.deleteCategory(cat.id).subscribe(
          (data) => {
            this.util.openSnackBar(config.messages.catDeleted);
            this.loadCategories();
          },
          (err) => this.util.showError(err, config.messages.catDelteErr)
        );
      }
    });
  }

  openCategories(cat: CategoryModel) {
    const ref = this.dialog.open(CategoryDialogComponent, {
      width: "100vw",
      height: "40vh",
      data: {
        category: cat ? JSON.parse(JSON.stringify(cat)) : null,
      },
    });
    ref.afterClosed().subscribe((data) => {
      if (data && data.isChanged) {
        this.loadCategories();
      }
    });
  }
}
