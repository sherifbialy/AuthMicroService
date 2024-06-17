package com.sumerge.auth.producer.entity;



import com.sumerge.auth.common.enums.OperationType;
import com.sumerge.auth.producer.AbstractEvent;
import com.sumerge.auth.repository.entity.UserDocument;
import lombok.Data;

@Data
public class UserEvent extends AbstractEvent<UserDocument> {

    public UserEvent() {
        super();
    }

    public UserEvent(String user, OperationType operationType, String id, UserDocument entity) {
        super(user, operationType, id, entity);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
