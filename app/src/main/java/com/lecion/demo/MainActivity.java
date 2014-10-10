package com.lecion.demo;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.Button;

import at.markushi.ui.RevealColorView;


public class MainActivity extends Activity implements View.OnClickListener{
    private RevealColorView revealColorView;
    private View selectedView;
    private int bgColor;
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        revealColorView = (RevealColorView)findViewById(R.id.reveal_color_view);
        bgColor = Color.parseColor("#212121");
        findViewById(R.id.btn_1).setOnClickListener(this);
        findViewById(R.id.btn_2).setOnClickListener(this);
        findViewById(R.id.btn_3).setOnClickListener(this);
        findViewById(R.id.btn_4).setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int color = getColor(v);
        Point p = getLocationInView(revealColorView, v);

        if (selectedView == v) {
            revealColorView.hide(p.x, p.y, bgColor, 0, 300, null);
            selectedView = null;
        } else {
            revealColorView.reveal(p.x, p.y, color, v.getHeight() / 2, 5000, null);
            selectedView = v;
        }
    }

    private Point getLocationInView(View src, View target) {
        int[] l0 = new int[2];
        src.getLocationOnScreen(l0);
        Log.d("getLocationInView ", "src: " + l0[0] + ", " + l0[1]);

        int[] l1 = new int[2];
        target.getLocationOnScreen(l1);
        Log.d("getLocationInView ", "target: " + l1[0] + ", " + l1[1]);


        l1[0] = l1[0] - l0[0] + target.getWidth() / 2;
        l1[1] = l1[1] - l0[1] + target.getHeight() / 2;

        Log.d("getLocationInView ", "l1[0]: " + l1[0] + ", " + l1[1]);
        return new Point(l1[0], l1[1]);
    }

    private int getColor(View v) {
        return Color.parseColor((String)v.getTag());
    }
}
