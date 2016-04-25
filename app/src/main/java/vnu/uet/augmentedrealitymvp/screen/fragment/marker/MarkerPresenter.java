package vnu.uet.augmentedrealitymvp.screen.fragment.marker;

import vnu.uet.augmentedrealitymvp.base.BasePresenter;

/**
 * Created by huylv on 23-Apr-16.
 */
public interface MarkerPresenter extends BasePresenter {
    void getAllMarkers();

    void checkLocalMarker();
}
