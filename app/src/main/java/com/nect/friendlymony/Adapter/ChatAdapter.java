package com.nect.friendlymony.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nect.friendlymony.Model.Chat.ChatModel;
import com.nect.friendlymony.R;
import com.nect.friendlymony.Utils.HawkAppUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> implements OnMapReadyCallback {

    public static List<ChatModel> listChat;
    private static View view;
    Activity context;
    String uid;

    public ChatAdapter(Activity context1, List<ChatModel> listChat) {

        this.listChat = listChat;
        this.context = context1;
        uid = HawkAppUtils.getInstance().getUSERDATA().getData().getId();
    }


    @NonNull
    @Override
    public ChatAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Log.e("return viewType", viewType + "");

        try {
            if (view != null) {
                parent = (ViewGroup) view.getParent();
                if (parent != null)
                    parent.removeView(view);
            }
            if (viewType == 0) {
                if (parent != null)
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_my, null);
            } else {
                if (parent != null)
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_oppo, null);
            }
        } catch (InflateException e) {
            /* map is already there, just return view as it is */
        }

        return new MyViewHolder(view);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.MyViewHolder holder, final int position) {

        holder.layout.setTag(this);
        holder.tvMessage.setText(listChat.get(position).getMessage());

        try {
            long milliSecond = Long.parseLong(listChat.get(position).getTimeStamp());
            DateFormat formatter = new SimpleDateFormat("hh:mma");
            String text = formatter.format(new Date(milliSecond));
            holder.tvTime.setText(text);

            if (listChat.get(position).isImageAvailable()) {
                holder.lyrMessage.setVisibility(View.GONE);
                holder.ivImage.setVisibility(View.VISIBLE);
                holder.llAddress.setVisibility(View.GONE);
                holder.mapView.setVisibility(View.GONE);
                //if (listChat.get(position).isImageSuccess()) {
                Glide.with(context).load(listChat.get(position).getImageUrl())
                        .thumbnail(0.5f).apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.appicon).dontAnimate())
                        .into(holder.ivImage);
                //}
            } else if (listChat.get(position).isLocationAvailable()) {
                holder.lyrMessage.setVisibility(View.GONE);
                holder.ivImage.setVisibility(View.GONE);
                holder.llAddress.setVisibility(View.VISIBLE);
                holder.mapView.setVisibility(View.VISIBLE);


            /*holder.tvAddress.setText(listChat.get(position).getMessage());
            String latLong = listChat.get(position).getLatitude() + " , " + listChat.get(position).getLongitude();
            holder.tvLatlong.setText(latLong);*/

            /*MapFragment mapFragment = (MapFragment) context.getFragmentManager()
                    .findFragmentById(R.id.mapView);
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.parseDouble(listChat.get(position).getLatitude()), Double.parseDouble(listChat.get(position).getLongitude()))));
                }
            });*/

            /*FrameLayout view = (FrameLayout) (holder).map_layout;
            FrameLayout frame = new FrameLayout(context);
            frame.setId(1010101); //you have to set unique id

            int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 170, context.getResources().getDisplayMetrics());
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, height);
            frame.setLayoutParams(layoutParams);

            view.addView(frame);

            GoogleMapOptions options = new GoogleMapOptions();
            options.liteMode(true);
            SupportMapFragment mapFrag = SupportMapFragment.newInstance(options);

            //Create the the class that implements OnMapReadyCallback and set up your map
            mapFrag.getMapAsync(new MapReadyCallback(position));

            FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
            manager.beginTransaction().replace(frame.getId(), mapFrag).addToBackStack(null).commit();*/

                holder.mapView.setTag(position);
                if (holder.map == null) return;

                if (holder.mapView.getTag() != null) {
                /*ChatModel data = (ChatModel) holder.mapView.getTag();
                if (data == null) return;*/

                    // Add a marker for this item and set the camera
                    LatLng location = new LatLng(Double.parseDouble(listChat.get(position).getLatitude()), Double.parseDouble(listChat.get(position).getLongitude()));
                    holder.map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 13f));
                    holder.map.addMarker(new MarkerOptions().position(location));

                    // Set the map type back to normal.
                    holder.map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }

            /*holder.llAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    *//*String uri = String.format(Locale.ENGLISH, context.getString(R.string.map_format), String.valueOf(listChat.get(position).getLatitude()), String.valueOf(listChat.get(position).getLongitude()));
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    context.startActivity(intent);*//*

                    String geoUri = "http://maps.google.com/maps?q=loc:" + listChat.get(position).getLatitude() + "," + listChat.get(position).getLongitude() + " (" + "" + ")";
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                    if (mapIntent.resolveActivity(context.getPackageManager()) != null)
                        context.startActivity(mapIntent);
                }
            });*/
            } else {
                holder.lyrMessage.setVisibility(View.VISIBLE);
                holder.ivImage.setVisibility(View.GONE);
                holder.llAddress.setVisibility(View.GONE);
                holder.mapView.setVisibility(View.GONE);
            }

        } catch (IllegalArgumentException e) {
            // holder.tvTime.setText(listChat.get(position).getConversations_date());
        } catch (Exception e) {
            // holder.tvTime.setText(listChat.get(position).getConversations_date());
        }




    }

    @Override
    public int getItemCount() {
        return listChat.size();
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = 1; //Default is 1

        if ((listChat.get(position).getSenderId()).equals(uid)) {
            viewType = 0;
        }//if zero, it will be a header view

        return viewType;
    }

    /*public void addFriendData(ChatModel fm) {

        listChat.add(fm);

    }*/

    @Override
    public void onMapReady(GoogleMap googleMap) {
        /*googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0)));*/
    }

    private class MapReadyCallback implements OnMapReadyCallback {
        int position;

        public MapReadyCallback(int position) {
            this.position = position;
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(listChat.get(position).getLatitude()), Double.parseDouble(listChat.get(position).getLongitude()))));
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {
        public GoogleMap map;
        TextView tvName, tvMessage, tvTime, tvAddress, tvLatlong;
        ImageView ivImage;
        LinearLayout lyrMessage;
        RelativeLayout llAddress;
        MapView mapView;
        View layout;

        public MyViewHolder(View itemView) {
            super(itemView);
            layout = itemView;
            mapView = itemView.findViewById(R.id.lite_listrow_map);
            tvName = itemView.findViewById(R.id.tvName);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvTime = itemView.findViewById(R.id.tvTime);
            ivImage = itemView.findViewById(R.id.ivImage);
            lyrMessage = itemView.findViewById(R.id.lyrMessage);
            llAddress = itemView.findViewById(R.id.llAddress);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvLatlong = itemView.findViewById(R.id.tvLatlong);

            if (mapView != null) {
                // Initialise the MapView
                mapView.onCreate(null);
                // Set the map ready callback to receive the GoogleMap object
                mapView.getMapAsync(this);
            }

        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            MapsInitializer.initialize(context);
            map = googleMap;
            setMapLocation();
        }

        private void setMapLocation() {
            try {
                if (map == null) return;

                if (mapView.getTag() != null) {
                    //ChatModel data = (ChatModel) mapView.getTag();
                    ChatModel data = listChat.get((Integer) mapView.getTag());
                    if (data == null) return;

                    // Add a marker for this item and set the camera
                    LatLng location = new LatLng(Double.parseDouble(data.getLatitude()), Double.parseDouble(data.getLongitude()));
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 13f));
                    map.addMarker(new MarkerOptions().position(location));

                    // Set the map type back to normal.
                    map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }
            } catch (Exception e) {

            }
        }

    }
}
