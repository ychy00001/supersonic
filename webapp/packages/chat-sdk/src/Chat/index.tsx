import { updateMessageContainerScroll, isMobile, uuid, setToken } from '../utils/utils';
import {
  ForwardRefRenderFunction,
  forwardRef,
  useEffect,
  useImperativeHandle,
  useRef,
  useState,
} from 'react';
import MessageContainer from './MessageContainer';
import styles from './style.module.less';
import { ConversationDetailType, MessageItem, MessageTypeEnum, AgentType } from './type';
import { queryAgentList } from './service';
import { useThrottleFn } from 'ahooks';
import Conversation from './Conversation';
import ChatFooter from './ChatFooter';
import classNames from 'classnames';
import { cloneDeep } from 'lodash';
import AgentList from './AgentList';
import MobileAgents from './MobileAgents';
import { HistoryMsgItemType, MsgDataType, SendMsgParamsType } from '../common/type';
import { getHistoryMsg } from '../service';
import ShowCase from '../ShowCase';
import { Modal } from 'antd';

type Props = {
  token?: string;
  agentIds?: number[];
  initialAgentId?: number;
  chatVisible?: boolean;
  noInput?: boolean;
  isDeveloper?: boolean;
  integrateSystem?: string;
  isCopilot?: boolean;
  onCurrentAgentChange?: (agent?: AgentType) => void;
  onReportMsgEvent?: (msg: string, valid: boolean) => void;
};

