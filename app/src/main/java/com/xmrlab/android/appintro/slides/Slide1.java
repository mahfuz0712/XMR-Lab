
package com.xmrlab.android.appintro.slides;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.xmrlab.android.R;
import com.xmrlab.android.appintro.AppIntroActivity;
import com.xmrlab.android.appintro.IntroPage;
import com.xmrlab.android.utils.Core;

public class Slide1 extends Fragment implements IntroPage {

    private Core core;
    private ViewPager2 mPager;

    private MaterialCheckBox box1;
    private MaterialCheckBox box2;
    private MaterialCheckBox box3;

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(
                R.layout.new_slide1,
                container,
                false
        );

        core = new Core(requireContext());
        mPager = requireActivity().findViewById(R.id.view_pager);

        box1 = view.findViewById(R.id.slide_checkbox);
        box2 = view.findViewById(R.id.slide_checkbox2);
        box3 = view.findViewById(R.id.slide_checkbox3);

        bindRow(view, R.id.consent_row_1, box1);
        bindRow(view, R.id.consent_row_2, box2);
        bindRow(view, R.id.consent_row_3, box3);

        return view;
    }

    private void bindRow(View root, int rowId, MaterialCheckBox box) {

        // Checkbox itself handles direct checkbox taps.
        box.setClickable(true);
        box.setFocusable(true);

        // Refresh Continue button whenever checked state changes.
        box.setOnCheckedChangeListener((button, checked) -> {
            refreshContinue();
        });

        // Tapping anywhere else on the row toggles the checkbox.
        View row = root.findViewById(rowId);

        row.setOnClickListener(v -> {
            box.setChecked(!box.isChecked());
        });
    }

    private void refreshContinue() {
        if (getActivity() instanceof AppIntroActivity) {
            ((AppIntroActivity) getActivity()).refreshPrimary();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshContinue();
    }

    @Override
    public CharSequence primaryLabel(Context context) {
        return context.getString(R.string.intro_action_continue);
    }

    @Override
    public boolean primaryEnabled() {
        return box1 != null
                && box2 != null
                && box3 != null
                && box1.isChecked()
                && box2.isChecked()
                && box3.isChecked();
    }

    @Override
    public void onPrimary() {
        if (!primaryEnabled()) return;

        core.moveNext(mPager);
    }
}
