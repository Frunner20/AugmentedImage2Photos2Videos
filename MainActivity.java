package com.example.myapplication;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.ar.core.Anchor;
import com.google.ar.core.AugmentedImage;
import com.google.ar.core.Frame;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.ExternalTexture;
import com.google.ar.sceneform.rendering.ModelRenderable;

import java.util.Collection;

public class MainActivity extends AppCompatActivity {

    private ExternalTexture texture;
    private MediaPlayer mediaPlayer;
    private MediaPlayer mediaPlayer2;
    private CustomArFragment arFragment;
    private Scene scene;
    private ModelRenderable renderable;
    private boolean isImageDetected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        texture = new ExternalTexture();

        mediaPlayer = MediaPlayer.create(this, R.raw.kai);
        mediaPlayer.setSurface(texture.getSurface());
        mediaPlayer.setLooping(true);

        mediaPlayer2 = MediaPlayer.create(this, R.raw.kai2);
        mediaPlayer2.setSurface(texture.getSurface());
        mediaPlayer2.setLooping(true);

        ModelRenderable
                .builder()
                .setSource(this, Uri.parse("video_screen.sfb"))
                .build()
                .thenAccept(modelRenderable -> {
                    modelRenderable.getMaterial().setExternalTexture("videoTexture",
                            texture);
                    modelRenderable.getMaterial().setFloat4("keyColor",
                            new Color(0.01843f, 1f, 0.098f));

                    renderable = modelRenderable;
                });

        arFragment = (CustomArFragment)
                getSupportFragmentManager().findFragmentById(R.id.arFragment);

        assert arFragment != null;
        scene = arFragment.getArSceneView().getScene();

        scene.addOnUpdateListener(this::onUpdate);

    }

    private void onUpdate(FrameTime frameTime) {

        if (isImageDetected)
            return;

        Frame frame = arFragment.getArSceneView().getArFrame();

        assert frame != null;
        Collection<AugmentedImage> augmentedImages =
                frame.getUpdatedTrackables(AugmentedImage.class);


        for (AugmentedImage image : augmentedImages) {

            if (image.getTrackingState() == TrackingState.TRACKING) {
                switch(image.getName()){
                    case "kai":
                        isImageDetected = true;
                        playVideo (image.createAnchor(image.getCenterPose()), image.getExtentX(),
                                image.getExtentZ());
                        break;
                    case "kai2":
                        isImageDetected = true;
                        playVideo2 (image.createAnchor(image.getCenterPose()), image.getExtentX(),
                                image.getExtentZ());
                        break;
                }/*
                if (image.getName().equals("kai")) {

                    isImageDetected = true;

                    playVideo (image.createAnchor(image.getCenterPose()), image.getExtentX(),
                            image.getExtentZ());

                    break;
                }*/

            }

        }

    }

    private void playVideo(Anchor anchor, float extentX, float extentZ) {
        mediaPlayer.start();
        AnchorNode anchorNode = new AnchorNode(anchor);
        texture.getSurfaceTexture().setOnFrameAvailableListener(surfaceTexture -> {
            anchorNode.setRenderable(renderable);
            texture.getSurfaceTexture().setOnFrameAvailableListener(null);
        });
        anchorNode.setWorldScale(new Vector3(extentX, 1f, extentZ));
        scene.addChild(anchorNode);
    }

    private void playVideo2(Anchor anchor, float extentX, float extentZ) {
        mediaPlayer2.start();
        AnchorNode anchorNode = new AnchorNode(anchor);
        texture.getSurfaceTexture().setOnFrameAvailableListener(surfaceTexture -> {
            anchorNode.setRenderable(renderable);
            texture.getSurfaceTexture().setOnFrameAvailableListener(null);
        });
        anchorNode.setWorldScale(new Vector3(extentX, 1f, extentZ));
        scene.addChild(anchorNode);
    }

}
