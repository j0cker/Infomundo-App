package com.app.infomundo;

import java.util.ArrayList;

import com.app.imgloader.ImageLoader;
import com.app.infomundo.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterDirectivos extends BaseAdapter {

	protected Activity activity;
	protected ArrayList<Directivo> items;
	LayoutInflater mInflater;
	int largo = 0;
	int alto;

	public AdapterDirectivos(Activity activity, ArrayList<Directivo> items) {
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

	public int setLargo(int largo) {
		return this.largo = largo;
	}

	public int setAlto(int alto) {
		return this.alto = alto;
	}

	@SuppressLint({ "NewApi", "InflateParams" })
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		// Obtenemos Datos del Adapter Array
		Directivo dir = items.get(position);
		// Asociamos el layout de la lista que hemos creado
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.tabs_index, null);
			convertView.setBackgroundColor(Color.argb(255, 224, 224, 224));
			holder = new ViewHolder();
			holder.nombre = (TextView) convertView.findViewById(R.id.text);
			holder.imgLoader = new ImageLoader(activity.getApplicationContext());
			holder.foto = (ImageView) convertView.findViewById(R.id.foto);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.nombre.setText(dir.getFecha());
		holder.nombre.setTextColor(Color.BLUE);
		holder.nombre.setTextSize(15);
		holder.nombre.setPadding(5, 5, 0, 0);
		if (dir.getUrlFoto() != "") {
			holder.imgLoader
					.DisplayImage(dir.getUrlFoto(),
							"android.resource://com.app.infomundo/"
									+ R.drawable.loader, holder.foto, 70);
		} else {
			// Rellenamos la fotografía
			holder.foto.setImageDrawable(dir.getFoto());
		}
		// Retornamos la vista
		return convertView;

	}

	static class ViewHolder {
		public TextView nombre;
		public ImageView foto;
		public ImageLoader imgLoader;
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
}