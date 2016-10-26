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

import com.buffet.activities.ChooseBranchActivity;
import com.buffet.adapters.BranchRecyclerAdapter;
import com.buffet.models.Branch;
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
 * {@link ChooseBranchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChooseBranchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChooseBranchFragment extends Fragment {


    private BranchRecyclerAdapter adapter;
    private RecyclerView recyclerView;
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
        recyclerView = (RecyclerView) rootView.findViewById(R.id.branch_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        Bundle bundle = this.getArguments();
        getData(bundle.getInt("promotion_id"));

        return rootView;
    }

    public void getData(int pro_id) {
        ServiceAction service = createService(ServiceAction.class);
        ServerRequest request = new ServerRequest();
        request.setOperation("getbranch");
        request.setPromotionId(pro_id);
        Call<ServerResponse> call = service.getBranch(request);
        call.enqueue(new Callback<ServerResponse>() {
             @Override
             public void onResponse(Response<ServerResponse> response){
                 List<Branch> branches = new ArrayList<>();
                 ServerResponse model = response.body();
                 if(model.getResult().equals("failure")){
                     System.out.println("BRANCH IS NULL");
                 } else {
                     System.out.println("Result : " + model.getResult()
                             + "\nMessage : " + model.getMessage());
                     for (int i = 0; i<model.getBranch().size(); i++) {
                         Branch current = new Branch();
                         current.setBranchId(model.getBranch().get(i).getBranchId());
                         current.setBranchName(model.getBranch().get(i).getBranchName());
                         current.setLatitude(model.getBranch().get(i).getLatitude());
                         current.setLongitude(model.getBranch().get(i).getLongitude());
                         branches.add(current);
                     }
                 }
                 adapter = new BranchRecyclerAdapter(getActivity(), branches);
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
