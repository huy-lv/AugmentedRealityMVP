package vnu.uet.augmentedrealitymvp.screen.crop;

import android.content.ContentResolver;
import android.net.Uri;

import java.io.FileNotFoundException;

import vnu.uet.augmentedrealitymvp.base.BasePresenter;
import vnu.uet.augmentedrealitymvp.helper.SessionManager;

/**
 * Created by huylv on 24-Apr-16.
 */
public interface CropPresenter extends BasePresenter{
    void uploadMarker(String name, String encoded, SessionManager session);

    void encodeImage(ContentResolver contentResolver, Uri uriImage) throws FileNotFoundException;
}