const Chat: ForwardRefRenderFunction<any, Props> = (
  {
    token,
    agentIds,
    initialAgentId,
    chatVisible,
    noInput,
    isDeveloper,
    integrateSystem,
    isCopilot,
    onCurrentAgentChange,
    onReportMsgEvent,
  },
  ref
) => {
  const [messageList, setMessageList] = useState<MessageItem[]>([]);
  const [inputMsg, setInputMsg] = useState('');
  const [pageNo, setPageNo] = useState(1);
  const [hasNextPage, setHasNextPage] = useState(false);
  const [historyInited, setHistoryInited] = useState(false);
  const [currentConversation, setCurrentConversation] = useState<
    ConversationDetailType | undefined
  >(isMobile ? { chatId: 0, chatName: '问答' } : undefined);
  const [historyVisible, setHistoryVisible] = useState(false);
  const [agentList, setAgentList] = useState<AgentType[]>([]);
  const [currentAgent, setCurrentAgent] = useState<AgentType>();
  const [mobileAgentsVisible, setMobileAgentsVisible] = useState(false);
  const [agentListVisible, setAgentListVisible] = useState(true);
  const [showCaseVisible, setShowCaseVisible] = useState(false);

  const conversationRef = useRef<any>();
  const chatFooterRef = useRef<any>();

  useImperativeHandle(ref, () => ({
    sendCopilotMsg,
  }));

  const sendCopilotMsg = (params: SendMsgParamsType) => {
    setAgentListVisible(false);
    const { agentId, msg, modelId } = params;
    if (currentAgent?.id !== agentId) {
      setMessageList([]);
      const agent = agentList.find(item => item.id === agentId) || ({} as AgentType);
      updateCurrentAgent({ ...agent, initialSendMsgParams: params });
    } else {
      onSendMsg(msg, messageList, modelId, params);
    }
  };

  const updateCurrentAgent = (agent?: AgentType) => {
    setCurrentAgent(agent);
    onCurrentAgentChange?.(agent);
    localStorage.setItem('AGENT_ID', `${agent?.id}`);
    if (!isCopilot) {
      window.history.replaceState({}, '', `${window.location.pathname}?agentId=${agent?.id}`);
    }
  };

  const initAgentList = async () => {
    const res = await queryAgentList();
    const agentListValue = (res.data || []).filter(
      item => item.status === 1 && (agentIds === undefined || agentIds.includes(item.id))
    );
    setAgentList(agentListValue);
    if (agentListValue.length > 0) {
      const agentId = initialAgentId || localStorage.getItem('AGENT_ID');
      if (agentId) {
        const agent = agentListValue.find(item => item.id === +agentId);
        updateCurrentAgent(agent || agentListValue[0]);
      } else {
        updateCurrentAgent(agentListValue[0]);
      }
    }
  };

  useEffect(() => {
    initAgentList();
  }, []);

  useEffect(() => {
    if (token) {
      setToken(token);
    }
  }, [token]);

  useEffect(() => {
    if (chatVisible) {
      inputFocus();
      updateMessageContainerScroll();
    }
  }, [chatVisible]);

  useEffect(() => {
    if (!currentConversation) {
      return;
    }
    const { initialMsgParams, isAdd } = currentConversation;
    if (isAdd) {
      inputFocus();
      if (initialMsgParams) {
        onSendMsg(initialMsgParams.msg, [], initialMsgParams.modelId, initialMsgParams);
        return;
      }
      sendHelloRsp();
      return;
    }
    updateHistoryMsg(1);
    setPageNo(1);
  }, [currentConversation]);

  useEffect(() => {
    if (historyInited) {
      const messageContainerEle = document.getElementById('messageContainer');
      messageContainerEle?.addEventListener('scroll', handleScroll);
    }
    return () => {
      const messageContainerEle = document.getElementById('messageContainer');
      messageContainerEle?.removeEventListener('scroll', handleScroll);
    };
  }, [historyInited]);

  const sendHelloRsp = (agent?: AgentType) => {
    if (noInput) {
      return;
    }
    setMessageList([
      {
        id: uuid(),
        type: MessageTypeEnum.AGENT_LIST,
        msg: agent?.name || currentAgent?.name || agentList?.[0]?.name,
      },
    ]);
  };

  const convertHistoryMsg = (list: HistoryMsgItemType[]) => {
    return list.map((item: HistoryMsgItemType) => ({
      id: item.questionId,
      type: MessageTypeEnum.QUESTION,
      msg: item.queryText,
      parseInfos: item.parseInfos,
      msgData: item.queryResult,
      score: item.score,
      agentId: currentAgent?.id,
    }));
  };

  const updateHistoryMsg = async (page: number) => {
    const res = await getHistoryMsg(page, currentConversation!.chatId, 3);
    const { hasNextPage, list } = res?.data || { hasNextPage: false, list: [] };
    const msgList = [...convertHistoryMsg(list), ...(page === 1 ? [] : messageList)];
    setMessageList(msgList);
    setHasNextPage(hasNextPage);
    if (page === 1) {
      if (list.length === 0) {
        sendHelloRsp();
      }
      updateMessageContainerScroll();
      setHistoryInited(true);
      inputFocus();
    } else {
      const msgEle = document.getElementById(`${messageList[0]?.id}`);
      msgEle?.scrollIntoView();
    }
  };

  const { run: handleScroll } = useThrottleFn(
    e => {
      if (e.target.scrollTop === 0 && hasNextPage) {
        updateHistoryMsg(pageNo + 1);
        setPageNo(pageNo + 1);
      }
    },
    {
      leading: true,
      trailing: true,
      wait: 200,
    }
  );

  const inputFocus = () => {
    if (!isMobile) {
      chatFooterRef.current?.inputFocus();
    }
  };

  const inputBlur = () => {
    chatFooterRef.current?.inputBlur();
  };

  const onSendMsg = async (
    msg?: string,
    list?: MessageItem[],
    modelId?: number,
    sendMsgParams?: SendMsgParamsType
  ) => {
    const currentMsg = msg || inputMsg;
    if (currentMsg.trim() === '') {
      setInputMsg('');
      return;
    }

    const msgAgent = agentList.find(item => currentMsg.indexOf(item.name) === 1);
    const certainAgent = currentMsg[0] === '/' && msgAgent;
    const agentIdValue = certainAgent ? msgAgent.id : undefined;
    const agent = agentList.find(item => item.id === sendMsgParams?.agentId);

    if (agent || certainAgent) {
      updateCurrentAgent(agent || msgAgent);
    }
    const msgs = [
      ...(list || messageList),
      {
        id: uuid(),
        msg: currentMsg,
        msgValue: certainAgent
          ? currentMsg.replace(`/${certainAgent.name}`, '').trim()
          : currentMsg,
        modelId: modelId === -1 ? undefined : modelId,
        agentId: agent?.id || agentIdValue || currentAgent?.id,
        type: MessageTypeEnum.QUESTION,
        filters: sendMsgParams?.filters,
      },
    ];
    setMessageList(msgs);
    updateMessageContainerScroll();
    setInputMsg('');
  };

  const onInputMsgChange = (value: string) => {
    const inputMsgValue = value || '';
    setInputMsg(inputMsgValue);
  };

  const saveConversationToLocal = (conversation: ConversationDetailType) => {
    if (conversation) {
      if (conversation.chatId !== -1) {
        localStorage.setItem('CONVERSATION_ID', `${conversation.chatId}`);
      }
    } else {
      localStorage.removeItem('CONVERSATION_ID');
    }
  };

  const onSelectConversation = (
    conversation: ConversationDetailType,
    sendMsgParams?: SendMsgParamsType,
    isAdd?: boolean
  ) => {
    setCurrentConversation({
      ...conversation,
      initialMsgParams: sendMsgParams,
      isAdd,
    });
    saveConversationToLocal(conversation);
  };

  const onMsgDataLoaded = (
    data: MsgDataType,
    questionId: string | number,
    question: string,
    valid: boolean,
    isRefresh?: boolean
  ) => {
    onReportMsgEvent?.(question, valid);
    if (!isMobile) {
      conversationRef?.current?.updateData(currentAgent?.id);
    }
    if (!data) {
      return;
    }
    const msgs = cloneDeep(messageList);
    const msg = msgs.find(item => item.id === questionId);
    if (msg) {
      msg.msgData = data;
      setMessageList(msgs);
    }
    if (!isRefresh) {
      updateMessageContainerScroll(`${questionId}`);
    }
  };

  const onToggleHistoryVisible = () => {
    setHistoryVisible(!historyVisible);
  };

  const onAddConversation = () => {
    conversationRef.current?.onAddConversation();
    inputFocus();
  };

  const onSelectAgent = (agent: AgentType) => {
    if (agent.id === currentAgent?.id) {
      return;
    }
    if (messageList.length === 1 && messageList[0].type === MessageTypeEnum.AGENT_LIST) {
      setMessageList([]);
    }
    updateCurrentAgent(agent);
    updateMessageContainerScroll();
  };

  const sendMsg = (msg: string, modelId?: number) => {
    onSendMsg(msg, messageList, modelId);
    if (isMobile) {
      inputBlur();
    }
  };

  const onCloseConversation = () => {
    setHistoryVisible(false);
  };

  const chatClass = classNames(styles.chat, {
    [styles.mobile]: isMobile,
    [styles.historyVisible]: historyVisible,
  });

  return (
    <div className={chatClass}>
      <div className={styles.chatSection}>
        {!isMobile && agentList.length > 1 && agentListVisible && (
          <AgentList
            agentList={agentList}
            currentAgent={currentAgent}
            onSelectAgent={onSelectAgent}
          />
        )}
        <div className={styles.chatApp}>
          {currentConversation && (
            <div className={styles.chatBody}>
              <div className={styles.chatContent}>
                {currentAgent && !isMobile && !noInput && (
                  <div className={styles.chatHeader}>
                    <div className={styles.chatHeaderTitle}>{currentAgent.name}</div>
                    <div className={styles.chatHeaderTip}>{currentAgent.description}</div>
                  </div>
                )}
                <MessageContainer
                  id="messageContainer"
                  messageList={messageList}
                  chatId={currentConversation?.chatId}
                  historyVisible={historyVisible}
                  currentAgent={currentAgent}
                  chatVisible={chatVisible}
                  isDeveloper={isDeveloper}
                  integrateSystem={integrateSystem}
                  onMsgDataLoaded={onMsgDataLoaded}
                  onSendMsg={onSendMsg}
                />
                {!noInput && (
                  <ChatFooter
                    inputMsg={inputMsg}
                    chatId={currentConversation?.chatId}
                    agentList={agentList}
                    currentAgent={currentAgent}
                    onToggleHistoryVisible={onToggleHistoryVisible}
                    onInputMsgChange={onInputMsgChange}
                    onSendMsg={sendMsg}
                    onAddConversation={onAddConversation}
                    onSelectAgent={onSelectAgent}
                    onOpenAgents={() => {
                      if (isMobile) {
                        setMobileAgentsVisible(true);
                      } else {
                        setAgentListVisible(!agentListVisible);
                      }
                    }}
                    onOpenShowcase={() => {
                      setShowCaseVisible(!showCaseVisible);
                    }}
                    ref={chatFooterRef}
                  />
                )}
              </div>
            </div>
          )}
        </div>
        <Conversation
          currentAgent={currentAgent}
          currentConversation={currentConversation}
          historyVisible={historyVisible}
          onSelectConversation={onSelectConversation}
          onCloseConversation={onCloseConversation}
          ref={conversationRef}
        />
        {currentAgent && (
          <Modal
            title="showcase"
            width="98%"
            open={showCaseVisible}
            centered
            footer={null}
            wrapClassName={styles.showCaseModal}
            onCancel={() => {
              setShowCaseVisible(false);
            }}
          >
            <div className={styles.showCase}>
              <ShowCase agentId={currentAgent.id} onSendMsg={onSendMsg} />
            </div>
          </Modal>
        )}
      </div>
      <MobileAgents
        open={mobileAgentsVisible}
        agentList={agentList}
        currentAgent={currentAgent}
        onSelectAgent={onSelectAgent}
        onClose={() => {
          setMobileAgentsVisible(false);
        }}
      />
    </div>
  );
};

export default forwardRef(Chat);
