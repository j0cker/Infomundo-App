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
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.extras.SoundPullEventListener;
import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class Index extends Fragment {
	final Handler handler = new Handler();
	TextView txt;
	Button btn;
	View v;
	View layout;
	ProgressDialog dialog;
	ArrayList<Directivo> arraydir;
	String[] nombre_categorias;
	String[] url_categorias;
	String user = "";
	String text = "";
	AdapterDirectivos adapter;
	PullToRefreshListView list;
	int alto = 0, largo = 0, contNoticias = 20, actualizarBottom = 0;
	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
			.permitAll().build();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		StrictMode.setThreadPolicy(policy);
		this.user = getArguments().getString("user");
		v = new View(getActivity());
		v = inflater.inflate(R.layout.tabs_index_refresh, container, false);
		layout();
		cargar(1);
		return v;
	}

	public void layout() {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay()
				.getMetrics(displaymetrics);
		alto = displaymetrics.heightPixels;
		largo = displaymetrics.widthPixels;
		layout = v.findViewById(R.id.layout);
		// layout.setBackgroundColor(Color.argb(255, 224, 224, 224));
		txt = new TextView(layout.getContext());
		txt.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		txt.setText("");
		txt.setPaddingRelative(0, 0, 0, 0);
		((LinearLayout) layout).addView(txt);

		btn = (Button) v.findViewById(R.id.button);
		btn.setText("Selecciona Categoría");
		btn.setBackgroundColor(Color.argb(255, 0, 0, 0));
		btn.setBackgroundResource(R.drawable.board);
		btn.setTextSize(15);
		btn.setClickable(false);
		btn.setTextColor(Color.argb(255, 255, 255, 255));
		// postexecute
		btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				AlertDialog.Builder adb = new AlertDialog.Builder(layout
						.getContext());
				CharSequence items[] = nombre_categorias;
				adb.setSingleChoiceItems(items, 0,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface d, int n) {

							}

						});
				adb.setNegativeButton("Cancel", null);
				adb.setTitle("Selecciona Categoría");
				adb.show();
			}
		});
		list = (PullToRefreshListView) v.findViewById(R.id.pull_refresh_list);
		list.setDescendantFocusability(ListView.FOCUS_BLOCK_DESCENDANTS);
		list.setClickable(false);
		// Inicializar arraylist
		arraydir = new ArrayList<Directivo>();
		// Set a listener to be invoked when the list should be refreshed.
		list.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(v.getContext(),
						System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
								| DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);

				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				contNoticias = 20;
				// Do work to refresh the list here.
				new GetDataTaskNoticias().execute();
			}
		});
		// Add an end-of-list listener
		list.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				if (actualizarBottom == 0 && contNoticias != -1) {
					// Do work to refresh the list here end-of-list.
					actualizarBottom = 1;
					new GetDataTaskNoticiasAdd().execute();
				} else if (contNoticias == -1)
					Toast.makeText(getActivity(), "Fin", Toast.LENGTH_SHORT)
							.show();
			}
		});
		ListView actualListView = list.getRefreshableView();
		// Need to use the Actual ListView when registering for Context Menu
		registerForContextMenu(actualListView);

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				txt.setText(arraydir.get(position - 1).getKey());
				Intent intent = new Intent(getActivity(), Topic.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				Bundle extras = new Bundle();
				extras.putString("key", arraydir.get(position - 1).getKey());
				extras.putString("user", user);
				intent.putExtras(extras);
				startActivity(intent);
				android.os.Process.killProcess(android.os.Process.myPid());

			}
		});

		// Add Sound Event Listener
		SoundPullEventListener<ListView> soundListener = new SoundPullEventListener<ListView>(
				getActivity());
		soundListener.addSoundEvent(State.PULL_TO_REFRESH, R.raw.pull_event);
		soundListener.addSoundEvent(State.RESET, R.raw.reset_sound);
		soundListener.addSoundEvent(State.REFRESHING, R.raw.refreshing_sound);
		list.setOnPullEventListener(soundListener);
	}

	public void categorias_connect() {
		String result = "";
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
					"http://infomundo.org/r/android/infomundo/categorias.php");
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
			text = e.toString();
		}

		// response
		if (result != "") {
			// split en java android usa \\ (expresiones regulares)
			String[] parte = result.split("\\|");
			String[] parte2;
			url_categorias = new String[parte.length - 1];
			nombre_categorias = new String[parte.length - 1];

			for (int i = 0; i < (parte.length - 1); i++) {
				parte2 = parte[i].split(",");
				url_categorias[i] = parte2[0];
				nombre_categorias[i] = parte2[1];
			}

		}

	}

	public void noticias_connect() {
		String result = "";

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("contNoticias", Integer
				.toString(contNoticias)));
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
					"http://infomundo.org/r/android/infomundo/noticias.php");
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
			text = e.toString();
		}

		// response
		if (result != "") {
			// split en java android usa \\ (expresiones regulares)
			String[] parte = result.split("\\|");

			for (int i = 0; i < (parte.length - 1); i++) {
				String[] parte2 = parte[i].split("\\,");
				if (parte2[0] != "" && parte2[2] != "" && parte2[3] != "")
					// Introduzco los datos a listview
					arraydir.add(new Directivo(this.user, parte2[0], parte2[2],
							parte2[3]));
				/*
				 * directivo = new
				 * Directivo(getResources().getDrawable(R.drawable
				 * .ariannahuffington), "Arianna Huffington", "Presidenta");
				 * arraydir.add(directivo);
				 */
			}
		}

	}

	public void cargar(int opcion) {
		if (opcion == 1) {
			empezarCargar("Cargando Categorias");
			new AsyncTask<Void, Void, Void>() {
				@Override
				protected Void doInBackground(Void... params) {
					// no acepta UI
					categorias_connect();
					return null;
				}

				@Override
				protected void onPostExecute(Void result) {
					// acepta UI y meter UI de background
					terminarCargar();
					cargar(3);
				}
			}.execute();
		} else if (opcion == 2) {
			empezarCargar("Cargando Lista");
			Toast.makeText(getActivity(), "Cargando Lista", Toast.LENGTH_SHORT)
					.show();
			new AsyncTask<Void, Void, Void>() {
				@Override
				protected Void doInBackground(Void... params) {
					// no acepta UI
					// Creo el adapter personalizado
					adapter = new AdapterDirectivos(getActivity(), arraydir);
					adapter.setAlto(alto);
					adapter.setLargo(largo);
					return null;
				}

				@Override
				protected void onPostExecute(Void result) {
					// acepta UI y meter UI de background
					// Agregamos a la lista
					list.setAdapter(adapter);
					terminarCargar();
				}
			}.execute();

		} else if (opcion == 3) {
			empezarCargar("Cargando Noticias");
			new AsyncTask<Void, Void, Void>() {
				@Override
				protected Void doInBackground(Void... params) {
					// no acepta UI
					noticias_connect();
					return null;
				}

				@Override
				protected void onPostExecute(Void result) {
					// acepta UI y meter UI de background
					terminarCargar();
					cargar(2);
				}
			}.execute();
		} else if (opcion == 4) {
			empezarCargar("Cargando Noticias");
			new AsyncTask<Void, Void, Void>() {
				@Override
				protected Void doInBackground(Void... params) {
					// no acepta UI
					noticias_connect();
					return null;
				}

				@Override
				protected void onPostExecute(Void result) {
					// acepta UI y meter UI de background
					terminarCargar();
				}
			}.execute();
		}
	}

	public void empezarCargar(String cadena) {
		dialog = new ProgressDialog(getActivity());
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

	public int display(int opcion, int valor) {
		// largo == 1, alto != 1
		if (opcion == 1) {
			if (largo != 854)
				return largo * valor / 854;
			else
				return valor;
		} else {
			if (alto != 480)
				return alto * valor / 480;
			else
				return valor;
		}
	}

	private class GetDataTaskNoticias extends
			AsyncTask<Void, Void, ArrayList<Directivo>> {

		@Override
		protected void onPreExecute() {
			// acepta UI
			super.onPreExecute();
		}

		@Override
		protected ArrayList<Directivo> doInBackground(Void... params) {
			// no acepta UI
			// Simulates a background job.
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}
			return arraydir;
		}

		@Override
		protected void onPostExecute(ArrayList<Directivo> result) {
			// acepta UI y meter UI de background
			arraydir.clear();
			cargar(3);
			// Call onRefreshComplete when the list has been refreshed.
			list.onRefreshComplete();
			super.onPostExecute(result);
		}
	}

	private class GetDataTaskNoticiasAdd extends
			AsyncTask<Void, Void, ArrayList<Directivo>> {

		@Override
		protected void onPreExecute() {
			// acepta UI
			super.onPreExecute();
		}

		@Override
		protected ArrayList<Directivo> doInBackground(Void... params) {
			// No acepta UI
			// Simulates a background job.
			// incremento
			contNoticias = contNoticias + 20;
			return arraydir;
		}

		@Override
		protected void onPostExecute(ArrayList<Directivo> result) {
			// acepta UI y meter UI de background
			// arraydir.clear();
			cargar(4);
			adapter.notifyDataSetChanged();
			// Call onRefreshComplete when the list has been refreshed.
			list.onRefreshComplete();
			// list.getRefreshableView().setSelection(contNoticias - 20);
			actualizarBottom = 0;
			super.onPostExecute(result);
		}
	}

}