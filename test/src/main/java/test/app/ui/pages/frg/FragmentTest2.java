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
import test.app.ui.activity.test.ElementAnimation4Activity;
import test.app.ui.pages.BaseFragmentViewPage;
import test.app.utiles.other.DLog;


public class FragmentTest2 extends Fragment {
    private View root;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (root == null) {
            root = inflater.inflate(R.layout.frg_element_animation, container, false);
        }
        return root;
    }
private TextView tvMsg;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentActivity activity = getActivity();
        tvMsg=view.findViewById(R.id.tv_msg);
        tvMsg .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                FragmentActivity activity = getActivity();
                activity.finishAfterTransition();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        FragmentActivity activity = getActivity();
        ElementAnimation4Activity act = ((ElementAnimation4Activity) activity);
        int index = act.getIndex();
        tvMsg.setText(""+index);
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


}
