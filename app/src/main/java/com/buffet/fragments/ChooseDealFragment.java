package com.buffet.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.buffet.adapters.DealRecyclerAdapter;
import com.buffet.dialogs.CreateDealDialog;
import com.buffet.models.Deal;
import com.buffet.network.ServerRequest;
import com.buffet.network.ServerResponse;
import com.buffet.network.ServiceAction;

import java.util.ArrayList;
import java.util.List;

import ggwp.caliver.banned.buffetteamfinderv2.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.buffet.network.ServiceGenerator.createService;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChooseDealFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChooseDealFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChooseDealFragment extends Fragment {

    private DealRecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private OnFragmentInteractionListener mListener;

    public ChooseDealFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ChooseDealFragment newInstance() {
        ChooseDealFragment fragment = new ChooseDealFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_choose_deal, container, false);
        // Deal Recycler

        recyclerView = (RecyclerView) rootView.findViewById(R.id.deal_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        Bundle bundle = this.getArguments();
        getData(bundle.getInt("branch_id"));

        // Fab button
        final FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fabBtn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                CreateDealDialog createDealDialog = new CreateDealDialog();
                createDealDialog.show(manager, "dialog");
            }
        });

        // fab will hide when scroll down
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && fab.isShown())
                {
                    fab.hide();
                } else fab.show();
            }
        });
        return rootView;
    }

    public void getData(int branch_id) {
        ServiceAction service = createService(ServiceAction.class);
        ServerRequest request = new ServerRequest();
        request.setOperation("getdeal");
        request.setBranchId(branch_id);
        Call<ServerResponse> call = service.getDeal(request);
        call.enqueue(new Callback<ServerResponse>(){
            @Override
            public void onResponse(Response<ServerResponse> response) {
                ServerResponse model = response.body();
                List<Deal> deals = new ArrayList<>();
                if(model.getResult().equals("failure")){
                    System.out.println("DEAL IS NULL");
                } else {
                    System.out.println("Result : " + model.getResult()
                            + "\nMessage : " + model.getMessage());
                    for (int i = 0; i < model.getDeal().size(); i++) {
                        Deal current = new Deal();
                        current.setDealId(model.getDeal().get(i).getDealId());
                        current.setDealOwner(model.getDeal().get(i).getDealOwner());
                        current.setCurrentPerson(model.getDeal().get(i).getCurrentPerson());
                        current.setDate(model.getDeal().get(i).getDate());
                        current.setTime(model.getDeal().get(i).getTime());
                        deals.add(current);
                    }
                }
                adapter = new DealRecyclerAdapter(getActivity(), deals);
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onFailure(Throwable t) {
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
