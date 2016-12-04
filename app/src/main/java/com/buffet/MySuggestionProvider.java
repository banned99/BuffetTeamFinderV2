package com.buffet;

import android.content.SearchRecentSuggestionsProvider;
import android.database.Cursor;
import android.net.Uri;
import android.widget.Toast;

/**
 * Created by Banned on 11/16/2016.
 */

public class MySuggestionProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = MySuggestionProvider.class.getName();
    public final static int MODE = DATABASE_MODE_QUERIES | DATABASE_MODE_2LINES;

    public MySuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if(selectionArgs != null && selectionArgs.length > 0 && selectionArgs[0].length() > 0){

        }else{

        }
        return super.query(uri, projection, selection, selectionArgs, sortOrder);
    }
}