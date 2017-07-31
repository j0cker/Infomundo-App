package com.app.infomundo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

public class SubMenu {

	Activity activity;
	String user;
	MenuItem item;

	public SubMenu(Activity activity, String user) {
		this.activity = activity;
		this.user = user;
	}

	public boolean handleOnItemSelected(int item) {
		// Take appropriate action for each action item click
		switch (item) {
		case R.id.action_search:
			return true;
		case R.id.profile:
			Profile();
			return true;
		case R.id.ayuda:
			return true;
		case R.id.salir:
			Salir();
			return true;
		default:
			return true;
		}
	}

	private void Salir() {
		Intent i = new Intent(this.activity, MainActivity.class);
		activity.startActivity(i);
	}

	private void Profile() {
		Intent i = new Intent(this.activity, Profile.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		Bundle extras = new Bundle();
		extras.putString("user", user);
		i.putExtras(extras);
		activity.startActivity(i);
	}
}
