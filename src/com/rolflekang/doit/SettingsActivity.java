package com.rolflekang.doit;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends Activity implements OnClickListener {

	private TextView widgetColorLabel;
	private Settings settings;
	private RelativeLayout widgetColorHolder;
	private RelativeLayout widgetStyleHolder;
	private TextView widgetStyleLabel;
	private String[] colorItems;
	private String[] styleItems;
	private Dialog fontSizeDialog;
	private SeekBar fontSizeSeekBar;
	private RelativeLayout widgetFontSizeHolder;
	private TextView widgetFontSizeLabel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		colorItems = getResources().getStringArray(R.array.coloritems);
		styleItems = getResources().getStringArray(R.array.styleitems);
		settings = new Settings(getApplicationContext());
		setupFields();
		updateValues();updateWidget();
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.s_widgetcolor:
			changeWidgetColor();
			break;
		case R.id.s_widgetstyle:
			changeWidgetStyle();
			break;
		case R.id.s_widgetfontsize:
			changeWidgetFontSize();
			break;
		}
	}

	private void changeWidgetColor() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.settings_widgetcolor_dialogtitle);
		builder.setSingleChoiceItems(colorItems, settings.getWidgetTextColorIndex(), new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		    	dialog.dismiss();
		    	settings.setWidgetTextColorByIndex(item);
		    	updateValues();updateWidget();
		        Toast.makeText(getApplicationContext(), getResources().getString(R.string.settings_widgetcolor_set) + colorItems[item], Toast.LENGTH_SHORT).show(); 
		    }
		});
		AlertDialog alert = builder.create();
		alert.show();
	}
	private void changeWidgetStyle() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.settings_widgetstyle_dialogtitle);
		builder.setSingleChoiceItems(styleItems, settings.getWidgetStyle(), new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		    	dialog.dismiss();
		    	settings.setWidgetStyle(item);
		    	updateValues();updateWidget();
		        Toast.makeText(getApplicationContext(), getResources().getString(R.string.settings_widgetstyle_set) + styleItems[item], Toast.LENGTH_SHORT).show(); 
		    }
		});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	private void changeWidgetFontSize() {
		fontSizeDialog = new Dialog(this);
		fontSizeDialog.setTitle(R.string.settings_widgetfontsize_dialogtitle);
		fontSizeDialog.setContentView(R.layout.dialog_fontsize);
		fontSizeSeekBar = (SeekBar) fontSizeDialog.findViewById(R.id.dialog_fontsize_seekbar);
		Button saveBtn = (Button) fontSizeDialog.findViewById(R.id.dialog_fontsize_savebtn);
		saveBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(settings.setWidgetFontSize(fontSizeSeekBar.getProgress())) 
					Toast.makeText(getApplicationContext(), R.string.settings_widgetfontsize_set, Toast.LENGTH_SHORT);
				fontSizeDialog.dismiss();
				updateValues();
				updateWidget();
			}
		});

		fontSizeSeekBar.setProgress(2);
		fontSizeDialog.show();
	}
	
	private void setupFields() {
		widgetColorHolder = (RelativeLayout) findViewById(R.id.s_widgetcolor);
		widgetColorHolder.setOnClickListener(this);
		widgetColorLabel = (TextView) findViewById(R.id.s_widgetcolorvalue);
		widgetStyleHolder = (RelativeLayout) findViewById(R.id.s_widgetstyle);
		widgetStyleHolder.setOnClickListener(this);
		widgetStyleLabel = (TextView) findViewById(R.id.s_widgetstylevalue);
		widgetFontSizeHolder = (RelativeLayout) findViewById(R.id.s_widgetfontsize);
		widgetFontSizeHolder.setOnClickListener(this);
		widgetFontSizeLabel = (TextView) findViewById(R.id.s_widgetfontsizevalue);
	}
	private void updateValues() throws NullPointerException {
		widgetColorLabel.setText(colorItems[settings.getWidgetTextColorIndex()]);
		widgetStyleLabel.setText(styleItems[settings.getWidgetStyle()]);
		widgetFontSizeLabel.setText(Integer.toString(settings.getWidgetFontSize()));
	}

	protected void updateWidget() {
		Intent i = new Intent();
		i.setAction(WidgetProvider.ACTION_WIDGET_RECEIVER);
        getApplicationContext().sendBroadcast(i);
	}
	
}
