import IconFont from '../../../components/IconFont';
import styles from './style.module.less';
import {NumberOutlined} from "@ant-design/icons";

const CopilotAvatar = () => {
  return <NumberOutlined className={styles.leftAvatar}/>
  // return <IconFont type="icon-zhinengsuanfa" className={styles.leftAvatar} />;
};

export default CopilotAvatar;
