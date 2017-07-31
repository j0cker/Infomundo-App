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
import com.app.imgloader.ImageLoader;
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
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

@SuppressLint("NewApi")
public class Profile extends Activity {
	final Handler handler = new Handler();
	String userProfile = "";
	String fotoProfile = "";
	String user = "";
	String error = "";
	String result = "";
	SubMenu subMenu;
	int alto = 0;
	int largo = 0;
	ProgressDialog dialog;
	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
			.permitAll().build();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.setThreadPolicy(policy);
		if (getIntent().getExtras() != null) {
			Bundle extras = getIntent().getExtras();
			if (extras.containsKey("user")) {
				this.user = extras.getString("user");
			}
			if (extras.containsKey("userProfile"))
				this.userProfile = extras.getString("userProfile");
		}
		subMenu = new SubMenu(this, user);
		head();
		setContentView(R.layout.profile);
		cargar(1);
	}

	public void fotoPerfil_connect() {
		result = "";

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		if (userProfile.equals(""))
			nameValuePairs.add(new BasicNameValuePair("usuario", this.user));
		else
			nameValuePairs.add(new BasicNameValuePair("usuario",
					this.userProfile));
		// the year data to send
		// ("year", "1980") $_POST["year"]

		// necesita el policy
		// Manifest <uses-permission android:name="android.permission.INTERNET"
		// />
		// localhost -> 10.0.2.2
		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://infomundo.org/r/android/infomundo/red-social/fotoPerfil.php");
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
			this.fotoProfile = result;
		}
	}

	public void layout() {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		alto = displaymetrics.heightPixels;
		largo = displaymetrics.widthPixels;
		TextView user = (TextView) findViewById(R.id.user);
		if (userProfile.equals(""))
			user.setText(this.user);
		else
			user.setText(this.userProfile);
		user.setSingleLine();
		user.setTextSize(25);
		user.setTextColor(Color.WHITE);
		ImageView imgProfile = (ImageView) findViewById(R.id.imageProfile);
		android.view.ViewGroup.LayoutParams layoutParamsImage = imgProfile
				.getLayoutParams();
		layoutParamsImage.width = this.largo / 4;
		layoutParamsImage.height = this.largo / 4;
		imgProfile.setLayoutParams(layoutParamsImage);
		ImageLoader imgLoader = new ImageLoader(this);
		imgLoader.DisplayImage(fotoProfile,
				"android.resource://com.app.infomundo/" + R.drawable.loader,
				imgProfile, 70);

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
		actionBar.setTitle("Profile");
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
					Intent intent = new Intent(Profile.this, Login.class);
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
		titulo.setText("Profile");
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
			empezarCargar("Cargando Perfil");
			new AsyncTask<Void, Void, Void>() {
				@Override
				protected Void doInBackground(Void... params) {
					// no acepta UI
					fotoPerfil_connect();
					return null;
				}

				@Override
				protected void onPostExecute(Void result) {
					// acepta UI y meter UI de background
					layout();
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
