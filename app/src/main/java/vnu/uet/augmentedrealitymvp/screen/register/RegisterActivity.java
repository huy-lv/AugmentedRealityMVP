package vnu.uet.augmentedrealitymvp.screen.register;

import vnu.uet.augmentedrealitymvp.R;
import vnu.uet.augmentedrealitymvp.base.BaseActivity;

/**
 * Created by huylv on 22-Apr-16.
 */
public class RegisterActivity extends BaseActivity<RegisterPresenter> implements  RegisterView{
    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void onRegisterSuccess() {

    }

    @Override
    public RegisterPresenter onCreatePresenter() {
        return null;
    }
}
