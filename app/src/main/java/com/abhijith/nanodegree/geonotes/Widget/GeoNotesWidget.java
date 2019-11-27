package com.abhijith.nanodegree.geonotes.Widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.abhijith.nanodegree.geonotes.Model.Notes;
import com.abhijith.nanodegree.geonotes.R;
import com.abhijith.nanodegree.geonotes.Utils.Constants;
import com.google.gson.Gson;

/**
 * Implementation of App Widget functionality.
 */
public class GeoNotesWidget extends AppWidgetProvider {

    SharedPreferences sharedPreferences;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, String notesTitle, String notesDescription) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.layout_geonotes_widget);
        views.setTextViewText(R.id.appwidget_text, notesTitle);
        views.removeAllViews(R.id.container_notesList);

        RemoteViews notesView = new RemoteViews(context.getPackageName(),
                android.R.layout.activity_list_item);
        notesView.setTextViewText(android.R.id.text1, notesDescription);
        views.addView(R.id.container_notesList, notesView);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES,
                Context.MODE_PRIVATE);
        String result = sharedPreferences.getString(Constants.WIDGET_NOTES_SELECTED, null);
        Gson gson = new Gson();
        Notes notes = gson.fromJson(result, Notes.class);
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, notes.getTitle(), notes.getDescription());
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

