package vnu.uet.augmentedrealitymvp.screen.markerdetail;

import android.content.Intent;

import com.google.gson.Gson;

import java.io.File;

import vnu.uet.augmentedrealitymvp.common.Constants;
import vnu.uet.augmentedrealitymvp.helper.CacheHelper;
import vnu.uet.augmentedrealitymvp.helper.SQLiteHandler;
import vnu.uet.augmentedrealitymvp.model.Marker;

/**
 * Created by huylv on 27-Apr-16.
 */
public class MarkerDetailPresenterImpl implements MarkerDetailPresenter {
    private MarkerDetailView view;

    public MarkerDetailPresenterImpl(MarkerDetailView v) {
        view = v;
    }

    @Override
    public void deleteMarker(Marker marker) {
        //delete marker
        File fIset = new File(marker.get_iset());
        File fFset = new File(marker.get_fset());
        File fFset3 = new File(marker.get_fset3());
        File fImage = new File(marker.get_image());
        if (fIset.exists()) fIset.delete();
        if (fFset.exists()) fFset.delete();
        if (fFset3.exists()) fFset3.delete();
        if (fImage.exists()) fImage.delete();

        SQLiteHandler db = new SQLiteHandler(view.getContext());
        db.deleteMarkersOnline(marker.get_id());
        db.close();

        CacheHelper cacheHelper = CacheHelper.getInstance();
        cacheHelper.deleteMarker(view.getContext(), marker.get_name());
        view.onDeleteMarkerSuccess();

    }

    @Override
    public Marker getMarkerFromJson(Intent i) {
        Gson gson = new Gson();
        return gson.fromJson(i.getStringExtra(Constants.KEY_INTENT_MARKER_OBJECT), Marker.class);
    }

}
