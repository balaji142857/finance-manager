import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import { ChartsModule } from 'ng2-charts';
import { BusySpinnerModule } from 'busy-spinner';
import { MaterialModule } from 'src/common/material/material.module';
import { DashboardComponent } from './dashboard/dashboard.component';
import { ExpenseComponent } from './expense/expense.component';
import { IncomeComponent } from './income/income.component';
import { AssetComponent } from './asset/asset.component';
import { ReportComponent } from './report/report.component';
import { FileUploadModule } from 'src/common/file-upload/file-upload.module';
import { NavModule } from 'src/common/nav/nav.module';
import { MatNumberInputModule } from 'mat-number-input';
import { HamburgerButtonModule } from 'src/common/hamburger-button/hamburger-button.module';
import { ChartModule } from 'src/common/chart/chart.module';
import { AssetDialogComponent } from './dialogs/asset-dialog/asset-dialog.component';
import { ExpenseDialogComponent } from './dialogs/expense-dialog/expense-dialog.component';
import { CategoryDialogComponent } from './dialogs/category-dialog/category-dialog.component';
import { HttpClientModule } from '@angular/common/http';
import { SettingsComponent } from './settings/settings.component';
import { GenericDialogComponent } from './dialogs/generic-dialog/generic-dialog.component';
import { SnackbarModule } from 'src/common/snackbar/snackbar.module';
import { ExpenseFilterComponent } from './expense-filter/expense-filter.component';
import { VerificationComponent } from './report/verification/verification.component';


// Pass the fusioncharts library and chart modules
@NgModule({
  declarations: [
    AppComponent,
    DashboardComponent,
    ExpenseComponent,
    IncomeComponent,
    AssetComponent,
    ReportComponent,
    AssetDialogComponent,
    ExpenseDialogComponent,
    CategoryDialogComponent,
    GenericDialogComponent,
    SettingsComponent,
    ExpenseFilterComponent,
    VerificationComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    AppRoutingModule,
    BusySpinnerModule,
    MaterialModule,
    ChartsModule,
    NavModule,
    FormsModule,
    ReactiveFormsModule,
    MatNumberInputModule,
    HamburgerButtonModule,
    ChartModule,
    SnackbarModule,
    FileUploadModule,
  ],
  providers: [],
  bootstrap: [AppComponent],
  entryComponents: [AssetDialogComponent,
    ExpenseDialogComponent,
    CategoryDialogComponent,
    GenericDialogComponent]
})
export class AppModule { }
