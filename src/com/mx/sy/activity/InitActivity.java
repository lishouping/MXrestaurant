package com.mx.sy.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.Header;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mx.sy.R;
import com.mx.sy.api.ApiConfig;
import com.mx.sy.base.BaseActivity;
import com.mx.sy.dialog.SweetAlertDialog;

/**
* <p>Title: InitActivity<／p>
* <p>Description: <／p>
* <p>Company: LTGames<／p> 
* @author lishouping
* @date 2017年4月2日
 */
public class InitActivity extends BaseActivity {


	public SweetAlertDialog sweetAlertDialog;
	
	private SharedPreferences preferences;
	
    private String newApkUrl;
    private Dialog downloadDialog;
    /* 下载包安装路径 */
    @SuppressLint("SdCardPath")
    private static final String savePath = "/sdcard/SxstUpdate/";
    private static final String saveFileName = savePath + "SXST.apk";
    /* 进度条与通知ui刷新的handler和msg常量 */
    private ProgressBar mProgress;
    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;
    private int progress;
    private Thread downLoadThread;
    private boolean interceptFlag = false;
    
    String autoLogin;
	String telephone;
	@Override
	public void widgetClick(View v) {
		// TODO Auto-generated method stub

	}
	@Override
	public void initParms(Bundle parms) {
		// TODO Auto-generated method stub
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
				startActivity(intent);
				finish();
			}
		}, 3000);
	}
	@Override
	public View bindView() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int bindLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_init;
	}
	@Override
	public void initView(View view) {
		// TODO Auto-generated method stub
		//showDilog("");
		
		preferences = getSharedPreferences("userinfo", LoginActivity.MODE_PRIVATE); 
		autoLogin = preferences.getString("autoLogin", "");// 记住密码
		telephone = preferences.getString("telephone", "");
		

		
	}
	@Override
	public void setListener() {
		// TODO Auto-generated method stub

	}
	@Override
	public void doBusiness(Context mContext) {
		// TODO Auto-generated method stub
		versionUpdate();
	}
	@Override
	protected void initdata() {
		// TODO Auto-generated method stub

	}
	
	// 获取版本号(内部识别号)
    public int getVersionCode(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return 0;
        }
    }
    
    private void versionUpdate(){
    	AsyncHttpClient client = new AsyncHttpClient();
		String url = ApiConfig.API_URL + ApiConfig.SERVICEUPDATE;
		client.get(url, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				if (arg0 ==200) {
					try {
						String response = new String(arg2, "UTF-8");
						JSONObject jsonObject = new JSONObject(response);
						String code = jsonObject.getString("CODE");
						JSONObject object = new JSONObject(jsonObject.getString("DATA"));
						if (code.equals("1000")) {
							dissmissDilog();
							 // 获取下载地址
							newApkUrl = object.getString("url");
			                updateDialog();
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						//				Intent intent = new Intent(getApplicationContext(),MainActivity.class);
						Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
						startActivity(intent);
						dissmissDilog();
						finish();
					}
				}, 3000);
			}
		});
    }
    
    protected void updateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(InitActivity.this);
        builder.setMessage("当前版本已过期，请下载最新版本");
        builder.setTitle("升级提示");
        builder.setPositiveButton("马上下载", new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showDownloadDialog();
            }
        });
//        builder.setNegativeButton("以后再说", new android.content.DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                InitActivity.this.finish();
//            }
//        });
        builder.show();
    }
    
    private void showDownloadDialog() {
        AlertDialog.Builder builder = new Builder(InitActivity.this);
        builder.setTitle("软件版本更新");
        final LayoutInflater inflater = LayoutInflater.from(InitActivity.this);
        View v = inflater.inflate(R.layout.progress, null);
        mProgress = (ProgressBar) v.findViewById(R.id.progress);
        builder.setView(v);
        builder.setNegativeButton("取消更新", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                interceptFlag = true;
            }
        });
        downloadDialog = builder.create();
        downloadDialog.show();
        downloadApk();
    }
    
    private Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL(newApkUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();

                File file = new File(savePath);
                if (!file.exists()) {
                    file.mkdir();
                }
                String apkFile = saveFileName;
                File ApkFile = new File(apkFile);
                FileOutputStream fos = new FileOutputStream(ApkFile);

                int count = 0;
                byte buf[] = new byte[1024];

                do {
                    int numread = is.read(buf);
                    count += numread;
                    progress = (int) (((float) count / length) * 100);
                    // 更新进度
                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                    if (numread <= 0) {
                        // 下载完成通知安装
                        mHandler.sendEmptyMessage(DOWN_OVER);
                        break;
                    }
                    fos.write(buf, 0, numread);
                } while (!interceptFlag);// 点击取消就停止下载.
                fos.close();
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            } 

        }
    };
    
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case DOWN_UPDATE:
                mProgress.setProgress(progress);
                break;
            case DOWN_OVER:
                installApk();
                break;
            default:
                break;
            }
        };
    };
    
    private void downloadApk() {
        downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }

    private void installApk() {
        File apkfile = new File(saveFileName);
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        InitActivity.this.startActivity(i);
    }


}
