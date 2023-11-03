import {InfoCircleOutlined} from '@ant-design/icons';
import {TinyArea, TinyColumn, Progress} from '@ant-design/charts';
import {Col, Row, Tooltip} from 'antd';

import numeral from 'numeral';
import {ChartCard, Field} from './Charts';
import type {DataItem} from '../data.d';
import Trend from './Trend';
import Yuan from '../utils/Yuan';
import styles from '../style.less';

const topColResponsiveProps = {
  xs: 24,
  sm: 12,
  md: 12,
  lg: 12,
  xl: 6,
  style: {marginBottom: 24},
};

const IntroduceRow = ({loading, visitData}: { loading: boolean; visitData: DataItem[] }) => (
  <Row gutter={24}>
    <Col {...topColResponsiveProps}>
      <ChartCard
        bordered={false}
        title="总执行任务数"
        action={
          <Tooltip title="指标说明">
            <InfoCircleOutlined/>
          </Tooltip>
        }
        loading={loading}
        total={() => 139407}
        footer={<Field label="日执行数" value={`${numeral(302).format('0,0')}`}/>}
        contentHeight={46}
      >
        <Trend flag="up" style={{marginRight: 16}}>
          周同比
          <span className={styles.trendText}>8%</span>
        </Trend>
        <Trend flag="down">
          日同比
          <span className={styles.trendText}>11%</span>
        </Trend>
      </ChartCard>
    </Col>

    <Col {...topColResponsiveProps}>
      <ChartCard
        bordered={false}
        loading={loading}
        title="总采纳数"
        action={
          <Tooltip title="指标说明">
            <InfoCircleOutlined/>
          </Tooltip>
        }
        total={numeral(8846).format('0,0')}
        footer={<Field label="日采纳数" value={numeral(13).format('0,0')}/>}
        contentHeight={46}
      >
        <TinyArea
          height={46}
          color="#975FE4"
          forceFit
          smooth
          data={visitData}
        />
      </ChartCard>
    </Col>
    <Col {...topColResponsiveProps}>
      <ChartCard
        bordered={false}
        loading={loading}
        title="插件配置数"
        action={
          <Tooltip title="指标说明">
            <InfoCircleOutlined/>
          </Tooltip>
        }
        total={numeral(6560).format('0,0')}
        footer={<Field label="使用率" value="90%"/>}
        contentHeight={46}
      >
        <TinyColumn height={46} forceFit data={visitData}/>
      </ChartCard>
    </Col>
    <Col {...topColResponsiveProps}>
      <ChartCard
        loading={loading}
        bordered={false}
        title="总采纳比"
        action={
          <Tooltip title="指标说明">
            <InfoCircleOutlined/>
          </Tooltip>
        }
        total="78%"
        footer={
          <div style={{whiteSpace: 'nowrap', overflow: 'hidden'}}>
            <Trend flag="up" style={{marginRight: 16}}>
              周同比
              <span className={styles.trendText}>3%</span>
            </Trend>
            <Trend flag="down">
              日同比
              <span className={styles.trendText}>17%</span>
            </Trend>
          </div>
        }
        contentHeight={46}
      >
        <Progress
          height={46}
          percent={0.69}
          color="#13C2C2"
          forceFit
          size={8}
          marker={[
            {
              value: 0.8,
              style: {
                stroke: '#13C2C2',
              },
            },
          ]}
        />
      </ChartCard>
    </Col>
  </Row>
);

export default IntroduceRow;
