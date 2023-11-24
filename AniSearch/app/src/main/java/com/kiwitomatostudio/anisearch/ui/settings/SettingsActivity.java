package com.kiwitomatostudio.anisearch.ui.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.kiwitomatostudio.anisearch.R;
import com.kiwitomatostudio.anisearch.databinding.ActivitySettingsBinding;
import com.kiwitomatostudio.anisearch.manager.AppSettingsManager;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    ActivitySettingsBinding m_binding;
    AppSettingsManager m_appSettingsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(m_binding.getRoot());
        m_appSettingsManager = AppSettingsManager.getInstance(this);
        m_binding.ivBack.setOnClickListener(this);
        initializeVideoSwitch();

    }

    @Override
    public void onClick(View v) {
        if (v == m_binding.ivBack) {
            finish();
        }
    }

    private void initializeVideoSwitch() {
        boolean isVideoEnabled = m_appSettingsManager.getVideoEnabled();
        m_binding.msVideo.setChecked(isVideoEnabled);

        m_binding.msVideo.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            m_appSettingsManager.setVideoEnabled(isChecked);
        });
    }
}