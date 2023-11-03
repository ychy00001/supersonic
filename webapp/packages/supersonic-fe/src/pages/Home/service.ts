// import { request } from 'umi';
import type { AnalysisData } from './data';
import getFakeChartData from './_mock';

// export function fakeChartData(){
//   return getFakeChartData;
// }

export async function fakeChartData(): Promise<{ data: AnalysisData }> {
  return getFakeChartData;
  // return request('/api/fake_analysis_chart_data');
}
