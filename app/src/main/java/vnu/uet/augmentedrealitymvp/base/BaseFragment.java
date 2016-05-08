package vnu.uet.augmentedrealitymvp.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import vnu.uet.augmentedrealitymvp.common.util.DialogUtils;

/**
 * Created by huylv on 22-Apr-16.
 */
public abstract class BaseFragment<T extends BasePresenter> extends Fragment implements BaseView<T> {
    private ProgressDialog mProgressDialog;
    private T mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Presenter for this view
        mPresenter = onCreatePresenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(getLayoutId(), null);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onPrepareLayout() {
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void onRequestError(String errorMessage) {
        showErrorDialog(errorMessage);
    }

    @Override
    public void showProgress() {
        if (mProgressDialog != null && !mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }
    @Override
    public void showProgress(String title){
        if (mProgressDialog != null && !mProgressDialog.isShowing()) {
            mProgressDialog.setTitle(title);
            mProgressDialog.show();
        }
    }
    @Override
    public void hideProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
    @Override
    public void showErrorDialog(String message){
        DialogUtils.showErrorAlert(getContext(), message);
    }

    @Override
    public Context getContext(){
        return getActivity();
    }

    @Override
    public T getPresenter() {
        return mPresenter;
    }

    protected abstract int getLayoutId();

}

