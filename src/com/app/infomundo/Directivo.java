package com.app.infomundo;

import android.graphics.drawable.Drawable;

public class Directivo {
	protected Drawable foto;
	protected String urlFoto = "";
	protected String fecha = "";
	protected String nombre = "";
	protected String contenido = "";
	protected String video = "";
	protected String audio = "";
	protected String key = "";
	protected String user = "";
	protected long id;

	public Directivo(Drawable foto, String fecha) {
		super();
		this.foto = foto;
		this.fecha = fecha;
	}

	public Directivo(String user, String contenido, String fecha, int c) {
		super();
		if (c == 1)
			this.contenido = contenido;
		else if (c == 2)
			this.video = contenido;
		else if (c == 3)
			this.audio = contenido;
		this.fecha = fecha;
		this.user = user;
	}

	public Directivo(String user, String urlFoto, String fecha, String key) {
		super();
		this.user = user;
		this.urlFoto = urlFoto;
		this.fecha = fecha;
		this.key = key;
	}

	public Directivo(Drawable foto, String nombre, String key) {
		super();
		this.foto = foto;
		this.nombre = nombre;
		this.key = key;
	}

	public Directivo(Drawable foto, String nombre, String key, long id) {
		super();
		this.foto = foto;
		this.nombre = nombre;
		this.key = key;
		this.id = id;
	}

	public Drawable getFoto() {
		return foto;
	}

	public String getVideo() {
		return video;
	}

	public String getAudio() {
		return audio;
	}

	public String getStringContenido() {
		return contenido;
	}

	public void setFoto(Drawable foto) {
		this.foto = foto;
	}

	public String getUrlFoto() {
		return urlFoto;
	}

	public void setUrlFoto(String urlFoto) {
		this.urlFoto = urlFoto;
	}

	public String getFecha() {
		return fecha;
	}

	public String getNombre() {
		return nombre;
	}

	public String getUser() {
		return user;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}