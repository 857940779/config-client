package com.configclient.mq;

import com.alibaba.fastjson.JSON;
import com.configclient.common.MyPlaceHolder;
import com.configclient.model.PropertyDTO;
import com.configclient.model.TopicPropertyDTO;


import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.List;

/**
 * @Author: luohanwen
 * @Date: 2019/9/12 15:36
 */
public class MessageListenerImpl implements MessageListener {

    //对于是topic类型的消息监听，需要先启动消费者，再启动生产者，才能拿到消息。

    //该方法再用单元测试时是需要测试方法中暂停一会或者运行时间长一点，不然还没来得及监听就结束测试了
    public void onMessage(Message message) {
        TextMessage textMsg = (TextMessage) message;

        System.out.println("--------消费的内容-----------------");

        try {
            System.out.println(textMsg.getText());

            String str=textMsg.getText();
            TopicPropertyDTO topicPropertyDTO=JSON.parseObject(str,TopicPropertyDTO.class);
            if(MyPlaceHolder.judgeServerProperty(
                    topicPropertyDTO.getEnvironmentName(),topicPropertyDTO.getServiceName())){

                System.out.println("------需要处理配置-------");
                //接收到信息后，更改property缓存中的数据，达到实时刷新效果，配合配置中心使用
                MyPlaceHolder.setProperty(topicPropertyDTO.getList());
            }else{
                System.out.println("-------不需要--------");
            }

        } catch (JMSException e) {
            e.printStackTrace();
        }


    }

}
