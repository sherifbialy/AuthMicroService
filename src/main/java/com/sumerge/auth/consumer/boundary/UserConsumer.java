package com.sumerge.auth.consumer.boundary;



import com.sumerge.auth.common.Constants;
import com.sumerge.auth.common.Utilities;
import com.sumerge.auth.consumer.ctrl.ConsumerCtrl;
import com.sumerge.auth.producer.entity.UserEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserConsumer {

    private final ConsumerCtrl consumerCtrl;




    @KafkaListener(topics = Constants.USER_DATA_TOPIC)
    public void consume(ConsumerRecord<String, LinkedHashMap> record) {
        LinkedHashMap recordMap = record.value();
        UserEvent event = Utilities.getObjectMapper().convertValue(recordMap, UserEvent.class);

    }


}