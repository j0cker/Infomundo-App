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
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity extends Activity {
	final Handler handler = new Handler();
	private TextView txt;
	private Button btn;
	private EditText edit;
	private EditText edit2;
	private ImageView image;
	private Button btn2;
	private Button btn3;
	ProgressDialog dialog;
	int alto, largo, activityBack = 0;
	String text = "";
	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
			.permitAll().build();

	@SuppressLint("InflateParams")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.setThreadPolicy(policy);
		ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#003166")));
		LayoutInflater inflater = (LayoutInflater) getActionBar()
				.getThemedContext().getSystemService(LAYOUT_INFLATER_SERVICE);
		View customActionBarView = inflater.inflate(R.layout.actionbar_custom,
				null);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
				| ActionBar.DISPLAY_SHOW_HOME);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		getActionBar().setDisplayUseLogoEnabled(true);
		actionBar.setDisplayUseLogoEnabled(true);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setCustomView(customActionBarView,
				new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
						ViewGroup.LayoutParams.MATCH_PARENT));
		final ImageButton ayuda = (ImageButton) customActionBarView
				.findViewById(R.id.ayuda);
		ayuda.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					ayuda.setBackgroundColor(Color.argb(150, 255, 255, 255));
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					ayuda.setBackgroundColor(Color.parseColor("#003166"));
				}
				return false;
			}

		});
		actionBar.show();
		layout();
	}

	public void layout() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		this.alto = metrics.heightPixels;
		this.largo = metrics.widthPixels;
		RelativeLayout layout = new RelativeLayout(this);
		layout.setBackgroundColor(Color.argb(255, 224, 224, 224));
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		layout.setLayoutParams(params);
		setContentView(layout);
		txt = new TextView(this);
		txt.setText("Ingresar");
		txt.setTextColor(Color.parseColor("#000000"));
		txt.setTextAlignment(Gravity.CENTER);
		txt.setGravity(Gravity.CENTER);
		txt.setPadding(0, 10, 0, 0);
		params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		layout.addView(txt, params);
		btn2 = new Button(this);
		btn2.setTextColor(Color.BLUE);
		btn2.setText("No puedo Acceder");
		btn2.setTextSize(12);
		btn2.setBackgroundColor(Color.argb(255, 224, 224, 224));
		btn2.setOnClickListener(new OnClickListener() {
			@Override
			// On click function
			public void onClick(View view) {
				visitarURL("http://infomundo.org/reminder");
			}
		});
		params = new RelativeLayout.LayoutParams(this.largo / 2,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(this.largo / 2 - this.largo / 4, this.alto / 2
				+ (this.alto / 8) - (this.alto / 16) - (this.alto / 32), 0, 0);
		layout.addView(btn2, params);
		btn3 = new Button(this);
		btn3.setClickable(true);
		btn3.setText("Registrarse");
		btn3.setTextColor(Color.RED);
		btn3.setTextSize(12);
		btn3.setBackgroundColor(Color.argb(255, 224, 224, 224));
		btn3.setOnClickListener(new OnClickListener() {
			@Override
			// On click function
			public void onClick(View view) {
				visitarURL("http://infomundo.org/register");
			}
		});
		params = new RelativeLayout.LayoutParams(this.largo / 2,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(this.largo / 2 - this.largo / 4, this.alto / 2
				+ (this.alto / 8), 0, 0);
		layout.addView(btn3, params);
		btn = new Button(this);
		btn.setText("Entrar");
		btn.setBackgroundColor(Color.argb(255, 0, 0, 0));
		btn.setBackgroundResource(R.drawable.board);
		btn.setTextSize(15);
		btn.setClickable(true);
		btn.setTextColor(Color.argb(255, 255, 255, 255));
		// btn.callOnClick(ball());
		btn.setOnClickListener(new OnClickListener() {
			@Override
			// On click function
			public void onClick(View view) {
				cargar();
			}
		});
		params = new RelativeLayout.LayoutParams(this.largo,
				(this.alto / 8) * 2);
		params.setMargins(0, this.alto - (this.alto / 8) - (this.alto / 8), 0,
				0);
		layout.addView(btn, params);
		edit = new EditText(this);
		edit.setText("Usuario");
		edit.setTextColor(Color.parseColor("#000000"));
		edit.setBackgroundColor(Color.parseColor("#ffffff"));
		// evita enter
		edit.setSingleLine();
		/*
		 * edit.setOnFocusChangeListener(new OnFocusChangeListener() { public
		 * void onFocusChange(View v, boolean hasFocus) { if (hasFocus)
		 * edit.setText(""); } });
		 */
		params = new RelativeLayout.LayoutParams(this.largo / 2,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(this.largo / 2 - this.largo / 4, this.alto / 3
				- (this.alto / 8) - (this.alto / 16), 0, 0);
		layout.addView(edit, params);
		edit2 = new EditText(this);
		edit2.setText("Password");
		// evita enter
		edit2.setSingleLine();
		edit2.setTextColor(Color.parseColor("#000000"));
		edit2.setBackgroundColor(Color.parseColor("#ffffff"));
		edit2.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);
		params = new RelativeLayout.LayoutParams(this.largo / 2,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(this.largo / 2 - this.largo / 4, this.alto / 3
				- (this.alto / 16), 0, 0);
		layout.addView(edit2, params);
		image = new ImageView(this);
		Bitmap bmp = BitmapFactory.decodeResource(getResources(),
				R.drawable.infomundo);
		bmp = Bitmap.createScaledBitmap(bmp, this.largo / 4, this.largo / 4
				- this.largo / 20, true);
		image.setImageBitmap(bmp);
		image.setPadding((this.largo / 2) - this.largo / 8, (this.alto / 2)
				- (this.alto / 32) - (this.alto / 16), 0, 0);
		layout.addView(image);
	}

	public void connect() {
		String result = "";
		// the year data to send
		// ("year", "1980") $_POST["year"]
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("user", edit.getText()
				.toString()));
		nameValuePairs.add(new BasicNameValuePair("password", edit2.getText()
				.toString()));
		// necesita el policy
		// Manifest <uses-permission android:name="android.permission.INTERNET"
		// />
		// localhost -> 10.0.2.2
		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://infomundo.org/r/android/infomundo/login.php");
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
			text = e.getMessage().toString();
		}

		// response
		if (result != "") {
			if (result.contains("true")) {
				text = "Accesing";
				// truena
				// txt.setTextColor(Color.WHITE);
				new AsyncTask<Void, Void, Void>() {
					@Override
					protected Void doInBackground(Void... params) {
						// no acepta UI
						Intent intent = new Intent(MainActivity.this,
								Loading.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
						Bundle extras = new Bundle();
						extras.putString("user", edit.getText().toString());
						extras.putInt("classC", 1);
						intent.putExtras(extras);
						startActivity(intent);
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						// acepta UI y meter UI de background
						android.os.Process.killProcess(android.os.Process
								.myPid());
					}
				}.execute();
			} else if (result.contains("user")) {
				text = "ERROR: no existe usuario.";
			} else {
				text = "ERROR: password incorrecto.";
			}
		} else {
			text = "Sin conexion a internet";
		}

	}

	public void visitarURL(String url) {
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(browserIntent);
	}

	public void cargar() {
		empezarCargar("Cargando");
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				// no acepta UI
				connect();
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// acepta UI y meter UI de background
				terminarCargar();
				txt.setTextColor(Color.RED);
				txt.setText(text);
				Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT)
						.show();
			}
		}.execute();
	}

	public void empezarCargar(String cadena) {
		dialog = new ProgressDialog(MainActivity.this);
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
