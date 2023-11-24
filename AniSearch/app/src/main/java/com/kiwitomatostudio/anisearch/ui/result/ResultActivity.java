package com.kiwitomatostudio.anisearch.ui.result;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kiwitomatostudio.anisearch.R;
import com.kiwitomatostudio.anisearch.api.dto.AnimeResult;
import com.kiwitomatostudio.anisearch.databinding.ActivityResultBinding;
import com.kiwitomatostudio.anisearch.manager.AppSettingsManager;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity implements View.OnClickListener {
    private String m_imageUri;
    private ResultActivityViewModel m_viewModel;
    private ActivityResultBinding m_binding;
    private AppSettingsManager m_appSettingsManager;
    private int m_index = 0;
    private int m_size = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(m_binding.getRoot());
        m_imageUri = getIntent().getStringExtra("imageUri");
        m_viewModel = new ViewModelProvider(this).get(ResultActivityViewModel.class);
        m_viewModel.requestMatchResult(Uri.parse(m_imageUri).getPath());
        m_binding.btnNext.setOnClickListener(this);
        m_binding.btnPrev.setOnClickListener(this);
        m_binding.ivBack.setOnClickListener(this);
        m_appSettingsManager = AppSettingsManager.getInstance(this);
        observe();
        //判斷button是否enable
        toggleButton();
    }

    @Override
    public void onClick(View v) {
        if (v == m_binding.btnNext) {
            if (isIndexValid(m_index + 1)) {
                updateDataByIndex(m_viewModel.result.getValue(), m_index + 1);
                toggleButton();
            }
        } else if (v == m_binding.btnPrev) {
            if (isIndexValid(m_index - 1)) {
                updateDataByIndex(m_viewModel.result.getValue(), m_index - 1);
                toggleButton();
            }
        } else if (v == m_binding.ivBack) {
            finish();
        }
    }

    private String getImagePathFromUri(Context context, Uri imageUri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = null;

        try {
            cursor = context.getContentResolver().query(imageUri, projection, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                return cursor.getString(columnIndex);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    private void observe() {
        m_viewModel.showLoading.observe(this, showLoading -> {
            if (showLoading) {
                m_binding.pbLoading.setVisibility(View.VISIBLE);
            } else {
                m_binding.pbLoading.setVisibility(View.GONE);
            }
        });
        m_viewModel.result.observe(this, results -> {
            if (results != null) {
                m_size = results.size();
                updateDataByIndex(results, 0);
            }
        });
    }

    private void updateDataByIndex(ArrayList<AnimeResult> results, int index) {
        if (results.size() == 0) {
            m_binding.svContent.setVisibility(View.GONE);
            m_binding.llButton.setVisibility(View.GONE);
            return;
        }
        if (index >= results.size()) {
            return;
        }
        m_binding.svContent.setVisibility(View.VISIBLE);
        m_binding.llButton.setVisibility(View.VISIBLE);
        m_index = index;
        m_binding.tvCount.setText("共有" + results.size() + "筆結果，目前為第" + (index + 1) + "筆");
        if (results.get(index).anilist.getNative() != null) {
            m_binding.tvAnimeTitle.setText(results.get(index).anilist.getNative());
        }
        if (results.get(index).anilist.getRomaji() != null) {
            m_binding.tvAnimeRomaji.setText(results.get(index).anilist.getRomaji());
        }
        if (m_appSettingsManager.getVideoEnabled()) {
            m_binding.vvPhoto.setVisibility(View.VISIBLE);
            m_binding.ivPhoto.setVisibility(View.GONE);
            if (results.get(index).video != null) {
                m_binding.vvPhoto.setVideoURI(Uri.parse(results.get(index).video));
                m_binding.vvPhoto.start();
                m_binding.vvPhoto.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        m_binding.vvPhoto.seekTo(0);
                        m_binding.vvPhoto.start();
                    }
                });
            }
        } else {
            m_binding.vvPhoto.setVisibility(View.GONE);
            m_binding.ivPhoto.setVisibility(View.VISIBLE);
            if (results.get(index).image != null) {
                Glide.with(this)
                        .load(results.get(index).image)
                        .into(m_binding.ivPhoto);
            }
        }
        m_binding.tvEpisode.setText("第" + (int) results.get(index).episode + "集");
        m_binding.tvFromTo.setText("從 " + secondsToMinutesAndSeconds(results.get(index).from) + " 到 " + secondsToMinutesAndSeconds(results.get(index).myto));
        m_binding.tvSimilarity.setText("相似度爲 " + results.get(index).similarity);
        toggleButton();
    }

    private void toggleButton() {
        if (m_index >= m_size - 1) {
            m_binding.btnNext.setEnabled(false);
        } else {
            m_binding.btnNext.setEnabled(true);
        }
        if (m_index <= 0) {
            m_binding.btnPrev.setEnabled(false);
        } else {
            m_binding.btnPrev.setEnabled(true);
        }
    }

    private boolean isIndexValid(int index) {
        return index >= 0 && index < m_viewModel.result.getValue().size();
    }

    private String secondsToMinutesAndSeconds(double seconds) {
        int minutes = (int) seconds / 60;
        int remainingSeconds = (int) seconds % 60;

        return String.format("%d分%d秒", minutes, remainingSeconds);
    }
}
