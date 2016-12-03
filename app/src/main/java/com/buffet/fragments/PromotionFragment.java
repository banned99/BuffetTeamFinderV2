package com.buffet.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buffet.activities.MainActivity;
import com.buffet.adapters.PromotionViewPagerAdapter;

import ggwp.caliver.banned.buffetteamfinderv2.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PromotionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PromotionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PromotionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    TabLayout tabLayout;
    ViewPager viewPager;
    PromotionViewPagerAdapter adapter;
    TabLayout.Tab newPro, categories, topTen, aToZ;

    public PromotionFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static PromotionFragment newInstance() {
        PromotionFragment fragment = new PromotionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_promotion, container, false);

////         Tab
        tabLayout = (TabLayout) rootView.findViewById(R.id.tabLayout);

        newPro = tabLayout.newTab();
        categories = tabLayout.newTab();
        topTen = tabLayout.newTab();

        tabLayout.addTab(topTen);
        tabLayout.addTab(categories);
        tabLayout.addTab(newPro);

        // ViewPager
        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        adapter = new PromotionViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager, false);

        newPro.setText("new");
        categories.setText("category");
        topTen.setText("top 10");

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                Change Icon
//                switch (position) {
//                    case 0: {
//                        homeTab.setIcon(R.drawable.home_tab_white);
//                        profileTab.setIcon(R.drawable.profile_tab);
//                        break;
//                    }
//                    case 1: {
//                        profileTab.setIcon(R.drawable.profile_tab_white);
//                        homeTab.setIcon(R.drawable.home_tab);
//                        break;
//                    }
//                }
//
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            context = (MainActivity) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

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
