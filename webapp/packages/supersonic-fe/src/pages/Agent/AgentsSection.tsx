import {DeleteOutlined, EditOutlined, PlusOutlined, UserOutlined} from '@ant-design/icons';
import {Button, Card, Input, Popconfirm, Rate, Switch} from 'antd';
import classNames from 'classnames';
import {useEffect, useState} from 'react';
import styles from './style.less';
import {AgentType} from './type';
import {Bar} from '@ant-design/charts';


const {Search, TextArea} = Input;

type Props = {
  agents: AgentType[];
  currentAgent?: AgentType;
  loading: boolean;
  onSelectAgent: (agent: AgentType) => void;
  onDeleteAgent: (id: number) => void;
  onEditAgent: (agent?: AgentType) => void;
  onSaveAgent: (agent: AgentType, noTip?: boolean) => Promise<void>;
};

const RecommendAgents = [
  {
   id: 101,
    name: '公文写作',
    description: '加速办公效率，润色改进您的公文',
    x: 4.5,
    y: 5
  },
  {
    id: 102,
    name: '情感咨询',
    description: '情感对话，你的专属助理',
    x: 5,
    y: 4
  },
  {
    id: 103,
    name: '智能音乐',
    description: '智能音乐生成，来试试生成自己的专属BGM',
    x: 3.5,
    y: 5
  },
  {
    id: 104,
    name: '图片融合',
    description: 'AI高质量图片融合',
    x: 5,
    y: 4
  },
  {
    id: 105,
    name: '代码分析',
    description: '智能加速软件开发',
    x: 4.5,
    y: 5
  },
  {
    id: 106,
    name: 'SQL生成',
    description: 'AI SQL语句生成和修改',
    x: 3.5,
    y: 4.5
  },
];

const StarBarValOne = [
  {
    x: 4,
    y: 5
  },
  {
    x: 3,
    y: 4
  },
  {
    x: 5,
    y: 3
  },
  {
    x: 4,
    y: 4
  },
  {
    x: 2,
    y: 3
  },
  {
    x: 5,
    y: 5
  },
  {
    x: 3,
    y: 2
  },
  {
    x: 4,
    y: 5
  },
];

const HotHopBar = () => {
  const data = [
    {
      title: '春节海报',
      期望值: 145,
    },
    {
      title: '周报助手',
      期望值: 61,
    },
    {
      title: '视频总结',
      期望值: 52,
    },
    {
      title: '英文翻译',
      期望值: 31,
    },
    {
      title: '图片融合',
      期望值: 10,
    },
  ];
  const config = {
    title: {
      visible: false,
      text: '热度排行',
    },
    forceFit: true,
    data,
    xField: '期望值',
    yField: 'title',
    height: 300,
    color: '#13c2c2',
    label: {
      visible: true,
      position: 'middle',
    },
  };
  return <Bar {...config} />;
};

