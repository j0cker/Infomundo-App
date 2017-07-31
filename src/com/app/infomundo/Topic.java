package com.app.infomundo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import com.app.boogapp.URLImageParser;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.Html;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

@SuppressLint("NewApi")
public class Topic extends Activity {
	final Handler handler = new Handler();
	ProgressDialog dialog;
	TextView subTituloText;
	TextView bodyText;
	String user = "";
	String result = "";
	String error = "";
	String key = "";
	String subTitulo = "";
	String html;
	int alto = 0;
	int largo = 0;
	SubMenu subMenu;

	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
			.permitAll().build();

	@SuppressLint("InflateParams")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.setThreadPolicy(policy);
		if (getIntent().getExtras() != null) {
			Bundle extras = getIntent().getExtras();
			if (extras.containsKey("user"))
				this.user = extras.getString("user");
			if (extras.containsKey("key"))
				this.key = extras.getString("key");
		}
		subMenu = new SubMenu(this, user);
		head();
		setContentView(R.layout.topic);
		layout();
		cargar(1);
	}

	public void layout() {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		alto = displaymetrics.heightPixels;
		largo = displaymetrics.widthPixels;
		subTituloText = (TextView) findViewById(R.id.subTitulo);
		subTituloText.setTextSize(18);
		bodyText = (TextView) findViewById(R.id.body);
		bodyText.setMovementMethod(new ScrollingMovementMethod());
		bodyText.setVerticalScrollBarEnabled(true);
	}

	public void topic_connect() {
		String result = "";
		// the year data to send
		// ("year", "1980") $_POST["year"]

		// necesita el policy
		// Manifest <uses-permission android:name="android.permission.INTERNET"
		// />
		// localhost -> 10.0.2.2
		// http post
		try {
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("msg", key));
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://infomundo.org/r/android/infomundo/topic.php");
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			InputStream isy = entity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					isy, "utf-8"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			isy.close();

			result = sb.toString();
		} catch (Exception e) {
			error = e.toString();
		}

		// response
		if (result != "") {
			// split en java android usa \\ (expresiones regulares)
			String[] parte = result.split("\\|\\|\\|");
			subTitulo = parte[0];
			html = parte[1]
					.replace("[center][img]",
							"<center><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><img src=");
			html = html.replace("[/img][/center]", " /></center>");
			html = html.replace("[center]", "<center>");
			html = html.replace("[/center]", "</center>");
			html = html.replace("[i]", "");
			html = html.replace("[/i]", "");
			html = html.replace("[b]", "");
			html = html.replace("[/b]", "");
		}
	}

	public void topic_apply() {
		subTituloText.setText("\n" + subTitulo + "\n");
		URLImageParser urlParser = new URLImageParser(bodyText, this, largo);
		Spanned htmlSpan = Html
				.fromHtml("<br /><br />" + html, urlParser, null);
		bodyText.setText(htmlSpan);
	}

	@SuppressLint("InflateParams")
	public void head() {
		// get action bar
		ActionBar actionBar = getActionBar();
		// Enabling Up / Back navigation
		actionBar = getActionBar();
		LayoutInflater inflater = (LayoutInflater) getActionBar()
				.getThemedContext().getSystemService(LAYOUT_INFLATER_SERVICE);
		View customActionBarView = inflater.inflate(R.layout.actionbar_custom2,
				null);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
				| ActionBar.DISPLAY_SHOW_HOME);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setTitle("Topic");
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#003166")));
		getActionBar().setDisplayUseLogoEnabled(false);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setCustomView(customActionBarView,
				new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
						ViewGroup.LayoutParams.MATCH_PARENT));
		final ImageButton ayuda = (ImageButton) customActionBarView
				.findViewById(R.id.back);
		ayuda.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					ayuda.setBackgroundColor(Color.argb(150, 255, 255, 255));
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					ayuda.setBackgroundColor(Color.parseColor("#003166"));
					Intent intent = new Intent(Topic.this, Login.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
					Bundle extras = new Bundle();
					extras.putString("user", user);
					intent.putExtras(extras);
					startActivity(intent);
					android.os.Process.killProcess(android.os.Process.myPid());
				}
				return false;
			}

		});
		TextView titulo = (TextView) customActionBarView
				.findViewById(R.id.titulo);
		titulo.setText("Topic");
		titulo.setSingleLine();
		titulo.setTextSize(25);
		actionBar.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);

		// Widget Buscar ActionBar
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
				.getActionView();
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return subMenu.handleOnItemSelected(item.getItemId());

	}

	public void cargar(int opcion) {
		if (opcion == 1) {
			empezarCargar("Cargando Topic");
			new AsyncTask<Void, Void, Void>() {
				@Override
				protected Void doInBackground(Void... params) {
					// no acepta UI
					topic_connect();
					return null;
				}

				@Override
				protected void onPostExecute(Void result) {
					// acepta UI y meter UI de background
					topic_apply();
					terminarCargar();
				}
			}.execute();
		}
	}

	public void empezarCargar(String cadena) {
		dialog = new ProgressDialog(this);
		dialog.setMessage(cadena);
		dialog.setCancelable(false);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.show();
	}

	public void terminarCargar() {
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
	}

}
