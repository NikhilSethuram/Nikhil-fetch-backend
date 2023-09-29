package com.fetch.backendproject.model;

import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PayersModel {
    String payer;
    int points;
}
