package com.julia.tictactoe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomListAdapter extends BaseAdapter {

    private List<HighscoreList.Score> scoreList;

    public CustomListAdapter(List<HighscoreList.Score> scoreList) {
        this.scoreList = scoreList;
    }

    @Override
    public int getCount() {
        return scoreList.size();
    }

    @Override
    public HighscoreList.Score getItem(int position) {
        return scoreList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //inflate layout for each row
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_score_item, parent, false);
        }

        //get the textview for the item
        TextView nameText = convertView.findViewById(R.id.score_item_name);
        TextView scoreText = convertView.findViewById(R.id.score_item);

        //write info
        HighscoreList.Score score = scoreList.get(position);
        nameText.setText(score.name);
        scoreText.setText(score.score);

        return convertView;
    }


}
