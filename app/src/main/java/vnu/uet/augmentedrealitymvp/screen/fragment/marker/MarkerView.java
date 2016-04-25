package vnu.uet.augmentedrealitymvp.screen.fragment.marker;

import java.util.List;

import vnu.uet.augmentedrealitymvp.base.BaseView;
import vnu.uet.augmentedrealitymvp.model.Marker;

/**
 * Created by huylv on 23-Apr-16.
 */
public interface MarkerView extends BaseView<MarkerPresenter>{
    void onGetAllMarkersSuccess(List<Marker> markers);

    void onCheckLocalSuccess(List<Marker> markerList);
}
