import { Settings as LayoutSettings } from '@ant-design/pro-layout';

const Settings: LayoutSettings & {
  pwa?: boolean;
  logo?: string;
} = {
  navTheme: 'light',
  // primaryColor: '#296DF3',
  primaryColor: '#bfcfff',
  layout: 'top',
  // layout: 'side',
  contentWidth: 'Fluid',
  fixedHeader: false,
  fixSiderbar: true,
  colorWeak: false,
  title: '',
  pwa: false,
  iconfontUrl: '//at.alicdn.com/t/c/font_4120566_x5c4www9bqm.js',
  splitMenus: true,
  menu: {
    autoClose: false,
    ignoreFlatMenu: true,
  },
};
export const publicPath = '/webapp/';

export default Settings;
