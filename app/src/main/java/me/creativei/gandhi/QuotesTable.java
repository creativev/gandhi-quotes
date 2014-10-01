package me.creativei.gandhi;

import android.provider.BaseColumns;

public class QuotesTable implements BaseColumns {
    public static final String QUOTES_TABLE = "quotes";
    public static final String FAVORITE_COL = "favorite";
    public static final String QUOTE_COL = "quote";
    public static final String[] ALL_QUOTES_COLS = new String[]{_ID, QUOTE_COL, FAVORITE_COL};
}
