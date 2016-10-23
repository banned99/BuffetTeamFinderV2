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

import java.util.ArrayList;
import java.util.List;

import ggwp.caliver.banned.buffetteamfinderv2.R;

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
        adapter = new DealRecyclerAdapter(getActivity(), getData());
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.deal_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        recyclerView.setAdapter(adapter);

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

    public static List<Deal> getData() {
        List<Deal> deals = new ArrayList<>();
        int[] deal_id = {1,2,3,4,5};
        String[] name= {"b1", "b2", "b3", "b4", "b5"};

        for (int i = 0; i<deal_id.length && i<name.length; i++) {

            Deal current = new Deal();
            current.setDealId(deal_id[i]);
            current.setDealName(name[i]);
            deals.add(current);
        }

        return deals;
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
