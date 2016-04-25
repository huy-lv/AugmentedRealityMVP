package vnu.uet.augmentedrealitymvp.screen.login;

import vnu.uet.augmentedrealitymvp.base.BaseView;

/**
 * Created by huylv on 22-Apr-16.
 */
public interface LoginView extends BaseView<LoginPresenter> {
    void onLoginSuccess();
    void onLoginError(String errorMessage);
    void onEmptyEmail();
}
