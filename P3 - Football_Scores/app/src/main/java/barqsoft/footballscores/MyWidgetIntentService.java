package barqsoft.footballscores;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>

 * helper methods.
 */
public class MyWidgetIntentService extends IntentService {
    private static final String[] MATCH_COLUMNS = {
            DatabaseContract.scores_table.LEAGUE_COL,
            DatabaseContract.scores_table.HOME_COL,
            DatabaseContract.scores_table.AWAY_COL,
            DatabaseContract.scores_table.HOME_GOALS_COL,
            DatabaseContract.scores_table.AWAY_GOALS_COL,
            DatabaseContract.scores_table.DATE_COL,
            DatabaseContract.scores_table.TIME_COL
    };

    private static final int INDEX_LEAGUE_ID = 0;
    private static final int INDEX_HOME_COL = 1;
    private static final int INDEX_AWAY_COL = 2;
    private static final int INDEX_HOME_GOALS = 3;
    private static final int INDEX_AWAY_GOALS = 4;
    private static final int INDEX_MATCHDATE = 5;
    private static final int INDEX_TIME = 6;

    public MyWidgetIntentService() {
        super("TodayWidgetIntentService");
    }



    @Override
    protected void onHandleIntent(Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                FootballWidget.class));
        Cursor data = getContentResolver().query(DatabaseContract.BASE_CONTENT_URI, MATCH_COLUMNS, null, null, DatabaseContract.scores_table.DATE_COL + " ASC");
        if (data == null) {
            return;
        }
        if (!data.moveToFirst()) {
            data.close();
            return;
        }

        String League = data.getString(INDEX_LEAGUE_ID);
        String Home = data.getString(INDEX_HOME_COL);
        String Away = data.getString(INDEX_AWAY_COL);
        String Scores = Utilies.getScores(data.getInt(INDEX_HOME_GOALS), data.getInt(INDEX_AWAY_GOALS));
        String MatchDate = data.getString(INDEX_MATCHDATE);
        String Time = data.getString(INDEX_TIME);

        data.close();

        for (int appWidgetId : appWidgetIds) {
            int layoutId = R.layout.scores_list_item;
            RemoteViews views = new RemoteViews(getPackageName(), layoutId);
            views.setTextViewText(R.id.score_textview, Scores);
            views.setTextViewText(R.id.home_name, Home);
            views.setTextViewText(R.id.away_name, Away);
            views.setTextViewText(R.id.date_textview, MatchDate);
            views.setTextViewText(R.id.time_textview, Time);
            views.setImageViewResource(R.id.home_crest, Utilies.getTeamCrestByTeamName(Home));
            views.setImageViewResource(R.id.away_crest, Utilies.getTeamCrestByTeamName(Away));


            Intent launchIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, launchIntent, 0);
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, views);

        }



    }
}
