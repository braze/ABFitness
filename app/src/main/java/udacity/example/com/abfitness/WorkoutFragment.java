package udacity.example.com.abfitness;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class WorkoutFragment extends Fragment {

    private static final String ARG_WORK_OUT_VID_URL = "work_out_video_url";
    private static final String ARG_WORK_OUT_VIDEO_DESCRIPTION = "work_out_video_description";

    private String mWorkOutVideoUrl;
    private ArrayList<String> mWorkOutDescription;

    private WorkoutFragment.OnFragmentInteractionListener mListener;

    public WorkoutFragment() {
        // Required empty public constructor
    }

    public static WorkoutFragment newInstance(String videoUrlString, ArrayList<String> description) {
        WorkoutFragment fragment = new WorkoutFragment();
        Bundle args = new Bundle();
        args.putString(ARG_WORK_OUT_VID_URL, videoUrlString);
        args.putStringArrayList(ARG_WORK_OUT_VIDEO_DESCRIPTION, description);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mWorkOutVideoUrl = getArguments().getString(ARG_WORK_OUT_VID_URL);
            mWorkOutDescription = getArguments().getStringArrayList(ARG_WORK_OUT_VIDEO_DESCRIPTION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.master_fragment_exercises, container, false);
        ArrayAdapter adapter = new ArrayAdapter<>(getContext(),
                R.layout.exercises_list_view_item, mWorkOutDescription);
        ListView listView = rootView.findViewById(R.id.list_view);
        listView.setAdapter(adapter);

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
        if (context instanceof WorkoutFragment.OnFragmentInteractionListener) {
            mListener = (WorkoutFragment.OnFragmentInteractionListener) context;
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

