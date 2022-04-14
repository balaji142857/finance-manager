import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';
import { ExpenseComponent } from './expense/expense.component';
import { IncomeComponent } from './income/income.component';
import { AssetComponent } from './asset/asset.component';
import { ReportComponent } from './report/report.component';

import { AssetResolverService } from './services/asset-resolver.service';
import { ExpCatResolverService } from './services/exp-cat-resolver.service';
import { DailyExpenseResolverService } from './services/daily-expense-resolver.service';
import { MonthlyExpenseResolverService } from './services/monthly-expense-resolver.service';
import { CategoryExpenseResolverService } from './services/category-expense-resolver.service';
import { SettingsComponent } from './settings/settings.component';
import { ExpenseResolverService } from './services/expense-resolver.service';
import { ImportFormatsResovlerService } from './services/import-formats-resovler.service';
import { VerificationComponent } from './report/verification/verification.component';



const routes: Routes = [
  {
    path: '',
    redirectTo: 'dashboard',
    pathMatch: 'full',
    resolve: {
      'expCat': ExpCatResolverService
    }
  }, {
    component: DashboardComponent, path: 'dashboard',
    resolve: {
      'assets': AssetResolverService,
      'categories': ExpCatResolverService,
      'dailyExpenes': DailyExpenseResolverService,
      'monthlyExpenes': MonthlyExpenseResolverService,
      'categoryExpenses': CategoryExpenseResolverService
    }
  }, {
    component: ExpenseComponent, path: 'expense',
    resolve: {
      'assets': AssetResolverService,
      'categories': ExpCatResolverService,
      'expenses': ExpenseResolverService
    }
  }, {
    component: AssetComponent, path: 'assets',
    resolve: {
      'assets': AssetResolverService
    }
  }, {
    component: ReportComponent, path: 'reports',
    resolve: {
      'assets': AssetResolverService,
      'categories': ExpCatResolverService,
      'importFormats': ImportFormatsResovlerService
    }
  }, {
    component: VerificationComponent, path: 'verify',
    resolve: {
      'assets': AssetResolverService,
      'categories': ExpCatResolverService,
      'importFormats': ImportFormatsResovlerService
    }
  }, {
    component: SettingsComponent, path: 'settings',
    resolve: {
      'categories': ExpCatResolverService
    }
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
