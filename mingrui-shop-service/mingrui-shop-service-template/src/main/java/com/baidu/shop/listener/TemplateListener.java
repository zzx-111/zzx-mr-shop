package com.baidu.shop.listener;

import com.baidu.shop.constant.BaiduMessageConstant;
import com.baidu.shop.service.TemplateService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 2 * @ClassName GoodsListener
 * 3 * @Description: TODO
 * 4 * @Author zzx
 * 5 * @Date 2021/3/10
 * 6 * @Version V1.0
 * 7
 **/
@Component
@Slf4j
public class TemplateListener {
    @Autowired
    private TemplateService templateService;

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(
                            value = BaiduMessageConstant.SPU_QUEUE_PAGE_SAVE,
                            durable = "true"
                    ),
                    exchange = @Exchange(
                            value =BaiduMessageConstant.EXCHANGE,
                            ignoreDeclarationExceptions = "true",
                            type = ExchangeTypes.TOPIC
                    ),
                    key = {BaiduMessageConstant.SPU_ROUT_KEY_SAVE,BaiduMessageConstant.SPU_ROUT_KEY_UPDATE}
            )
    )
    public void save(Message message, Channel channel)  {

        log.info("templ接受到需要创建的模板id: " + new String(message.getBody()));

        try {
            templateService.createStaticHTMLTemplate(Integer.parseInt(new String(message.getBody())));
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(channel!=null){
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(
                            value = BaiduMessageConstant.SPU_QUEUE_PAGE_DELETE,
                            durable = "true"
                    ),
                    exchange = @Exchange(
                            value =BaiduMessageConstant.EXCHANGE,
                            ignoreDeclarationExceptions = "true",
                            type = ExchangeTypes.TOPIC
                    ),
                    key = BaiduMessageConstant.SPU_ROUT_KEY_DELETE
            )
    )
    public  void delete(Message message,Channel channel){
        log.info("template 接受到需要删除的id: " + new String(message.getBody()));


        try {
            templateService.deleteStaticHTMLTemplate(Integer.parseInt(new String(message.getBody())));
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(channel!=null){
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
