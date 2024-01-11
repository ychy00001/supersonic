import React, { useState, useEffect, useRef } from 'react';
import { message, Row, Col, Button, Space, Select, Form, Tooltip, Radio } from 'antd';
import {
  queryStruct,
  getDrillDownDimension,
  getDimensionList,
} from '@/pages/SemanticModel/service';
import {
  InfoCircleOutlined,
  LineChartOutlined,
  TableOutlined,
  DownloadOutlined,
  PoweroffOutlined,
} from '@ant-design/icons';
import DimensionAndMetricRelationModal from '../../components/DimensionAndMetricRelationModal';
import TrendChart from '@/pages/SemanticModel/Metric/components/MetricTrend';
import MetricTrendDimensionFilterContainer from './MetricTrendDimensionFilterContainer';
import MDatePicker from '@/components/MDatePicker';
import {
  DateRangeType,
  DateSettingType,
  DynamicAdvancedConfigType,
  DatePeriodType,
} from '@/components/MDatePicker/type';
import { getDatePickerDynamicInitialValues } from '@/components/MDatePicker/utils';
import StandardFormRow from '@/components/StandardFormRow';
import MetricTable from './Table';
import { ColumnConfig } from '../data';
import dayjs from 'dayjs';
import { ISemantic } from '../../data';
import { DateFieldMap,theme_component_bg_color } from '@/pages/SemanticModel/constant';

const FormItem = Form.Item;
const { Option } = Select;

type Props = {
  metircData?: ISemantic.IMetricItem;
  [key: string]: any;
};

