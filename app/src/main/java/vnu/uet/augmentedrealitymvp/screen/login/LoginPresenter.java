package vnu.uet.augmentedrealitymvp.screen.login;

import vnu.uet.augmentedrealitymvp.base.BasePresenter;

/**
 * Created by huylv on 22-Apr-16.
 */
public interface LoginPresenter extends BasePresenter{
    void doLogin(String email,String password);
}
