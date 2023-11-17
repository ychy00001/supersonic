import { isMobile } from '../../utils/utils';
import {DeleteFilled, DislikeOutlined, LikeOutlined} from '@ant-design/icons';
import { CLS_PREFIX } from '../../common/constants';
import { useState } from 'react';
import classNames from 'classnames';
import { updateQAFeedback } from '../../service';
import {MsgDataType} from "../../common/type";
import { message, Popconfirm } from 'antd';

type Props = {
  queryId: number;
  bubbleIndex: number;
  scoreValue?: number;
  isLastMessage?: boolean;
  onMsgDelete?: (queryId: number, bubbleIndex:number) => void;
};

const Tools: React.FC<Props> = ({ queryId,bubbleIndex, scoreValue, isLastMessage ,onMsgDelete}) => {
  const [score, setScore] = useState(scoreValue || 0);

  const prefixCls = `${CLS_PREFIX}-tools`;

  const like = () => {
    setScore(5);
    updateQAFeedback(queryId, 5);
  };

  const dislike = () => {
    setScore(1);
    updateQAFeedback(queryId, 1);
  };

  const delConfirm = (e) => {
    onMsgDelete?.(queryId, bubbleIndex);
    message.success('删除成功！');
  };

  const likeClass = classNames(`${prefixCls}-like`, {
    [`${prefixCls}-feedback-active`]: score === 5,
  });
  const dislikeClass = classNames(`${prefixCls}-dislike`, {
    [`${prefixCls}-feedback-active`]: score === 1,
  });
  const deleteClass = classNames(`${prefixCls}-delete`);

  return (
    <div className={prefixCls}>
      {!isMobile && (
        <div className={`${prefixCls}-feedback`}>
          <div>内容满意吗？</div>
          <LikeOutlined className={likeClass} onClick={like} />
          <DislikeOutlined
            className={dislikeClass}
            onClick={e => {
              e.stopPropagation();
              dislike();
            }}
          />
          <Popconfirm
              title="确定删除该查询?"
              onConfirm={delConfirm}
              okText="是"
              cancelText="否"
          >
            <DeleteFilled className={deleteClass} />
          </Popconfirm>

        </div>
      )}
    </div>
  );
};

export default Tools;
