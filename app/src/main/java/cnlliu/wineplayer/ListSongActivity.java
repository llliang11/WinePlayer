package cnlliu.wineplayer;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Debug;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import java.util.List;


public class ListSongActivity extends ActionBarActivity {

    private List<String> musicList = null;
    private MusicLoader musicProvider = null;
    private ListView listview = null;
    private ServiceConnection serviceConnection = null;
    private PlaybackService.WinePlayer winePlayer = null;
    private Button play_pause = null;
    private boolean bindServiceFlag = false;
    private String sourceUrl = null;
    private final String DEBUG = "List Song Activity ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wine_layout);

        musicProvider = MusicLoader.instance(this.getContentResolver());
        musicList = musicProvider.getMusicList();
        connectPlayBackService();
        initialView();

    }

    private void initialView() {
        if (musicList != null) {
            listview = (ListView)findViewById(R.id.songListView);
            BaseAdapter adapter = new ArrayAdapter(this, R.layout.music_item, R.id.musicText, musicList);
            listview.setAdapter(adapter);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (winePlayer != null) {
                        String title = (String) listview.getItemAtPosition(position);
                        Intent intent = new Intent();
                        intent.setClass(ListSongActivity.this, PlaybackActivity.class);
                        startActivity(intent);
                        winePlayer.play(title);
                        sourceUrl = title;
                    }
                    else {
                        System.out.println(DEBUG + "wine player is null");
                    }
                }
            });
        }

        TextView welText = (TextView)findViewById(R.id.welEnd);
        welText.setText("It's about spring season, it's about dream, just listen" +
                " these beautiful musics");

        LinearLayout controlBar = (LinearLayout)findViewById(R.id.bottomLayout);
        controlBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ListSongActivity.this, PlaybackActivity.class);
                startActivity(intent);
            }
        });

        play_pause = (Button)findViewById(R.id.play_pause_btn);
        play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (winePlayer != null) {
                    boolean isPlaing = winePlayer.isPlaying();
                    if (isPlaing) {
                        winePlayer.pause();
                        play_pause.setBackgroundResource(R.drawable.play);
                    } else {
                        if (sourceUrl == null) {
                            winePlayer.play((String) listview.getItemAtPosition(0));
                        }
                        else {
                            winePlayer.resume();
                        }
                        play_pause.setBackgroundResource(R.drawable.pause);
                    }
                }
            }
        });
    }

    private void playBtnBackSet() {
        if (winePlayer != null) {
            boolean isPlaing = winePlayer.isPlaying();
            if (isPlaing) {
                play_pause.setBackgroundResource(R.drawable.pause);
            } else {
                play_pause.setBackgroundResource(R.drawable.play);
            }
        }
    }
    private void connectPlayBackService() {
        if (bindServiceFlag == false) {
            serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    winePlayer = (PlaybackService.WinePlayer) service;
                    System.out.println(DEBUG + "wine binder created");
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {

                }
            };
            Intent intent = new Intent(ListSongActivity.this, PlaybackService.class);
            bindService(intent, serviceConnection, BIND_AUTO_CREATE);
            bindServiceFlag = true;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
        winePlayer = null;
        bindServiceFlag = false;
        System.out.println(DEBUG + "on Destroy");
    }

    @Override
    protected void onResume() {
        super.onResume();
        //connectPlayBackService();
        playBtnBackSet();
    }
}
