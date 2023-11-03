import { getLeafList } from '@/utils/utils';
import { PlusOutlined } from '@ant-design/icons';
import { Button, Input, message, Popconfirm, Select, Table, Tag } from 'antd';
import moment from 'moment';
import { history } from 'umi';
import { useEffect, useState } from 'react';
import { PARSE_MODE_MAP, PLUGIN_TYPE_MAP } from './constants';
import DetailModal from './DetailModal';
import { deletePlugin, getModelList, getPluginList } from './service';
import styles from './style.less';
import { ModelType, ParseModeEnum, PluginType, PluginTypeEnum } from './type';

const { Search } = Input;

const PluginManage = () => {
  const [name, setName] = useState<string>();
  const [type, setType] = useState<PluginTypeEnum>();
  const [pattern, setPattern] = useState<string>();
  const [model, setModel] = useState<string>();
  const [data, setData] = useState<PluginType[]>([]);
  const [modelList, setModelList] = useState<ModelType[]>([]);
  const [loading, setLoading] = useState(false);
  const [currentPluginDetail, setCurrentPluginDetail] = useState<PluginType>();
  const [detailModalVisible, setDetailModalVisible] = useState(false);

  const initModelList = async () => {
    const res = await getModelList();
    setModelList(getLeafList(res.data));
  };

  const appendPlug = [
    {
      id: 10000,
      name: '数据库管理',
      modelList: [-1],
      type: 'DATA_PLUG',
      pattern: '数据库管理',
      updatedBy: 'admin',
      updatedAt: '2023-10-29T11:15:46.000+00:00',
    },
    {
      id: 10001,
      name: '语义模型',
      modelList: [-1],
      type: 'DATA_PLUG',
      pattern: '语义模型',
      updatedBy: 'admin',
      updatedAt: '2023-10-29T15:30:46.000+00:00',
    },
    {
      id: 10002,
      name: '指标看板',
      modelList: [-1],
      type: 'DATA_PLUG',
      pattern: '指标看板',
      updatedBy: 'admin',
      updatedAt: '2023-10-29T15:23:46.000+00:00',
    },
  ]

  const updateData = async (filters?: any) => {
    setLoading(true);
    const res = await getPluginList({ name, type, pattern, model, ...(filters || {}) });
    setLoading(false);
    console.log(res.data)
    // TODO 此处给data增加三行信息 数据库管理 语义管理 指标管理
    res.data = res.data.concat(appendPlug)
    setData(res.data?.map((item) => ({ ...item, config: JSON.parse(item.config || '{}') })) || []);
  };

  useEffect(() => {
    initModelList();
    updateData();
  }, []);

  const onCheckPluginDetail = (record: PluginType) => {
    setCurrentPluginDetail(record);
    setDetailModalVisible(true);
  };

  const onDeletePlugin = async (record: PluginType) => {
    await deletePlugin(record.id);
    message.success('插件删除成功');
    updateData();
  };

  const columns: any[] = [
    {
      title: '组件名称',
      dataIndex: 'name',
      key: 'name',
    },
    // {
    //   title: '主题域',
    //   dataIndex: 'modelList',
    //   key: 'modelList',
    //   width: 200,
    //   render: (value: number[]) => {
    //     if (value?.includes(-1)) {
    //       return '默认';
    //     }
    //     return value ? (
    //       <div className={styles.modelColumn}>
    //         {value.map((id, index) => {
    //           const name = modelList.find((model) => model.id === +id)?.name;
    //           return name ? <Tag key={id}>{name}</Tag> : null;
    //         })}
    //       </div>
    //     ) : (
    //       '-'
    //     );
    //   },
    // },
    {
      title: '组件类型',
      dataIndex: 'type',
      key: 'type',
      render: (value: string) => {
        return (
          <Tag color={value === PluginTypeEnum.WEB_PAGE ? 'blue' : value === PluginTypeEnum.DATA_PLUG ? 'purple' : 'cyan'}>
            {PLUGIN_TYPE_MAP[value]}
          </Tag>
        );
      },
    },
    {
      title: '简介',
      dataIndex: 'pattern',
      key: 'pattern',
      width: 450,
    },
    // {
    //   title: '更新人',
    //   dataIndex: 'updatedBy',
    //   key: 'updatedBy',
    //   render: (value: string) => {
    //     return value || '-';
    //   },
    // },
    {
      title: '更新时间',
      dataIndex: 'updatedAt',
      key: 'updatedAt',
      render: (value: string) => {
        return value ? moment(value).format('YYYY-MM-DD HH:mm') : '-';
      },
    },
    {
      title: '操作',
      dataIndex: 'x',
      key: 'x',
      render: (_: any, record: any) => {
        if(record.id === 10000){
          return (<div className={styles.operator}>
            <a
              onClick={() => {
                history.push('/database');
              }}
            >
              编辑
            </a>
            <Popconfirm
              title="确定删除吗？"
              onConfirm={() => {
              }}
            >
              <a>删除</a>
            </Popconfirm>
          </div>)
        }
        if(record.id === 10001){
          return (<div className={styles.operator}>
            <a
              onClick={() => {
                history.push('/model');
              }}
            >
              编辑
            </a>
            <Popconfirm
              title="确定删除吗？"
              onConfirm={() => {
              }}
            >
              <a>删除</a>
            </Popconfirm>
          </div>)
        }
        if(record.id === 10002){
          return (<div className={styles.operator}>
            <a
              onClick={() => {
                history.push('/metric');
              }}
            >
              编辑
            </a>
            <Popconfirm
              title="确定删除吗？"
              onConfirm={() => {
              }}
            >
              <a>删除</a>
            </Popconfirm>
          </div>)
        }

        return (
          <div className={styles.operator}>
            <a
              onClick={() => {
                onCheckPluginDetail(record);
              }}
            >
              编辑
            </a>
            <Popconfirm
              title="确定删除吗？"
              onConfirm={() => {
                onDeletePlugin(record);
              }}
            >
              <a>删除</a>
            </Popconfirm>
          </div>
        );
      },
    },
  ];

  const onModelChange = (value: string) => {
    setModel(value);
    updateData({ model: value });
  };

  const onTypeChange = (value: PluginTypeEnum) => {
    setType(value);
    updateData({ type: value });
  };

  const onSearch = () => {
    updateData();
  };

  const onCreatePlugin = () => {
    setCurrentPluginDetail(undefined);
    setDetailModalVisible(true);
  };

  const onSavePlugin = () => {
    setDetailModalVisible(false);
    updateData();
  };

  return (
    <div className={styles.pluginManage}>
      <div className={styles.filterSection}>
        {/*<div className={styles.filterItem}>*/}
        {/*  <div className={styles.filterItemTitle}>主题域</div>*/}
        {/*  <Select*/}
        {/*    className={styles.filterItemControl}*/}
        {/*    placeholder="请选择主题域"*/}
        {/*    options={modelList.map((model) => ({ label: model.name, value: model.id }))}*/}
        {/*    value={model}*/}
        {/*    allowClear*/}
        {/*    onChange={onModelChange}*/}
        {/*  />*/}
        {/*</div>*/}
        <div className={styles.filterItem}>
          <div className={styles.filterItemTitle}>组件名称</div>
          <Search
            className={styles.filterItemControl}
            placeholder="请输入组件名称"
            value={name}
            onChange={(e) => {
              setName(e.target.value);
            }}
            onSearch={onSearch}
          />
        </div>
        <div className={styles.filterItem}>
          <div className={styles.filterItemTitle}>描述</div>
          <Search
            className={styles.filterItemControl}
            placeholder="请输入组件描述"
            value={pattern}
            onChange={(e) => {
              setPattern(e.target.value);
            }}
            onSearch={onSearch}
          />
        </div>
        <div className={styles.filterItem}>
          <div className={styles.filterItemTitle}>组件类型</div>
          <Select
            className={styles.filterItemControl}
            placeholder="请选择组件类型"
            options={Object.keys(PLUGIN_TYPE_MAP).map((key) => ({
              label: PLUGIN_TYPE_MAP[key],
              value: key,
            }))}
            value={type}
            allowClear
            onChange={onTypeChange}
          />
        </div>
      </div>
      <div className={styles.pluginList}>
        <div className={styles.titleBar}>
          <div className={styles.title}>组件列表</div>
          <Button type="primary" onClick={onCreatePlugin}>
            <PlusOutlined />
            新建组件
          </Button>
        </div>
        <Table
          columns={columns}
          dataSource={data}
          size="small"
          pagination={{ defaultPageSize: 20 }}
          loading={loading}
        />
      </div>
      {detailModalVisible && (
        <DetailModal
          detail={currentPluginDetail}
          onSubmit={onSavePlugin}
          onCancel={() => {
            setDetailModalVisible(false);
          }}
        />
      )}
    </div>
  );
};

export default PluginManage;
