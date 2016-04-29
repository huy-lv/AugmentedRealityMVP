package vnu.uet.augmentedrealitymvp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import vnu.uet.augmentedrealitymvp.R;
import vnu.uet.augmentedrealitymvp.app.APIDefine;
import vnu.uet.augmentedrealitymvp.common.Constants;
import vnu.uet.augmentedrealitymvp.model.Marker;
import vnu.uet.augmentedrealitymvp.screen.markerdetail.MarkerDetailActivity;

/**
 * Created by hienbx94 on 3/21/16.
 */
public class MarkersAdapter extends RecyclerView.Adapter<MarkersAdapter.MyViewHolder> {

    private List<Marker> markersList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, create_at, user_name;
        public ImageView image;
        public RelativeLayout relativeLayout;
        public MyViewHolder(View view) {
            super(view);
            relativeLayout = (RelativeLayout)view.findViewById(R.id.item_marker_rl);
            name = (TextView) view.findViewById(R.id.item_marker_name_tv);
            create_at = (TextView) view.findViewById(R.id.item_marker_create_at_tv);
            image = (ImageView) view.findViewById(R.id.item_marker_image_iv);
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
        if(marker.get_image().contains("ARManager")){
            File imgFile = new  File(marker.get_image());
            if(imgFile.exists()){
                Picasso.with(context).load(imgFile).into(holder.image);
            }
        }else{
            String image_link = APIDefine.baseURL + marker.get_image();
            Picasso.with(context).load(image_link).into(holder.image);
        }
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, MarkerDetailActivity.class);
                Gson gson = new Gson();
                String json = gson.toJson(marker);
                i.putExtra(Constants.KEY_INTENT_MARKER_OBJECT,json);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return markersList.size();
    }
}
