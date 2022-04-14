import { Component, OnInit, Input, OnChanges, SimpleChanges } from '@angular/core';
import { ChartDataSets, ChartOptions, ChartType  } from 'chart.js';
import { Color, Label, MultiDataSet} from 'ng2-charts';
import { ChartDataModel } from 'src/app/models/chart-data.model';
import { ChartConfigModel } from 'src/app/models/chart-config.model';

@Component({
  selector: 'app-chart',
  templateUrl: './chart.component.html',
  styleUrls: ['./chart.component.scss']
})
export class ChartComponent implements OnInit, OnChanges {

  @Input('data') data: ChartDataModel ;
  @Input('userConfig') userConfig: ChartConfigModel;
  config: ChartConfigModel = this.getDefaultConfig();

  chartOptions = {
    responsive: true,
  };
  chartLegend = true;
  chartPlugins = [];
  chartType = 'bar';
  chartData: ChartDataSets[] = [{ data: [], label: '' }];
  chartLabels: Label[] = [];

  constructor() { }

  ngOnChanges(changes: SimpleChanges): void {
      this.initialize();
  }

  ngOnInit() {
    this.initialize();
  }

  initialize() {
    this.initializeChartData();
    this.configureChart();
  }

  initializeChartData() {
    const valueArray = [];
    const labelArray = [];
    this.data.data.forEach(item => {
      labelArray.push(item.label);
      valueArray.push(item.value);
    });
    this.chartData[0].data=valueArray;
    this.chartData[0].label = this.data.title;
    this.chartLabels = labelArray;
  }

  configureChart() {
    Object.assign(this.config, this.userConfig);
    this.chartOptions.responsive = this.config.responsive;
    this.chartLegend = this.config.showLegends;
    this.chartType = this.config.chartType;
  }

  getDefaultConfig(): ChartConfigModel {
    return {
      responsive: true,
      showLegends: true,
      chartType: 'bar'
    }
  }

}
