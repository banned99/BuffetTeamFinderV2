package com.buffet.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buffet.adapters.NewPromotionRecyclerAdapter;
import com.buffet.models.Promotion;

import java.util.ArrayList;
import java.util.List;

import ggwp.caliver.banned.buffetteamfinderv2.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewProFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewProFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewProFragment extends Fragment {

    private NewPromotionRecyclerAdapter adapter;
    private OnFragmentInteractionListener mListener;

    public NewProFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static NewProFragment newInstance() {
        NewProFragment fragment = new NewProFragment();
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
        View rootView =  inflater.inflate(R.layout.fragment_new, container, false);

        adapter = new NewPromotionRecyclerAdapter(getActivity(), getData());
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.new_promotion_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        recyclerView.setAdapter(adapter);

        return rootView;
    }

    public static List<Promotion> getData() {
        List<Promotion> promotions = new ArrayList<>();
        int[] promotion_id = {1,2,3,4,5,6,7,8};
        int[] images = {R.drawable.promo_1, R.drawable.promo_2, R.drawable.promo_3, R.drawable.promo_4, R.drawable.promo_5, R.drawable.promo_6, R.drawable.promo_7, R.drawable.promo_8};
        String[] name = {"Promotion_1", "Promotion_2", "Promotion_3", "Promotion_4", "Promotion_5", "Promotion_6", "Promotion_7", "Promotion_8"};
        double[] price = {35.25, 53.66, 552.3, 255.4, 6.777, 7.44, 24.55, 600.43};
        int[] max_person = {4, 4, 3, 5, 4, 2, 4, 3};

        for (int i = 0; i<name.length && i<images.length; i++) {

            Promotion current = new Promotion();
            current.setPromotionID(promotion_id[i]);
            current.setImage(images[i]);
            current.setPromotionName(name[i]);
            current.setPrice(price[i]);
            current.setMaxPerson(max_person[i]);
            promotions.add(current);

        }

        return promotions;
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
     * to the activity and potentially other com.buffet.fragments contained in that
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
