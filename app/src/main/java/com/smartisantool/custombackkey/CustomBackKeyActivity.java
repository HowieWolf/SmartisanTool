package com.smartisantool.custombackkey;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import com.smartisantool.R;

import me.xiaopan.switchbutton.SwitchButton;

public class CustomBackKeyActivity extends AppCompatActivity {

    SwitchButton customBackPower;
    SeekBar doubleBackSpaceProgress;
    boolean isAccessibilityServiceOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custombackkey);

        initView();
    }

    private void initView() {
        customBackPower = (SwitchButton) findViewById(R.id.customBackPower);
        customBackPower.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == isAccessibilityServiceOn){
                    changePower();
                }
            }
        });

        doubleBackSpaceProgress = (SeekBar) findViewById(R.id.doubleBackSpaceProgress);
        doubleBackSpaceProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int newProgress = seekBar.getProgress();
                //Log.i("MYTAG", newProgress + "");
                CustomBackKeyAccessibilityService.DOUBLE_BACK_SPACE = CustomBackKeyAccessibilityService
                        .DOUBLE_BACK_SPACE_MIN + newProgress;
            }
        });

    }

    @Override
    protected void onResume() {
        isAccessibilityServiceOn = isAccessibilitySettingsOn();
        customBackPower.setChecked(!isAccessibilityServiceOn);
        doubleBackSpaceProgress.setEnabled(isAccessibilityServiceOn);
        doubleBackSpaceProgress.setProgress(CustomBackKeyAccessibilityService.DOUBLE_BACK_SPACE -
                CustomBackKeyAccessibilityService.DOUBLE_BACK_SPACE_MIN);
        super.onResume();
    }

    // 此方法用来判断当前应用的辅助功能服务是否开启
    public boolean isAccessibilitySettingsOn() {
        int accessibilityEnabled = 0;
        try {
            accessibilityEnabled = Settings.Secure.getInt(getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            Log.i("MYTAG", e.getMessage());
        }

        if (accessibilityEnabled == 1) {
            String services = Settings.Secure.getString(getContentResolver(), Settings.Secure
                    .ENABLED_ACCESSIBILITY_SERVICES);
            if (services != null) {
                return services.toLowerCase().contains(getPackageName().toLowerCase());
            }
        }

        return false;
    }

    private void changePower() {
        startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
    }

}
