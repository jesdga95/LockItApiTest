package com.jedga.lockitapitest;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

    public final static String ACCESS_GRANTED = "com.jedga.lockit.ACCESS_GRANTED";
    public final static String ACCESS_DENIED = "com.jedga.lockit.ACCESS_DENIED";
    public final static String BROADCASTING = "broadcasting";
    public final static String PASSWORD = "password";

    private LockItReceiver mLockItReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        mLockItReceiver = new LockItReceiver();

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACCESS_GRANTED);
        filter.addAction(ACCESS_DENIED);

        registerReceiver(mLockItReceiver, filter);

        final EditText text = (EditText) findViewById(R.id.editText);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setComponent(ComponentName.unflattenFromString("com.jedga.lockit/.security.PasswordActivity"));
                intent.putExtra(BROADCASTING, true);
                String psw = text.getText().toString();
                intent.putExtra(PASSWORD, psw.isEmpty() ? 123456 : Integer.parseInt(psw));
                startActivity(intent);
            }
        });
    }

    private static class LockItReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(ACCESS_GRANTED)) {
                Toast.makeText(context, R.string.pass, Toast.LENGTH_LONG).show();
            } else if(intent.getAction().equals(ACCESS_DENIED)) {
                Toast.makeText(context, R.string.fail, Toast.LENGTH_LONG).show();
            }
        }
    }
}
