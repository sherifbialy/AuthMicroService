package com.sumerge.auth.producer;

import com.sumerge.auth.common.enums.OperationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AbstractEvent<T> implements Serializable {

    public String user;
    public OperationType operationType;
    public String id;

    public T entity;

}
