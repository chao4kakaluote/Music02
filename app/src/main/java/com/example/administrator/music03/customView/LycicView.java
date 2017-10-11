package com.example.administrator.music03.customView;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.example.administrator.music03.Utils.Utility;
import com.example.administrator.music03.entries.LrcMusic;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by 饶建雄 on 2016/8/21.
 */
public class LycicView extends ScrollView {
    LinearLayout rootView;
    LinearLayout lrcViewLayout;

    ArrayList<LrcMusic> lrcTextLsit = new ArrayList<LrcMusic>();
    ArrayList<Integer> heights = new ArrayList<Integer>();
    ArrayList<TextView> lrcTextViewList = new ArrayList<TextView>();

    public LycicView(Context context) {
        super(context);
    }

    public LycicView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LycicView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void getLrc(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String s = "";
            while ((s = br.readLine()) != null) {
                if (!TextUtils.isEmpty(s)) {
                    String lrc = s.replace("[", "");
                    String[] lrcAndTime = lrc.split("]");
                    if (lrcAndTime.length > 1) {
                        String time = lrcAndTime[0];
                        String lrcText = lrcAndTime[1];
                        LrcMusic lrcMusic = new LrcMusic(Utility.lrcData(time), lrcText);
                        lrcTextLsit.add(lrcMusic);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initView()
    {
       addFirstLayout();
     //   addLrcView();
     //   scrollTo(0,0);
    }
    public void addFirstLayout()
    {
        rootView=new LinearLayout(getContext());
        rootView.setOrientation(LinearLayout.VERTICAL);
        lrcViewLayout=new LinearLayout(getContext());
        lrcViewLayout.setOrientation(LinearLayout.VERTICAL);
        for(int i=0;i<lrcTextLsit.size();i++) {
            final TextView textView = new TextView(getContext());
            Log.d("lrctext", lrcTextLsit.get(i).getLrc());
            textView.setText(lrcTextLsit.get(i).getLrc());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
             params.gravity = Gravity.CENTER_HORIZONTAL;
            textView.setLayoutParams(params);
            lrcViewLayout.addView(textView);
            lrcTextViewList.add(textView);

            ViewTreeObserver vto = textView.getViewTreeObserver();
            final int index = i;
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
            {
                @Override
                public void onGlobalLayout()
                {
                    textView.getViewTreeObserver().removeOnGlobalLayoutListener(this);//api 要在16以上 >=16
                    heights.add(index,textView.getHeight());//将高度添加到对应的item位置
                }
            });
        }
        rootView.addView(lrcViewLayout);
        addView(rootView);
    }
    public void addLrcView()
    {
        lrcViewLayout=new LinearLayout(getContext());
        lrcViewLayout.setOrientation(LinearLayout.VERTICAL);

        lrcViewLayout.removeAllViews();
        for(int i=0;i<lrcTextLsit.size();i++)
        {
            final TextView textView=new TextView(getContext());
            Log.d("lrctext",lrcTextLsit.get(i).getLrc());
            textView.setText(lrcTextLsit.get(i).getLrc());
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//            params.gravity = Gravity.CENTER_HORIZONTAL;
//           // textView.setLayoutParams(params);
//            //对高度进行测量
//            ViewTreeObserver vto = textView.getViewTreeObserver();
//            final int index = i;
//            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
//            {
//                @Override
//                public void onGlobalLayout()
//                {
//                    textView.getViewTreeObserver().removeOnGlobalLayoutListener(this);//api 要在16以上 >=16
//                    heights.add(index,textView.getHeight());//将高度添加到对应的item位置
//                }
//            });
            lrcTextViewList.add(textView);
            lrcViewLayout.addView(textView);
        }
        rootView.addView(lrcViewLayout);
        this.addView(rootView);
    }
    public void scrollToTime(int currentTime,int TotalTime)
    {
        int index=(int)((float)currentTime/(float)TotalTime * lrcTextLsit.size());
        scrollToIndex(index);
        for(int i=0;i<lrcTextViewList.size();i++)
        {
            if(i==index)
            {
                lrcTextViewList.get(i).setTextColor(Color.BLUE);
            }
            else
            {
                lrcTextViewList.get(i).setTextColor(Color.WHITE);
            }
        }
    }
    public void scrollToIndex(int index)
    {
        if(index < 0){
            scrollTo(0,0);
        }
        //计算index对应的textview的高度
        if(index < heights.size()){
            int sum = 0;
            for(int i = 0;i<=index-1;i++){
                sum+=heights.get(i);
            }
            //加上index这行高度的一半
            sum+=heights.get(index)/2;
            scrollTo(0,sum);
        }
    }
}
