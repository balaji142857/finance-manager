export interface ChartDataModel {
  title: string;
  data: ChartData[];
}

export interface ChartData {
  id?: number; label?: string | number; value?: string | number;
}
