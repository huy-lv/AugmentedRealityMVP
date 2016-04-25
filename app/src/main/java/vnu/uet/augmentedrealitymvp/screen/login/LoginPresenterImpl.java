package vnu.uet.augmentedrealitymvp.screen.login;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import vnu.uet.augmentedrealitymvp.app.APIDefine;
import vnu.uet.augmentedrealitymvp.app.ArApplication;
import vnu.uet.augmentedrealitymvp.helper.SessionManager;

/**
 * Created by huylv on 22-Apr-16.
 */
public class LoginPresenterImpl implements LoginPresenter {
    private LoginView view;

    public LoginPresenterImpl(LoginView v) {
        view = v;
    }

    @Override
    public void doLogin(final String email, final String password) {
        if (!email.isEmpty() && !password.isEmpty()) {

            // Tag used to cancel the request
            String tag_string_req = "req_login";

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    APIDefine.URL_LOGIN, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jObj = new JSONObject(response);
                        if (!jObj.has("errors")) {
                            SessionManager session = new SessionManager(view.getContext());
                            session.setLogin(true);

                            session.addValue("id", String.valueOf(jObj.getInt("id")));
                            session.addValue("name", jObj.getString("name"));
                            session.addValue("email", jObj.getString("email"));
                            session.addValue("auth_token", jObj.getString("auth_token"));

                            view.onLoginSuccess();
                        } else {
                            String errors = jObj.getString("errors");
                            view.onLoginError(errors);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        view.onLoginError(e.getMessage());
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    view.onLoginError(error.getMessage());
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("email", email);
                    params.put("password", password);
                    return params;
                }

            };

            ArApplication.getInstance().addToRequestQueue(strReq, tag_string_req);

        } else {
            view.onEmptyEmail();
        }
    }
}
