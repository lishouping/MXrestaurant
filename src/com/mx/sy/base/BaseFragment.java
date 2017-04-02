package com.mx.sy.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mx.sy.dialog.SweetAlertDialog;

/**
 * Created by hk on 16/9/12.
 */
public abstract class BaseFragment extends Fragment
{
    /**
     * 贴附的activity
     */
    protected FragmentActivity mActivity;

    /**
     * 根view
     */
    protected View mRootView;

    /**
     * 是否对用户可见
     */
    protected boolean mIsVisible;
    /**
     * 是否加载完成
     * 当执行完oncreatview,View的初始化方法后方法后即为true
     */
    protected boolean mIsPrepare;

    public SweetAlertDialog sweetAlertDialog;
    @Override
    public void onAttach(Activity activity) {
    	mActivity = getActivity();
    	super.onAttach(activity);
    }
//    @Override
//    public void onAttach(Context context)
//    {
//        super.onAttach(context);
//
//        mActivity = getActivity();
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState)
    {
        if (mRootView == null){
            mRootView = inflater.inflate(setLayoutResouceId(), container, false);
        }else{
            // 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，
            // 要不然会发生这个rootview已经有parent的错误。
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (parent != null)
            {
                parent.removeView(mRootView);
            }

        }
        sweetAlertDialog = new SweetAlertDialog(getActivity() ,SweetAlertDialog.PROGRESS_TYPE);

        initData(getArguments());

        initView();

        mIsPrepare = true;

        onLazyLoad();

        setListener();

        return mRootView;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	loadDate();
    	super.onActivityCreated(savedInstanceState);
    }
    protected void loadDate(){
    	
    }
    /**
     * 初始化数据
     * @author 漆可
     * @date 2016-5-26 下午3:57:48
     * @param arguments 接收到的从其他地方传递过来的参数
     */
    protected void initData(Bundle arguments)
    {

    }

    /**
     * 初始化View
     * @author 漆可
     * @date 2016-5-26 下午3:58:49
     */
    protected void initView()
    {

    }

    /**
     * 设置监听事件
     * @author 漆可
     * @date 2016-5-26 下午3:59:36
     */
    protected void setListener()
    {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);

        this.mIsVisible = isVisibleToUser;

        if (isVisibleToUser)
        {
            onVisibleToUser();
        }
    }

    /**
     * 用户可见时执行的操作
     * @author 漆可
     * @date 2016-5-26 下午4:09:39
     */
    protected void onVisibleToUser()
    {
        if (mIsPrepare && mIsVisible)
        {
            onLazyLoad();
        }
    }

    /**
     * 懒加载，仅当用户可见切view初始化结束后才会执行
     * @author 漆可
     * @date 2016-5-26 下午4:10:20
     */
    protected void onLazyLoad()
    {

    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T findViewById(int id)
    {
        if (mRootView == null)
        {
            return null;
        }

        return (T) mRootView.findViewById(id);
    }

    /**
     * 设置根布局资源id
     * @author 漆可
     * @date 2016-5-26 下午3:57:09
     * @return
     */
    protected abstract int setLayoutResouceId();

    /**
     * [简化Toast]
     * @param msg
     */
    protected void showToast(String msg){
        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
    }
    /**
     * [进度条开始]
     *
     * @param
     */
    public void showDilog(String title){
        if (title.equals("")){
            sweetAlertDialog.setTitleText("加载中...");
        }else {
            sweetAlertDialog.setTitleText(title);
        }
        sweetAlertDialog.show();
    };
    /**
     * [进度条结束]
     *
     * @param
     */
    public  void dissmissDilog( ){
        sweetAlertDialog.dismiss();
    };
}