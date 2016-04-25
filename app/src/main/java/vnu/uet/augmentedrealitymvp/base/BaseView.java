package vnu.uet.augmentedrealitymvp.base;

import android.content.Context;

/**
 * Created by huylv on 22-Apr-16.
 */
public interface BaseView<P extends BasePresenter> {
    void showProgress();
    void hideProgress();
    void showProgress(String title);
    void showErrorDialog(String message);

    Context getContext();
    void onPrepareLayout();

    P getPresenter();
    P onCreatePresenter();

    void onRequestError(String errorMessage);
}
