package vnu.uet.augmentedrealitymvp.screen.markerdetail;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.Bind;
import butterknife.OnClick;
import vnu.uet.augmentedrealitymvp.R;
import vnu.uet.augmentedrealitymvp.app.APIDefine;
import vnu.uet.augmentedrealitymvp.base.BaseActivityToolbar;
import vnu.uet.augmentedrealitymvp.common.util.VarUtils;
import vnu.uet.augmentedrealitymvp.helper.CacheHelper;
import vnu.uet.augmentedrealitymvp.model.Marker;

/**
 * Created by huylv on 27-Apr-16.
 */
public class MarkerDetailActivity extends BaseActivityToolbar<MarkerDetailPresenter> implements MarkerDetailView {
    @Bind(R.id.marker_detail_id_tv)
    public TextView marker_detail_id_tv;
    @Bind(R.id.marker_detail_name_tv)
    public TextView marker_detail_name_tv;
    @Bind(R.id.marker_detail_iset_tv)
    public TextView marker_detail_iset_tv;
    @Bind(R.id.marker_detail_fset_tv)
    public TextView marker_detail_fset_tv;
    @Bind(R.id.marker_detail_fset3_tv)
    public TextView marker_detail_fset3_tv;
    @Bind(R.id.marker_detail_status_tv)
    public TextView marker_detail_status_tv;
    @Bind(R.id.marker_detail_image)
    public ImageView marker_detail_image;
    @Bind(R.id.marker_detail_download_bt)
    public Button marker_detail_download_bt;
    @Bind(R.id.marker_detail_delete_bt)
    Button marker_detail_delete_bt;
    @Bind(R.id.marker_detail_copy_bt)
    Button marker_detail_copy_bt;

    @Bind(R.id.marker_detail_download_iset_pb)
    ProgressBar marker_detail_download_iset_pb;
    @Bind(R.id.marker_detail_download_fset_pb)
    ProgressBar marker_detail_download_fset_pb;
    @Bind(R.id.marker_detail_download_fset3_pb)
    ProgressBar marker_detail_download_fset3_pb;

    Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // o day se bi null
//        assert getSupportActionBar() != null;
//        ActionBar actionBar = getSupportActionBar();
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        marker = getPresenter().getMarkerFromJson(getIntent());

        marker_detail_id_tv.setText(String.valueOf(marker.get_id()));
        marker_detail_name_tv.setText(marker.get_name());
        if (marker.get_iset() != null) {
            marker_detail_iset_tv.setText(marker.get_iset());
            marker_detail_fset_tv.setText(marker.get_fset());
            marker_detail_fset3_tv.setText(marker.get_fset3());
        }

        if (marker.get_image().contains("ARManager")) {                //means local marker
            marker_detail_download_bt.setVisibility(View.GONE);
            File imgFile = new File(marker.get_image());
            if (imgFile.exists()) {
                Picasso.with(this).load(imgFile).into(marker_detail_image);
            } else {
                Toast.makeText(this, "File not found!", Toast.LENGTH_SHORT).show();
            }
            marker_detail_status_tv.setText("Available offline");
        } else {
            Picasso.with(this).load(APIDefine.baseURL + marker.get_image()).into(marker_detail_image);
            marker_detail_delete_bt.setVisibility(View.GONE);
            marker_detail_copy_bt.setVisibility(View.GONE);
        }

