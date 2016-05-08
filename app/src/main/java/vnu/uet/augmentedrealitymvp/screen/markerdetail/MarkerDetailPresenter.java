package vnu.uet.augmentedrealitymvp.screen.markerdetail;

import android.content.Intent;

import vnu.uet.augmentedrealitymvp.base.BasePresenter;
import vnu.uet.augmentedrealitymvp.model.Marker;

/**
 * Created by huylv on 27-Apr-16.
 */
public interface MarkerDetailPresenter extends BasePresenter {
    void deleteMarker(Marker marker);

    Marker getMarkerFromJson(Intent i);
}
