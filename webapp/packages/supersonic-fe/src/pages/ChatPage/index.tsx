import { useLocation } from 'umi';
import { getToken } from '@/utils/utils';
import { Chat } from 'superdo-chat-pkg';

const ChatPage = () => {
  const location = useLocation();
  const { agentId } = (location as any).query;

  return (
    <Chat initialAgentId={agentId ? +agentId : undefined} token={getToken() || ''} isDeveloper />
  );
};

export default ChatPage;
