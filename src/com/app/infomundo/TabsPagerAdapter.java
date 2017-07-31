package com.app.infomundo;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

//agregar properties ->java build path -> libraries -> add jar's -> android supports v4
public class TabsPagerAdapter extends FragmentPagerAdapter {

	String user = "";
	Bundle extras;

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	public void setUser(String user) {
		this.user = user;
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Fragment activity
			Fragment fIndex = new Index();
			extras = new Bundle();
			extras.putString("user", this.user);
			fIndex.setArguments(extras);
			return fIndex;
		case 1:
			// Fragment activity
			Fragment fMuro = new Muro();
			extras = new Bundle();
			extras.putString("user", this.user);
			fMuro.setArguments(extras);
			return fMuro;
		case 2:
			// Fragment activity
			Fragment fNoticiasAmigos = new NoticiasAmigos();
			extras = new Bundle();
			extras.putString("user", this.user);
			fNoticiasAmigos.setArguments(extras);
			return fNoticiasAmigos;
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 3;
	}
	
	

	// agregar properties ->java build path -> libraries -> add jar's -> android
	// supports v7
	public static void setHasEmbeddedTabs(Object inActionBar,
			final boolean inHasEmbeddedTabs) {
		// get the ActionBar class
		Class<?> actionBarClass = inActionBar.getClass();

		// if it is a Jelly Bean implementation (ActionBarImplJB), get the super
		// class (ActionBarImplICS)
		if ("android.support.v7.app.ActionBarImplJB".equals(actionBarClass
				.getName())) {
			actionBarClass = actionBarClass.getSuperclass();
		}

		try {
			// try to get the mActionBar field, because the current ActionBar is
			// probably just a wrapper Class
			// if this fails, no worries, this will be an instance of the native
			// ActionBar class or from the ActionBarImplBase class
			final Field actionBarField = actionBarClass
					.getDeclaredField("mActionBar");
			actionBarField.setAccessible(true);
			inActionBar = actionBarField.get(inActionBar);
			actionBarClass = inActionBar.getClass();
		} catch (IllegalAccessException e) {
		} catch (IllegalArgumentException e) {
		} catch (NoSuchFieldException e) {
		}

		try {
			// now call the method setHasEmbeddedTabs, this will put the tabs
			// inside the ActionBar
			// if this fails, you're on you own <img
			// src="http://www.blogc.at/wp-includes/images/smilies/icon_wink.gif"
			// alt=";-)" class="wp-smiley">
			final Method method = actionBarClass.getDeclaredMethod(
					"setHasEmbeddedTabs", new Class[] { Boolean.TYPE });
			method.setAccessible(true);
			method.invoke(inActionBar, new Object[] { inHasEmbeddedTabs });
		} catch (NoSuchMethodException e) {
		} catch (InvocationTargetException e) {
		} catch (IllegalAccessException e) {
		} catch (IllegalArgumentException e) {
		}
	}

}
