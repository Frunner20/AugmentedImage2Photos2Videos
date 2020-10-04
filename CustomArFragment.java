package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.google.ar.core.AugmentedImageDatabase;
import com.google.ar.core.Config;
import com.google.ar.core.Session;
import com.google.ar.sceneform.ux.ArFragment;

public class CustomArFragment extends ArFragment {

    @Override
    protected Config getSessionConfiguration(Session session) {

        Config config = new Config(session);
        config.setFocusMode(Config.FocusMode.AUTO);
        config.setUpdateMode(Config.UpdateMode.LATEST_CAMERA_IMAGE);

        AugmentedImageDatabase aid = new AugmentedImageDatabase(session);

        // Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.image);
        Bitmap kai = BitmapFactory.decodeResource(getResources(), R.drawable.kai);
        Bitmap kai2 = BitmapFactory.decodeResource(getResources(), R.drawable.kai2);
        aid.addImage("kai", kai);
        aid.addImage("kai2", kai2);
        // aid.addImage("image", image);
        config.setAugmentedImageDatabase(aid);

        this.getArSceneView().setupSession(session);

        return config;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FrameLayout frameLayout = (FrameLayout) super.onCreateView(inflater, container, savedInstanceState);

        getPlaneDiscoveryController().hide();
        getPlaneDiscoveryController().setInstructionView(null);

        return frameLayout;
    }
}