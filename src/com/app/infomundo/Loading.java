package com.app.infomundo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Loading extends Activity {
	ProgressBar prgs;
	TextView cargandoText;
	ImageView boogapp;
	RelativeLayout layout;
	LoadingClass task;
	int alto = 0, largo = 0;
	int classC = 0;
	String user = "";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getIntent().getExtras() != null) {
			Bundle extras = getIntent().getExtras();
			if (extras.containsKey("user"))
				this.user = extras.getString("user");
			if (extras.containsKey("classC"))
				this.classC = extras.getInt("classC");
		} else
			this.classC = 0;
		layout();
	}

	public void layout() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		this.alto = metrics.heightPixels;
		this.largo = metrics.widthPixels;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		RelativeLayout layout = new RelativeLayout(this);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		params.setMargins(0, 0, 0, 0);
		layout.setLayoutParams(params);
		cargandoText = new TextView(this);
		cargandoText.setText("Loading");
		cargandoText.setTextColor(Color.parseColor("#ffffff"));
		cargandoText.setTextSize(30);
		cargandoText.setPadding(0, this.alto / 4, 0, 0);
		cargandoText.setGravity(Gravity.CENTER_HORIZONTAL);
		layout.addView(cargandoText, params);
		prgs = new ProgressBar(this, null,
				android.R.attr.progressBarStyleHorizontal);
		prgs.setMinimumWidth(largo);
		prgs.setMinimumHeight(50);
		prgs.setPadding(0, this.alto / 2, 0, this.alto / 2 - 50);
		prgs.setMax(100);
		layout.addView(prgs, params);
		boogapp = new ImageView(this);
		boogapp.setBackgroundResource(R.drawable.footer);
		RelativeLayout.LayoutParams boogappParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		boogappParams.setMargins(0, this.alto / 2 + this.alto / 4, 0, 0);
		boogappParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		boogapp.setLayoutParams(boogappParams);
		boogapp.setClickable(true);
		boogapp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				visitarURL("http://boogapp.com");
			}
		});
		layout.addView(boogapp, boogappParams);
		task = new LoadingClass();
		setContentView(layout);
		task.execute(0);
	}

	private class LoadingClass extends AsyncTask<Integer, Integer, Void> {
		Intent intent = null;
		Bundle extras = null;

		@Override
		protected void onPreExecute() {
			// Acepta UI
			super.onPreExecute();
			prgs.setMax(100);
		}

		@Override
		protected Void doInBackground(Integer... params) {
			// no acepta UI
			int start = params[0];
			for (int i = start; i <= 100; i = i + 5) {
				try {
					if (i == 15 && classC == 0)
						intent = new Intent(Loading.this, MainActivity.class);
					if (i == 15 && classC == 1)
						intent = new Intent(Loading.this, Login.class);
					if (i == 30)
						intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
					if (i == 45)
						extras = new Bundle();
					if (i == 65)
						extras.putString("user", user);
					if (i == 80) {
						intent.putExtras(extras);
					}
					if (i == 100) {
						startActivity(intent);
						// android.os.Process.killProcess(android.os.Process
						// .myPid());
					}
					boolean cancelled = isCancelled();
					if (!cancelled) {
						// /////////////////////////publishProgress(i); ///
						prgs.setProgress(i);
						// /////////////////////////onProgressUpdate(i); ///
						SystemClock.sleep(200);
					}
				} catch (Exception e) {
					Log.e("Error", e.toString());
				}

			}
			return null; // onPostExecute(Void result)
		}

		protected void onPostExecute(Void result) {
			// acepta UI y meter UI de background
			// async task finished
			super.onPostExecute(result);
			task.cancel(true);
			// android.os.Process.killProcess(android.os.Process.myPid());
		}

		protected void onCancelled() {
			prgs.setMax(0); // stop the progress
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// increment progress bar by progress value
			// ////////////////////setProgress(10);
			// ////////////////////prgs.setProgress(prgs.getProgress() + 5); //
			prgs.setProgress(5);
		}
	}

	public void visitarURL(String url) {
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(browserIntent);
	}
}
