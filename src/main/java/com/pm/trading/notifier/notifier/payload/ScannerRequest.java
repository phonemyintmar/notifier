package com.pm.trading.notifier.notifier.payload;


import com.pm.trading.notifier.notifier.model.MarketType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;


@Getter
@Setter
public class ScannerRequest {

//    public ScannerRequest(boolean isPremarket) {
//        if (isPremarket) {
//            this.columns = new ArrayList<>(Arrays.asList("name",
//                    "premarket_close",
//                    "premarket_change",
//                    "premarket_volume",
//                    "volume"));
//            this.ignore_unknown_fields = false;
//            this.options = new Options();
//            this.range = new ArrayList<>(Arrays.asList(0, 10));
//            this.sort = new Sort(true);
//            this.preset = "pre-market-gainers";
//        } else {
//            this.columns = new ArrayList<>(Arrays.asList("name",
//                    "close",
//                    "change",
//                    "premarket_volume",
//                    "volume"));
//            this.ignore_unknown_fields = false;
//            this.options = new Options();
//            this.range = new ArrayList<>(Arrays.asList(0, 10));
//            this.sort = new Sort();
//            this.preset = "gainers";
//        }
//    }

    public ScannerRequest(MarketType type) {
        if (type == MarketType.AFTER_MARKET) {
            this.columns = new ArrayList<>(Arrays.asList("name",
                    "postmarket_close",
                    "postmarket_change",
                    "postmarket_volume",
                    "volume"));
            this.ignore_unknown_fields = false;
            this.options = new Options();
            this.range = new ArrayList<>(Arrays.asList(0, 10));
            this.sort = new Sort(type);
            this.preset = "after_hours_gainers";
        } else if (type == MarketType.MARKET){
            this.columns = new ArrayList<>(Arrays.asList("name",
                    "close",
                    "change",
                    "premarket_volume",
                    "volume"));
            this.ignore_unknown_fields = false;
            this.options = new Options();
            this.range = new ArrayList<>(Arrays.asList(0, 10));
            this.sort = new Sort(type);
            this.preset = "gainers";
        } else {
            this.columns = new ArrayList<>(Arrays.asList("name",
                    "premarket_close",
                    "premarket_change",
                    "premarket_volume",
                    "volume"));
            this.ignore_unknown_fields = false;
            this.options = new Options();
            this.range = new ArrayList<>(Arrays.asList(0, 10));
            this.sort = new Sort(type);
            this.preset = "pre-market-gainers";
        }
    }

    private ArrayList<String> columns;
    private boolean ignore_unknown_fields;
    private Options options;
    private ArrayList<Integer> range;
    private Sort sort;
    private String preset;

    @Getter
    @Setter
    private static class Options {
        public Options() {
            this.lang = "en";
        }

        public String lang;

    }

    @Getter
    @Setter
    private static class Sort {
        public Sort(MarketType type) {
            // dr ka enhanced switch statement loh call tl tae, intellij recommend tr,
            // instead of case MARKET :
            // and then break; we can use like this tae case MARKET ->{ }
            switch (type) {
                case MARKET -> {
                    this.sortBy = "change";
                    this.sortOrder = "desc";
                    this.nullsFirst = false;
                }
                case PRE_MARKET -> {
                    this.sortBy = "premarket_change";
                    this.sortOrder = "desc";
                    this.nullsFirst = false;
                }
                case AFTER_MARKET -> {
                    this.sortBy = "postmarket_change";
                    this.sortOrder = "desc";
                    this.nullsFirst = false;
                }
            }
        }

        public String sortBy;
        public String sortOrder;
        public boolean nullsFirst;
    }


}


