import { Component, OnInit } from '@angular/core';
import { ChartDataSets, ChartOptions, ChartType  } from 'chart.js';
import { Color, Label, MultiDataSet} from 'ng2-charts';
import { ActivatedRoute } from '@angular/router';
import { Asset } from '../models/asset.model';
import { ChartConfigModel } from '../models/chart-config.model';
import { ChartDataModel, ChartData } from '../models/chart-data.model';
import { RestService } from '../rest.service';
import * as config from '../../common/config';
import { UtilService } from 'src/common/util.service';
import { CategoryModel } from '../models/category.model';
import { Asset as AssetModel } from '../models/asset.model';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent {

  categories: CategoryModel[] = [];
  assets: AssetModel[] = [];
  assetChartData: ChartDataModel;
  dailyExpenseData: ChartDataModel;
  monthlyExpenseData: ChartDataModel;
  categoryExpenseData: ChartDataModel;
  from: string;
  to: string;
  filedsToDisable = {
    subCategory: true,
    comment: true,
    txDetail: true,
    minAmount: true,
    maxAmount: true
  }
  barChartConfig: ChartConfigModel = {
    chartType: 'bar',
    responsive: true,
    showLegends: true
  }
  doughnutChartConfig: ChartConfigModel = {
    chartType: 'doughnut',
    responsive: true,
    showLegends: true
  }
  lineChartConfig: ChartConfigModel = {
    chartType: 'line',
    responsive: true,
    showLegends: true
  }
  polarChartConfig: ChartConfigModel = {
    chartType: 'polarArea',
    responsive: true,
    showLegends: true
  }
  constructor(private route: ActivatedRoute,
      private util: UtilService,
      private service: RestService) {
    this.assets = this.route.snapshot.data['assets'];
    this.assetChartData = this.translate(this.assets, 'Asset Usage');
    this.categories = route.snapshot.data['categories'];
    this.dailyExpenseData = this.route.snapshot.data['dailyExpenes'];
    this.monthlyExpenseData = this.route.snapshot.data['monthlyExpenes'];
    this.categoryExpenseData = this.route.snapshot.data['categoryExpenses'];
  }
  retrieveChartData(filterObj) {
    this.service.getExpensesByCategory(filterObj).subscribe(
      data => this.categoryExpenseData = data,
      err => this.util.showError(err, config.default.messages.chartLoadExpCat)
    );
    this.service.getAssetUsage(filterObj).subscribe(
      data =>  this.assetChartData = data,
      err => this.util.showError(err, config.default.messages.chartLoadAssetExp)
    );
    this.service.getDailyExpenses(filterObj).subscribe(
      data =>  this.dailyExpenseData = data,
      err => this.util.showError(err, config.default.messages.chartLoadDailyExp)
    );
    this.service.getMonthlyExpenses(filterObj).subscribe(
      data => this.monthlyExpenseData = data,
      err => this.util.showError(err, config.default.messages.chartLoadMonthlyExp)
    );
  }

  translate(data: Asset[], title: string): ChartDataModel {
    if (!data || !data.length) {
      return { data: [], title: title };
    }
    const chartData: ChartData[] = [];
    data.forEach(asset => {
      chartData.push({
        id: asset.id,
        label: asset.name,
        value: asset.usage
      });
    });
    return { data: chartData, title: title };
  }

}
