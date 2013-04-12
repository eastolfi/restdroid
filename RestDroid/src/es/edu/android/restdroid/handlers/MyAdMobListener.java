package es.edu.android.restdroid.handlers;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest.ErrorCode;

public class MyAdMobListener implements AdListener {

	@Override
	public void onDismissScreen(Ad arg0) {
		System.out.println("onDismissScreen");
	}

	@Override
	public void onFailedToReceiveAd(Ad arg0, ErrorCode arg1) {
		System.out.println("onFailedToReceiveAd");
	}

	@Override
	public void onLeaveApplication(Ad arg0) {
		System.out.println("onLeaveApplication");
	}

	@Override
	public void onPresentScreen(Ad arg0) {
		System.out.println("onPresentScreen");
	}

	@Override
	public void onReceiveAd(Ad arg0) {
		System.out.println("onReceiveAd");
	}

}
