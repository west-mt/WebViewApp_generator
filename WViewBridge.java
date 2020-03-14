package net.sytes.vision.WViewApp;

import java.io.IOException;
import android.content.Context;
import android.content.Intent;
import android.view.Window;
import android.widget.Toast;
import android.net.Uri;
import android.media.MediaPlayer;

public class WViewBridge
{
	private Context ctx;
	private MediaPlayer mplayer;

	public WViewBridge(Context c){
		this.ctx = c;
	}

	public void makeToast(String messege){
        Toast.makeText(ctx, messege, Toast.LENGTH_SHORT).show();
    }

	public void makeLongToast(String messege){
        Toast.makeText(ctx, messege, Toast.LENGTH_LONG).show();
    }

	public void startIntent(String a, String uri, String type){
		Intent intent;
		String action;

		//makeToast(Integer.toString(type.length()));
		if(a.startsWith("android.intent.action.")){
			action = a;
		}else{
			action = "android.intent.action." + a;
		}

		intent = new Intent(action);

		if(type.length() > 0){
			//makeToast("null!");
			intent.setDataAndType(Uri.parse(uri), type);
		}else{
			intent.setData(Uri.parse(uri));
		}
		//intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		makeLongToast(intent.toUri(0));

		ctx.startActivity(intent);
		//ctx.startService(intent);
	}

	public void mediaPlay(String uri, int loop){
		mediaOpen(uri, loop);
		mediaStart();
	}

	public void mediaOpen(String uri, int loop){
		Boolean b = (loop != 0);

		/*
		if(mplayer != null){
			mplayer.release();
		}
		mplayer = MediaPlayer.create(ctx, Uri.parse(uri));
		if(mplayer != null){
			mplayer.setLooping(b);
			mplayer.setVolume(1.0f, 1.0f);
		}else{
			makeToast("Open "+uri+" failed.");
		}
		return;
		*/

		mplayer = new MediaPlayer();

		try{
			mplayer.setDataSource(ctx, Uri.parse(uri));
			mplayer.prepare();

		}catch(IOException e){
			makeToast("Open "+uri+" failed.");
			return;
		}


	}

	public void mediaStart(){
		if(mplayer != null){
			mplayer.start();
		}
	}


	public void mediaPause(){
		if(mplayer != null){
			mplayer.pause();
		}
	}

	public void mediaStop(){
		if(mplayer != null){
			mplayer.stop();
			mplayer.reset();
		}
	}

	public int mediaIsPlaying(){
		return mplayer.isPlaying() ? 1:0;
	}

	public int mediaGetDuration(){
		return mplayer.getDuration();
	}

	public int mediaGetCurrentPosition(){
		return mplayer.getCurrentPosition();
	}

}

