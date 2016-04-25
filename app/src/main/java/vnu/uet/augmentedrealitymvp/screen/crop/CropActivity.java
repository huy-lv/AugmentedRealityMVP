package vnu.uet.augmentedrealitymvp.screen.crop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;

import com.isseiaoki.simplecropview.CropImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
    Bitmap scaled ;

    @Bind(R.id.crop_activity_marker_name_et)
    EditText crop_activity_marker_name_et;
    String encoded;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionManager(this);
        Intent i= getIntent();
        cropImageView.setCropMode(CropImageView.CropMode.RATIO_FREE);
        switch (i.getIntExtra(Constants.KEY_ACTIVITY_RESULT_TYPE,0)){
            case Constants.REQUEST_CAMERA:
                Bitmap bitmap1 = null;
                String filename = i.getStringExtra(Constants.KEY_ACTIVITY_RESULT_DATA);
                try {
                    FileInputStream is = this.openFileInput(filename);
                    bitmap1 = BitmapFactory.decodeStream(is);
                    if(bitmap1.getHeight()>=4096 || bitmap1.getWidth()>=4096){
                        int nh = (int) ( bitmap1.getHeight() * (512.0 / bitmap1.getWidth()));
                        scaled = Bitmap.createScaledBitmap(bitmap1, 512, nh, true);
                    }
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case Constants.SELECT_FILE:
                Uri uriImage = Uri.parse(i.getStringExtra(Constants.KEY_ACTIVITY_RESULT_DATA));
                try {
                    imageStream = getContentResolver().openInputStream(uriImage);
                    Bitmap bitmap2 = BitmapFactory.decodeStream(imageStream);
                    if(bitmap2.getHeight()>=4096 || bitmap2.getWidth()>=4096){
                        int nh = (int) ( bitmap2.getHeight() * (512.0 / bitmap2.getWidth()));
                        scaled = Bitmap.createScaledBitmap(bitmap2, 512, nh, true);
                    }
                    cropImageView.setImageBitmap(scaled);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case 0:
                showErrorDialog("error when get image data");
                break;
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
        finish();
    }
}
