import { Button, Spin } from 'antd';
import {CheckCircleFilled, ReloadOutlined, SmileTwoTone} from '@ant-design/icons';
import { PREFIX_CLS,PRIMARY_COLOR } from '../../common/constants';
import { MsgDataType } from '../../common/type';
import ChatMsg from '../ChatMsg';
import WebPage from '../ChatMsg/WebPage';
import ImagePage from '../ChatMsg/ImagePage';
import TextPage from '../ChatMsg/TextPage';
import Loading from './Loading';
import React, { ReactNode } from 'react';

type Props = {
  queryId?: number;
  executeLoading: boolean;
  entitySwitchLoading: boolean;
  chartIndex: number;
  executeTip?: string;
  executeItemNode?: ReactNode;
  renderCustomExecuteNode?: boolean;
  data?: MsgDataType;
  triggerResize?: boolean;
  isDeveloper?: boolean;
};

const ExecuteItem: React.FC<Props> = ({
  queryId,
  executeLoading,
  entitySwitchLoading,
  chartIndex,
  executeTip,
  executeItemNode,
  renderCustomExecuteNode,
  data,
  triggerResize,
  isDeveloper,
}) => {
  const prefixCls = `${PREFIX_CLS}-item`;

  const getNodeTip = (title: ReactNode, tip?: string) => {
    return (
      <>
        <div className={`${prefixCls}-title-bar`}>
          <SmileTwoTone className={`${prefixCls}-step-icon`} twoToneColor={PRIMARY_COLOR} />
          <div className={`${prefixCls}-step-title`}>
            {title}
            {!tip && <Loading />}
          </div>
        </div>
        {tip && <div className={`${prefixCls}-content-container`}>{tip}</div>}
      </>
    );
  };

  if (executeLoading) {
    return getNodeTip('数据查询中');
  }

  if (executeTip) {
    return getNodeTip(
      <>
        数据查询失败
        {data?.queryTimeCost && isDeveloper && (
          <span className={`${prefixCls}-title-tip`}>(耗时: {data.queryTimeCost}ms)</span>
        )}
      </>,
      executeTip
    );
  }

  if (!data) {
    return null;
  }

  return (
    <>
      <div className={`${prefixCls}-title-bar`}>
        <SmileTwoTone className={`${prefixCls}-step-icon`} twoToneColor={PRIMARY_COLOR} />
        <div className={`${prefixCls}-step-title`}>
          结果查询
          {data?.queryTimeCost && isDeveloper && (
            <span className={`${prefixCls}-title-tip`}>(耗时: {data.queryTimeCost}ms)</span>
          )}
        </div>
      </div>
      <div className={`${prefixCls}-content-container`}>
        <Spin spinning={entitySwitchLoading}>
          {data.queryAuthorization?.message && (
            <div className={`${prefixCls}-auth-tip`}>提示：{data.queryAuthorization.message}</div>
          )}
          {renderCustomExecuteNode && executeItemNode ? (
            executeItemNode
          ) : data?.queryMode === 'WEB_PAGE' ? (
            <WebPage id={queryId!} data={data} />
          ) :  data?.resultType === 'IMAGE' ? (
              <ImagePage id={queryId!} data={data} />
          ) :  data?.resultType === 'TEXT' ? (
              <TextPage id={queryId!} data={data} />
          ) : (
            <ChatMsg
              queryId={queryId}
              data={data}
              chartIndex={chartIndex}
              triggerResize={triggerResize}
            />
          )}
        </Spin>
      </div>
    </>
  );
};

export default ExecuteItem;
