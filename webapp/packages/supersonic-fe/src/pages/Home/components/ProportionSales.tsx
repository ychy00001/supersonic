import { Card, Radio, Typography } from 'antd';
import numeral from 'numeral';
import type { RadioChangeEvent } from 'antd/es/radio';
import { Pie, G2 } from '@ant-design/charts';
import React from 'react';
import type { DataItem } from '../data.d';
import styles from '../style.less';

const { Text } = Typography;
const G = G2.getEngine('canvas');
const ProportionSales = ({
  dropdownGroup,
  salesType,
  loading,
  salesPieData,
  handleChangeSalesType,
}: {
  loading: boolean;
  dropdownGroup: React.ReactNode;
  salesType: 'all' | 'online' | 'stores';
  salesPieData: DataItem[];
  handleChangeSalesType?: (e: RadioChangeEvent) => void;
}) => (
  <Card
    loading={loading}
    className={styles.salesCard}
    bordered={false}
    title="各助手使用占比"
    style={{
      height: '100%',
    }}
    extra={
      <div className={styles.salesCardExtra}>
        {dropdownGroup}
        <div className={styles.salesTypeRadio}>
          <Radio.Group value={salesType} onChange={handleChangeSalesType}>
            <Radio.Button value="all">全平台</Radio.Button>
            <Radio.Button value="online">本地</Radio.Button>
            {/*<Radio.Button value="stores">全平台</Radio.Button>*/}
          </Radio.Group>
        </div>
      </div>
    }
  >
    <div>
      <Text>执行任务数</Text>
      <Pie
        appendPadding={10}
        height={340}
        radius={0.8}
        angleField="y"
        colorField="x"
        data={salesPieData as any}
        legend={false}
        label={{
          type: 'spider',
          labelHeight: 40,
          formatter: (text, item) => {
            const group = new G.Group({});
            group.addShape({
              type: 'circle',
              attrs: {
                x: 0,
                y: 0,
                width: 40,
                height: 50,
                r: 5,
                fill: item.color,
              },
            });
            group.addShape({
              type: 'text',
              attrs: {
                x: 10,
                y: 8,
                text: `${text.x}`,
                fill: item.color,
              },
            });
            group.addShape({
              type: 'text',
              attrs: {
                x: 0,
                y: 25,
                text: `${(text.percent * 100).toFixed(1)}%`,
                fill: 'rgba(0, 0, 0, 0.65)',
                fontWeight: 700,
              },
            });
            return group;
            // eslint-disable-next-line no-underscore-dangle
            // return `${item._origin.x}: ${numeral(item._origin.y).format('0,0')}`;
          },
        }}
        interactions= {[
          {
            type: 'element-selected',
          },
          {
            type: 'element-active',
          },
        ]}
      />
    </div>
  </Card>
);

export default ProportionSales;
