package net.diyigemt.roll;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import net.diyigemt.meabuttontest.MeaSounds;
import net.diyigemt.meabuttontest.requestresult.IRequestPermissionsResult;
import net.diyigemt.meabuttontest.requestresult.RequestPermissionsResultSetApp;
import net.diyigemt.meabuttontest.utils.CrashHandler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity{
  private int rolls = 1;
  private int target = 10;
  private int extend = 0;
  private boolean isDebug = false;
  private TextView log;
  private Intent numpad;
  private EditText text;
  EditText data1;
  EditText data2;
  private Random random = new Random();
  private String logString = "";
  public static int NumPadStatus = 1;
  IRequestPermissionsResult requestPermissionsResult = RequestPermissionsResultSetApp.getInstance();//动态权限请求结果处理
  private enum DataType {
    ROLL, TARGET, EXTEND
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    this.log = (TextView) findViewById(R.id.textViewLog);
    this.log.setMovementMethod(ScrollingMovementMethod.getInstance());

    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    data1 = (EditText) findViewById(R.id.data1);
    data2 = (EditText) findViewById(R.id.data2);
    data1.setOnTouchListener(touchListener);
    data2.setOnTouchListener(touchListener);

    IntentFilter filter = new IntentFilter();
    filter.addAction(Numpad.ACTION);
    registerReceiver(broadcastReceiver, filter);
    numpad = new Intent(MainActivity.this, Numpad.class);

    //errLOG
    CrashHandler crashHandler = CrashHandler.getInstance();
    crashHandler.init(getApplicationContext());
  }

  @Override
  protected void onStart() {
    new MeaSounds(MainActivity.this);
    MeaSounds.StartDownloadService(MainActivity.this);
    super.onStart();
  }

  @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            //用户给APP授权的结果
        //判断grantResults是否已全部授权，如果是，执行相应操作，如果否，提醒开启权限
        if(requestPermissionsResult.doRequestPermissionsResult(this, permissions, grantResults)){
        //请求的权限全部授权成功，此处可以做自己想做的事了
        //输出授权结果
        Toast.makeText(MainActivity.this,"授权成功",Toast.LENGTH_LONG).show();
        new MeaSounds(MainActivity.this);
        MeaSounds.StartDownloadService(MainActivity.this);
        }else{
        //输出授权结果
        Toast.makeText(MainActivity.this,"请给APP授权，否则功能无法正常使用！", Toast.LENGTH_LONG).show();
        }
    }

    @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.activity_main_actions, menu);
    final MenuItem toggleservice = menu.findItem(R.id.app_bar_switch);
    final Switch actionView = (Switch) toggleservice.getActionView();
    actionView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      Button changebtn = (Button) findViewById(R.id.btnChangeRoll);
      Button wipe_log = (Button) findViewById(R.id.btnResetLog);

      @Override
      public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        isDebug = isChecked;
        if (isDebug) {
          changebtn.setVisibility(View.VISIBLE);
          wipe_log.setVisibility(View.VISIBLE);
        } else {
          changebtn.setVisibility(View.INVISIBLE);
          wipe_log.setVisibility(View.INVISIBLE);
        }
        switchLog();
      }
    });
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    unregisterReceiver(broadcastReceiver);
  }

  BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      Button rollbtn = (Button) findViewById(R.id.btnStartRoll);
      switch (intent.getExtras().getString("data")) {
        case "-1":
          String contain = text.getText().toString();
          if (contain.length() > 0) {
            text.setText(contain.substring(0, contain.length() - 1));
          }
          break;
        case "10":
          roll(rollbtn);
          break;
        case "11":
          text = (EditText) findViewById(R.id.data1);
          data1.requestFocus();
          break;
        case "12":
          text = (EditText) findViewById(R.id.data2);
          data2.requestFocus();
          break;
        case "13":
          handleAllReset(rollbtn);
        case "-4":
          new Thread() {
            @Override
            public void run() {
              super.run();
              try {
                while (text.getText().toString().length() > 0 && Numpad.StopStatus == 1) {
                  MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                      if (Numpad.StopStatus == 0) {
                        String contain = text.getText().toString();
                        text.setText(contain.substring(0, contain.length() - 1));
                      }
                    }
                  });
                  Thread.sleep(250);
                }
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            }
          }.start();
          break;
        default:
          int num = Integer.valueOf(text.getText().toString() + intent.getExtras().getString("data"));
          if (text.getId() == R.id.data1) {
            rolls = num;
          } else {
            target = num;
          }
          text.setText(String.valueOf(num));
          break;
      }
    }
  };

  View.OnTouchListener touchListener = new View.OnTouchListener() {
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
      switch (view.getId()) {
        case R.id.data1:
          text = (EditText) findViewById(R.id.data1);
          Numpad.STATUS = "下一步";
          break;
        case R.id.data2:
          text = (EditText) findViewById(R.id.data2);
          Numpad.STATUS = "ROLL";
          break;
        default:
          break;
      }
      if (NumPadStatus == 1) {
        int inputType = text.getInputType();
        text.setInputType(InputType.TYPE_NULL);
        Intent numpad = new Intent(MainActivity.this, Numpad.class);
        startActivity(numpad);
        text.setInputType(InputType.TYPE_CLASS_NUMBER);
        text.setSelection(text.getText().length());
        NumPadStatus = 0;
      }
      return false;
    }
  };

  public void handleAllReset(View view) {
    new MeaSounds(MainActivity.this).clickEvent();
    this.target = 10;
    this.rolls = 1;
    EditText restext = (EditText) findViewById(R.id.data_res);
    restext.setText("-1");
    this.changeDataShow(view);
  }

  public void changeRoll(View view) {
    long seed = System.nanoTime();
    random.setSeed(seed);
    if (isDebug) {
      appendSeedLog(seed);
    } else {
      appendLog("骰子已经重置");
    }
  }

  private void changeData(View view, DataType type, int num) {
    switch (type) {
      case ROLL: {
        int res = num;
        if (res < 1) {
          Toast.makeText(this, "骰子数量不能小于1!", Toast.LENGTH_LONG).show();
          res = 1;
        }
        this.rolls = res;
        if (isDebug) {
          this.appendLog("设置骰子数量为" + res);
        }
        break;
      }
      case TARGET: {
        int res = num;
        if (res < 1) {
          Toast.makeText(this, "目标不能小于1!", Toast.LENGTH_LONG).show();
          res = 10;
        }
        this.target = res;
        if (isDebug) {
          this.appendLog("设置骰子面数为" + res);
        }
        break;
      }
      case EXTEND: {
        if (num < 0) {
          Toast.makeText(this, "结果可能小于0!", Toast.LENGTH_LONG).show();
        }
        this.extend = num;
        if (isDebug) {
          this.appendLog("设置保底为" + num);
        }
        break;
      }
      default:
        break;
    }
    this.changeDataShow(view);
  }

  private void changeDataShow(View view) {
    EditText textView1 = (EditText) findViewById(R.id.data1);
    EditText textView2 = (EditText) findViewById(R.id.data2);
    textView1.setText(this.rolls + "");
    textView2.setText(this.target + "");
  }

  public void roll(View view) {
    new MeaSounds(MainActivity.this).clickEvent((String log) -> {
      appendLog("播放音频:" + log);
    });
    EditText data1 = (EditText) findViewById(R.id.data1);
    EditText data2 = (EditText) findViewById(R.id.data2);
    EditText restext = (EditText) findViewById(R.id.data_res);
    String s1 = data1.getText().toString();
    String s2 = data2.getText().toString();
    int rolls = this.rolls = s1.equals("") ? 1 : Integer.parseInt(s1);
    int target = this.target = s2.equals("") ? 10 : Integer.parseInt(s2);
    changeDataShow(view);
    StringBuffer sb = new StringBuffer();

    sb.append(rolls).append("d").append(target).append("=");
    long seed = System.nanoTime();
    random.setSeed(seed);

    if (this.isDebug) {
      appendSeedLog(seed);
    }
    if (rolls == 1) {
      int res = (random.nextInt(target) + 1);
      sb.append(res);
      if (extend != 0) sb.append("+").append(extend).append("=").append(res + extend);
      restext.setText(String.valueOf(res));
      appendLog(sb.toString());
      return;
    }
    int sum = 0;
    for (int i = 0; i < rolls; i++) {
      int res = random.nextInt(target) + 1;
      sum += res;
      sb.append(res);
      if (i + 1 == rolls) break;
      sb.append("+");
    }
    if (extend != 0) sb.append("+").append(extend);
    sb.append("=").append((sum + extend));
    appendLog(sb.toString());

    restext.setText(String.valueOf(sum));
  }

  private void appendLog(String log) {
    DateFormat dataFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS", Locale.CHINA);
    String time = dataFormatter.format(new Date());
    StringBuilder sb = new StringBuilder();
    String tmp = this.logString;
    sb.insert(0, log + "\n");
    sb.insert(0, time + "\n");
    sb.append(tmp);
    this.logString = sb.toString();
    if (isDebug) {
      this.resetLog();
      this.log.append(sb.toString());
    }
  }

  private void appendSeedLog(long seed) {
    appendLog("更新生成种子为" + seed);
  }

  private void switchLog() {
    this.resetLog();
    if (this.isDebug) {
      this.log.append(logString);
    }
  }

  public void resetLog(View view) {
    this.logString = "";
    this.resetLog();
  }

  private void resetLog() {
    this.log.setText("");
    this.log.scrollTo(0, 0);
  }
}