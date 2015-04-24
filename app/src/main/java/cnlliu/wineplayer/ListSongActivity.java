package cnlliu.wineplayer;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
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
    private final String DEBUG = "wine player";

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
                    String title = (String) listview.getItemAtPosition(position);
                    Intent intent = new Intent();
                    intent.setClass(ListSongActivity.this, PlaybackActivity.class);
                    startActivity(intent);
                    winePlayer.play(title);
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
                boolean isPlaing = winePlayer.isPlaying();
                if (isPlaing) {
                    winePlayer.pause();
                    play_pause.setBackgroundResource(R.drawable.play);
                }
                else {
                    String title = (String) listview.getItemAtPosition(0);
                    winePlayer.play(title);
                    play_pause.setBackgroundResource(R.drawable.pause);
                }
            }
        });
    }

    private void connectPlayBackService() {
        serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    winePlayer = (PlaybackService.WinePlayer)service;
                    bindServiceFlag = true;
                    System.out.println(DEBUG + "wine binder created");
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    winePlayer = null;
                    bindServiceFlag = false;
                    System.out.println(DEBUG + "service disconnect ...");
                }
        };
        Intent intent = new Intent(ListSongActivity.this, PlaybackService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(serviceConnection);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bindServiceFlag == false) {
            connectPlayBackService();
        }
    }
dfdf
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
