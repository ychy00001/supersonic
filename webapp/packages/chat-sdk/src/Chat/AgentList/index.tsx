import {
    AlertOutlined, BankOutlined, ContactsOutlined,
    CrownOutlined, ExperimentOutlined,
    FileProtectOutlined,
    HeatMapOutlined,
    PlusCircleOutlined,
    RadarChartOutlined, ReconciliationOutlined, SlidersOutlined
} from '@ant-design/icons';
import { AgentType } from '../type';
import styles from './style.module.less';
import classNames from 'classnames';
import { message } from 'antd';
import IconFont from '../../components/IconFont';
import { AGENT_ICONS } from '../constants';

type Props = {
  agentList: AgentType[];
  currentAgent?: AgentType;
  onSelectAgent: (agent: AgentType) => void;
};

export const AGENT_ICONS_NEWS = (index) => {
    if(index % 10 == 0){
        return <AlertOutlined className={styles.avatar}/>;
    }
    if(index % 10 == 1){
        return <ReconciliationOutlined className={styles.avatar}/>
    }
    if(index % 10 == 2){
        return <ExperimentOutlined className={styles.avatar}/>;
    }
    if(index % 10 == 3){
        return <ContactsOutlined className={styles.avatar}/>
    }
    if(index % 10 == 4){
        return <BankOutlined className={styles.avatar}/>;
    }
    if(index % 10 == 5){
        return <SlidersOutlined className={styles.avatar}/>;
    }
    if(index % 10 == 6){
        return <RadarChartOutlined className={styles.avatar} />;
    }
    if(index % 10 == 7){
        return <HeatMapOutlined className={styles.avatar} />;
    }
    if(index % 10 == 8){
        return <CrownOutlined className={styles.avatar}/>
    }
    if(index % 10 == 9){
        return <FileProtectOutlined className={styles.avatar}/>;
    }
}

const AgentList: React.FC<Props> = ({ agentList, currentAgent, onSelectAgent }) => {
  const onAddAgent = () => {
    message.info('正在开发中，敬请期待');
  };

  return (
    <div className={styles.agentList}>
      <div className={styles.header}>
        <div className={styles.headerTitle}>智能助理</div>
        {/*<PlusCircleOutlined className={styles.plusIcon} onClick={onAddAgent} />*/}
      </div>
      <div className={styles.agentListContent}>
        {agentList.map((agent, index) => {
          const agentItemClass = classNames(styles.agentItem, {
            [styles.active]: currentAgent?.id === agent.id,
          });
          return (
            <div
              key={agent.id}
              className={agentItemClass}
              onClick={() => {
                onSelectAgent(agent);
              }}
            >
                {AGENT_ICONS_NEWS(index)}
              {/*<IconFont type={AGENT_ICONS[index % AGENT_ICONS.length]} className={styles.avatar} />*/}
              <div className={styles.agentInfo}>
                <div className={styles.agentName}>{agent.name}</div>
                <div className={styles.agentDesc}>{agent.description}</div>
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default AgentList;
