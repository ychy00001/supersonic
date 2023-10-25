import {useCallback, useEffect, useState} from 'react';
import {Image} from 'antd';
import {MsgDataType} from '../../../common/type';
import {getToken, isProd} from '../../../utils/utils';
import {webPageHost} from '../../../common/env';

type Props = {
    id: string | number;
    data: MsgDataType;
};

const DEFAULT_HEIGHT = 500;

const ImagePage: React.FC<Props> = ({id, data}) => {
    const [imgList, setImgList] = useState<string[]>([]);
    const [height, setHeight] = useState(DEFAULT_HEIGHT);

    const {
        result,
        webBase: {url, paramOptions},
    } = data.response || {};

    const handleMessage = useCallback((event: MessageEvent) => {
        const messageData = event.data;
        const {type, payload} = messageData;
        if (type === 'changeMiniProgramContainerSize') {
            const {msgId, height} = payload;
            if (`${msgId}` === `${id}`) {
                setHeight(height);
                // updateMessageContainerScroll();
            }
            return;
        }
        if (messageData === 'storyResize') {
            const ifr: any = document.getElementById(`reportIframe_${id}`);
            const iDoc = ifr.contentDocument || ifr.document || ifr.contentWindow;
            setTimeout(() => {
                setHeight(isProd() ? calcPageHeight(iDoc) : DEFAULT_HEIGHT);
            }, 200);
            return;
        }
    }, []);

    useEffect(() => {
        window.addEventListener('message', handleMessage);
        return () => {
            window.removeEventListener('message', handleMessage);
        };
    }, [handleMessage]);

    function calcPageHeight(doc: any) {
        const titleAreaEl = doc.getElementById('titleArea');
        const titleAreaHeight = Math.max(
            titleAreaEl?.clientHeight || 0,
            titleAreaEl?.scrollHeight || 0
        );
        const dashboardGridEl = doc.getElementsByClassName('dashboardGrid')?.[0];
        const dashboardGridHeight = Math.max(
            dashboardGridEl?.clientHeight || 0,
            dashboardGridEl?.scrollHeight || 0
        );
        return Math.max(titleAreaHeight + dashboardGridHeight + 10, DEFAULT_HEIGHT);
    }

    const initData = () => {
        const heightValue =
            paramOptions?.find((option: any) => option.paramType === 'FORWARD' && option.key === 'height')
                ?.value || DEFAULT_HEIGHT;
        setHeight(heightValue);

        let urlValue = url;
        const valueParams = (paramOptions || [])
            .filter((option: any) => option.paramType !== 'FORWARD')
            .reduce((result: any, item: any) => {
                result[item.key] = item.value;
                return result;
            }, {});

        setImgList(result);
    };

    useEffect(() => {
        initData();
    }, []);

    return (
        <div style={{width: '500px', height, border: 'none'}}>
            <Image.PreviewGroup>
                {imgList.map((item, index) =>
                    <Image width={200} src={item}/>
                )}
            </Image.PreviewGroup>
        </div>
    );
};

export default ImagePage;
