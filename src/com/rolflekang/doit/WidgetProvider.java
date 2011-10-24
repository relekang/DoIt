package com.rolflekang.doit;


import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class WidgetProvider extends AppWidgetProvider {

	public static String ACTION_WIDGET_RECEIVER = "com.rolflekang.doit.action.WIDGET_UPDATE";
	private Settings settings;
	private DataHelper dHelper;
	private String todosString;

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		Log.i("DoIt Widget", "Update Time");
		settings = new Settings(context);
		dHelper = new DataHelper(context);
		todosString = "DoIt:";
		for(Todo t : dHelper.selectAll(DataHelper.KEY_DONE+"=0")){
			todosString += "\n "+t.getTitle();
		}

		// Perform this loop procedure for each App Widget that belongs to this provider
		for (int appWidgetId : appWidgetIds) {
			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
			Intent updateIntent = new Intent(context, WidgetProvider.class);
			updateIntent.setAction(ACTION_WIDGET_RECEIVER);			
			Intent launchIntent = new Intent(context, StartActivity.class);
            PendingIntent pendingLaunchIntent = PendingIntent.getActivity(context, 0, launchIntent, 0);
            views.setOnClickPendingIntent(R.id.w_todos, pendingLaunchIntent);
            
			views.setTextViewText(R.id.w_todos, todosString);
			
			views.setTextColor(R.id.w_todos, settings.getWidgetTextColor());
			if(settings.isWidgetStyle(Settings.BG_TRANSPARENT)) views.setImageViewResource(R.id.w_background, R.drawable.empty);
			else if(settings.isWidgetStyle(Settings.BG_LIGHT)) views.setImageViewResource(R.id.w_background, R.drawable.w_light);
			else views.setImageViewResource(R.id.w_background, R.drawable.w_dark);
			appWidgetManager.updateAppWidget(appWidgetId, views);
		}
	}


	@Override
	public void onReceive(Context context, Intent intent) {    	
		if(intent.getAction().equals(ACTION_WIDGET_RECEIVER)) {
			AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
			int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, WidgetProvider.class));
			if (appWidgetIds.length > 0) {
				dHelper = new DataHelper(context);
				onUpdate(context, appWidgetManager, appWidgetIds);
			}    	    
		}

		super.onReceive(context, intent);
	}
}
