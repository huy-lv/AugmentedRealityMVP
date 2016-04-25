package vnu.uet.augmentedrealitymvp.screen.fragment.profile;

import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import vnu.uet.augmentedrealitymvp.helper.SessionManager;

/**
 * Created by huylv on 23-Apr-16.
 */
public class ProfileFragment extends Fragment {
    private SessionManager session;
    private TextView name, email;
    public NetworkImageView img_avatar;

    public ProfileFragment() {
    }
}
