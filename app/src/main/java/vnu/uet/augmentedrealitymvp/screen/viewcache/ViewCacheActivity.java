package vnu.uet.augmentedrealitymvp.screen.viewcache;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import vnu.uet.augmentedrealitymvp.common.Constants;

/**
 * Created by huylv on 07-May-16.
 */
public class ViewCacheActivity extends ListActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<String> values = getIntent().getStringArrayListExtra(Constants.KEY_INTENT_FILES_LIST);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
    }
}
