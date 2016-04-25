package vnu.uet.augmentedrealitymvp.screen.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.OnClick;
import vnu.uet.augmentedrealitymvp.R;
import vnu.uet.augmentedrealitymvp.base.BaseActivity;
import vnu.uet.augmentedrealitymvp.screen.main.MainActivity;
import vnu.uet.augmentedrealitymvp.screen.register.RegisterActivity;

/**
 * Created by huylv on 22-Apr-16.
 */
public class LoginActivity extends BaseActivity<LoginPresenter> implements  LoginView {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    @Bind(R.id.login_link_to_register_bt)
    Button btnLinkToRegister;
    @Bind(R.id.login_login_bt)
    Button btnLogin;
    @Bind(R.id.login_email_et)
    EditText login_email_et;
    @Bind(R.id.login_password_et)
    EditText login_password_et;

    ProgressDialog pDialog;
    @OnClick(R.id.login_link_to_register_bt)
    void openRegister(){
        Intent i = new Intent(getApplicationContext(),RegisterActivity.class);
        startActivity(i);
        finish();
    }
    @OnClick(R.id.login_login_bt)
    void btLoginClick(){
        login_email_et.setText("huylv@gmail.com");
        login_password_et.setText("321ewqdsa");
        String email = login_email_et.getText().toString().trim();
        String password = login_password_et.getText().toString().trim();
        getPresenter().doLogin(email, password);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
    }

    @Override
    public void onLoginSuccess() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onLoginError(String errorMessage) {
        showErrorDialog(errorMessage);
    }

    @Override
    public void onEmptyEmail() {
        showErrorDialog("Please enter the email and password!");
    }

    @Override
    public LoginPresenter onCreatePresenter() {
        return new LoginPresenterImpl(this);
    }
}
