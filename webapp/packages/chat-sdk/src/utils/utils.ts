import moment, { Moment } from 'moment';
import { NumericUnit } from '../common/constants';

export function formatByDecimalPlaces(value: number | string, decimalPlaces: number) {
  if (isNaN(+value) || decimalPlaces < 0 || decimalPlaces > 100) {
    return value;
  }
  let strValue = (+value).toFixed(decimalPlaces);
  // 备注：因为增长率等数据为0.0xx的数据，这里统一处理为0有点不合适暂时注释掉
  // if (!/^[0-9.]+$/g.test(strValue)) {
  //   return '0';
  // }
  while (strValue.includes('.') && (strValue.endsWith('.') || strValue.endsWith('0'))) {
    strValue = strValue.slice(0, -1);
  }
  return strValue;
}

export function formatByThousandSeperator(value: number | string) {
  if (isNaN(+value)) {
    return value;
  }
  const partValues = value.toString().split('.');
  partValues[0] = partValues[0].replace(/\B(?=(\d{3})+(?!\d))/g, ',');
  return partValues.join('.');
}

export function formatMetric(value: number | string) {
  return formatByThousandSeperator(formatByDecimalPlaces(value, 4));
}

export function formatByUnit(value: number | string, unit: NumericUnit) {
  const numericValue = +value;
  if (isNaN(numericValue) || unit === NumericUnit.None) {
    return value;
  }
  let exponentValue = 0;
  switch (unit) {
    case NumericUnit.TenThousand:
    case NumericUnit.EnTenThousand:
      exponentValue = 4;
      break;
    case NumericUnit.OneHundredMillion:
      exponentValue = 8;
      break;
    case NumericUnit.Thousand:
      exponentValue = 3;
      break;
    case NumericUnit.Million:
      exponentValue = 6;
      break;
    case NumericUnit.Giga:
      exponentValue = 9;
      break;
  }
  return numericValue / Math.pow(10, exponentValue);
}

export const getFormattedValue = (value: number | string, remainZero?: boolean) => {
  if (remainZero && (value === undefined || +value === 0)) {
    return 0;
  }
  if (value === undefined) {
    return '-';
  }
  if (!isFinite(+value)) {
    return value;
  }
  const unit =
    +value >= 100000000
      ? NumericUnit.OneHundredMillion
      : +value >= 10000
        ? NumericUnit.TenThousand
        : NumericUnit.None;
  let formattedValue = formatByUnit(value, unit);
  formattedValue = formatByDecimalPlaces(
    formattedValue,
    unit === NumericUnit.OneHundredMillion ? 2 : +value < 1 ? 3 : 1,
  );
  formattedValue = formatByThousandSeperator(formattedValue);
  if ((typeof formattedValue === 'number' && isNaN(formattedValue)) || +formattedValue === 0) {
    return '-';
  }
  return `${formattedValue}${unit === NumericUnit.None ? '' : unit}`;
};

export const formatNumberWithCN = (num: number) => {
  if (isNaN(num)) return '-';
  if (num >= 10000) {
    return (num / 10000).toFixed(1) + "万";
  } else {
    return num;
  }
}

export const groupByColumn = (data: any[], column: string) => {
  return data.reduce((result, item) => {
    const resultData = { ...result };
    const key = item[column];
    if (!resultData[key]) {
      resultData[key] = [];
    }
    resultData[key].push(item);
    return resultData;
  }, {});
};

// 获取任意两个日期中的所有日期
export function enumerateDaysBetweenDates(startDate: Moment, endDate: Moment, dateType?: any) {
  let daysList: any[] = [];
  const day = endDate.diff(startDate, dateType || 'days');
  const format = dateType === 'months' ? 'YYYY-MM' : 'YYYY-MM-DD';
  daysList.push(startDate.format(format));
  for (let i = 1; i <= day; i++) {
    daysList.push(startDate.add(1, dateType || 'days').format(format));
  }
  return daysList;
}

export const normalizeTrendData = (
  resultList: any[],
  dateColumnName: string,
  valueColumnName: string,
  startDate: string,
  endDate: string,
  dateType?: string,
) => {
  const dateList = enumerateDaysBetweenDates(moment(startDate), moment(endDate), dateType);
  const result = dateList.map((date) => {
    const item = resultList.find((result) => moment(result[dateColumnName]).format(dateType === 'months' ? 'YYYY-MM' : 'YYYY-MM-DD') === date);
    return {
      ...(item || {}),
      [dateColumnName]: date,
      [valueColumnName]: item ? item[valueColumnName] : 0,
    };
  });
  return result;
};

export const getMinMaxDate = (resultList: any[], dateColumnName: string) => {
  const dateList = resultList.map((item) => moment(item[dateColumnName]));
  return [moment.min(dateList).format('YYYY-MM-DD'), moment.max(dateList).format('YYYY-MM-DD')];
};

export function hexToRgbObj(hex) {
  var result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(hex);
  return result
    ? {
      r: parseInt(result[1], 16),
      g: parseInt(result[2], 16),
      b: parseInt(result[3], 16),
    }
    : null;
}

export function getLightenDarkenColor(col, amt) {
  let result;
  if (col?.includes('rgb')) {
    const [r, g, b, a] = col.match(/\d+/g).map(Number);
    result = { r, g, b, a };
  } else {
    result = hexToRgbObj(col) || {};
  }
  return `rgba(${result.r + amt},${result.g + amt},${result.b + amt}${
    result.a ? `,${result.a}` : ''
  })`;
}

export function getChartLightenColor(col) {
  return getLightenDarkenColor(col, 80);
}

export const isMobile = window.navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i);


export function isProd() {
  return process.env.NODE_ENV === 'production';
}

export function setToken(token: string) {
  localStorage.setItem('SUPERSONIC_CHAT_TOKEN', token);
}

export function getToken() {
  return localStorage.getItem('SUPERSONIC_CHAT_TOKEN');
}
