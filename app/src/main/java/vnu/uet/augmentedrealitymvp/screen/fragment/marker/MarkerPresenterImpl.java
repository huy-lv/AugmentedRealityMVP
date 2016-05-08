package vnu.uet.augmentedrealitymvp.screen.fragment.marker;

import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import vnu.uet.augmentedrealitymvp.app.APIDefine;
import vnu.uet.augmentedrealitymvp.app.ArApplication;
import vnu.uet.augmentedrealitymvp.helper.CheckLocalMarker;
import vnu.uet.augmentedrealitymvp.helper.SQLiteHandler;
import vnu.uet.augmentedrealitymvp.model.Marker;

/**
 * Created by huylv on 23-Apr-16.
 */
public class MarkerPresenterImpl implements MarkerPresenter {
    private MarkerView view;
    private List<Marker> markerList = new ArrayList<>();

    public MarkerPresenterImpl(MarkerView v) {
        view = v;
    }

    @Override
    public void checkLocalMarker() {
        //check local marker
        CheckLocalMarker clm = new CheckLocalMarker(view.getContext());
        clm.execute();
        try {

            switch (clm.get()) {
                case 1:
                    Toast.makeText(view.getContext(), "success", Toast.LENGTH_SHORT).show();
                    markerList.clear();
                    SQLiteHandler db = new SQLiteHandler(view.getContext());
                    markerList.addAll(db.getAllMarkersOffline());
                    db.close();
                    view.onCheckLocalSuccess(markerList);
                    break;
                default:
                    view.onRequestError("check error");
                    break;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void getAllMarkers() {
        String tag_string_req = "get_all_marker";

        StringRequest strReq = new StringRequest(Request.Method.GET,
                APIDefine.URL_MARKERS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray markers = jObj.getJSONArray("markers");
                    markerList.clear();
                    for(int i = 0; i < markers.length(); i++) {
                        JSONObject marker = (JSONObject) markers.get(i);
                        Integer id = marker.getInt("id");
                        String name = marker.getString("name");
                        String image = marker.getString("image");
                        String iset = marker.getString("iset");
                        String fset = marker.getString("fset");
                        String fset3 = marker.getString("fset3");
                        String created_at = marker.getString("created_at");
                        String user_name = marker.getString("user_name");
                        Marker temp = new Marker(id, name, image, iset, fset, fset3, created_at, user_name);
                        markerList.add(temp);
                    }
                    view.onGetAllMarkersSuccess(markerList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                view.onRequestError(error.getMessage());
            }
        });

        ArApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
