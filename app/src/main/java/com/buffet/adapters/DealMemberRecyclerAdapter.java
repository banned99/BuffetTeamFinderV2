package com.buffet.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.buffet.activities.MapActivity;
import com.buffet.fragments.PromotionFragment;
import com.buffet.models.Constants;
import com.buffet.models.Deal;
import com.buffet.models.DealMember;
import com.buffet.models.User;
import com.buffet.network.ServerRequest;
import com.buffet.network.ServerResponse;
import com.buffet.network.ServiceAction;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ggwp.caliver.banned.buffetteamfinderv2.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.buffet.activities.LoginActivity.pref;
import static com.buffet.network.ServiceGenerator.createService;

/**
 * Created by icespw on 12/4/2016 AD.
 */

public class DealMemberRecyclerAdapter extends RecyclerView.Adapter<DealMemberRecyclerAdapter.ViewHolder>{

    private LayoutInflater inflater;
    String status;

    List<DealMember> dealMembers = Collections.emptyList();
    List<User> users = Collections.emptyList();

    public DealMemberRecyclerAdapter(Context context, List<DealMember> dealMembers, List<User> users, String status){
        inflater = LayoutInflater.from(context);
        this.dealMembers = dealMembers;
        this.users = users;
        this.status = status;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.deal_member_recycler_child, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DealMemberRecyclerAdapter.ViewHolder holder, final int position) {

        holder.username.setText(users.get(position).getName());
        if (users.get(position).getFbid() != null) {
            if (users.get(position).getImageUrl() != null){
                System.out.println("have Fb, have image");
                Picasso.with(holder.itemView.getContext()).load("http://api.tunacon.com/uploads/" + users.get(position).getImageUrl()).resize(1200, 650).into(holder.userPic);
            } else {
                // facebook image
                System.out.println("have Fb, no image");

                Picasso.with(holder.itemView.getContext()).load("https://graph.facebook.com/" + users.get(position).getFbid() + "/picture?type=large").resize(1200, 650).into(holder.userPic);
            }
        } else if (users.get(position).getImageUrl() != null) {
            System.out.println("no Fb, have image");

            Picasso.with(holder.itemView.getContext()).load("http://api.tunacon.com/uploads/" + users.get(position).getImageUrl()).resize(1200, 650).into(holder.userPic);
        }
        else {
            // default image
            holder.userPic.setImageResource(R.drawable.user_default_img);
        }

        if (status.equals("owner")) {
            holder.memberStatus.setVisibility(View.VISIBLE);
            if (dealMembers.get(position).getStatus() == 1) {
                holder.memberStatus.setText("อนุมัติแล้ว");
            } else if (dealMembers.get(position).getStatus() == 0){
                holder.memberStatus.setText("รอการอนุมัติ");
            } else {
                holder.memberStatus.setText("เจ้าของอีเว้น");
                holder.acBtn.setVisibility(View.GONE);
                holder.deBtn.setVisibility(View.GONE);
                holder.kiBtn.setVisibility(View.GONE);
            }
            holder.acBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("Accept");
                    builder.setMessage("Do you want to accept this join request?");
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ServiceAction service = createService(ServiceAction.class);
                            ServerRequest request = new ServerRequest();
                            request.setOperation("setaccept");

                            Deal d = new Deal();
                            User u = new User();

                            d.setDealId(dealMembers.get(position).getDealId());
                            u.setMemberId(users.get(position).getMemberId());

                            request.setUser(u);
                            request.setDeal(d);

                            Call<ServerResponse> call = service.getDealMember(request);
                            call.enqueue(new Callback<ServerResponse>() {
                                @Override
                                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                                    ServerResponse model = response.body();
                                    if(model.getResult().equals("failure")){
                                        System.out.println("Event IS NULL");
                                    }else {
                                        System.out.println("Result : " + model.getResult()
                                                + "\nMessage : " + model.getMessage());
                                        holder.memberStatus.setText("อนุมัติแล้ว");
                                        holder.deBtn.setVisibility(View.GONE);
                                        holder.acBtn.setVisibility(View.GONE);
                                        holder.kiBtn.setVisibility(View.VISIBLE);
                                        Snackbar.make(v, "You have accepted !!", Snackbar.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ServerResponse> call, Throwable t) {
                                    t.printStackTrace();
                                }
                            });

                        }
                    });
                    builder.show();
                }
            });

            holder.deBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("Decline");
                    builder.setMessage("Do you want to decline this join request?");
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ServiceAction service = createService(ServiceAction.class);
                            ServerRequest request = new ServerRequest();
                            request.setOperation("setdecline");

                            Deal d = new Deal();
                            User u = new User();

                            d.setDealId(dealMembers.get(position).getDealId());
                            u.setMemberId(users.get(position).getMemberId());

                            request.setUser(u);
                            request.setDeal(d);

                            Call<ServerResponse> call = service.getDealMember(request);
                            call.enqueue(new Callback<ServerResponse>() {
                                @Override
                                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                                    ServerResponse model = response.body();
                                    if(model.getResult().equals("failure")){
                                        System.out.println("Event IS NULL");
                                    }else {
                                        System.out.println("Result : " + model.getResult()
                                                + "\nMessage : " + model.getMessage());
                                        users.remove(position);
                                        notifyDataSetChanged();
                                        Snackbar.make(v, "You've decline the request", Snackbar.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ServerResponse> call, Throwable t) {
                                    t.printStackTrace();
                                }
                            });

                        }
                    });
                    builder.show();
                }
            });

            if (dealMembers.get(position).getStatus() == 1) {
                holder.acBtn.setVisibility(View.GONE);
                holder.deBtn.setVisibility(View.GONE);
                holder.kiBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setTitle("Kick");
                        builder.setMessage("Do you want to kick this member?");
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ServiceAction service = createService(ServiceAction.class);
                                ServerRequest request = new ServerRequest();
                                request.setOperation("setkick");

                                Deal d = new Deal();
                                User u = new User();

                                d.setDealId(dealMembers.get(position).getDealId());
                                u.setMemberId(users.get(position).getMemberId());

                                request.setUser(u);
                                request.setDeal(d);

                                Call<ServerResponse> call = service.getDealMember(request);
                                call.enqueue(new Callback<ServerResponse>() {
                                    @Override
                                    public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                                        ServerResponse model = response.body();
                                        if(model.getResult().equals("failure")){
                                            System.out.println("Event IS NULL");
                                        }else {
                                            System.out.println("Result : " + model.getResult()
                                                    + "\nMessage : " + model.getMessage());
                                            users.remove(position);
                                            notifyDataSetChanged();
                                            Snackbar.make(v, "You've kick the member", Snackbar.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ServerResponse> call, Throwable t) {
                                        t.printStackTrace();
                                    }
                                });
                            }
                        });
                        builder.show();
                    }
                });

            } else {
                holder.kiBtn.setVisibility(View.GONE);
            }


        } else {
            holder.memberStatus.setVisibility(View.VISIBLE);
            if (dealMembers.get(position).getStatus() == 1) {
                holder.memberStatus.setText("อนุมัติแล้ว");
            } else if (dealMembers.get(position).getStatus() == 0){
                holder.memberStatus.setText("รอการอนุมัติ");
            } else {
                holder.memberStatus.setText("เจ้าของอีเว้น");
            }
            holder.deBtn.setVisibility(View.GONE);
            holder.kiBtn.setVisibility(View.GONE);
            holder.acBtn.setVisibility(View.GONE);
        }



    }

    @Override
    public int getItemCount() {
        return users.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView username, memberStatus;
        Button acBtn, deBtn, kiBtn;
        CircleImageView userPic;

        public ViewHolder(View itemView) {
            super(itemView);

            username = (TextView) itemView.findViewById(R.id.member_username);
            memberStatus = (TextView) itemView.findViewById(R.id.member_status);
            acBtn = (Button) itemView.findViewById(R.id.accept_button);
            deBtn = (Button) itemView.findViewById(R.id.decline_button);
            kiBtn = (Button) itemView.findViewById(R.id.kick_button);
            userPic = (CircleImageView) itemView.findViewById(R.id.user_pic);
        }
    }
}
