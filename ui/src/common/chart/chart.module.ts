import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { ChartsModule } from 'ng2-charts';
import { ChartComponent } from './chart.component';



@NgModule({
  declarations: [ChartComponent],
  imports: [
    CommonModule,
    ChartsModule,
    MatCardModule
  ],
  exports: [ChartComponent]
})
export class ChartModule { }
