package com.jj.grouplist;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ListView listView = (ListView) findViewById(R.id.listview);
        final GroupAdapter adapter = new GroupAdapter(getApplicationContext());
        adapter.bindListView(listView);
        listView.setAdapter(adapter);

        adapter.addGroup(10); // add a group
        final ImageView image = new ImageView(getApplicationContext());
        image.setImageResource(R.drawable.image);
        adapter.addStableView(image); // add a view
        adapter.addGroup(10); // add a group
        adapter.addGroup(10); // add a group
        adapter.notifyDataSetChanged();

        // some method to contrll
        // adapter.addGroup(10);
        // adapter.setGroupCount(0, 10);
        // adapter.removeGroup(0);
        // adapter.addStableView(button);
        // adapter.removeStableView(button);
    }

    class GroupAdapter extends AbsGroupAdapter {

        public GroupAdapter(Context context) {
            super(context);
        }

        @Override
        public View getChildView(View probableView, int groupIndex, int childIndex, int position, ViewGroup parent) {
            switch (groupIndex) {
                case 0:
                    TextView view = null;
                    if (probableView != null) {
                        view = (TextView) probableView;
                    } else {
                        view = new TextView(mContext);
                    }
                    view.setText("position:" + childIndex);
                    return view;
                case 2:
                    ImageView view2 = null;
                    if (probableView != null) {
                        view2 = (ImageView) probableView;
                    } else {
                        view2 = new ImageView(mContext);
                    }
                    view2.setImageResource(android.R.drawable.ic_dialog_email);
                    return view2;
                case 3:
                    Button button = null;
                    if (probableView != null) {
                        button = (Button) probableView;
                    } else {
                        button = new Button(mContext);
                    }
                    button.setText("button:" + childIndex);
                    return button;
                default:
                    break;
            }
            return null;
        }

    }
}