package com.xpf.mediaplayer.activity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.xpf.mediaplayer.R;
import com.xpf.mediaplayer.utils.LogUtil;
import com.xpf.mediaplayer.view.BaseVisualizerView;

/**
 * Created by xinpengfei on 2019/02/21.
 * Function:Android 跳动频谱页面
 */
public class AudioFxActivity extends AppCompatActivity {

    private static final String TAG = "AudioFxActivity";
    private MediaPlayer mMediaPlayer;
    private Visualizer mVisualizer;
    private BaseVisualizerView mBaseVisualizerView;
    private Toolbar mToolbar;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        setContentView(R.layout.activity_audio_fx);
        mBaseVisualizerView = findViewById(R.id.visualizerview);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        // 设置是否有返回箭头
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
            actionBar.setHomeButtonEnabled(true); //设置返回键可用

            mToolbar.setNavigationOnClickListener(v -> finish());
        }

        mMediaPlayer = MediaPlayer.create(this, R.raw.small_star);
        mMediaPlayer.setOnPreparedListener(mp -> {
            mMediaPlayer.start();
            setupVisualizerFxAndUi();
        });

        mMediaPlayer.setLooping(true);
    }

    /**
     * 生成一个 VisualizerView 对象，使音频频谱的波段能够反映到 VisualizerView上
     */
    private void setupVisualizerFxAndUi() {
        int audioSessionId = mMediaPlayer.getAudioSessionId();
        LogUtil.i("audioSessionId==" + audioSessionId);
        mVisualizer = new Visualizer(audioSessionId);
        // 参数内必须是2的位数
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        // 设置允许波形表示，并且捕获它
        mBaseVisualizerView.setVisualizer(mVisualizer);
        mVisualizer.setEnabled(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing() && mMediaPlayer != null) {
            mVisualizer.release();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

}