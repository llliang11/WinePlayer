package cnlliu.wineplayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import java.io.IOException;
import java.net.URL;

public class PlaybackService extends Service {
    private WinePlayer winebinder = new WinePlayer();
    private MusicLoader musicLoader = null;
    private MediaPlayer mediaPlayer = null;
    private boolean isPlayingFlag = false;
    private final String DEBUG = "Play Back Service ";

    @Override
    public void onCreate() {
        super.onCreate();
        musicLoader = MusicLoader.instance(this.getContentResolver());
        initMediaPlayer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
        mediaPlayer = null;
        System.out.println(DEBUG + "service ondestroy");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        System.out.println(DEBUG + "service onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return winebinder;
    }

    private boolean isPlayerPlaying() {
        return mediaPlayer.isPlaying();
    }

    private void winePlay(String url) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            mediaPlayer.start();
            isPlayingFlag = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void wineResume() {
        mediaPlayer.start();
        isPlayingFlag = true;
    }

    private void winePause() {
        mediaPlayer.pause();
        isPlayingFlag = false;
    }

    private void wineStop() {

    }

    private void winePlayNext() {

    }

    private void winePalyPrevious() {

    }

    class WinePlayer extends Binder {
        public void play(String sourceUrl) {
            if (sourceUrl != null) {
                sourceUrl = musicLoader.sourceUrlGet(sourceUrl);
                winePlay(sourceUrl);
            }
            System.out.println("play a music " + sourceUrl);
        }

        public void play(URL sourceUrl) {
            //winePlay(sourceUrl);
        }

        public void pause() {
            winePause();
            System.out.println("pause a music");
        }

        public void stop() {
            mediaPlayer.stop();
        }

        public boolean isPlaying() {
            return isPlayingFlag;
        }

        public void resume() {
            wineResume();
        }
        public void playNext() {
            winePlayNext();
        }

        public void playPrevious() {
            winePalyPrevious();
        }
    }

    private void initMediaPlayer() {
        mediaPlayer = new MediaPlayer();
    }
}
