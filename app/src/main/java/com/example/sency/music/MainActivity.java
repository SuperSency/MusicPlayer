package com.example.sency.music;

import android.app.Activity;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private List<ItemBean> mList;
    private TextView title;
    private TextView artist;
    private ImageView front;
    private ImageView play;
    private ImageView next;
    private ListView mListView;
    int position;
    ListAdapter mAdapter;
    MediaPlayer mPlayer = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        mListView.setOnItemClickListener(this);
        front.setOnClickListener(this);
        play.setOnClickListener(this);
        next.setOnClickListener(this);
    }

    private void initData() {
        mList = new ArrayList<>();
        mList = getMusicList();
        mAdapter = new ListAdapter(this, mList);
        mListView.setAdapter(mAdapter);
    }

    private void initView() {
        title = (TextView) findViewById(R.id.songName);
        artist = (TextView) findViewById(R.id.songerName);
        front = (ImageView) findViewById(R.id.front);
        play = (ImageView) findViewById(R.id.play);
        next = (ImageView) findViewById(R.id.next);
        mListView = (ListView) findViewById(R.id.listView);
    }

    public List<ItemBean> getMusicList() {
        //通过数据库搜索
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        List<ItemBean> datas = new ArrayList<>();
        for (int i = 0; i < cursor.getCount(); i++) {
            ItemBean data = new ItemBean();
            cursor.moveToNext();
            //歌曲名
            String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            //歌手
            String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));

            String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            //是否为音乐
            int isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));
            if (isMusic != 0) {
                data.setSong(title);
                data.setSonger(artist);
                data.setUrl(url);
                datas.add(data);
            }
        }
        return datas;
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        this.position = i;
        play(position);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.front:
                getFront(position);
                break;
            case R.id.next:
                getNext(position);
                break;
            case R.id.play:
                if (mPlayer.isPlaying()){
                    mPlayer.pause();
                    play.setImageResource(R.drawable.play);
                }else{
                    mPlayer.start();
                    play.setImageResource(R.drawable.pause);
                }
                break;
        }
    }

    private void getFront(int i) {
        position = position == 0 ? mList.size() - 1 : position - 1;
        play(position);
    }

    private void next() {
        position = position == mList.size() - 1 ? 0 : position + 1;
        play(position);
    }

    public void getNext(int i){
        next();
    }

    public void play(int i) {
        title.setText(mList.get(i).getSong());
        artist.setText(mList.get(i).getSonger());
        play.setImageResource(R.drawable.pause);
        try {
            mPlayer.reset();
            mPlayer.setDataSource(mList.get(i).getUrl());
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                next();
            }
        });
    }

    //释放资源
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
        }
    }
}

class ItemBean {
    private String song;
    private String songer;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ItemBean() {
    }

    public ItemBean(String songer, String song) {
        this.songer = songer;
        this.song = song;
    }

    public String getSonger() {

        return songer;
    }

    public void setSonger(String songer) {
        this.songer = songer;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }
}
