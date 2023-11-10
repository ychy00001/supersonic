import { Space } from 'antd';
export interface IProps {
  title: string;
  subTitle?: string;
}

const FormItemTitle: React.FC<IProps> = ({ title, subTitle }) => {
  return (
    <Space direction="vertical" size={2}>
      <span>{title}</span>
      {subTitle && <span style={{ fontSize: '12px', color: '#a7a7a7' }}>{subTitle}</span>}
    </Space>
  );
};

export default FormItemTitle;
