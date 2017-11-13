package com.example.gaoming.commonlanguage;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.ActionMode;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText mEditText;
    private ListPopupWindow mListPop;
    private List<String> lists = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lists.add("one");
        lists.add("two");
        lists.add("three");
        mEditText = (EditText) findViewById(R.id.common_language_edittext);

        mListPop = new ListPopupWindow(this);
        mListPop.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, lists));
        mListPop.setWidth(ActionBar.LayoutParams.WRAP_CONTENT);
        mListPop.setHeight(ActionBar.LayoutParams.WRAP_CONTENT);
        //设置ListPopupWindow的锚点，即关联PopupWindow的显示位置和这个锚点
        mListPop.setAnchorView(mEditText);
        mListPop.setModal(true);//设置是否是模式
        mListPop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                int index = mEditText.getSelectionStart();//获取光标所在位置
                Editable edit = mEditText.getEditableText();//获取EditText的文字
                if (index < 0 || index >= edit.length() ){
                    edit.append(lists.get(position));
                }else{
                    edit.insert(index,lists.get(position));//光标所在位置插入文字
                }
                mListPop.dismiss();
            }
        });

        mEditText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                MenuInflater menuInflater = actionMode.getMenuInflater();
                menuInflater.inflate(R.menu.select_menu,menu);
                return true;//返回false则不会显示弹窗
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.common_language:
                        //mListPop.show();
                        popWindow();
                        Toast.makeText(MainActivity.this,
                                "点击的是22", Toast.LENGTH_SHORT).show();
                        actionMode.finish();//收起操作菜单
                        break;
                }
                return false;//返回true则系统的"复制"、"搜索"之类的item将无效，只有自定义item有响应
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {

            }
        });
    }

    private void popWindow() {
        //获取一个填充器
        LayoutInflater inflater = LayoutInflater.from(this);
        //填充我们自定义的布局
        View view = inflater.inflate(R.layout.common_language_container, null);
        //得到当前屏幕的显示器对象
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = getWindowManager().getDefaultDisplay();
        //创建一个Point点对象用来接收屏幕尺寸信息
        Point size = new Point();
        //Point点对象接收当前设备屏幕尺寸信息
        display.getSize(size);
        int width = size.x;//从Point点对象中获取屏幕的宽度(单位像素)
        int height = size.y;//从Point点对象中获取屏幕的高度(单位像素)
        //width=480,height=854可知手机的像素是480x854的
        //创建一个PopupWindow对象，第二个参数是设置宽度的，用刚刚获取到的屏幕宽度乘以2/3，
        // 取该屏幕的2/3宽度，从而在任何设备中都可以适配，高度则包裹内容即可，最后一个参数是设置得到焦点
        Log.v("zxy", "width=" + width + ",height=" + height);
        PopupWindow popWindow = new PopupWindow(view, 2 * width / 3,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        //设置PopupWindow的背景为一个空的Drawable对象，
        // 如果不设置这个，那么PopupWindow弹出后就无法退出了
        popWindow.setBackgroundDrawable(new BitmapDrawable());
        //设置是否点击PopupWindow外退出PopupWindow
        popWindow.setOutsideTouchable(true);
        //创建当前界面的一个参数对象
        WindowManager.LayoutParams params = getWindow().getAttributes();
        //设置参数的透明度为0.8，透明度取值为0~1，1为完全不透明，
        // 0为完全透明，因为android中默认的屏幕颜色都是纯黑色的，
        // 所以如果设置为1，那么背景将都是黑色，设置为0，背景显示我们的当前界面
        params.alpha = 0.8f;
        //把该参数对象设置进当前界面中
        getWindow().setAttributes(params);
        //设置PopupWindow退出监听器
        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //如果PopupWindow消失了，即退出了，那么触发该事件，然后把当前界面的透明度设置为不透明
                WindowManager.LayoutParams params = getWindow().getAttributes();
                //设置为不透明，即恢复原来的界面
                params.alpha = 1.0f;
                getWindow().setAttributes(params);
            }
        });
        //第一个参数为父View对象，即PopupWindow所在的父控件对象，
        // 第二个参数为它的重心，后面两个分别为x轴和y轴的偏移量
        popWindow.showAtLocation(inflater.inflate(R.layout.activity_main, null),
                Gravity.CENTER, 0, 0);

    }
}
