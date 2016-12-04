package com.buffet.fragments;

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
import android.widget.Toast;

import com.buffet.activities.MainActivity;
import com.buffet.adapters.NewPromotionRecyclerAdapter;
import com.buffet.models.Promotion;
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
 * {@link NewProFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewProFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewProFragment extends Fragment {

    private NewPromotionRecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private OnFragmentInteractionListener mListener;
    private ProgressBar progressBar;
    private TextView noeventText;

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

        progressBar = (ProgressBar) rootView.findViewById(R.id.progress);

        noeventText = (TextView) rootView.findViewById(R.id.noevent_text);

        getPromotionData();

        return rootView;
    }

    public void getPromotionData() {
        ServiceAction service = createService(ServiceAction.class);
        Call<ServerResponse> call = service.getPromotion();
        call.enqueue(new Callback<ServerResponse>(){
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response){
                ServerResponse model = response.body();
                List<Promotion> promotions = new ArrayList<>();
                if(model.getResult().equals("failure")){
                    System.out.println("PROMOTION IS NULL");
                    noeventText.setVisibility(View.VISIBLE);
                } else {
                    System.out.println("Result : " + model.getResult()
                                    + "\nMessage : " + model.getMessage());
                    noeventText.setVisibility(View.INVISIBLE);
                    for (int i = 0; i< model.getPromotion().size(); i++) {
                        Promotion current = new Promotion();
                        current.setProId(model.getPromotion().get(i).getProId());
                        current.setImage(model.getPromotion().get(i).getImage());
                        current.setProName(model.getPromotion().get(i).getProName());
                        current.setPrice(model.getPromotion().get(i).getPrice());
                        current.setDateStart(model.getPromotion().get(i).getDateStart());
                        current.setExpire(model.getPromotion().get(i).getExpire());
                        current.setMaxPerson(model.getPromotion().get(i).getMaxPerson());
                        current.setCatId(model.getPromotion().get(i).getCatId());
                        current.setCatName(model.getPromotion().get(i).getCatName());
                        current.setDescription(model.getPromotion().get(i).getDescription());
                        promotions.add(current);
                    }
                }
                progressBar.setVisibility(View.INVISIBLE);
                if(getActivity()!=null){
                    adapter = new NewPromotionRecyclerAdapter(getActivity(), promotions);
                    recyclerView.setAdapter(adapter);
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