        marker_detail_iset_tv.setSelected(true);
        marker_detail_fset_tv.setSelected(true);
        marker_detail_fset3_tv.setSelected(true);
    }

    @OnClick(R.id.marker_detail_copy_bt)
    void copyMarkerToCache() {
        CacheHelper cacheHelper = CacheHelper.getInstance();
        cacheHelper.cacheDataNFT(this, marker.get_name());
    }

    @OnClick(R.id.marker_detail_delete_bt)
    void deleteMarker() {
        new AlertDialog.Builder(this)
                .setTitle("Delete marker")
                .setMessage("Are you sure you want to delete this marker?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        getPresenter().deleteMarker(marker);


                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @OnClick(R.id.marker_detail_download_bt)
    public void downloadMarker() {

        marker_detail_download_bt.setText("Cancel");
        marker_detail_download_iset_pb.setVisibility(View.VISIBLE);
        marker_detail_download_fset_pb.setVisibility(View.VISIBLE);
        marker_detail_download_fset3_pb.setVisibility(View.VISIBLE);
        downloadIset();

    }

    void downloadIset() {
        Ion.with(this)
                .load(APIDefine.baseURL + marker.get_iset())
                // have a ProgressBar get updated automatically with the percent
                .progressBar(marker_detail_download_iset_pb)
                // can also use a custom callback
                .progress(new ProgressCallback() {
                    @Override
                    public void onProgress(long downloaded, long total) {
                        marker_detail_download_iset_pb.setProgress((int) (100 * downloaded / total));
                    }
                })
                .write(new File(VarUtils.PATH_AR + File.separator + marker.get_name() + ".iset"))
                .setCallback(new FutureCallback<File>() {
                    @Override
                    public void onCompleted(Exception e, File file) {
                        // download done...
                        marker_detail_iset_tv.setVisibility(View.VISIBLE);
                        marker_detail_iset_tv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                        marker_detail_download_iset_pb.setVisibility(View.GONE);
                        downloadFset();
                    }
                });
    }

    void downloadFset() {
        Ion.with(this)
                .load(APIDefine.baseURL + marker.get_fset())
                // have a ProgressBar get updated automatically with the percent
                .progressBar(marker_detail_download_fset_pb)
                // can also use a custom callback
                .progress(new ProgressCallback() {
                    @Override
                    public void onProgress(long downloaded, long total) {
                        marker_detail_download_fset_pb.setProgress((int) (100 * downloaded / total));
                    }
                })
                .write(new File(VarUtils.PATH_AR + File.separator + marker.get_name() + ".fset"))
                .setCallback(new FutureCallback<File>() {
                    @Override
                    public void onCompleted(Exception e, File file) {
                        // download done...
                        marker_detail_fset_tv.setVisibility(View.VISIBLE);
                        marker_detail_fset_tv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                        marker_detail_download_fset_pb.setVisibility(View.GONE);
                        downloadFset3();
                    }
                });
    }

    void downloadFset3() {
        Ion.with(this)
                .load(APIDefine.baseURL + marker.get_fset3())
                // have a ProgressBar get updated automatically with the percent
                .progressBar(marker_detail_download_fset3_pb)
                // can also use a custom callback
                .progress(new ProgressCallback() {
                    @Override
                    public void onProgress(long downloaded, long total) {
                        marker_detail_download_fset3_pb.setProgress((int) (100 * downloaded / total));
                    }
                })
                .write(new File(VarUtils.PATH_AR + File.separator + marker.get_name() + ".fset3"))
                .setCallback(new FutureCallback<File>() {
                    @Override
                    public void onCompleted(Exception e, File file) {
                        // download done...
                        marker_detail_fset3_tv.setVisibility(View.VISIBLE);
                        marker_detail_fset3_tv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                        marker_detail_download_fset3_pb.setVisibility(View.GONE);
                        downloadImage();
                    }
                });
    }

    void downloadImage() {
        Ion.with(this)
                .load(APIDefine.baseURL + marker.get_image())
                // have a ProgressBar get updated automatically with the percent
                .progressBar(marker_detail_download_fset3_pb)
                // can also use a custom callback
                .progress(new ProgressCallback() {
                    @Override
                    public void onProgress(long downloaded, long total) {
                        marker_detail_download_fset3_pb.setProgress((int) (100 * downloaded / total));
                    }
                })
                .write(new File(VarUtils.PATH_AR + File.separator + marker.get_name() + ".jpg"))
                .setCallback(new FutureCallback<File>() {
                    @Override
                    public void onCompleted(Exception e, File file) {
                        // download done...

                        marker_detail_download_bt.setText("Download");
                        marker_detail_download_bt.setEnabled(false);
                        marker_detail_status_tv.setText("Downloaded");
                    }
                });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_marker_detail;
    }

    @Override
    public MarkerDetailPresenter onCreatePresenter() {
        return new MarkerDetailPresenterImpl(this);
    }

    @Override
    public void onDeleteMarkerSuccess() {
        finish();
    }
}
