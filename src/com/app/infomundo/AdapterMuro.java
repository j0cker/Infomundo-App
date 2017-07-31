package com.app.infomundo;

import java.util.ArrayList;
import com.app.imgloader.ImageLoader;
import com.app.infomundo.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class AdapterMuro extends BaseAdapter {

	protected Activity activity;
	protected ArrayList<Directivo> items;
	LayoutInflater mInflater;
	String user = "";
	int largo = 0, alto;
	String mensajes = "0", seguidores = "0", siguiendo = "0", fotoProfile = "";

	public AdapterMuro(Activity activity, ArrayList<Directivo> items) {
		this.mInflater = activity.getLayoutInflater();
		this.activity = activity;
		this.items = items;
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int arg0) {
		return items.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return items.get(position).getId();
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setLargo(int largo) {
		this.largo = largo;
	}

	public void setAlto(int alto) {
		this.alto = alto;
	}

	public void setMensajes(String mensajes) {
		this.mensajes = mensajes;
	}

	public void setSeguidores(String seguidores) {
		this.seguidores = seguidores;
	}

	public void setSiguiendo(String siguiendo) {
		this.siguiendo = siguiendo;
	}

	public void setFotoProfile(String fotoProfile) {
		this.fotoProfile = fotoProfile;
	}

	@SuppressWarnings("deprecation")
	@SuppressLint({ "NewApi", "SetJavaScriptEnabled" })
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		// Obtenemos Datos del Adapter Array
		final Directivo dir = items.get(position);
		// Asociamos el layout de la lista que hemos creado
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.tabs_muro, null);
			convertView.setBackgroundColor(Color.argb(255, 224, 224, 224));
			holder = new ViewHolder();
			// image profile
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			holder.textoProfile = (TextView) convertView
					.findViewById(R.id.textoProfile);
			holder.textoMensajes = (Button) convertView
					.findViewById(R.id.textoMensajes);
			holder.numMensajes = (Button) convertView
					.findViewById(R.id.numMensajes);
			holder.textoSiguiendo = (Button) convertView
					.findViewById(R.id.textoSiguiendo);
			holder.numSiguiendo = (Button) convertView
					.findViewById(R.id.numSiguiendo);
			holder.textoSeguidores = (Button) convertView
					.findViewById(R.id.textoSeguidores);
			holder.numSeguidores = (Button) convertView
					.findViewById(R.id.numSeguidores);
			holder.fecha = (TextView) convertView.findViewById(R.id.fecha);
			holder.nombrePerfil = (Button) convertView
					.findViewById(R.id.nombreUsuario);
			holder.imgLoader = new ImageLoader(activity.getApplicationContext());
			holder.imgLoaderPerfil = new ImageLoader(
					activity.getApplicationContext());
			holder.foto = (ImageView) convertView.findViewById(R.id.foto);
			holder.stringContenido = (TextView) convertView
					.findViewById(R.id.stringContenido);
			holder.vWebView = (WebView) convertView.findViewById(R.id.vwebview);
			convertView.setTag(holder);
			holder.aWebView = (WebView) convertView.findViewById(R.id.awebview);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (position != 0) {
			android.view.ViewGroup.LayoutParams layoutParamsImage = holder.image
					.getLayoutParams();
			layoutParamsImage.width = 0;
			layoutParamsImage.height = 0;
			holder.image.setLayoutParams(layoutParamsImage);
			android.view.ViewGroup.LayoutParams layoutParamsText = holder.textoProfile
					.getLayoutParams();
			layoutParamsText.width = 0;
			layoutParamsText.height = 0;
			holder.textoProfile.setText("");
			holder.textoProfile.setLayoutParams(layoutParamsText);

			holder.textoMensajes.setText("");
			holder.numMensajes.setText("");
			holder.textoMensajes.setLayoutParams(layoutParamsText);
			holder.numMensajes.setLayoutParams(layoutParamsText);

			holder.textoSiguiendo.setText("");
			holder.numSiguiendo.setText("");
			holder.textoSiguiendo.setLayoutParams(layoutParamsText);
			holder.numSiguiendo.setLayoutParams(layoutParamsText);

			holder.textoSeguidores.setText("");
			holder.numSeguidores.setText("");
			holder.textoSeguidores.setLayoutParams(layoutParamsText);
			holder.numSeguidores.setLayoutParams(layoutParamsText);

		} else {
			android.view.ViewGroup.LayoutParams layoutParamsImage = holder.image
					.getLayoutParams();
			layoutParamsImage.width = this.largo / 4;
			layoutParamsImage.height = this.largo / 4;
			holder.image.setLayoutParams(layoutParamsImage);
			holder.imgLoaderPerfil
					.DisplayImage(fotoProfile,
							"android.resource://com.app.infomundo/"
									+ R.drawable.loader, holder.image, 70);

			android.view.ViewGroup.LayoutParams layoutParamsText = holder.textoProfile
					.getLayoutParams();
			layoutParamsText.width = LayoutParams.WRAP_CONTENT;
			layoutParamsText.height = LayoutParams.WRAP_CONTENT;

			holder.textoProfile.setTextColor(Color.BLUE);
			holder.textoProfile.setText(this.user);
			holder.textoProfile.setTextSize(20);

			holder.textoMensajes.setTextColor(Color.BLUE);
			holder.textoMensajes.setBackgroundColor(Color.argb(255, 224, 224,
					224));
			holder.textoMensajes.setText("Mensajes");
			holder.textoMensajes.setTextSize(12);
			holder.numMensajes.setText(mensajes);
			holder.numMensajes.setTextSize(12);
			holder.numMensajes.setTextColor(Color.BLUE);
			holder.numMensajes.setBackgroundColor(Color
					.argb(255, 224, 224, 224));

			holder.textoSiguiendo.setTextColor(Color.BLUE);
			holder.textoSiguiendo.setBackgroundColor(Color.argb(255, 224, 224,
					224));
			holder.textoSiguiendo.setText("Siguiendo");
			holder.textoSiguiendo.setTextSize(12);
			holder.numSiguiendo.setText(siguiendo);
			holder.numSiguiendo.setTextSize(12);
			holder.numSiguiendo.setTextColor(Color.BLUE);
			holder.numSiguiendo.setBackgroundColor(Color.argb(255, 224, 224,
					224));

			holder.textoSeguidores.setTextColor(Color.BLUE);
			holder.textoSeguidores.setBackgroundColor(Color.argb(255, 224, 224,
					224));
			holder.textoSeguidores.setText("Seguidores");
			holder.textoSeguidores.setTextSize(12);
			holder.numSeguidores.setText(seguidores);
			holder.numSeguidores.setTextSize(12);
			holder.numSeguidores.setTextColor(Color.BLUE);
			holder.numSeguidores.setBackgroundColor(Color.argb(255, 224, 224,
					224));

			holder.textoProfile.setLayoutParams(layoutParamsText);
			holder.textoMensajes.setLayoutParams(layoutParamsText);
			holder.numMensajes.setLayoutParams(layoutParamsText);
			holder.textoSeguidores.setLayoutParams(layoutParamsText);
			holder.numSeguidores.setLayoutParams(layoutParamsText);
			holder.textoSiguiendo.setLayoutParams(layoutParamsText);
			holder.numSiguiendo.setLayoutParams(layoutParamsText);
		}
		holder.nombrePerfil.setTextColor(Color.BLUE);
		holder.nombrePerfil.setBackgroundColor(Color.argb(255, 224, 224, 224));
		holder.nombrePerfil.setText("@" + dir.getUser());
		holder.nombrePerfil.setOnClickListener(new OnClickListener() {
			@Override
			// On click function
			public void onClick(View view) {
				Intent intent = new Intent(view.getContext(), Profile.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				Bundle extras = new Bundle();
				extras.putString("user", user);
				extras.putString("userProfile", dir.getUser());
				intent.putExtras(extras);
				view.getContext().startActivity(intent);
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		});
		holder.nombrePerfil.setTextSize(12);
		holder.fecha.setText(dir.getFecha());
		holder.fecha.setTextColor(Color.BLACK);
		holder.fecha.setTextSize(15);
		holder.fecha.setPadding(5, 5, 0, 0);
		if (!dir.getUrlFoto().equals("")) {
			android.view.ViewGroup.LayoutParams layoutParamsImage3 = holder.foto
					.getLayoutParams();
			layoutParamsImage3.width = this.largo;
			holder.foto.setLayoutParams(layoutParamsImage3);
			holder.foto.setWillNotDraw(false);
			holder.foto.setMaxHeight(this.largo);
			holder.foto.setMaxWidth(this.largo);
			holder.foto.setMinimumWidth(this.largo);
			android.view.ViewGroup.LayoutParams layoutParamsvWebView = holder.vWebView
					.getLayoutParams();
			layoutParamsvWebView.width = 0;
			layoutParamsvWebView.height = 0;
			holder.vWebView.setLayoutParams(layoutParamsvWebView);
			android.view.ViewGroup.LayoutParams layoutParamsaWebView = holder.aWebView
					.getLayoutParams();
			layoutParamsaWebView.width = 0;
			layoutParamsaWebView.height = 0;
			holder.aWebView.setLayoutParams(layoutParamsaWebView);
			android.view.ViewGroup.LayoutParams layoutParamsText2 = holder.stringContenido
					.getLayoutParams();
			layoutParamsText2.width = 0;
			layoutParamsText2.height = 0;
			holder.stringContenido.setLayoutParams(layoutParamsText2);
			holder.stringContenido.setText("");
			holder.imgLoader
					.DisplayImage(dir.getUrlFoto(),
							"android.resource://com.app.infomundo/"
									+ R.drawable.loader, holder.foto, 230);

		} else if (!dir.getStringContenido().equals("")) {
			holder.foto.setWillNotDraw(true);
			holder.foto.setMaxHeight(0);
			holder.foto.setMaxWidth(0);
			holder.foto.setMinimumWidth(0);
			android.view.ViewGroup.LayoutParams layoutParamsvWebView = holder.vWebView
					.getLayoutParams();
			layoutParamsvWebView.width = 0;
			layoutParamsvWebView.height = 0;
			holder.vWebView.setLayoutParams(layoutParamsvWebView);
			android.view.ViewGroup.LayoutParams layoutParamsaWebView = holder.aWebView
					.getLayoutParams();
			layoutParamsaWebView.width = 0;
			layoutParamsaWebView.height = 0;
			holder.aWebView.setLayoutParams(layoutParamsaWebView);
			android.view.ViewGroup.LayoutParams layoutParamsText3 = holder.stringContenido
					.getLayoutParams();
			layoutParamsText3.width = this.largo;
			layoutParamsText3.height = 200;
			holder.stringContenido.setLayoutParams(layoutParamsText3);
			holder.stringContenido.setMaxHeight(200);
			holder.stringContenido.setMinimumHeight(50);
			holder.stringContenido.setMaxWidth(this.largo);
			holder.stringContenido.setMinimumWidth(this.largo);
			holder.stringContenido.setText(dir.getStringContenido());
			holder.stringContenido.setTextSize(15);
			holder.stringContenido.setTextColor(Color.BLACK);
		} else if (!dir.getVideo().equals("")) {
			holder.foto.setWillNotDraw(true);
			holder.foto.setMaxHeight(0);
			holder.foto.setMaxWidth(0);
			holder.foto.setMinimumWidth(0);
			android.view.ViewGroup.LayoutParams layoutParamsvWebView = holder.vWebView
					.getLayoutParams();
			layoutParamsvWebView.width = this.largo;
			layoutParamsvWebView.height = this.largo;
			holder.vWebView.setLayoutParams(layoutParamsvWebView);
			android.view.ViewGroup.LayoutParams layoutParamsaWebView = holder.aWebView
					.getLayoutParams();
			layoutParamsaWebView.width = 0;
			layoutParamsaWebView.height = 0;
			holder.aWebView.setLayoutParams(layoutParamsaWebView);
			android.view.ViewGroup.LayoutParams layoutParamsText2 = holder.stringContenido
					.getLayoutParams();
			layoutParamsText2.width = 0;
			layoutParamsText2.height = 0;
			holder.stringContenido.setLayoutParams(layoutParamsText2);
			holder.stringContenido.setText("");
			WebSettings webSettings = holder.vWebView.getSettings();
			webSettings.setJavaScriptEnabled(true);
			webSettings.setPluginState(PluginState.ON);
			holder.vWebView.loadUrl("http://www.youtube.com/embed/"
					+ dir.getVideo() + "?autoplay=1&vq=small");
			holder.vWebView.setWebChromeClient(new WebChromeClient());
			holder.vWebView.setWebViewClient(new WebViewClient());
		} else if (!dir.getAudio().equals("")) {
			android.view.ViewGroup.LayoutParams layoutParamsvWebView = holder.vWebView
					.getLayoutParams();
			layoutParamsvWebView.width = 0;
			layoutParamsvWebView.height = 0;
			holder.vWebView.setLayoutParams(layoutParamsvWebView);
			android.view.ViewGroup.LayoutParams layoutParamsaWebView = holder.aWebView
					.getLayoutParams();
			layoutParamsaWebView.width = this.largo;
			layoutParamsaWebView.height = 350;
			holder.aWebView.setLayoutParams(layoutParamsaWebView);
			holder.foto.setWillNotDraw(true);
			holder.foto.setMaxHeight(0);
			holder.foto.setMaxWidth(0);
			holder.foto.setMinimumWidth(0);
			android.view.ViewGroup.LayoutParams layoutParamsText2 = holder.stringContenido
					.getLayoutParams();
			layoutParamsText2.width = 0;
			layoutParamsText2.height = 0;
			holder.stringContenido.setLayoutParams(layoutParamsText2);
			holder.stringContenido.setText("");
			WebSettings webSettings = holder.aWebView.getSettings();
			webSettings.setJavaScriptEnabled(true);
			webSettings.setPluginState(PluginState.ON);
			holder.aWebView.loadUrl(dir.getAudio());
			holder.aWebView.setWebChromeClient(new WebChromeClient());
			holder.aWebView.setWebViewClient(new WebViewClient());
		} else {
			// Rellenamos la fotografía
			holder.foto.setImageDrawable(dir.getFoto());
		}
		// Retornamos la vista
		return convertView;

	}

	static class ViewHolder {
		public Button nombrePerfil;
		public TextView fecha;
		public TextView stringContenido;
		public ImageView foto;
		public ImageLoader imgLoader;
		public ImageLoader imgLoaderPerfil;
		public ImageView image;
		public TextView textoProfile;
		public Button textoMensajes;
		public Button textoSiguiendo;
		public Button textoSeguidores;
		public Button numMensajes;
		public Button numSiguiendo;
		public Button numSeguidores;
		public WebView vWebView;
		public WebView aWebView;
	}
}