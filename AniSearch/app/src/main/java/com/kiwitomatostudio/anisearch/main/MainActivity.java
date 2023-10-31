package com.kiwitomatostudio.anisearch.main;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kiwitomatostudio.anisearch.R;
import com.kiwitomatostudio.anisearch.databinding.ActivityMainBinding;
import com.kiwitomatostudio.anisearch.result.ResultActivity;
import com.kiwitomatostudio.anisearch.utils.NetworkUtils;
import com.yalantis.ucrop.UCrop;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MainActivityViewModel m_viewModel;
    private ActivityMainBinding m_binding;
    private ActivityResultLauncher<PickVisualMediaRequest> m_pickMedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(m_binding.getRoot());
        m_viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        observe();
        m_binding.btnSearch.setOnClickListener(this);
        m_binding.ivScreenshot.setOnClickListener(this);
        // Registers a photo picker activity launcher in single-select mode.
        m_pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    // Callback is invoked after the user selects a media item or closes the
                    // photo picker.
                    if (uri != null) {
                        File croppedFile = new File(getFilesDir(), "cropped_image.jpg");
                        Uri destinationUri = Uri.fromFile(croppedFile);
                        UCrop.of(uri, destinationUri)
                                .withAspectRatio(16, 9)
                                .withMaxResultSize(4096, 4096)
                                .start(this);

                        Log.d("PhotoPicker", "Selected URI: " + uri);
                    } else {
                        Log.d("PhotoPicker", "No media selected");
                    }
                });
    }

    @Override
    public void onClick(View view) {
        if (view == m_binding.btnSearch){
            if(m_viewModel.getButtonText().getValue().equals("選擇截圖")){
                m_pickMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
            }else{
                if(NetworkUtils.isNetworkAvailable(this)){
                    Intent intent = new Intent();
                    intent.putExtra("imageUri", m_viewModel.getSelectedImageUri().getValue());
                    intent.setClass(this, ResultActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(this, "請檢查網路連線再繼續", Toast.LENGTH_SHORT).show();
                }
            }
        }
        if(view == m_binding.ivScreenshot){
            m_pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            m_binding.cvScreenshot.setVisibility(View.VISIBLE);
            final Uri resultUri = UCrop.getOutput(data);
            m_viewModel.setImageSelected(resultUri.toString());
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }

    private void observe() {
        m_viewModel.getButtonText().observe(this, buttonText -> {
            m_binding.btnSearch.setText(buttonText);

        });
        m_viewModel.getSelectedImageUri().observe(this, selectedImageUri -> {
            if (selectedImageUri != null) {
                Glide.with(this)
                        .load(selectedImageUri)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(m_binding.ivScreenshot);
            } else {
                m_binding.ivScreenshot.setImageResource(R.drawable.ic_launcher_background);
            }
        });
    }
}