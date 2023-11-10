export const ROUTE_AUTH_CODES = {};

const ENV_KEY = {
  CHAT: 'chat',
  SEMANTIC: 'semantic',
  HOME: 'home'
};

const { APP_TARGET } = process.env;

const ROUTES = [
  {
    path: '/home',
    name: 'home',
    component: './Home',
    icon: 'dashboard',
    // hideInMenu: true,
    // layout: false,
    envEnableList: [ENV_KEY.HOME],
  },
  {
    path: '/chat/mobile',
    name: 'chat',
    component: './ChatPage',
    hideInMenu: true,
    layout: false,
    envEnableList: [ENV_KEY.CHAT],
  },
  {
    path: '/chat',
    name: 'chat',
    icon: 'comment',
    component: './ChatPage',
    envEnableList: [ENV_KEY.CHAT],
  },
  {
    path: '/chatSetting/model/:domainId?/:modelId?/:menuKey?',
    component: './SemanticModel/ChatSetting/ChatSetting',
    name: 'chatSetting',
    icon: 'profile',
    envEnableList: [ENV_KEY.CHAT],
    hideInMenu: true,
  },
  {
    path: '/agent',
    name: 'agent',
    component: './Agent',
    icon: 'partition',
    envEnableList: [ENV_KEY.CHAT],
  },
  {
    path: '/model',
    name: 'semanticModel',
    hideInMenu: true,
    envEnableList: [ENV_KEY.SEMANTIC],
    routes: [
      {
        path: '/model/:domainId?/:modelId?/:menuKey?',
        component: './SemanticModel/DomainManager',
        name: 'model',
        envEnableList: [ENV_KEY.SEMANTIC],
      },
      {
        path: '/database',
        name: 'database',
        component: './SemanticModel/components/Database/DatabaseTable',
        envEnableList: [ENV_KEY.SEMANTIC],
      },
    ],
  },

  {
    path: '/database',
    name: 'database',
    hideInMenu: true,
    component: './SemanticModel/components/Database/DatabaseTable',
    envEnableList: [ENV_KEY.SEMANTIC],
  },
  {
    path: '/metric',
    name: 'metric',
    hideInMenu: true,
    component: './SemanticModel/Metric',
    envEnableList: [ENV_KEY.SEMANTIC],
  },
  {
    path: '/plugin',
    name: 'plugin',
    icon: 'appstore',
    component: './ChatPlugin',
    envEnableList: [ENV_KEY.CHAT],
  },
  {
    path: '/login',
    name: 'login',
    layout: false,
    hideInMenu: true,
    component: './Login',
  },
  {
    path: '/',
    redirect: APP_TARGET === 'inner' ? '/model' : '/home',
    envRedirect: {
      [ENV_KEY.HOME]: '/home',
      [ENV_KEY.SEMANTIC]: '/model',
    },
  },
  {
    path: '/401',
    component: './401',
  },
];

export default ROUTES;
