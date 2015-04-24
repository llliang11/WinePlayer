package cnlliu.wineplayer;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Media;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by cnlliu on 2015/4/21.
 */
public class MusicLoader {

    private static MusicLoader musicLoader = null;
    private static ContentResolver musicResolver = null;
    private static List<MusicInfo> musicInfos = null;

    public static MusicLoader instance(ContentResolver resolver) {
        if (musicLoader == null) {
            musicResolver = resolver;
            musicLoader = new MusicLoader();
            loadMusicInfo();
        }
        return musicLoader;
    }

    public List<String> getMusicList() {
        List<String> list = new ArrayList<String>();
        Iterator iterator = musicInfos.iterator();

        while (iterator.hasNext()) {
            MusicInfo musicInfo = (MusicInfo)iterator.next();
            list.add(musicInfo.title);
            //System.out.println(musicInfo.title);
            //System.out.println(musicInfo.sourceUrl);
        }
        return list;
    }

    static class MusicInfo implements Parcelable {
        private String title;
        private String sourceUrl;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

        }
    }
    private static void loadMusicInfo() {
        Cursor musicCursor;
        musicInfos = new ArrayList<MusicInfo>();
        musicCursor = musicResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        if (musicCursor.moveToFirst()) {
            do {
                MusicInfo musicInfo = new MusicInfo();
                musicInfo.title = musicCursor.getString(musicCursor.getColumnIndexOrThrow(Media.TITLE));
                musicInfo.sourceUrl = musicCursor.getString(musicCursor.getColumnIndex(Media.DATA));
                musicInfos.add(musicInfo);
            }while (musicCursor.moveToNext());
        }
    }

    public String sourceUrlGet(String title) {
        Iterator iterator = musicInfos.iterator();

        while (iterator.hasNext()) {
            MusicInfo musicInfo = (MusicInfo)iterator.next();
            if (title.endsWith(musicInfo.title)) {
                return musicInfo.sourceUrl;
            }
        }
        return null;
    }
}
