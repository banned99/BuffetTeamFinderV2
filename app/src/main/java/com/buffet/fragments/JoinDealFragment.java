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

import com.buffet.adapters.MyCreateDealRecyclerAdapter;
import com.buffet.adapters.MyJoinDealRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import ggwp.caliver.banned.buffetteamfinderv2.R;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link JoinDealFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link JoinDealFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JoinDealFragment extends Fragment {

    RecyclerView joinDealRecyclerView;
    MyJoinDealRecyclerAdapter joinAdapter;

    private OnFragmentInteractionListener mListener;

    public JoinDealFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static JoinDealFragment newInstance() {
        JoinDealFragment fragment = new JoinDealFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_join_deal, container, false);
        joinDealRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_join_deal_list);
        joinDealRecyclerView.setHasFixedSize(false);
        joinDealRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        getJoinedDeal();
        return rootView;
    }

    public void getJoinedDeal() {
        List<String> list = new ArrayList<>();
        list.add("join1");
        list.add("join2");
        joinAdapter = new MyJoinDealRecyclerAdapter(getApplicationContext(), list);
        joinDealRecyclerView.setAdapter(joinAdapter);
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
