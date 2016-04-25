package vnu.uet.augmentedrealitymvp.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import vnu.uet.augmentedrealitymvp.R;

/**
 * Created by huylv on 22-Apr-16.
 */
public abstract class BaseActivityToolbar <T extends BasePresenter> extends BaseActivity<T> implements BaseView<T>{

    @Nullable
    Toolbar toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        toolBar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
    }
}
