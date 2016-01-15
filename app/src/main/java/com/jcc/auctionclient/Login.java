package com.jcc.auctionclient;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jcc.auctionclient.util.DialogUtil;
import com.jcc.auctionclient.util.HttpCallbackListener;
import com.jcc.auctionclient.util.HttpUtil;

import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    private EditText etName, etPass;
    Button bnLogin, bnCancel;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x111:
                    Intent intent = new Intent(Login.this, AuctionClientActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case 0x222:
                    DialogUtil.showDialog(Login.this, (String)msg.obj, false);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etName = (EditText) findViewById(R.id.userEditText);
        etPass = (EditText) findViewById(R.id.pwdEditText);
        bnLogin = (Button)findViewById(R.id.btnLogin);
        bnCancel = (Button)findViewById(R.id.btnCancel);
        bnCancel.setOnClickListener(new HomeListener(this));
        bnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    String username = etName.getText().toString();
                    String pwd = etPass.getText().toString();
                    query(username, pwd);
                }
            }
        });

        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
    }

    // 对用户输入的用户名、密码进行校验
    private boolean validate()
    {
        String username = etName.getText().toString().trim();
        if (username.equals(""))
        {
            DialogUtil.showDialog(this, "用户账户是必填项！", false);
            return false;
        }
        String pwd = etPass.getText().toString().trim();
        if (pwd.equals(""))
        {
            DialogUtil.showDialog(this, "用户口令是必填项！", false);
            return false;
        }
        return true;
    }

    // 定义发送请求的方法
    private void query(String username, String password)
    {
        // 使用Map封装请求参数
        Map<String, String> map = new HashMap<String, String>();
        map.put("user", username);
        map.put("pass", password);
        // 定义发送请求的URL
        String url = HttpUtil.BASE_URL + "login.jsp";
        // 发送请求
        HttpUtil.submitPostData(url, map, "GBK", new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    // 如果userId 大于0
                    if (jsonObj.getInt("userId") > 0) {
                        Message msg = new Message();
                        msg.what = 0x111;
                        msg.obj = "OK";
                        handler.sendMessage(msg);
                    } else {
                        Message msg = new Message();
                        msg.what = 0x222;
                        msg.obj = "用户名称或密码错误，请重新输入！";
                        handler.sendMessage(msg);
                    }
                }catch (Exception e){
                    Message msg = new Message();
                    msg.what = 0x222;
                    msg.obj = "异常，请稍后再试！" + e.getMessage();
                    handler.sendMessage(msg);
                }
            }

            @Override
            public void onError(Exception e) {
                Message msg = new Message();
                msg.what = 0x222;
                msg.obj = "服务器响应异常，请稍后再试！" + e.getMessage();
                handler.sendMessage(msg);
                e.printStackTrace();
            }
        });
    }
}
