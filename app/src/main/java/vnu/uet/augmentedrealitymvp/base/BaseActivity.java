package vnu.uet.augmentedrealitymvp.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import vnu.uet.augmentedrealitymvp.common.util.DialogUtils;

/**
 * Created by huylv on 22-Apr-16.
 */
public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity implements BaseView<T> {
    private ProgressDialog mProgressDialog;
    private T mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        ButterKnife.bind(this);
        onPrepareLayout();
        mPresenter = onCreatePresenter();
    }

    @Override
    public T getPresenter() {
        return mPresenter;
    }

    @Override
    public void onPrepareLayout() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
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
        DialogUtils.showErrorAlert(this,message);
    }

    @Override
    public Context getContext(){
        return this;
    }


    @Override
    public void onRequestError(String errorMessage) {
        DialogUtils.showErrorAlert(this, errorMessage);
    }


    /**
     * Return layout resource id for activity
     */
    protected abstract int getLayoutId();
}
