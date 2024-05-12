package com.pm.trading.notifier.notifier.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TradingData {

    private String ticker;

    private Double currentPrice;

    private Double increasePercentage;

    private Integer preVolume;

    private Integer volume;
}
