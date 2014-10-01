package me.creativei.gandhi;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import static android.provider.BaseColumns._ID;
import static me.creativei.gandhi.QuotesTable.ALL_QUOTES_COLS;
import static me.creativei.gandhi.QuotesTable.FAVORITE_COL;
import static me.creativei.gandhi.QuotesTable.QUOTES_TABLE;
import static me.creativei.gandhi.QuotesTable.QUOTE_COL;
import static me.creativei.gandhi.Utils.LOG_TAG;

public class QuoteDBDataSource implements QuoteDataSource {
    private QuotesDbHelper databaseHelper;
    private SQLiteDatabase database;

    public QuoteDBDataSource(QuotesDbHelper quotesDb) {
        this.databaseHelper = quotesDb;
    }

    public void open() {
        if (database == null || !database.isOpen())
            database = databaseHelper.getWritableDatabase();
    }

    public void close() {
        databaseHelper.close();
    }

    @Override
    public Quote getQuote(int offset, boolean favorite) {
        if (favorite)
            return getFavoriteQuote(offset);
        Log.d(LOG_TAG, "Fetching quote with offset :" + offset);
        Cursor cursor = database.query(QUOTES_TABLE, ALL_QUOTES_COLS, "1=1 LIMIT 1 OFFSET ?",
                new String[]{Integer.toString(offset)}, null, null,
                null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            Quote quote = populateFromCursor(cursor);
            cursor.close();
            return quote;
        } else {
            return null;
        }
    }

    @Override
    public void setFavorite(int id, boolean favorite) {
        ContentValues values = new ContentValues();
        values.put(FAVORITE_COL, favorite ? 1 : 0);
        int rowsUpdated = database.update(QUOTES_TABLE, values, _ID + " = ?",
                new String[]{Integer.toString(id)});
        Log.d(LOG_TAG, "Marked id#" + id + " as favorite=" + favorite
                + ", rows updated: " + rowsUpdated);

    }

    @Override
    public int count(boolean favorite) {
        if (favorite)
            return favoriteCount();
        return (int) DatabaseUtils.longForQuery(database,
                "SELECT COUNT(*) from " + QUOTES_TABLE, null);
    }

    private int favoriteCount() {
        return (int) DatabaseUtils.longForQuery(database,
                "SELECT COUNT(*) from " + QUOTES_TABLE + " where "
                        + FAVORITE_COL + " = ?",
                new String[]{Integer.toString(1)});
    }

    private Quote getFavoriteQuote(int offset) {
        Log.d(LOG_TAG, "Fetching favorite quote with offset " + offset);
        Cursor cursor = database.query(QUOTES_TABLE, ALL_QUOTES_COLS,
                FAVORITE_COL + " = ? " + " LIMIT 1 OFFSET ?", new String[]{
                        Integer.toString(1), Integer.toString(offset)}, null,
                null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            Quote quote = populateFromCursor(cursor);
            cursor.close();
            return quote;
        } else {
            return null;
        }
    }

    private Quote populateFromCursor(Cursor cursor) {
        return new Quote(cursor.getInt(cursor.getColumnIndex(_ID)),
                cursor.getString(cursor.getColumnIndex(QUOTE_COL)),
                (cursor.getInt(cursor.getColumnIndex(FAVORITE_COL)) == 1));
    }
}

