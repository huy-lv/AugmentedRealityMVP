package vnu.uet.augmentedrealitymvp.screen.crop;

import vnu.uet.augmentedrealitymvp.base.BasePresenter;
import vnu.uet.augmentedrealitymvp.helper.SessionManager;

/**
 * Created by huylv on 24-Apr-16.
 */
public interface CropPresenter extends BasePresenter{
    void uploadMarker(String name, String encoded, SessionManager session);
}
