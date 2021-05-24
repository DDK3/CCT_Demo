package com.dipesh.cct_demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dipesh.cct_demo.R;
import com.dipesh.cct_demo.db.entity.LocationInfo;
import com.dipesh.cct_demo.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class LocationInfoListAdapter extends RecyclerView.Adapter<LocationInfoListAdapter.ViewHolder> {
    private Context context;
    private List<LocationInfo> LocationInfoList = new ArrayList<>();
    private LocationInfoListAdapterListener locationInfoListAdapterListener;

    public LocationInfoListAdapter(Context context, List<LocationInfo> locationInfoList, LocationInfoListAdapterListener locationInfoListAdapterListener) {
        this.context = context;
        this.LocationInfoList = locationInfoList;
        this.locationInfoListAdapterListener = locationInfoListAdapterListener;
    }

    @NonNull
    @Override
    public LocationInfoListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.location_info_item_view, parent, false);
        LocationInfoListAdapter.ViewHolder vh = new LocationInfoListAdapter.ViewHolder(v, parent.getContext());
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull LocationInfoListAdapter.ViewHolder holder, int position) {

        holder.mNote.setText(LocationInfoList.get(position).getLocationNote());

        holder.mDate.setText(CommonUtils.getDayMonthYearFromMiliseconds(LocationInfoList.get(position).getTimestamp()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationInfoListAdapterListener.onLocationInfoClick(LocationInfoList.get(position));
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                locationInfoListAdapterListener.onLocationUpdateInfoClick(LocationInfoList.get(position));
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return LocationInfoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mNote, mDate;

        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            mNote = itemView.findViewById(R.id.note);
            mDate = itemView.findViewById(R.id.date);

        }
    }
}
