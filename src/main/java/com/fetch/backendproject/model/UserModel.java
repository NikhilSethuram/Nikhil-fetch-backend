package com.fetch.backendproject.model;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {
    private String payer;
    private Integer points;
    private LocalDateTime timestamp;
}
