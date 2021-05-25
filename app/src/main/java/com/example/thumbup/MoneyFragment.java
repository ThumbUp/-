package com.example.thumbup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MoneyFragment extends Fragment {
    ImageButton moneyAddPlaceNMenu;
    ImageButton showSchedule;
    TextView scheduleName;
    TextView moneyAdd_scheduleName = ((MoneyAddActivity)MoneyAddActivity.moneyAddContext).scheduleName;

    public void showSchedule() {
        PopupMenu schedulePopup = new PopupMenu(getActivity(), scheduleName);
        Menu scheduleMenu = schedulePopup.getMenu();
        scheduleMenu.add("일정1");
        scheduleMenu.add("일정2");
        schedulePopup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuIndex = item.getItemId();
                scheduleName.setText((CharSequence) scheduleMenu.getItem(menuIndex));
                moneyAdd_scheduleName.setText((CharSequence) scheduleMenu.getItem(menuIndex));
                return false;
            }
        });
        schedulePopup.show();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View moneyView = inflater.inflate(R.layout.activity_money, container, false);
        showSchedule = (ImageButton) moneyView.findViewById(R.id.showSchedule);
        scheduleName = (TextView) moneyView.findViewById(R.id.schedule_name);
        moneyAddPlaceNMenu = (ImageButton) moneyView.findViewById(R.id.money_addPlaceNMenu);

        showSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSchedule();
            }
        });
        moneyAddPlaceNMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("money_add", "내역 추가 버튼 클릭");
                Intent intent = new Intent(getActivity().getApplicationContext(), MoneyAddActivity.class);
                startActivity(intent);
            }
        });
        return moneyView;
    }
}
