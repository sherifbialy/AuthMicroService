package com.sumerge.auth.producer.boundary;

import com.sumerge.auth.common.Constants;
import com.sumerge.auth.common.enums.OperationType;
import com.sumerge.auth.producer.AbstractProducer;
import com.sumerge.auth.producer.entity.UserEvent;
import com.sumerge.auth.repository.entity.UserDocument;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserEventProducer extends AbstractProducer<UserEvent> {

    @Autowired
    public UserEventProducer(KafkaTemplate<String, UserEvent> kafkaTemplate) {
        super(kafkaTemplate);
    }

    public void send(OperationType op, String user, String id, UserDocument vendorModel) {
        sendMessage(new UserEvent(user, op, id == null ? String.valueOf(vendorModel.getId()) : id, vendorModel), Constants.USER_DATA_TOPIC,
                vendorModel.getId() == null ? id : String.valueOf(vendorModel.getId()));
    }

}
