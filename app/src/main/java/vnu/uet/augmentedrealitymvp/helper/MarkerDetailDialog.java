package vnu.uet.augmentedrealitymvp.helper;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import butterknife.ButterKnife;
import butterknife.OnClick;
import vnu.uet.augmentedrealitymvp.R;
import vnu.uet.augmentedrealitymvp.app.APIDefine;
import vnu.uet.augmentedrealitymvp.common.util.VarUtils;
import vnu.uet.augmentedrealitymvp.model.Marker;

/**
 * Created by huylv on 28-Mar-16.
 */
public class MarkerDetailDialog extends Dialog {
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
    @Bind(R.id.marker_detail_ok_bt)
    Button marker_detail_ok_bt;

    @Bind(R.id.marker_detail_download_iset_pb)
    ProgressBar marker_detail_download_iset_pb;
    @Bind(R.id.marker_detail_download_fset_pb)
    ProgressBar marker_detail_download_fset_pb;
    @Bind(R.id.marker_detail_download_fset3_pb)
    ProgressBar marker_detail_download_fset3_pb;

    Marker marker;
    Context context;
    boolean IS_DOWNLOADING;

    public MarkerDetailDialog(Context c, Marker m) {
        super(c);
        context = c;
        marker = m;
        setContentView(R.layout.dialog_marker_detail);
        ButterKnife.bind(this);

        IS_DOWNLOADING = false;
        setTitle("Marker Detail");

        marker_detail_id_tv.setText(String.valueOf(marker.get_id()));
        marker_detail_name_tv.setText(marker.get_name());
        if (marker.get_iset() != null) {
            marker_detail_iset_tv.setText(marker.get_iset());
            marker_detail_fset_tv.setText(marker.get_fset());
            marker_detail_fset3_tv.setText(marker.get_fset3());
        }

        if (marker.get_image().contains("ARManager")) {
            marker_detail_download_bt.setVisibility(View.GONE);
            File imgFile = new File(marker.get_image());
            if (imgFile.exists()) {
                Picasso.with(context).load(imgFile).into(marker_detail_image);
            } else {
                Toast.makeText(context, "File not found!", Toast.LENGTH_SHORT).show();
            }
            marker_detail_status_tv.setText("Available offline");
        } else {
            Picasso.with(context).load(APIDefine.baseURL + marker.get_image()).into(marker_detail_image);
            marker_detail_delete_bt.setVisibility(View.GONE);
        }

        marker_detail_iset_tv.setSelected(true);
        marker_detail_fset_tv.setSelected(true);
        marker_detail_fset3_tv.setSelected(true);

    }

    @OnClick(R.id.marker_detail_delete_bt)
    void deleteMarker(){
        new AlertDialog.Builder(context)
                .setTitle("Delete marker")
                .setMessage("Are you sure you want to delete this marker?")
                .setPositiveButton(android.R.string.yes, new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //delete marker
                        File fIset = new File(marker.get_iset());
                        File fFset = new File(marker.get_fset());
                        File fFset3 = new File(marker.get_fset3());
                        File fImage = new File(marker.get_image());
                        if(fIset.exists()) fIset.delete();
                        if(fFset.exists()) fFset.delete();
                        if(fFset3.exists()) fFset3.delete();
                        if(fImage.exists()) fImage.delete();

                        MarkerDetailDialog.this.dismiss();
                        SQLiteHandler db = new SQLiteHandler(context);
                        db.deleteMarkersOnline(marker.get_id());
                        db.close();
                    }
                })
                .setNegativeButton(android.R.string.no, new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @OnClick(R.id.marker_detail_ok_bt)
    public void dismissDialog() {
        this.dismiss();
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
        Ion.with(context)
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
                .write(new File(VarUtils.PATH_AR +File.separator+ marker.get_name()+".iset"))
                .setCallback(new FutureCallback<File>() {
                    @Override
                    public void onCompleted(Exception e, File file) {
                        // download done...
                        marker_detail_iset_tv.setVisibility(View.VISIBLE);
                        marker_detail_iset_tv.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
                        marker_detail_download_iset_pb.setVisibility(View.GONE);
                        downloadFset();
                    }
                });
    }

    void downloadFset(){
        Ion.with(context)
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
                .write(new File(VarUtils.PATH_AR +File.separator+ marker.get_name()+".fset"))
                .setCallback(new FutureCallback<File>() {
                    @Override
                    public void onCompleted(Exception e, File file) {
                        // download done...
                        marker_detail_fset_tv.setVisibility(View.VISIBLE);
                        marker_detail_fset_tv.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
                        marker_detail_download_fset_pb.setVisibility(View.GONE);
                        downloadFset3();
                    }
                });
    }

    void downloadFset3(){
        Ion.with(context)
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
                .write(new File(VarUtils.PATH_AR +File.separator+ marker.get_name() + ".fset3"))
                .setCallback(new FutureCallback<File>() {
                    @Override
                    public void onCompleted(Exception e, File file) {
                        // download done...
                        marker_detail_fset3_tv.setVisibility(View.VISIBLE);
                        marker_detail_fset3_tv.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
                        marker_detail_download_fset3_pb.setVisibility(View.GONE);
                        downloadImage();
                    }
                });
    }

    void downloadImage(){
        Ion.with(context)
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
                .write(new File(VarUtils.PATH_AR +File.separator+ marker.get_name() + ".jpg"))
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

//    @Override
//    public void onInit(int status) {
//        if (status == TextToSpeech.SUCCESS) {
//
//            int result = tts.setLanguage(Locale.US);
//
//            if (result == TextToSpeech.LANG_MISSING_DATA
//                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
//                Log.e("TTS", "This Language is not supported");
//            } else {
//                marker_detail_ok_bt.setEnabled(true);
//                speakOut();
//            }
//
//        } else {
//            Log.e("TTS", "Initilization Failed!");
//        }
//    }

//    private void speakOut() {
//        String text = "white table";
//        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
//    }
}
