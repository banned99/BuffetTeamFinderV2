package com.buffet.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.buffet.adapters.MyCreateDealRecyclerAdapter;
import com.buffet.adapters.MyJoinDealRecyclerAdapter;
import com.buffet.models.Branch;
import com.buffet.models.Constants;
import com.buffet.models.Deal;
import com.buffet.models.Promotion;
import com.buffet.models.User;
import com.buffet.network.ServerRequest;
import com.buffet.network.ServerResponse;
import com.buffet.network.ServiceAction;

import java.util.ArrayList;
import java.util.List;

import ggwp.caliver.banned.buffetteamfinderv2.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.buffet.activities.LoginActivity.pref;
import static com.buffet.network.ServiceGenerator.createService;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OwnerDealFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OwnerDealFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OwnerDealFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    RecyclerView createDealRecyclerView;
    MyCreateDealRecyclerAdapter createAdapter;
    ProgressBar progressBar;
    TextView noeventText;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public OwnerDealFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static OwnerDealFragment newInstance() {
        OwnerDealFragment fragment = new OwnerDealFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_owner_deal, container, false);

        createDealRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_create_deal_list);
        createDealRecyclerView.setHasFixedSize(false);
        createDealRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress);
        noeventText = (TextView) rootView.findViewById(R.id.noevent_text);
        getCreatedDeal();

        return rootView;
    }

    public void getCreatedDeal() {
        ServiceAction service = createService(ServiceAction.class);
        ServerRequest request = new ServerRequest();
        request.setOperation("getdealowner");

        User users = new User();
        users.setMemberId(pref.getInt(Constants.MEMBER_ID, 0));

        request.setUser(users);
        Call<ServerResponse> call = service.getDealOwner(request);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse model = response.body();
                List<Deal> deals = new ArrayList<>();
                List<Branch> branchs = new ArrayList<>();
                List<Promotion> promotions = new ArrayList<>();
                List<User> users = new ArrayList<>();
                if(model.getResult().equals("failure")){
                    System.out.println("Result : " + model.getResult()
                            + "\nMessage : " + model.getMessage());
                    noeventText.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }else {
                    System.out.println("Result : " + model.getResult()
                            + "\nMessage : " + model.getMessage());
                    for (int i = 0; i< model.getDeal().size(); i++) {
                        Deal current = new Deal();
                        Branch b = new Branch();
                        Promotion p = new Promotion();
                        User u = new User();
                        current.setDealId(model.getDeal().get(i).getDealId());
                        current.setDate(model.getDeal().get(i).getDate());
                        current.setCurrentPerson(model.getDeal().get(i).getCurrentPerson());
                        current.setTime(model.getDeal().get(i).getTime());
                        current.setDealOwner(pref.getInt(Constants.MEMBER_ID, 0));

                        b.setBranchName(model.getBranch().get(i).getBranchName());
                        p.setProName(model.getPromotion().get(i).getProName());
                        p.setMaxPerson(model.getPromotion().get(i).getMaxPerson());
                        u.setName(model.getListUser().get(i).getName());

                        deals.add(current);
                        branchs.add(b);
                        promotions.add(p);
                        users.add(u);
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                    noeventText.setVisibility(View.INVISIBLE);
                    if(getApplicationContext()!=null) {
                        createAdapter = new MyCreateDealRecyclerAdapter(getApplicationContext(), deals, branchs, promotions, users);
                        createDealRecyclerView.setAdapter(createAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
