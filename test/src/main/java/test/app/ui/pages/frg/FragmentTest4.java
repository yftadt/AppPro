package test.app.ui.pages.frg;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import test.app.ui.activity.R;


public class FragmentTest4 extends Fragment {
    private View root;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (root == null) {
            root = inflater.inflate(R.layout.frg_element_animation4, container, false);
        }
        return root;
    }

    private TextView tvMsg;
    private int index;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle arguments = getArguments();
        index = arguments.getInt("arg0");
        tvMsg = view.findViewById(R.id.tv_msg);
        tvMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                tvMsg.setTransitionName("shared_iv");
                FragmentActivity activity = getActivity();
                activity.finishAfterTransition();
            }
        });
        //
        if (index == 0) {
            tvMsg.setTransitionName("shared_iv");
        }
        tvMsg.setText("" + index);
    }

    @Override
    public void onResume() {
        super.onResume();
        tvMsg.setTransitionName("");
    }


/*  @Override
    protected void onViewCreated() {
        setContentView(R.layout.frg_element_animation);
        findViewById(R.id.tv_msg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                FragmentActivity activity = getActivity();
                activity.finishAfterTransition();
            }
        });
    }*/

    public static FragmentTest4 getFragmentTest2(int index) {
        FragmentTest4 fragment = new FragmentTest4();
        Bundle args = new Bundle();
        args.putInt("arg0", index);
        fragment.setArguments(args);
        return fragment;
    }
}
