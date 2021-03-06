package com.example.xiyou3g.playxiyou.Utils;

import android.os.Message;

import com.example.xiyou3g.playxiyou.DataBean.MajorBean;
import com.example.xiyou3g.playxiyou.MeFragment.MajorFragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import static com.example.xiyou3g.playxiyou.Content.EduContent.majorBeanList;

/**
 * Created by Lance
 * on 2017/9/5.
 */

public class HandleMajorData {

    public static void handleMajor(String s){
        Document document = Jsoup.parse(s);
        Elements tr = document.getElementsByTag("tr");
        Elements tr1 = tr.get(4).getElementsByTag("tr");
        if(tr.get(4).getElementsByTag("tr").size() == 15){
            for(int i = 2;i<4;i++){
                Elements td = tr1.get(i).getElementsByTag("td");
                MajorBean majorBean = new MajorBean();
                majorBean.setmNeedScore(td.get(1).text());
                majorBean.setmGetScore(td.get(2).text());
                majorBean.setmUngetScore(td.get(3).text());
                majorBean.setmWantScore(td.get(4).text());
                majorBeanList.add(majorBean);
            }
        }else{
            for(int i = 2;i<5;i ++){
                Elements td = tr1.get(i).getElementsByTag("td");
                MajorBean majorBean = new MajorBean();
                majorBean.setmNeedScore(td.get(1).text());
                majorBean.setmGetScore(td.get(2).text());
                majorBean.setmUngetScore(td.get(3).text());
                majorBean.setmWantScore(td.get(4).text());
                majorBeanList.add(majorBean);
            }
        }
        Message message = Message.obtain();
        message.what = 5;
        MajorFragment.majorHandler.sendMessage(message);
    }
}