const MetricTrendSection: React.FC<Props> = ({ metircData }) => {
  const indicatorFields = useRef<{ name: string; column: string }[]>([]);
  const [metricTrendData, setMetricTrendData] = useState<ISemantic.IMetricTrendItem[]>([]);
  const [metricTrendLoading, setMetricTrendLoading] = useState<boolean>(false);
  const [metricColumnConfig, setMetricColumnConfig] = useState<ISemantic.IMetricTrendColumn>();
  const [metricRelationModalOpenState, setMetricRelationModalOpenState] = useState<boolean>(false);
  const [drillDownDimensions, setDrillDownDimensions] = useState<
    ISemantic.IDrillDownDimensionItem[]
  >(metircData?.relateDimension?.drillDownDimensions || []);
  const [authMessage, setAuthMessage] = useState<string>('');
  const [downloadLoding, setDownloadLoding] = useState<boolean>(false);
  const [relationDimensionOptions, setRelationDimensionOptions] = useState<
    { value: string; label: string; modelId: number }[]
  >([]);
  const [dimensionList, setDimensionList] = useState<ISemantic.IDimensionItem[]>([]);
  const [queryParams, setQueryParams] = useState<any>({});
  const [downloadBtnDisabledState, setDownloadBtnDisabledState] = useState<boolean>(true);
  // const [showDimensionOptions, setShowDimensionOptions] = useState<any[]>([]);
  const [periodDate, setPeriodDate] = useState<{
    startDate: string;
    endDate: string;
    dateField: string;
  }>({
    startDate: dayjs().subtract(6, 'days').format('YYYY-MM-DD'),
    endDate: dayjs().format('YYYY-MM-DD'),
    dateField: DateFieldMap[DateRangeType.DAY],
  });
  const [rowNumber, setRowNumber] = useState<number>(5);
  const [chartType, setChartType] = useState<'chart' | 'table'>('chart');
  const [tableColumnConfig, setTableColumnConfig] = useState<ColumnConfig[]>([]);

  const [transformState, setTransformState] = useState<boolean>(false);

  const [groupByDimensionFieldName, setGroupByDimensionFieldName] = useState<string>();

  const getMetricTrendData = async (params: any = { download: false }) => {
    const { download, dimensionGroup = [], dimensionFilters = [] } = params;
    if (download) {
      setDownloadLoding(true);
    } else {
      setMetricTrendLoading(true);
    }
    if (!metircData) {
      return;
    }
    const { modelId, bizName, name } = metircData;
    indicatorFields.current = [{ name, column: bizName }];

    const dimensionFiltersBizNameList = dimensionFilters.map((item) => {
      return item.bizName;
    });

    const bizNameList = Array.from(new Set([...dimensionFiltersBizNameList, ...dimensionGroup]));

    const modelIds = dimensionList.reduce(
      (idList: number[], item: ISemantic.IDimensionItem) => {
        if (bizNameList.includes(item.bizName)) {
          idList.push(item.modelId);
        }
        return idList;
      },
      [modelId],
    );

    const res = await queryStruct({
      // modelId,
      modelIds: Array.from(new Set(modelIds)),
      bizName,
      groups: dimensionGroup,
      dimensionFilters,
      dateField: periodDate.dateField,
      startDate: periodDate.startDate,
      endDate: periodDate.endDate,
      download,
      isTransform: transformState,
    });
    if (download) {
      setDownloadLoding(false);
      return;
    }
    const { code, data, msg } = res;
    setMetricTrendLoading(false);
    if (code === 200) {
      const { resultList, columns, queryAuthorization } = data;
      setMetricTrendData(resultList);
      setTableColumnConfig(columns);
      const message = queryAuthorization?.message;
      if (message) {
        setAuthMessage(message);
      }
      const targetConfig = columns.find((item: ISemantic.IMetricTrendColumn) => {
        return item.nameEn === bizName;
      });
      if (targetConfig) {
        setMetricColumnConfig(targetConfig);
      }
      setDownloadBtnDisabledState(false);
    } else {
      if (code === 401 || code === 400) {
        setAuthMessage(msg);
      } else {
        message.error(msg);
      }
      setDownloadBtnDisabledState(true);
      setMetricTrendData([]);
      setMetricColumnConfig(undefined);
    }
  };

  const queryDimensionList = async (ids: number[]) => {
    if (!(Array.isArray(ids) && ids.length > 0)) {
      return;
    }
    const { code, data, msg } = await getDimensionList({ ids });
    if (code === 200 && Array.isArray(data?.list)) {
      setDimensionList(data.list);
      setRelationDimensionOptions(
        data.list.map((item: ISemantic.IMetricItem) => {
          return { label: item.name, value: item.bizName, modelId: item.modelId };
        }),
      );
      return data.list;
    }
    message.error(msg);
    return [];
  };

  const queryDrillDownDimension = async (metricId: number) => {
    const { code, data, msg } = await getDrillDownDimension(metricId);
    if (code === 200 && Array.isArray(data)) {
      const ids = data.map((item) => item.dimensionId);
      queryDimensionList(ids);
      return data;
    } else {
      setDimensionList([]);
      setRelationDimensionOptions([]);
    }
    if (code !== 200) {
      message.error(msg);
    }
    return [];
  };

  const initDimensionData = async (metricItem: ISemantic.IMetricItem) => {
    await queryDrillDownDimension(metricItem.id);
  };

  useEffect(() => {
    if (metircData?.id) {
      getMetricTrendData({ ...queryParams });
      initDimensionData(metircData);
      setDrillDownDimensions(metircData?.relateDimension?.drillDownDimensions || []);
    }
  }, [metircData?.id, periodDate]);

  return (
    <div style={{ backgroundColor: theme_component_bg_color, marginTop: 20 }}>
      <div style={{ marginBottom: 25 }}>
        <Row>
          <Col flex="1 1 200px">
            <Form
              layout="inline"
              // form={form}
              colon={false}
              onValuesChange={(value, values) => {
                if (value.key) {
                  return;
                }
              }}
            >
              <StandardFormRow key="metricDate" title="日期区间:">
                <FormItem name="metricDate">
                  <MDatePicker
                    initialValues={getDatePickerDynamicInitialValues(7, DateRangeType.DAY)}
                    showCurrentDataRangeString={false}
                    onDateRangeChange={(value, config) => {
                      const [startDate, endDate] = value;
                      const { dateSettingType, dynamicParams, staticParams } = config;
                      let dateField = DateFieldMap[DateRangeType.DAY];
                      if (DateSettingType.DYNAMIC === dateSettingType) {
                        dateField = DateFieldMap[dynamicParams.dateRangeType];
                      }
                      if (DateSettingType.STATIC === dateSettingType) {
                        dateField = DateFieldMap[staticParams.dateRangeType];
                      }
                      setPeriodDate({ startDate, endDate, dateField });
                    }}
                    disabledAdvanceSetting={true}
                  />
                </FormItem>
              </StandardFormRow>
              <StandardFormRow key="dimensionSelected" title="维度下钻:">
                <FormItem name="dimensionSelected">
                  <Select
                    style={{ minWidth: 150, maxWidth: 200 }}
                    options={relationDimensionOptions}
                    showSearch
                    filterOption={(input, option) =>
                      ((option?.label ?? '') as string).toLowerCase().includes(input.toLowerCase())
                    }
                    mode="multiple"
                    placeholder="请选择下钻维度"
                    onChange={(value) => {
                      const params = { ...queryParams, dimensionGroup: value || [] };
                      setQueryParams(params);
                      getMetricTrendData({ ...params });
                      setGroupByDimensionFieldName(value[value.length - 1]);
                    }}
                  />
                </FormItem>
              </StandardFormRow>
              <StandardFormRow key="dimensionFilter" title="维度筛选:">
                <FormItem name="dimensionFilter">
                  <MetricTrendDimensionFilterContainer
                    modelId={metircData?.modelId || 0}
                    dimensionOptions={relationDimensionOptions}
                    periodDate={periodDate}
                    onChange={(filterList) => {
                      const dimensionFilters = filterList.map((item) => {
                        const { dimensionBizName, dimensionValue, operator } = item;
                        return {
                          bizName: dimensionBizName,
                          value: dimensionValue,
                          operator,
                        };
                      });
                      const params = {
                        ...queryParams,
                        dimensionFilters,
                      };
                      setQueryParams(params);
                      getMetricTrendData({ ...params });
                    }}
                  />
                </FormItem>
              </StandardFormRow>
            </Form>
          </Col>
          <Col flex="0 1">
            <Space>
              {metircData?.hasAdminRes && (
                <Button
                  type="primary"
                  key="addDimension"
                  onClick={() => {
                    setMetricRelationModalOpenState(true);
                  }}
                >
                  <Space>
                    下钻维度配置
                    <Tooltip title="配置下钻维度后，将可以在指标卡中进行下钻">
                      <InfoCircleOutlined />
                    </Tooltip>
                  </Space>
                </Button>
              )}

              <Space.Compact block>
                <Button
                  type="primary"
                  key="download"
                  loading={downloadLoding}
                  disabled={downloadBtnDisabledState}
                  onClick={() => {
                    getMetricTrendData({ download: true, ...queryParams });
                  }}
                >
                  <Space>
                    <DownloadOutlined />
                    下载
                  </Space>
                </Button>

                <Tooltip title="开启转置">
                  <Button
                    type={transformState ? 'primary' : 'default'}
                    icon={<PoweroffOutlined />}
                    onClick={() => {
                      setTransformState(!transformState);
                    }}
                  />
                </Tooltip>
              </Space.Compact>
            </Space>
          </Col>
        </Row>
      </div>
      {authMessage && <div style={{ color: '#d46b08', marginBottom: 15 }}>{authMessage}</div>}
      <Row style={{ marginBottom: 20 }}>
        <Col flex="1 1 300px">
          <Radio.Group
            size="small"
            buttonStyle="solid"
            options={[
              {
                label: (
                  <Tooltip title="折线图">
                    <LineChartOutlined />
                  </Tooltip>
                ),
                value: 'chart',
              },
              {
                label: (
                  <Tooltip title="表格">
                    <TableOutlined />
                  </Tooltip>
                ),
                value: 'table',
              },
            ]}
            onChange={(e) => {
              setChartType(e.target.value);
            }}
            value={chartType}
            optionType="button"
          />
          {/* <Space>
            <Select
              style={{ minWidth: 150, maxWidth: 200 }}
              options={showDimensionOptions}
              value={groupByDimensionFieldName}
              showSearch
              filterOption={(input, option) =>
                ((option?.label ?? '') as string).toLowerCase().includes(input.toLowerCase())
              }
              placeholder="展示维度切换"
              onChange={(value) => {
                setGroupByDimensionFieldName(value);
              }}
            />
          </Space> */}
        </Col>
        <Col flex="0 1 100px">
          <Space>
            <Select
              defaultValue={rowNumber}
              style={{
                width: 120,
                display:
                  Array.isArray(queryParams.dimensionGroup) &&
                  queryParams.dimensionGroup.length > 0 &&
                  chartType === 'chart'
                    ? 'block'
                    : 'none',
              }}
              onChange={(value) => {
                setRowNumber(value);
              }}
            >
              <Option value={5}>前5项</Option>
              <Option value={10}>前10项</Option>
              <Option value={15}>前15项</Option>
              <Option value={20}>前20项</Option>
            </Select>
          </Space>
        </Col>
      </Row>
      {chartType === 'chart' && (
        <TrendChart
          data={metricTrendData}
          isPer={
            metricColumnConfig?.dataFormatType === 'percent' &&
            metricColumnConfig?.dataFormat?.needMultiply100 === false
              ? true
              : false
          }
          isPercent={
            metricColumnConfig?.dataFormatType === 'percent' &&
            metricColumnConfig?.dataFormat?.needMultiply100 === true
              ? true
              : false
          }
          rowNumber={rowNumber}
          fields={indicatorFields.current}
          loading={metricTrendLoading}
          dateFieldName={periodDate.dateField}
          groupByDimensionFieldName={groupByDimensionFieldName}
          height={500}
          renderType="clear"
          decimalPlaces={metricColumnConfig?.dataFormat?.decimalPlaces || 2}
        />
      )}
      <div style={{ display: chartType === 'table' ? 'block' : 'none', marginBottom: 45 }}>
        <MetricTable
          loading={metricTrendLoading}
          columnConfig={tableColumnConfig}
          dataSource={metricTrendData}
          dateFieldName={periodDate.dateField}
          metricFieldName={indicatorFields.current?.[0]?.column}
        />
      </div>

      <DimensionAndMetricRelationModal
        metricItem={metircData}
        relationsInitialValue={drillDownDimensions}
        open={metricRelationModalOpenState}
        onCancel={() => {
          setMetricRelationModalOpenState(false);
        }}
        onSubmit={(relations) => {
          setDrillDownDimensions(relations);
          setMetricRelationModalOpenState(false);
          initDimensionData(metircData!);
        }}
      />
    </div>
  );
};

export default MetricTrendSection;
