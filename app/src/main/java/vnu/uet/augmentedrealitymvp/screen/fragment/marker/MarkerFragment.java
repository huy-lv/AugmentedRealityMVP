package vnu.uet.augmentedrealitymvp.screen.fragment.marker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import vnu.uet.augmentedrealitymvp.R;
import vnu.uet.augmentedrealitymvp.adapters.MarkersAdapter;
import vnu.uet.augmentedrealitymvp.base.BaseFragment;
import vnu.uet.augmentedrealitymvp.common.Constants;
import vnu.uet.augmentedrealitymvp.common.VerticalSpaceItemDecoration;
import vnu.uet.augmentedrealitymvp.common.util.VarUtils;
import vnu.uet.augmentedrealitymvp.model.Marker;
import vnu.uet.augmentedrealitymvp.screen.crop.CropActivity;

/**
 * Created by huylv on 23-Apr-16.
 */
public class MarkerFragment extends BaseFragment<MarkerPresenter> implements MarkerView {

    @Bind(R.id.markers_recycler_view)
    RecyclerView marker_list_recycler_view;
    @Bind(R.id.swipeRefreshLayoutMarkerOnline)
    SwipeRefreshLayout swipeRefreshLayoutMarkerOnline;
    @Bind(R.id.fab_pick_photo)
    FloatingActionButton fabPickPhoto;
    @Bind(R.id.fab_take_photo)
    FloatingActionButton fabTakePhoto;
    @Bind(R.id.fam)
    FloatingActionMenu floatingActionMenu;

    @Bind(R.id.marker_list_switch_online)
    Button marker_list_switch_online;

    @Bind(R.id.marker_list_switch_offline)
    Button marker_list_switch_offline;
    boolean FIRST_TAB = true;
    private MarkersAdapter adapterOnline;
    private MarkersAdapter adapterOffline;
    private List<Marker> markerListOnline = new ArrayList<>();
    private List<Marker> markerListOffline = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        if (swipeRefreshLayoutMarkerOnline.isRefreshing())
            swipeRefreshLayoutMarkerOnline.setRefreshing(false);

        marker_list_recycler_view.addItemDecoration(new VerticalSpaceItemDecoration(10));
        adapterOnline = new MarkersAdapter(getContext(), markerListOnline);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        marker_list_recycler_view.setLayoutManager(mLayoutManager);
        marker_list_recycler_view.setItemAnimator(new DefaultItemAnimator());
        marker_list_recycler_view.setAdapter(adapterOnline);
        adapterOnline.notifyDataSetChanged();
        showProgress("Getting markers...");
        getPresenter().getAllMarkers();

        adapterOffline = new MarkersAdapter(getContext(), markerListOffline);
        getPresenter().checkLocalMarker();

        swipeRefreshLayoutMarkerOnline.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (FIRST_TAB) {
                    getPresenter().getAllMarkers();
                } else {
                    getPresenter().checkLocalMarker();
                }
            }
        });
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            Bitmap bm = null;
            if (requestCode == Constants.REQUEST_CAMERA) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                //Write file
                String filename = System.currentTimeMillis() + ".jpg";
                File newFile = new File(VarUtils.PATH_AR, filename);
                if (newFile.exists()) newFile.delete();
                try {
                    FileOutputStream out = new FileOutputStream(newFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Intent i = new Intent(getActivity(), CropActivity.class);
                i.putExtra(Constants.KEY_ACTIVITY_RESULT_TYPE, Constants.REQUEST_CAMERA);
                i.putExtra(Constants.KEY_ACTIVITY_RESULT_DATA, filename);
                startActivity(i);
            } else if (requestCode == Constants.SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                Intent i = new Intent(getActivity(), CropActivity.class);
                i.putExtra(Constants.KEY_ACTIVITY_RESULT_TYPE, Constants.SELECT_FILE);
                i.putExtra(Constants.KEY_ACTIVITY_RESULT_DATA, selectedImageUri.toString());
                startActivity(i);
            }
        }
    }

    @OnClick(R.id.marker_list_switch_online)
    void switchToOnline() {
        if (!FIRST_TAB) {
            marker_list_recycler_view.setAdapter(adapterOnline);
            marker_list_switch_online.setBackgroundColor(Color.parseColor("#00C431"));
            marker_list_switch_offline.setBackgroundColor(Color.parseColor("#7BFF00"));
            FIRST_TAB = true;
        }
    }

    @OnClick(R.id.marker_list_switch_offline)
    void switchToOffline() {
        if (FIRST_TAB) {
            marker_list_recycler_view.setAdapter(adapterOffline);
            marker_list_switch_online.setBackgroundColor(Color.parseColor("#7BFF00"));
            marker_list_switch_offline.setBackgroundColor(Color.parseColor("#00C431"));
            FIRST_TAB = false;
        }
    }

    @OnClick(R.id.fab_pick_photo)
    void pickPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        floatingActionMenu.close(true);
        startActivityForResult(Intent.createChooser(intent, "Select File"), Constants.SELECT_FILE);
    }

    @OnClick(R.id.fab_take_photo)
    void takePhoto() {

//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        floatingActionMenu.close(true);
//        startActivityForResult(intent, Constants.REQUEST_CAMERA);
        String pictureImagePath = "";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        pictureImagePath = storageDir.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg";
        File file = new File(pictureImagePath);
        Uri outputFileUri = Uri.fromFile(file);
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(cameraIntent, 1);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_marker;
    }

    @Override
    public MarkerPresenter onCreatePresenter() {
        return new MarkerPresenterImpl(this);
    }

    @Override
    public void onGetAllMarkersSuccess(List<Marker> markers) {
        swipeRefreshLayoutMarkerOnline.setRefreshing(false);
        markerListOnline.clear();
        markerListOnline.addAll(markers);
        adapterOnline.notifyDataSetChanged();
    }

    @Override
    public void onCheckLocalSuccess(List<Marker> markerList) {
        swipeRefreshLayoutMarkerOnline.setRefreshing(false);
        markerListOffline.clear();
        markerListOffline.addAll(markerList);
        adapterOffline.notifyDataSetChanged();
    }
}
