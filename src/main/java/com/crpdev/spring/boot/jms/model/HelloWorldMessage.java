package com.crpdev.spring.boot.jms.model;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HelloWorldMessage implements Serializable {

    private static final long serialVersionUID = -891738598565812279L;

    private UUID id;
    private String message;
}
