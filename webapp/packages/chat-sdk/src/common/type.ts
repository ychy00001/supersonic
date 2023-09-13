export type SearchRecommendItem = {
  complete: boolean;
  modelId: number;
  modelName: string;
  recommend: string;
  subRecommend: string;
  schemaElementType: string;
};

export type FieldType = {
  bizName: string;
  id: number;
  name: string;
  status: number;
  model: number;
  type: string;
  value: string;
};

export type ModelInfoType = {
  bizName: string;
  itemId: number;
  name: string;
  primaryEntityBizName: string;
  value: string;
  words: string[];
};

export type EntityInfoType = {
  modelInfo: ModelInfoType;
  dimensions: FieldType[];
  metrics: FieldType[];
  entityId: number;
};

export type DateInfoType = {
  dateList: any[];
  dateMode: string;
  period: string; 
  startDate: string;
  endDate: string;
  text: string;
  unit: number;
};

export type FilterItemType = {
  elementID: number;
  name: string;
  bizName: string;
  operator: string;
  type: string;
  value: any;
};

export type ModelType = {
  alias: string;
  bizName: string;
  id: number;
  model: number;
  name: string;
  type: string;
  useCnt: number;
}

export type EntityDimensionType = {
  bizName: string;
  itemId: number;
  name: string;
  value: string;
}

export type ChatContextType = {
  id: number;
  queryId: number;
  aggType: string;
  modelId: number;
  modelName: string;
  model: ModelType;
  dateInfo: DateInfoType;
  dimensions: FieldType[];
  metrics: FieldType[];
  entity: { alias: string[], id: number };
  entityInfo: { dimensions: EntityDimensionType[] };
  elementMatches: any[];
  nativeQuery: boolean;
  queryMode: string;
  dimensionFilters: FilterItemType[];
  properties: any;
};

export enum MsgValidTypeEnum {
  NORMAL = 0,
  SEARCH_EXCEPTION = 1,
  EMPTY = 2,
  INVALID = 3,
};

export type PluginResonseType = {
  description: string;
  webPage: { url: string, paramOptions: any, params: any, valueParams: any };
  pluginId: number;
  pluginType: string;
  name: string;
}

export type MetricInfoType = {
  date: string;
  name: string;
  statistics: any;
  value: string;
}

export type AggregateInfoType = {
  metricInfos: MetricInfoType[]
}

export type MsgDataType = {
  id: number;
  question: string;
  aggregateInfo: AggregateInfoType;
  chatContext: ChatContextType;
  entityInfo: EntityInfoType;
  queryAuthorization: any;
  queryColumns: ColumnType[];
  queryResults: any[];
  queryId: number;
  queryMode: string;
  queryState: string;
  response: PluginResonseType;
  parseOptions?: ChatContextType[];
};

export enum ParseStateEnum {
  COMPLETED = 'COMPLETED',
  PENDING = 'PENDING',
  FAILED = 'FAILED',
}

export type ParseDataType = {
  chatId: number;
  queryId: number;
  queryText: string;
  state: ParseStateEnum;
  selectedParses: ChatContextType[];
  candidateParses: ChatContextType[];
}

export type QueryDataType = {
  chatContext: ChatContextType;
  aggregateInfo: AggregateInfoType;
  queryColumns: ColumnType[];
  queryResults: any[];
};

export type ColumnType = {
  authorized: boolean;
  name: string;
  nameEn: string;
  showType: string;
  type: string;
  dataFormatType: string;
  dataFormat: {
    decimalPlaces: number;
    needMultiply100: boolean;
  };
};

export enum SemanticTypeEnum {
  DOMAIN = 'DOMAIN',
  DIMENSION = 'DIMENSION',
  METRIC = 'METRIC',
  VALUE = 'VALUE',
};

export const SEMANTIC_TYPE_MAP = {
  [SemanticTypeEnum.DOMAIN]: '数据模型',
  [SemanticTypeEnum.DIMENSION]: '维度',
  [SemanticTypeEnum.METRIC]: '指标',
  [SemanticTypeEnum.VALUE]: '维度值',
};

export type SuggestionItemType = {
  model: number;
  name: string;
  bizName: string
};

export type SuggestionType = {
  dimensions: SuggestionItemType[];
  metrics: SuggestionItemType[];
};

export type SuggestionDataType = {
  currentAggregateType: string,
  columns: ColumnType[],
  mainEntity: EntityInfoType,
  suggestions: SuggestionType,
};

export type HistoryMsgItemType = {
  questionId: number;
  queryText: string;
  queryResult: MsgDataType;
  chatId: number;
  createTime: string;
  feedback: string;
  score: number;
};

export type HistoryType = {
  hasNextPage: boolean;
  list: HistoryMsgItemType[];
};

export type DrillDownDimensionType = {
  id: number;
  model: number;
  name: string;
  bizName: string;
}