package com.example.xiyou3g.playxiyou.Utils

import android.os.Message
import android.util.Log
import com.example.xiyou3g.playxiyou.AttendFragment.ACheckFragment
import com.example.xiyou3g.playxiyou.Content.AttenContent
import com.example.xiyou3g.playxiyou.Content.EduContent
import com.example.xiyou3g.playxiyou.DataBean.CheckBean
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by Lance
 * on 2017/9/6.
 */
object HandleAttCheck{

    fun hanleAttCheck(respone: Response){

        LogUtils.e("getAttendCheck3:","success")

        AttenContent.checkBeanList.clear()
        val responseBody = respone.body().string()
        Log.e("GetAttendCheck",responseBody)
        val jsonObject: JSONObject = JSONObject(responseBody)
        val total = jsonObject.get("total")
        LogUtils.e("total:",total.toString())

        if(total != "0"){
            val rowsArrays = jsonObject.get("rows") as JSONArray
            for(i in 0 until rowsArrays.length()){
                LogUtils.e("rows",rowsArrays.get(rowsArrays.length()-1-i).toString())
                val rows = JSONObject(rowsArrays.get(rowsArrays.length()-1-i).toString())
                LogUtils.e("rows:",rows.toString())
                val checkBean = CheckBean()
                checkBean.waterDate = rows.getString("WaterDate")
                checkBean.s_Name = rows.getString("S_Name")
                checkBean.s_Code = rows.getString("S_Code")
                checkBean.jT_No = rows.getString("JT_No")
                checkBean.roomNum = rows.getString("RoomNum")
                checkBean.bName = rows.getString("BName")
                checkBean.status = rows.getString("Status")

                checkBean.class_No = rows.getInt("Class_No")
                checkBean.sbh = rows.getInt("SBH")
                checkBean.rbh = rows.getInt("RBH")
                checkBean.term_No = rows.getString("Term_No")
                AttenContent.checkBeanList.add(checkBean)
                LogUtils.e("S_Name",checkBean.s_Name)
            }
            LogUtils.e("checkBeanListSize:",AttenContent.checkBeanList.size.toString())
            val message = Message()
            message.what = 91
            ACheckFragment.checkHandler.sendMessage(message)
        }
    }
}