const AgentsSection: React.FC<Props> = ({
                                          agents,
                                          currentAgent,
                                          onSelectAgent,
                                          onDeleteAgent,
                                          onEditAgent,
                                          onSaveAgent,
                                        }) => {
  // const [searchName, setSearchName] = useState('');
  const [showAgents, setShowAgents] = useState<AgentType[]>([]);

  useEffect(() => {
    setShowAgents(agents);
  }, [agents]);

  return (
    <div className={styles.agentsSection}>
      {/* <div className={styles.sectionTitle}>问答助理</div> */}
      <div className={styles.content}>
        <div className={styles.searchBar}>
          {/* <Search
            placeholder="请输入助理名称搜索"
            className={styles.searchControl}
            value={searchName}
            onChange={(e) => {
              setSearchName(e.target.value);
            }}
            onSearch={(value) => {
              setShowAgents(
                agents.filter((agent) =>
                  agent.name?.trim().toLowerCase().includes(value.trim().toLowerCase()),
                ),
              );
            }}
          /> */}
          <Button
            type="primary"
            onClick={() => {
              onEditAgent(undefined);
            }}
          >
            <PlusOutlined/>
            新建助理
          </Button>
        </div>
        <Card title="已配置" style={{width: '100%'}} bordered={false}>
          <div className={styles.agentsContainer}>
            {showAgents.map((agent, index) => {
              const agentItemClass = classNames(styles.agentItem, {
                [styles.agentActive]: agent.id === currentAgent?.id,
              });
              return (
                <div
                  className={agentItemClass}
                  key={agent.id}
                  onClick={() => {
                    onSelectAgent(agent);
                  }}
                >
                  <UserOutlined className={styles.agentIcon}/>
                  <div className={styles.agentContent}>
                    <div className={styles.agentNameBar}>
                      <div className={styles.agentName}>{agent.name}</div>
                      <div className={styles.operateIcons}>
                        <EditOutlined
                          className={styles.operateIcon}
                          onClick={(e) => {
                            e.stopPropagation();
                            onEditAgent(agent);
                          }}
                        />
                        <Popconfirm
                          title="确定删除吗？"
                          onCancel={(e) => {
                            e?.stopPropagation();
                          }}
                          onConfirm={(e) => {
                            e?.stopPropagation();
                            onDeleteAgent(agent.id!);
                          }}
                        >
                          <DeleteOutlined
                            className={styles.operateIcon}
                            onClick={(e) => {
                              e.stopPropagation();
                            }}
                          />
                        </Popconfirm>
                      </div>
                    </div>
                    <div className={styles.centerBar}>
                      <div className={styles.agentDescription} title={agent.description}>
                        {agent.description}
                      </div>
                      <div className={styles.toggleStatus}>
                        {agent.status === 0 ? (
                          '已禁用'
                        ) : (
                          <span className={styles.online}>已加载</span>
                        )}
                        <span
                          onClick={(e) => {
                            e.stopPropagation();
                          }}
                        >
                        <Switch
                          size="small"
                          defaultChecked={agent.status === 1}
                          onChange={(value) => {
                            onSaveAgent({...agent, status: value ? 1 : 0}, true);
                          }}
                        />
                      </span>
                      </div>
                    </div>
                    <div className={styles.gradeBar}>
                      <div className={styles.gradeItem}>
                        <span>成长值：</span>
                        <Rate className={styles.simpleRate} disabled
                              defaultValue={StarBarValOne[index] ? StarBarValOne[index].x : 5}
                              style={{fontSize: 15, color: '#ff8c94'}}/>
                      </div>
                      <div className={styles.gradeItem}>
                        <span>营养值：</span>
                        <Rate className={styles.simpleRate} character="■" disabled
                              defaultValue={StarBarValOne[index] ? StarBarValOne[index].y : 5}
                              style={{fontSize: 15, color: '#bae637'}}/>
                      </div>
                    </div>
                  </div>
                </div>
              );
            })}
          </div>
        </Card>
        <Card title="推荐使用" style={{width: '100%', marginTop: 16}} bordered={false}>
          <div className={styles.agentsContainer}>
            {RecommendAgents.map((agent, index) => {
              const agentItemClass = classNames(styles.agentItem, {
                [styles.agentActive]: agent.id === currentAgent?.id,
              });
              return (
                <div
                  className={styles.agentRecommend}
                  key={agent.id}
                  onClick={() => {
                    onSelectAgent(agent);
                  }}
                >
                  <UserOutlined className={styles.agentIcon}/>
                  <div className={styles.agentContent}>
                    <div className={styles.agentNameBar}>
                      <div className={styles.agentName}>{agent.name}</div>
                      <div className={styles.operateIcons}>
                      </div>
                    </div>
                    <div className={styles.centerBar}>
                      <div className={styles.agentDescription} title={agent.description}>
                        {agent.description}
                      </div>
                      <div className={styles.toggleStatus}>
                        <Button type="primary" shape="circle" size="small">
                          +
                        </Button>
                      </div>
                    </div>
                    <div className={styles.gradeBar}>
                      <div className={styles.gradeItem}>
                        <span>成长值：</span>
                        <Rate className={styles.simpleRate} disabled
                              defaultValue={StarBarValOne[index] ? StarBarValOne[index].x : 5}
                              style={{fontSize: 15, color: '#ff8c94'}}/>
                      </div>
                      <div className={styles.gradeItem}>
                        <span>营养值：</span>
                        <Rate className={styles.simpleRate} character="■" disabled
                              defaultValue={StarBarValOne[index] ? StarBarValOne[index].y : 5}
                              style={{fontSize: 15, color: '#bae637'}}/>
                      </div>
                    </div>
                  </div>
                </div>
              );
            })}
          </div>
        </Card>

        <Card title="我想要" style={{width: '100%', marginTop: 16}} bordered={false}>
          <TextArea rows={4} placeholder="请输入您期望的智能助理描述，不超过300字" maxLength={300}/>

        </Card>
        <Card title="热度排行" style={{width: '100%', marginTop: 16}} bordered={false}>
          <HotHopBar/>
        </Card>
      </div>
    </div>
  );
};

export default AgentsSection;
