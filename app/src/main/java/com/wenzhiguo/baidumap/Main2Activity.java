package com.wenzhiguo.baidumap;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * popupwindow的展示
 */
public class Main2Activity extends AppCompatActivity implements View.OnClickListener {

    private Button mDevice;
    private Button mGourmand;
    private Button mBauble;
    private int count;
    private String[] list1 = {"土豆", "茄子", "芹菜", "西红柿"};
    private String[] list2 = {"充电器", "充电线", "插排", "电脑", "手机", "充电头"};
    private String[] list3 = {"玩具枪", "玩具车", "超人"};
    private List<Map<String, String>> mListems1 = new ArrayList<>();
    private List<Map<String, String>> mListems2 = new ArrayList<>();
    private List<Map<String, String>> mListems3 = new ArrayList<>();
    private SimpleAdapter simpleAdapter1;
    private SimpleAdapter simpleAdapter2;
    private SimpleAdapter simpleAdapter3;
    //popuwindow所用的listView,PopupWindow对象
    private ListView mPopListView;
    private PopupWindow mPopupMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //找控件
        initView();
        //数据
        initData();
        //弹框
        initPopMenu();
    }

    private void initPopMenu() {
        //把包裹的ListVIew的布局XML文件转换为VIew对象
        View popView = LayoutInflater.from(this).inflate(R.layout.item, null);
        //创建popuwindow对象,参数1popuwindow要显示的布局,参数2,3:定义popuwindow所占用的宽高
        mPopupMenu = new PopupWindow(popView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        //设置popuwindow外部可以点击              true:可以，      false:不可以
        mPopupMenu.setOutsideTouchable(true);
        //是popuwindow里填充的listView拥有焦点
        mPopupMenu.setFocusable(true);
        //如果想要让popuwindow具有一些操作,比如动画效果之类的,必须给popuwindow设置背景
        mPopupMenu.setBackgroundDrawable(new ColorDrawable());
        //设置popuwindow的动画效果
        mPopupMenu.setAnimationStyle(R.style.popwin_anim_style);
        //设置popuwindow结束时的监听事件
        mPopupMenu.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //设置TextView的颜色,把所有LinearLayout的文本颜色该为灰色
                mGourmand.setTextColor(Color.parseColor("#5a5959"));
                mDevice.setTextColor(Color.parseColor("#5a5959"));
                mBauble.setTextColor(Color.parseColor("#5a5959"));
            }
        });
        //点击popuwindow以外的区域,使popuwindow消失(实际就是让ListView下面的LinerLayout,设置点击事件)
        LinearLayout list_bottom = (LinearLayout) popView.findViewById(R.id.linear5);
        list_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //当LinerLayout被点击时,这popuwindow消失
                mPopupMenu.dismiss();
            }
        });
        //获取listVIew对象
        mPopListView = (ListView) popView.findViewById(R.id.listview);
        //三种适配器
        simpleAdapter1 = new SimpleAdapter(Main2Activity.this, mListems1,
                R.layout.item_item, new String[]{"name"}, new int[]{R.id.text});
        simpleAdapter2 = new SimpleAdapter(Main2Activity.this, mListems2,
                R.layout.item_item, new String[]{"name"}, new int[]{R.id.text});
        simpleAdapter3 = new SimpleAdapter(Main2Activity.this, mListems3,
                R.layout.item_item, new String[]{"name"}, new int[]{R.id.text});
        //设置Popuwindow里的listView点击事件,当点击listVIew里的一个item时,把这个item数据显示到最上方
        mPopListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long ld) {
                //首先让Popuwindow消失
                mPopupMenu.dismiss();
                //设置一个标识,以方便做对应LinerLayout的点击事件.
                switch (count) {
                    case 1:
                        //获取点击对应条目的文本数据
                        String name = mListems1.get(position).get("name");
                        //把文本数据设置到原始布局里的文本
                        mGourmand.setText(name);
                        break;
                    case 2:
                        String name1 = mListems2.get(position).get("name");
                        mDevice.setText(name1);
                        break;
                    case 3:
                        String name2 = mListems3.get(position).get("name");
                        mBauble.setText(name2);
                        break;
                }
            }
        });
    }

    //数据
    private void initData() {
        //创建一个存放popupwindow加载数据的大盒子1,Map集合(键,值)
        //创建一个小盒子,放编号和值
        Map<String, String> map1;
        for (int i = 0; i < list1.length; i++) {
            map1 = new HashMap<String, String>();
            map1.put("name", list1[i]);
            mListems1.add(map1);
        }
        Map<String, String> map2;
        for (int i = 0; i < list2.length; i++) {
            map2 = new HashMap<String, String>();
            map2.put("name", list2[i]);
            mListems2.add(map2);
        }
        Map<String, String> map3;
        for (int i = 0; i < list3.length; i++) {
            map3 = new HashMap<String, String>();
            map3.put("name", list3[i]);
            mListems3.add(map3);
        }
    }

    private void initView() {
        mDevice = (Button) findViewById(R.id.device);
        mGourmand = (Button) findViewById(R.id.gourmand);
        mBauble = (Button) findViewById(R.id.bauble);
        //监听事件
        mDevice.setOnClickListener(this);
        mGourmand.setOnClickListener(this);
        mBauble.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //美食
            case R.id.gourmand:
                mPopupMenu.showAsDropDown(mGourmand, 0, 2);
                mPopListView.setAdapter(simpleAdapter1);
                count = 1;
                break;
            //电器
            case R.id.device:
                mPopupMenu.showAsDropDown(mDevice, 0, 2);
                mPopListView.setAdapter(simpleAdapter2);
                count = 2;
                break;
            //玩具
            case R.id.bauble:
                mPopupMenu.showAsDropDown(mBauble, 0, 2);
                mPopListView.setAdapter(simpleAdapter3);
                count = 3;
                break;
        }
    }

}
