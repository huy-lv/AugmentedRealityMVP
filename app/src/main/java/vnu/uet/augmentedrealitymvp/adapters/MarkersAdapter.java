package vnu.uet.augmentedrealitymvp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import vnu.uet.augmentedrealitymvp.R;
import vnu.uet.augmentedrealitymvp.app.APIDefine;
import vnu.uet.augmentedrealitymvp.app.ArApplication;
import vnu.uet.augmentedrealitymvp.helper.MarkerDetailDialog;
import vnu.uet.augmentedrealitymvp.model.Marker;

/**
 * Created by hienbx94 on 3/21/16.
 */
public class MarkersAdapter extends RecyclerView.Adapter<MarkersAdapter.MyViewHolder> {

    private List<Marker> markersList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, create_at, user_name;
        public NetworkImageView image;
        public RelativeLayout relativeLayout;
        public MyViewHolder(View view) {
            super(view);
            relativeLayout = (RelativeLayout)view.findViewById(R.id.item_marker_rl);
            name = (TextView) view.findViewById(R.id.item_marker_name_tv);
            create_at = (TextView) view.findViewById(R.id.item_marker_create_at_tv);
            image = (NetworkImageView) view.findViewById(R.id.item_marker_image_iv);
            user_name = (TextView) view.findViewById(R.id.item_marker_user_name_tv);
        }
    }


    public MarkersAdapter(Context context,List<Marker> markersList) {
        this.markersList = markersList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_marker_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Marker marker = markersList.get(position);
        holder.name.setText(marker.get_name());
        holder.create_at.setText(marker.getCreatedAt());
        holder.user_name.setText(marker.getUserName());
        String image_link = APIDefine.baseURL + marker.get_image();
        holder.image.setImageUrl(image_link, ArApplication.getInstance().getImageLoader());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MarkerDetailDialog markerDetailDialog = new MarkerDetailDialog(context, marker);
                markerDetailDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return markersList.size();
    }
}
