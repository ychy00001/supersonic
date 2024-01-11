import React, { ReactNode } from 'react';
import { AGG_TYPE_MAP, PREFIX_CLS, PRIMARY_COLOR } from '../../common/constants';
import { ChatContextType, DateInfoType, EntityInfoType, FilterItemType } from '../../common/type';
import { DatePicker, Tag, Button  } from 'antd';
import {CheckCircleFilled, SmileTwoTone, ReloadOutlined} from '@ant-design/icons';
import Loading from './Loading';
import FilterItem from './FilterItem';
import classNames from 'classnames';
import { isMobile } from '../../utils/utils';
import dayjs from 'dayjs';

const { RangePicker } = DatePicker;

type Props = {
  parseLoading: boolean;
  parseInfoOptions: ChatContextType[];
  parseTip: string;
  currentParseInfo?: ChatContextType;
  agentId?: number;
  dimensionFilters: FilterItemType[];
  dateInfo: DateInfoType;
  entityInfo: EntityInfoType;
  integrateSystem?: string;
  parseTimeCost?: number;
  isDeveloper?: boolean;
  onSelectParseInfo: (parseInfo: ChatContextType) => void;
  onSwitchEntity: (entityId: string) => void;
  onFiltersChange: (filters: FilterItemType[]) => void;
  onDateInfoChange: (dateRange: any) => void;
  onRefresh: () => void;
};

const MAX_OPTION_VALUES_COUNT = 2;

