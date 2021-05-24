package com.example.thumbup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MoneyFragment extends Fragment {
    ImageButton moneyAddPlaceNMenu;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View moneyView = inflater.inflate(R.layout.activity_money, container, false);
        moneyAddPlaceNMenu = (ImageButton) moneyView.findViewById(R.id.money_addPlaceNMenu);
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
