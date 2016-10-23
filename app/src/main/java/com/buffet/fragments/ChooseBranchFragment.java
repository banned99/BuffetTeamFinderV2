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

import com.buffet.adapters.BranchRecyclerAdapter;
import com.buffet.models.Branch;

import java.util.ArrayList;
import java.util.List;

import ggwp.caliver.banned.buffetteamfinderv2.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChooseBranchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChooseBranchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChooseBranchFragment extends Fragment {


    private BranchRecyclerAdapter adapter;
    private OnFragmentInteractionListener mListener;

    public ChooseBranchFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ChooseBranchFragment newInstance() {
        ChooseBranchFragment fragment = new ChooseBranchFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_choose_branch, container, false);
        // Branch Recycler
        adapter = new BranchRecyclerAdapter(getActivity(), getData());
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.branch_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        recyclerView.setAdapter(adapter);
        return rootView;
    }

    public static List<Branch> getData() {
        List<Branch> branches = new ArrayList<>();
        int[] branch_id = {1,2,3,4,5};
        String[] name= {"b1", "b2", "b3", "b4", "b5"};

        for (int i = 0; i<branch_id.length && i<name.length; i++) {

            Branch current = new Branch();
            current.setBranchId(branch_id[i]);
            current.setBranchName(name[i]);
            branches.add(current);
        }

        return branches;
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
