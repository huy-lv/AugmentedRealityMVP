package vnu.uet.augmentedrealitymvp.screen.crop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.EditText;

import com.isseiaoki.simplecropview.CropImageView;
import com.isseiaoki.simplecropview.callback.LoadCallback;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import butterknife.Bind;
import butterknife.OnClick;
import vnu.uet.augmentedrealitymvp.R;
import vnu.uet.augmentedrealitymvp.base.BaseActivity;
import vnu.uet.augmentedrealitymvp.common.Constants;
import vnu.uet.augmentedrealitymvp.helper.SessionManager;

/**
 * Created by huylv on 24-Apr-16.
 */
public class CropActivity extends BaseActivity<CropPresenter> implements CropView {

    @Bind(R.id.cropImageView)
    CropImageView cropImageView;
    InputStream imageStream;


    @Bind(R.id.crop_activity_marker_name_et)
    EditText crop_activity_marker_name_et;
    String encoded;
    SessionManager session;
    Uri uriImage=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionManager(this);
        Intent i= getIntent();
        switch (i.getIntExtra(Constants.KEY_ACTIVITY_RESULT_TYPE,0)){
            case Constants.REQUEST_CAMERA:
                uriImage = Uri.parse(i.getStringExtra(Constants.KEY_ACTIVITY_RESULT_DATA));
                try {
                    Bitmap captureBmp = MediaStore.Images.Media.getBitmap(getContentResolver(), uriImage);
                    Log.e("cxz", "cxz" + captureBmp.getHeight());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case Constants.SELECT_FILE:
                uriImage = Uri.parse(i.getStringExtra(Constants.KEY_ACTIVITY_RESULT_DATA));
                break;
            case 0:
                showErrorDialog("error when get image data");
                break;
        }

        if(uriImage!=null) {
            cropImageView.startLoad(uriImage, new LoadCallback() {
                @Override
                public void onSuccess() {
                    try {
                        getPresenter().encodeImage(getContentResolver(), uriImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError() {
                    onRequestError("load image error");
                }
            });
        }
    }
    @OnClick(R.id.crop_activity_upload_bt)
    void uploadMarker(){
        showProgress("Upload marker...");
        getPresenter().uploadMarker(crop_activity_marker_name_et.getText().toString().trim(),encoded,session);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_crop_photo;
    }

    @Override
    public CropPresenter onCreatePresenter() {
        return new CropPresenterImpl(this);
    }

    @Override
    public void onUploadSuccess() {
        hideProgress();
        finish();
    }

    @Override
    public void onEncodeSuccess(String encoded) {
        this.encoded = encoded;
    }
}
