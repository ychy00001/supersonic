import { SemanticNodeType } from './enum';
import { DateRangeType } from '@/components/MDatePicker/type';

export enum SENSITIVE_LEVEL {
  LOW = 0,
  MID = 1,
  HIGH = 2,
}

export const SENSITIVE_LEVEL_OPTIONS = [
  {
    label: '普通',
    value: SENSITIVE_LEVEL.LOW,
  },
  {
    label: '重要',
    value: SENSITIVE_LEVEL.MID,
  },
  {
    label: '核心',
    value: SENSITIVE_LEVEL.HIGH,
  },
];

export const SENSITIVE_LEVEL_ENUM = SENSITIVE_LEVEL_OPTIONS.reduce(
  (sensitiveEnum: any, item: any) => {
    const { label, value } = item;
    sensitiveEnum[value] = label;
    return sensitiveEnum;
  },
  {},
);

export const IS_TAG_ENUM = {
  1: '是',
  0: '否',
};

export const SENSITIVE_LEVEL_COLOR = {
  [SENSITIVE_LEVEL.LOW]: 'lime',
  [SENSITIVE_LEVEL.MID]: 'warning',
  [SENSITIVE_LEVEL.HIGH]: 'error',
};

export const SEMANTIC_NODE_TYPE_CONFIG = {
  [SemanticNodeType.DATASOURCE]: {
    label: '数据源',
    value: SemanticNodeType.DATASOURCE,
    color: 'cyan',
  },
  [SemanticNodeType.DIMENSION]: {
    label: '维度',
    value: SemanticNodeType.DIMENSION,
    color: 'blue',
  },
  [SemanticNodeType.METRIC]: {
    label: '指标',
    value: SemanticNodeType.METRIC,
    color: 'orange',
  },
};

export const DateFieldMap = {
  [DateRangeType.DAY]: 'sys_imp_date',
  [DateRangeType.WEEK]: 'sys_imp_week',
  [DateRangeType.MONTH]: 'sys_imp_month',
};

export const DatePeridMap = {
  sys_imp_date: DateRangeType.DAY,
  sys_imp_week: DateRangeType.WEEK,
  sys_imp_month: DateRangeType.MONTH,
};

export const theme_body_bg_color = "#1C2632";
export const theme_component_bg_color = "#262F3D";
export const text_color = "#ccc";
export const text_color_secondary = "#a7a7a7";
export const head_color = "#f8f7f7";
