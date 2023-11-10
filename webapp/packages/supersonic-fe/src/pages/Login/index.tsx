// import type { FC } from 'react';
import styles from './style.less';
import { Button, Form, Input, message, Space } from 'antd';
import { LockOutlined, UserOutlined } from '@ant-design/icons';
import RegisterForm from './components/RegisterForm';
// import ForgetPwdForm from './components/ForgetPwdForm';
import React, { useState } from 'react';
import { useForm } from 'antd/lib/form/Form';
import type { RegisterFormDetail } from './components/types';
import { postUserLogin, userRegister } from './services';
import { AUTH_TOKEN_KEY } from '@/common/constants';
import { queryCurrentUser } from '@/services/user';
import { history, useModel } from 'umi';
import logo from "@/assets/Logo.png";
import logo_text from "@/assets/logo_text.png";

const { Item } = Form;
const LoginPage: React.FC = () => {
  const [createModalVisible, setCreateModalVisible] = useState<boolean>(false);
  // const [forgetModalVisible, setForgetModalVisible] = useState<boolean>(false);
  const [form] = useForm();
  const { initialState = {}, setInitialState } = useModel('@@initialState');
  // 通过用户信息进行登录
  const loginDone = async (values: RegisterFormDetail) => {
    const { code, data, msg } = await postUserLogin(values);
    if (code === 200) {
      localStorage.setItem(AUTH_TOKEN_KEY, data);
      const { code: queryUserCode, data: queryUserData } = await queryCurrentUser();
      if (queryUserCode === 200) {
        const currentUser = {
          ...queryUserData,
          staffName: queryUserData.staffName || queryUserData.name,
        };
        setInitialState({ ...initialState, currentUser });
      }
      history.push('/');
      return;
    }
    message.success(msg);
  };

  // 处理登录按钮响应
  const handleLogin = async () => {
    const { validateFields } = form;
    const content = await validateFields();
    await loginDone(content);
  };

  // 处理注册弹窗确定按钮
  const handleRegister = async (values: RegisterFormDetail) => {
    const { code } = await userRegister({ ...values });
    if (code === 200) {
      message.success('注册成功');
      setCreateModalVisible(false);
      // 注册完自动帮用户登录
      await loginDone(values);
    }
  };

  // 相应注册按钮
  const handleRegisterBtn = () => {
    setCreateModalVisible(true);
  };

  // // 忘记密码弹窗确定响应
  // const handleForgetPwd = async (values: RegisterFormDetail) => {
  //   await getUserForgetPwd({ ...values });
  //   message.success('发送邮件成功，请在收到邮件后进入邮件链接进行密码重置');
  //   setForgetModalVisible(false);
  // };

  // // 响应忘记密码按钮
  // const handleForgetPwdBtn = () => {
  //   setForgetModalVisible(true);
  // };

  return (
    <div className={styles.loginWarp}>
      <div className={styles.content}>
        <div className={styles.formContent}>
          <div className={styles.formBox}>
            <Form form={form} labelCol={{ span: 6 }} colon={false}>
              <div className={styles.loginMain}>
                <img
                  src={logo}
                  style={{ display: 'inline-block' , width: 70, height: 70 }}
                />
                <h3 className={styles.title}>
                  <Space>
                    <div>欢迎来到</div>
                    <img
                      src={logo_text}
                      style={{ display: 'inline-block' , width: 140, height: 25 , marginLeft:10}}
                    />
                  </Space>
                </h3>
                <Item name="name" rules={[{ required: true }]} label="">
                  <Input size="large" placeholder="请输入账号（admin）" prefix={<UserOutlined />} />
                </Item>
                <Item name="password" rules={[{ required: true }]} label="">
                  <Input
                    size="large"
                    type="password"
                    placeholder="请输入密码（admin）"
                    onPressEnter={handleLogin}
                    prefix={<LockOutlined />}
                  />
                </Item>
                <div className={styles.tool}>
                  <div className={styles.buttonBg}>
                    <Button className={styles.button} onClick={handleRegisterBtn}>
                      账号注册
                    </Button>
                  </div>

                  {/* <Button className={styles.button} type="link" onClick={handleForgetPwdBtn}>
              忘记密码
            </Button> */}
                </div>
                <Item label="">
                  <Button className={styles.signInBtn} type="primary" onClick={handleLogin}>
                    登录
                  </Button>
                </Item>
              </div>
            </Form>
          </div>
        </div>
      </div>
      <RegisterForm
        onCancel={() => {
          setCreateModalVisible(false);
        }}
        onSubmit={handleRegister}
        createModalVisible={createModalVisible}
      />
    </div>
  );
};

export default LoginPage;
