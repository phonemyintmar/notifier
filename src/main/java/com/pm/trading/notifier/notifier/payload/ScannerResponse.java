package com.pm.trading.notifier.notifier.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class ScannerResponse {
    private ArrayList<Data> data; //dr ka a khu 20 shi tae array

    @Getter
    @Setter
    public static class Data {
        private ArrayList<Object> d; //dr ka value twy shi tae array
    }
}
