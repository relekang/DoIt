package com.rolflekang.doit;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.preference.PreferenceManager;

public class Settings {
	private static final int CWHITE = 0;
	private static final int CBLACK = 1;
	
	public static final int BG_TRANSPARENT = 0;
	public static final int BG_LIGHT = 1;
	public static final int BG_DARK = 2;
	
	private Context context;
	private SharedPreferences pref;
	/**
	 * Creates a new settings instance
	 * @param applicationContext the application context normally use {@link getApplicationContext()}
	 */
	public Settings(Context applicationContext) {
		this.context = applicationContext;
		pref = PreferenceManager.getDefaultSharedPreferences(context);
	}
	/**
	 * @return int value according to {@link Color}
	 */
	public int getWidgetTextColor(){
		return pref.getInt("widgetcolor", -1);
	}
	/**
	 * @return int value according to settingsarray
	 */
	public int getWidgetTextColorIndex(){
		int c = getWidgetTextColor();
		if (c == Color.WHITE) return 0;
		else return 1;
	}
	public void setWidgetTextColor(int color){
		pref.edit().putInt("widgetcolor", color).commit();
	}
	public void setWidgetTextColorByIndex(int index) {
		if(index == CWHITE) setWidgetTextColor(Color.WHITE);
		else if(index == CBLACK) setWidgetTextColor(Color.BLACK);
	}
	public int getWidgetStyle() {
		return pref.getInt("widgetstyle", 0);
	}
	public boolean setWidgetStyle(int style){
		Editor e = pref.edit();
		e.putInt("widgetstyle", style);
		switch (style) {
		case BG_TRANSPARENT:
			e.commit();
			return true;
		case BG_LIGHT:
			e.putInt("widgetcolor", Color.BLACK).commit();
			return true;
		case BG_DARK:
			e.putInt("widgetcolor", Color.WHITE).commit();
			return true;
		default:
			return false;
		}
	}
	public boolean isWidgetStyle(int style) {
		return (getWidgetStyle() == style);
	}
	
	public int getWidgetFontSize(){
		return pref.getInt("fontsize", 11);
	}
	public boolean setWidgetFontSize(int fontSize){
		if(fontSize > 7) return false;
		fontSize += 11;
		Editor e = pref.edit();
		e.putInt("fontsize", fontSize);
		return e.commit();
	}
	
	
}
