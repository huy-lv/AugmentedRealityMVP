package vnu.uet.augmentedrealitymvp.screen.crop;

import com.android.volley.AuthFailureError;
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
import vnu.uet.augmentedrealitymvp.model.Marker;

/**
 * Created by huylv on 24-Apr-16.
 */
public class CropPresenterImpl implements CropPresenter {
    private CropView view;
    public CropPresenterImpl(CropView v){view = v;}

    @Override
    public void uploadMarker(final String name, final String encoded, final SessionManager session) {
        // Tag used to cancel the request
        String tag_string_req = "create_marker";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                APIDefine.URL_CREATE_MARKER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    if (!jObj.has("errors")) {
                        JSONObject marker = jObj.getJSONObject("marker");
                        Integer id = marker.getInt("id");
                        String name = marker.getString("name");
                        String image = marker.getString("image");
                        String iset = marker.getString("iset");
                        String fset = marker.getString("fset");
                        String fset3 = marker.getString("fset3");
                        String created_at = marker.getString("created_at");
                        String user_name = marker.getString("user_name");
                        Marker temp = new Marker(id, name, image, iset, fset, fset3, created_at, user_name);

                        view.onUploadSuccess();
                    } else {
                        String errors = jObj.getString("errors");
                        view.onRequestError(errors);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.onRequestError(e.getMessage());
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                view.onRequestError(error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("base64", encoded);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Authorization", session.getValue("auth_token"));
                return map;
            }
        };

        ArApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
