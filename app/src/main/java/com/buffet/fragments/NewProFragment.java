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
 * {@link NewProFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewProFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewProFragment extends Fragment {

    private NewPromotionRecyclerAdapter adapter;
    private RecyclerView recyclerView;
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
        recyclerView = (RecyclerView) rootView.findViewById(R.id.new_promotion_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        getPromotionData();

        return rootView;
    }

    public void getPromotionData() {
        ServiceAction service = createService(ServiceAction.class);
        Call<ServerResponse> call = service.getPromotion();
        call.enqueue(new Callback<ServerResponse>(){
            @Override
            public void onResponse(Response<ServerResponse> response){
                ServerResponse model = response.body();
                List<Promotion> promotions = new ArrayList<>();
                if(model == null){
                    System.out.println("PROMOTION IS NULL");
                } else {
                    System.out.println("Result : " + model.getResult()
                                    + "\nMessage : " + model.getMessage());
                    for (int i = 0; i< model.getPromotion().size(); i++) {
                        Promotion current = new Promotion();
                        current.setProId(model.getPromotion().get(i).getProId());
                        current.setImage(model.getPromotion().get(i).getImage());
                        current.setProName(model.getPromotion().get(i).getProName());
                        current.setPrice(model.getPromotion().get(i).getPrice());
                        current.setMaxPerson(model.getPromotion().get(i).getMaxPerson());
                        promotions.add(current);
                    }
                    adapter = new NewPromotionRecyclerAdapter(getActivity(), promotions);
                    recyclerView.setAdapter(adapter);
                }
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
