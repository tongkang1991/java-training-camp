package com;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author 余胜军
 */
@RestController
@SpringBootApplication
public class KafkaController {

    /**
     * 注入kafkaTemplate
     */
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * 发送消息的方法
     *
     * @param key
     *            推送数据的key
     * @param data
     *            推送数据的data
     */
    private void send(String key, String data) {
        // topic 名称 key data 消息数据
        kafkaTemplate.send("test", key, data);

    }
    // test 主题 1 my_test 3

    @RequestMapping("/kafka")
    public String testKafka() {
        int iMax = 6;
        for (int i = 1; i < iMax; i++) {
            send("key" + i, "data" + i);
        }
        return "success";
    }

    public static void main(String[] args) {
        SpringApplication.run(KafkaController.class, args);
    }

    /**
     * 消费者使用日志打印消息
     */

    @KafkaListener(topics = "test")
    public void receive(ConsumerRecord<?, ?> consumer) {
        System.out.println("topic名称:" + consumer.topic() + ",key:" +
                consumer.key() + "," +
                "分区位置:" + consumer.partition()
                + ", 下标" + consumer.offset());
    }

}