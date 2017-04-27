package com.cooldevs.erasmuskit;

/**
 * Created by maite on 25/04/17
 */

public class Tip extends Post {

    private String tipCategory;

    public Tip() {

    }

    public Tip(String title, String content, String city, long timestamp, String tipCategory) {
        super(title, content, city, timestamp);
        this.tipCategory = tipCategory;
    }


    public enum TipCategory {
        ACCOMODATION("Accomodation"),
        TRANSPORT("Transport"),
        SHOPPING("Shopping"),
        OTHERS("Others");

        private String tipCategory;

        TipCategory(String tipCategory) {
            this.tipCategory = tipCategory;
        }

        public String getTipCategory() {
            return tipCategory;
        }

        public static CharSequence[] getAllValues() {
            CharSequence[] values = new String[TipCategory.values().length];
            int i = 0;

            for (TipCategory tipCategory : TipCategory.values())
                values[i++] = tipCategory.getTipCategory();

            return values;
        }
    }

    public String getTipCategory() {
        return tipCategory;
    }

    public void setTipCategory(String tipCategory) {
        this.tipCategory = tipCategory;
    }


}
