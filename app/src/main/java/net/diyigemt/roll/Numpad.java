package net.diyigemt.roll;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import net.diyigemt.meabuttontest.MeaSounds;

public class Numpad extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener, View.OnTouchListener {

    public static final String ACTION = "Numpad";
    Intent intent;
    public static String STATUS = "";
    public static int StopStatus = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numpad);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        intent = new Intent(ACTION);
        Button ok = (Button) findViewById(R.id.NumOK);
        ok.setText(STATUS);
        init();
        new MeaSounds (Numpad.this);
    }

    @Override
    public void onClick(View view) {
        Button ok = (Button) findViewById(R.id.NumOK);
        new MeaSounds(Numpad.this).clickEvent();
        switch (view.getId()){
            case R.id.Num0:
                intent.putExtra("data","0");
                sendBroadcast(intent);
                break;
            case R.id.Num1:
                intent.putExtra("data","1");
                sendBroadcast(intent);
                break;
            case R.id.Num2:
                intent.putExtra("data","2");
                sendBroadcast(intent);
                break;
            case R.id.Num3:
                intent.putExtra("data","3");
                sendBroadcast(intent);
                break;
            case R.id.Num4:
                intent.putExtra("data","4");
                sendBroadcast(intent);
                break;
            case R.id.Num5:
                intent.putExtra("data","5");
                sendBroadcast(intent);
                break;
            case R.id.Num6:
                intent.putExtra("data","6");
                sendBroadcast(intent);
                break;
            case R.id.Num7:
                intent.putExtra("data","7");
                sendBroadcast(intent);
                break;
            case R.id.Num8:
                intent.putExtra("data","8");
                sendBroadcast(intent);
                break;
            case R.id.Num9:
                intent.putExtra("data","9");
                sendBroadcast(intent);
                break;
            case R.id.NumOK:
                if(ok.getText().toString().equals("下一步")){
                    intent.putExtra("data","12");
                    sendBroadcast(intent);
                    ok.setText("ROLL");
                }
                else if(ok.getText().toString().equals("ROLL")){
                    intent.putExtra("data","10");
                    sendBroadcast(intent);
                }
                break;
            case R.id.NumBack:
                intent.putExtra("data","-1");
                sendBroadcast(intent);
                break;
            case R.id.hide:
                finish();
                MainActivity.NumPadStatus = 1;
                break;
            case R.id.clickarea1:
                intent.putExtra("data","11");
                sendBroadcast(intent);
                ok.setText("下一步");
                break;
            case R.id.clickarea2:
                intent.putExtra("data","12");
                sendBroadcast(intent);
                ok.setText("ROLL");
                break;
            case R.id.clickarea3:
                intent.putExtra("data","13");
                sendBroadcast(intent);
                break;
        }
    }

    @Override
    public boolean onLongClick(View view) {
        if(view.getId() == R.id.NumBack){
            intent.putExtra("data","-4");
            sendBroadcast(intent);
        }
        return false;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(motionEvent.getAction() == MotionEvent.ACTION_UP){
            StopStatus = 0;
        }
        else StopStatus = 1;
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void onLinearLayoutClick(View view){
        finish();
    }

    @Override
    protected void onDestroy() {
        MainActivity.NumPadStatus = 1;
        super.onDestroy();
    }

    private void init(){
        Button num1 = (Button) findViewById(R.id.Num1);
        Button num2 = (Button) findViewById(R.id.Num2);
        Button num3 = (Button) findViewById(R.id.Num3);
        Button num4 = (Button) findViewById(R.id.Num4);
        Button num5 = (Button) findViewById(R.id.Num5);
        Button num6 = (Button) findViewById(R.id.Num6);
        Button num7 = (Button) findViewById(R.id.Num7);
        Button num8 = (Button) findViewById(R.id.Num8);
        Button num9 = (Button) findViewById(R.id.Num9);
        Button num0 = (Button) findViewById(R.id.Num0);
        Button ok = (Button) findViewById(R.id.NumOK);
        ImageButton back = (ImageButton) findViewById(R.id.NumBack);
        ImageButton hide  = (ImageButton) findViewById(R.id.hide);
        TextView clickArea1 = (TextView) findViewById(R.id.clickarea1);
        TextView clickArea2 = (TextView) findViewById(R.id.clickarea2);
        TextView clickArea3 = (TextView) findViewById(R.id.clickarea3);

        num1.setOnClickListener(this);
        num2.setOnClickListener(this);
        num3.setOnClickListener(this);
        num4.setOnClickListener(this);
        num5.setOnClickListener(this);
        num6.setOnClickListener(this);
        num7.setOnClickListener(this);
        num8.setOnClickListener(this);
        num9.setOnClickListener(this);
        num0.setOnClickListener(this);
        hide.setOnClickListener(this);
        back.setOnClickListener(this);
        ok.setOnClickListener(this);
        back.setOnLongClickListener(this);
        back.setOnTouchListener(this);
        clickArea1.setOnClickListener(this);
        clickArea2.setOnClickListener(this);
        clickArea3.setOnClickListener(this);
    }
}