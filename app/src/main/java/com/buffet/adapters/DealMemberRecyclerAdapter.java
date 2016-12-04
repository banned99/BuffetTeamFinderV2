package com.buffet.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.buffet.models.Deal;
import com.buffet.models.DealMember;
import com.buffet.models.User;
import com.buffet.network.ServerRequest;
import com.buffet.network.ServerResponse;
import com.buffet.network.ServiceAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ggwp.caliver.banned.buffetteamfinderv2.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.buffet.network.ServiceGenerator.createService;

/**
 * Created by icespw on 12/4/2016 AD.
 */

public class DealMemberRecyclerAdapter extends RecyclerView.Adapter<DealMemberRecyclerAdapter.ViewHolder> {

    private LayoutInflater inflater;
    String status;

    List<DealMember> dealMembers = Collections.emptyList();
    List<User> users = Collections.emptyList();

    public DealMemberRecyclerAdapter(Context context, List<DealMember> dealMembers, List<User> users, String status) {
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

//        holder.username.setText(members.get(position));

        if (status.equals("owner")) {
            holder.acBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
                                        //Do something
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
                public void onClick(View v) {
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

            holder.kiBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
            holder.memberStatus.setVisibility(View.VISIBLE);
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

        public ViewHolder(View itemView) {
            super(itemView);

            username = (TextView) itemView.findViewById(R.id.member_username);
            memberStatus = (TextView) itemView.findViewById(R.id.member_status);
            acBtn = (Button) itemView.findViewById(R.id.accept_button);
            deBtn = (Button) itemView.findViewById(R.id.decline_button);
            kiBtn = (Button) itemView.findViewById(R.id.kick_button);

        }
    }
}
