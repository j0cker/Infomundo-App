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
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

public class NoticiasAmigos extends Fragment {
	final Handler handler = new Handler();
	int largo, alto, contNoticias = 10, actualizarBottom = 0;
	String mensajes = "0", seguidores = "0", siguiendo = "0", fotoPerfil = "";
	String user = "";
	AdapterMuro adapter;
	ProgressDialog dialog;
	// layout
	View v;
	// listview
	PullToRefreshListView list;
	// arraylist
	ArrayList<Directivo> arraydir;
	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
			.permitAll().build();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		StrictMode.setThreadPolicy(policy);
		this.user = getArguments().getString("user");
		v = new View(getActivity());
		v = inflater.inflate(R.layout.tabs_muro_refresh, container, false);
		// cargar numeros totales(msg, follows, etc..)
		cargar(1);
		layout();
		return v;
	}

	public void layout() {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay()
				.getMetrics(displaymetrics);
		alto = displaymetrics.heightPixels;
		largo = displaymetrics.widthPixels;
		list = (PullToRefreshListView) v.findViewById(R.id.pull_refresh_list);
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
				contNoticias = 10;
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
		arraydir = new ArrayList<Directivo>();
		// Add Sound Event Listener
		SoundPullEventListener<ListView> soundListener = new SoundPullEventListener<ListView>(
				getActivity());
		soundListener.addSoundEvent(State.PULL_TO_REFRESH, R.raw.pull_event);
		soundListener.addSoundEvent(State.RESET, R.raw.reset_sound);
		soundListener.addSoundEvent(State.REFRESHING, R.raw.refreshing_sound);
		list.setOnPullEventListener(soundListener);
	}

	public void mensajes_connect() {
		String result = "";

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("perfil", this.user));
		nameValuePairs.add(new BasicNameValuePair("contNoticias", Integer
				.toString(contNoticias)));
		nameValuePairs.add(new BasicNameValuePair("explorer", "phone"));

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
					"http://infomundo.org/r/android/infomundo/red-social/obtenerNoticias.php");
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
			Toast.makeText(v.getContext(), e.toString(), Toast.LENGTH_SHORT)
					.show();
		}
		// response
		if (result != "") {
			// split en java android usa \\ (expresiones regulares)
			String[] parte = result.split("\\|\\|");
			for (int i = 0; i < (parte.length - 1); i++) {
				String[] parte2 = parte[i].split("\\|");
				if (parte2[3].contains("<img src=")) {
					// imagen
					String[] contenido = parte2[3].split("\\ src\\=");
					String[] imagen = contenido[1].split("\\ height");

					arraydir.add(new Directivo(parte2[1], imagen[0], parte2[2],
							"6130"));
				} else if (parte2[3].contains("<iframe")) {
					// video
					String[] contenido = parte2[3].split("\\ src\\=");
					String[] video = contenido[1].split("\\ frameborder");
					String[] pre_url = video[0].split("embed\\/");
					String url = pre_url[1].substring(0,
							pre_url[1].length() - 1);
					arraydir.add(new Directivo(parte2[1], url, parte2[2], 2));

				} else if (parte2[3].contains("<audio src")) {
					// audio
					String[] contenido = parte2[3].split("\\ src\\=");
					String[] audio = contenido[1].split("\\ controls");
					arraydir.add(new Directivo(parte2[1], audio[0], parte2[2],
							3));
				} else {
					arraydir.add(new Directivo(parte2[1], parte2[3], parte2[2],
							1));
				}
			}
		} else {
			if (arraydir.isEmpty()) {
			}
			contNoticias = -1;
		}
	}

	public void fotoPerfil_connect() {
		String result = "";

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("usuario", this.user));
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
			Toast.makeText(v.getContext(), e.toString(), Toast.LENGTH_SHORT)
					.show();
		}

		// response
		if (result != "") {
			// Error
			this.fotoPerfil = result;
		}
	}

	public void totalMensajes_connect() {
		String result = "";

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("usuario", this.user));
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
					"http://infomundo.org/r/android/infomundo/red-social/totalMensajes.php");
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
			Toast.makeText(v.getContext(), e.toString(), Toast.LENGTH_SHORT)
					.show();
		}

		// response
		if (result != "") {
			// Error
			this.mensajes = result;
		}
	}

	public void totalSiguiendo_connect() {
		String result = "";

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("usuario", this.user));
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
					"http://infomundo.org/r/android/infomundo/red-social/totalSiguiendo.php");
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
			Toast.makeText(v.getContext(), e.toString(), Toast.LENGTH_SHORT)
					.show();
		}

		// response
		if (result != "") {
			// Error
			this.siguiendo = result;
		}

	}

	public void totalSeguidores_connect() {
		String result = "";

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("usuario", this.user));
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
					"http://infomundo.org/r/android/infomundo/red-social/totalSeguidores.php");
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
			Toast.makeText(v.getContext(), e.toString(), Toast.LENGTH_SHORT)
					.show();
		}

		// response
		if (result != "") {
			// Error
			this.seguidores = result;
		}

	}

	public void cargar(int opcion) {
		if (opcion == 1) {
			empezarCargar("Cargando Totales");
			new AsyncTask<Void, Void, Void>() {
				@Override
				protected Void doInBackground(Void... params) {
					// no acepta UI
					totalMensajes_connect();
					totalSiguiendo_connect();
					totalSeguidores_connect();
					fotoPerfil_connect();
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
					adapter = new AdapterMuro(getActivity(), arraydir);
					adapter.setUser(user);
					adapter.setLargo(largo);
					adapter.setAlto(alto);
					adapter.setMensajes(mensajes);
					adapter.setSiguiendo(siguiendo);
					adapter.setSeguidores(seguidores);
					adapter.setFotoProfile(fotoPerfil);
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
			empezarCargar("Cargando Mensajes");
			new AsyncTask<Void, Void, Void>() {
				@Override
				protected Void doInBackground(Void... params) {
					// no acepta UI
					mensajes_connect();
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
			empezarCargar("Cargando Mensajes");
			new AsyncTask<Void, Void, Void>() {
				@Override
				protected Void doInBackground(Void... params) {
					// no acepta UI
					mensajes_connect();
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

	private class GetDataTaskNoticias extends
			AsyncTask<Void, Void, ArrayList<Directivo>> {

		@Override
		protected void onPreExecute() {
			// Acepta UI
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
			// actualizar primeras 20
			cargar(3);
			// Call onRefreshComplete when the list has been refreshed.
			list.onRefreshComplete();
			super.onPostExecute(result);
		}
	}

	private class GetDataTaskNoticiasAdd extends
			AsyncTask<Void, Void, ArrayList<Directivo>> {

		@Override
		protected ArrayList<Directivo> doInBackground(Void... params) {
			// no acepta UI
			// Simulates a background job.
			// incremento
			contNoticias = contNoticias + 10;
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