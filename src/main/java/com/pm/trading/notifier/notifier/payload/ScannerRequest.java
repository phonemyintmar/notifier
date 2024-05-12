package com.pm.trading.notifier.notifier.payload;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;


@Getter
@Setter
public class ScannerRequest {

    public ScannerRequest(boolean isPremarket) {
        if (isPremarket) {
            this.columns = new ArrayList<>(Arrays.asList("name",
                    "premarket_close",
                    "premarket_change",
                    "premarket_volume",
                    "volume"));
            this.ignore_unknown_fields = false;
            this.options = new Options();
            this.range = new ArrayList<>(Arrays.asList(0, 20));
            this.sort = new Sort(true);
            this.preset = "pre-market-gainers";
        } else {
            this.columns = new ArrayList<>(Arrays.asList("name",
                    "close",
                    "change",
                    "premarket_volume",
                    "volume"));
            this.ignore_unknown_fields = false;
            this.options = new Options();
            this.range = new ArrayList<>(Arrays.asList(0, 20));
            this.sort = new Sort();
            this.preset = "gainers";
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
        public Sort() {
            this.sortBy = "change";
            this.sortOrder = "desc";
            this.nullsFirst = false;
        }

        public Sort(boolean isPremarket) {
            this.sortBy = "premarket_change";
            this.sortOrder = "desc";
            this.nullsFirst = false;
        }

        public String sortBy;
        public String sortOrder;
        public boolean nullsFirst;
    }


}


