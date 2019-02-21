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

public class CoolDownFragment extends Fragment {

    private static final String ARG_COOL_DOWN_VID_URL = "cool_down_video_url";
    private static final String ARG_COOL_DOWN_VIDEO_DESCRIPTION = "cool_down_video_description";

    private String mCoolDownVideoUrl;
    private ArrayList<String> mCoolDownDescription;

    private CoolDownFragment.OnFragmentInteractionListener mListener;

    public CoolDownFragment() {
        // Required empty public constructor
    }

    public static CoolDownFragment newInstance(String videoUrlString, ArrayList<String> description) {
        CoolDownFragment fragment = new CoolDownFragment();
        Bundle args = new Bundle();
        args.putString(ARG_COOL_DOWN_VID_URL, videoUrlString);
        args.putStringArrayList(ARG_COOL_DOWN_VIDEO_DESCRIPTION, description);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCoolDownVideoUrl = getArguments().getString(ARG_COOL_DOWN_VID_URL);
            mCoolDownDescription = getArguments().getStringArrayList(ARG_COOL_DOWN_VIDEO_DESCRIPTION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.master_fragment_exercises, container, false);
        ArrayAdapter adapter = new ArrayAdapter<>(getContext(),
                R.layout.exercises_list_view_item, mCoolDownDescription);
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
        if (context instanceof CoolDownFragment.OnFragmentInteractionListener) {
            mListener = (CoolDownFragment.OnFragmentInteractionListener) context;
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