const ParseTip: React.FC<Props> = ({
  parseLoading,
  parseInfoOptions,
  parseTip,
  currentParseInfo,
  agentId,
  dimensionFilters,
  dateInfo,
  entityInfo,
  integrateSystem,
  parseTimeCost,
  isDeveloper,
  onSelectParseInfo,
  onSwitchEntity,
  onFiltersChange,
  onDateInfoChange,
  onRefresh,
}) => {
  const prefixCls = `${PREFIX_CLS}-item`;

  const getNode = (tipTitle: ReactNode, tipNode?: ReactNode) => {
    return (
      <div className={`${prefixCls}-parse-tip`}>
        <div className={`${prefixCls}-title-bar`}>
          <SmileTwoTone className={`${prefixCls}-step-icon`} twoToneColor={PRIMARY_COLOR} />
          <div className={`${prefixCls}-step-title`}>
            {tipTitle}
            {!tipNode && <Loading />}
          </div>
        </div>
        {tipNode && <div className={`${prefixCls}-content-container`}>{tipNode}</div>}
      </div>
    );
  };

  if (parseLoading) {
    return getNode('意图解析中');
  }

  if (parseTip) {
    return getNode(
      <>
        意图解析失败
        {parseTimeCost && isDeveloper && (
          <span className={`${prefixCls}-title-tip`}>(耗时: {parseTimeCost}ms)</span>
        )}
      </>,
      parseTip
    );
  }

  if (parseInfoOptions.length === 0) {
    return null;
  }

  const {
    modelId,
    model,
    dimensions,
    metrics,
    aggType,
    queryMode,
    queryType,
    properties,
    entity,
    elementMatches,
    nativeQuery,
  } = currentParseInfo || {};

  const entityAlias = entity?.alias?.[0]?.split('.')?.[0];

  const entityDimensions = entityInfo?.dimensions?.filter(
    item =>
      !['zyqk_song_id', 'song_name', 'singer_id', 'zyqk_cmpny_id'].includes(item.bizName) &&
      !(
        entityInfo?.dimensions?.some(dimension => dimension.bizName === 'singer_id') &&
        item.bizName === 'singer_name'
      ) &&
      !(
        entityInfo?.dimensions?.some(dimension => dimension.bizName === 'zyqk_cmpny_id') &&
        item.bizName === 'cmpny_name'
      )
  );

  const getTipNode = () => {
    const dimensionItems = dimensions?.filter(item => item.type === 'DIMENSION');

    const itemValueClass = `${prefixCls}-tip-item-value`;
    const entityId = dimensionFilters?.length > 0 ? dimensionFilters[0].value : undefined;
    const entityAlias = entity?.alias?.[0]?.split('.')?.[0];
    const entityName = elementMatches?.find(item => item.element?.type === 'ID')?.element.name;

    const { type: agentType, name: agentName } = properties || {};

    const fields =
      queryMode === 'TAG_DETAIL' ? dimensionItems?.concat(metrics || []) : dimensionItems;

    return (
      <div className={`${prefixCls}-tip-content`}>
        {!!agentType && queryMode !== 'LLM_S2SQL' ? (
          <div className={`${prefixCls}-tip-item`}>
            将由{agentType === 'plugin' ? '插件' : '内置'}工具
            <span className={itemValueClass}>{agentName}</span>来解答
          </div>
        ) : (
          <>
            {(queryMode?.includes('ENTITY') || queryMode === 'LLM_S2SQL') &&
            typeof entityId === 'string' &&
            !!entityAlias &&
            !!entityName ? (
              <div className={`${prefixCls}-tip-item`}>
                <div className={`${prefixCls}-tip-item-name`}>{entityAlias}：</div>
                <div className={itemValueClass}>{entityName}</div>
              </div>
            ) : (
              <div className={`${prefixCls}-tip-item`}>
                <div className={`${prefixCls}-tip-item-name`}>数据模型：</div>
                <div className={itemValueClass}>
                  {model?.modelNames?.length === 1
                    ? model.modelNames[0]
                    : model?.modelNames?.map(modelName => <Tag key={modelName}>{modelName}</Tag>)}
                </div>
              </div>
            )}
            {(queryType === 'METRIC' || queryType === 'TAG') && (
              <div className={`${prefixCls}-tip-item`}>
                <div className={`${prefixCls}-tip-item-name`}>查询模式：</div>
                <div className={itemValueClass}>
                  {queryType === 'METRIC' ? '指标模式' : '标签模式'}
                </div>
              </div>
            )}
            {queryType !== 'TAG' &&
              metrics &&
              metrics.length > 0 &&
              !dimensions?.some(item => item.bizName?.includes('_id')) && (
                <div className={`${prefixCls}-tip-item`}>
                  <div className={`${prefixCls}-tip-item-name`}>指标：</div>
                  <div className={itemValueClass}>
                    {queryType === 'METRIC'
                      ? metrics[0].name
                      : metrics.map(metric => metric.name).join('、')}
                  </div>
                </div>
              )}
            {['METRIC_GROUPBY', 'METRIC_ORDERBY', 'TAG_DETAIL', 'LLM_S2SQL'].includes(queryMode!) &&
              fields &&
              fields.length > 0 && (
                <div className={`${prefixCls}-tip-item`}>
                  <div className={`${prefixCls}-tip-item-name`}>
                    {queryMode === 'LLM_S2SQL'
                      ? nativeQuery
                        ? '查询字段'
                        : '下钻维度'
                      : queryMode === 'TAG_DETAIL'
                      ? '查询字段'
                      : '下钻维度'}
                    ：
                  </div>
                  <div className={itemValueClass}>
                    {fields
                      .slice(0, MAX_OPTION_VALUES_COUNT)
                      .map(field => field.name)
                      .join('、')}
                    {fields.length > MAX_OPTION_VALUES_COUNT && '...'}
                  </div>
                </div>
              )}
            {queryMode !== 'TAG_ID' &&
              !dimensions?.some(item => item.bizName?.includes('_id')) &&
              entityDimensions
                ?.filter(dimension => dimension.value != null)
                .map(dimension => (
                  <div className={`${prefixCls}-tip-item`} key={dimension.itemId}>
                    <div className={`${prefixCls}-tip-item-name`}>{dimension.name}：</div>
                    <div className={itemValueClass}>{dimension.value}</div>
                  </div>
                ))}
            {(queryMode === 'METRIC_ORDERBY' || queryMode === 'METRIC_MODEL') &&
              aggType &&
              aggType !== 'NONE' && (
                <div className={`${prefixCls}-tip-item`}>
                  <div className={`${prefixCls}-tip-item-name`}>聚合方式：</div>
                  <div className={itemValueClass}>{AGG_TYPE_MAP[aggType]}</div>
                </div>
              )}
          </>
        )}
      </div>
    );
  };

  const getFilterContent = (filters: any) => {
    const itemValueClass = `${prefixCls}-tip-item-value`;
    const { startDate, endDate } = dateInfo || {};
    const tipItemOptionClass = classNames(`${prefixCls}-tip-item-option`, {
      [`${prefixCls}-mobile-tip-item-option`]: isMobile,
    });
    return (
        <div className={`${prefixCls}-tip-item-filter-content`}>
          <div className={tipItemOptionClass}>
            <span className={`${prefixCls}-tip-item-filter-name`}>数据时间：</span>
        {nativeQuery ? (
            <span className={itemValueClass}>
              {startDate === endDate ? startDate : `${startDate} ~ ${endDate}`}
            </span>
        ) : (
            <RangePicker
              value={[dayjs(startDate), dayjs(endDate)]}
              onChange={onDateInfoChange}
              getPopupContainer={trigger => trigger.parentNode as HTMLElement}
              allowClear={false}
            />
          )}
        </div>
        {filters?.map((filter: any, index: number) => (
          <FilterItem
            modelId={modelId!}
            filters={dimensionFilters}
            filter={filter}
            index={index}
            chatContext={currentParseInfo!}
            entityAlias={entityAlias}
            agentId={agentId}
            integrateSystem={integrateSystem}
            onFiltersChange={onFiltersChange}
            onSwitchEntity={onSwitchEntity}
            key={`${filter.name}_${index}`}
          />
        ))}
      </div>
    );
  };

  const getFiltersNode = () => {
    return (
      <>
        <div className={`${prefixCls}-tip-item`}>
          <div className={`${prefixCls}-tip-item-name`}>筛选条件：</div>
          <div className={`${prefixCls}-tip-item-content`}>
            {getFilterContent(dimensionFilters)}
          </div>
        </div>
        <Button className={`${prefixCls}-reload`} size="small" onClick={onRefresh}>
          <ReloadOutlined />
          重新查询
        </Button>
      </>
    );
  };

  const { type: agentType } = properties || {};

  const tipNode = (
    <div className={`${prefixCls}-tip`}>
      {getTipNode()}
      {!(!!agentType && queryMode !== 'LLM_S2SQL') && getFiltersNode()}
    </div>
  );

  return getNode(
    <div className={`${prefixCls}-title-bar`}>
      <div>
        意图解析
        {parseTimeCost && isDeveloper && (
          <span className={`${prefixCls}-title-tip`}>(耗时: {parseTimeCost}ms)</span>
        )}
        {parseInfoOptions?.length > 1 ? '：' : ''}
      </div>
      {parseInfoOptions?.length > 1 && (
        <div className={`${prefixCls}-content-options`}>
          {parseInfoOptions.map((parseInfo, index) => (
            <div
              className={`${prefixCls}-content-option ${
                parseInfo.id === currentParseInfo?.id ? `${prefixCls}-content-option-active` : ''
              }`}
              onClick={() => {
                onSelectParseInfo(parseInfo);
              }}
              key={parseInfo.id}
            >
              解析{index + 1}
            </div>
          ))}
        </div>
      )}
    </div>,
    tipNode
  );
};

export default ParseTip;
