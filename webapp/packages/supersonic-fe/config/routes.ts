export const ROUTE_AUTH_CODES = { SYSTEM_ADMIN: 'SYSTEM_ADMIN' };

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
  // {
  //   path: '/chatSetting/model/:domainId?/:modelId?/:menuKey?',
  //   component: './SemanticModel/ChatSetting/ChatSetting',
  //   name: 'chatSetting',
  //   envEnableList: [ENV_KEY.CHAT],
  // },
  {
    path: '/agent',
    name: 'agent',
    component: './Agent',
    icon: 'partition',
    envEnableList: [ENV_KEY.CHAT],
  },
  {
    path: '/model/:domainId?/:modelId?/:menuKey?',
    component: './SemanticModel/DomainManager',
    name: 'semanticModel',
    hideInMenu: true,
    envEnableList: [ENV_KEY.SEMANTIC],
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
    routes: [
      {
        path: '/metric',
        redirect: '/metric/market',
      },
      {
        path: '/metric/market',
        component: './SemanticModel/Metric/Market',
        hideInMenu: true,
        envEnableList: [ENV_KEY.SEMANTIC],
      },
      {
        path: '/metric/detail/:metricId',
        name: 'metricDetail',
        hideInMenu: true,
        component: './SemanticModel/Metric/Detail',
        envEnableList: [ENV_KEY.SEMANTIC],
      },
    ],
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
    path: '/system',
    name: 'system',
    icon: 'setting',
    component: './System',
    access: ROUTE_AUTH_CODES.SYSTEM_ADMIN,
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
