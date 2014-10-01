package me.creativei.gandhi;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import static android.provider.BaseColumns._ID;
import static me.creativei.gandhi.QuotesTable.FAVORITE_COL;
import static me.creativei.gandhi.QuotesTable.QUOTES_TABLE;
import static me.creativei.gandhi.QuotesTable.QUOTE_COL;
import static me.creativei.gandhi.Utils.LOG_TAG;

public class QuotesDbHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "creativei.quotes.db";

    private static final String CREATE_TABLE_QUOTES_STMT = "CREATE TABLE "
            + QUOTES_TABLE + " (" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + QUOTE_COL + " TEXT NOT NULL, "
            + FAVORITE_COL + " INTEGER DEFAULT 0);";
    private static final String CREATE_QUOTES_INDEX = "CREATE INDEX QUOTES_FAVORITE_INDEX ON "
            + QUOTES_TABLE + " (" + FAVORITE_COL + ");";

    private static final String STMT_DROP_TABLE_QUOTES = "DROP TABLE IF EXISTS "
            + QUOTES_TABLE + ";";
    private Context context;

    public QuotesDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_QUOTES_STMT);
        db.execSQL(CREATE_QUOTES_INDEX);
        try {
            long startTime = System.currentTimeMillis();
            String[] allFiles = context.getAssets().list("quotes");
            Log.d(LOG_TAG, "Files in assets " + Arrays.toString(allFiles));
            for (String allFile : allFiles) {
                if (allFile.endsWith(".csv")) {
                    importQuotes(db, allFile);
                }
            }
            Log.d(LOG_TAG,
                    "Total time for seeding database:"
                            + (System.currentTimeMillis() - startTime));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void importQuotes(SQLiteDatabase db, String filename)
            throws IOException {
        BufferedReader seedFile = new BufferedReader(new InputStreamReader(
                context.getAssets().open("quotes/" + filename)));
        int count = 0;
        String line = null;
        while ((line = seedFile.readLine()) != null) {
            if (line.trim().isEmpty())
                continue;
            ContentValues values = new ContentValues();
            values.put(QUOTE_COL, line.trim());
            long id = db.insert(QUOTES_TABLE, null, values);
            Log.d(LOG_TAG, "Inserted row with id " + id);
            count++;
        }
        Log.d(LOG_TAG, "Inserted " + count + " rows from file " + filename);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(STMT_DROP_TABLE_QUOTES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
