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
    private boolean pauseFlag = false;

    @Override
    public void onCreate() {
        super.onCreate();
        musicLoader = MusicLoader.instance(this.getContentResolver());
        initMediaPlayer();
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
        if (pauseFlag) {
            mediaPlayer.start();
            pauseFlag = false;
            isPlayingFlag = true;
        }
        else {
            try {
                mediaPlayer.setDataSource(url);
                mediaPlayer.prepare();
                mediaPlayer.start();
                isPlayingFlag = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void winePause() {
        mediaPlayer.pause();
        pauseFlag = true;
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
            sourceUrl = musicLoader.sourceUrlGet(sourceUrl);
            winePlay(sourceUrl);
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
