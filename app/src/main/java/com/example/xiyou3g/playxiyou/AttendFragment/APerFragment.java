package com.example.xiyou3g.playxiyou.AttendFragment;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.xiyou3g.playxiyou.Activity.AttenLoginActivity;
import com.example.xiyou3g.playxiyou.Adapter.CheckInforAdapter;
import com.example.xiyou3g.playxiyou.Adapter.PieAdapter;
import com.example.xiyou3g.playxiyou.HttpRequest.GetCheckInfor;
import com.example.xiyou3g.playxiyou.R;
import com.example.xiyou3g.playxiyou.Utils.HandleCheckInfor;
import com.example.xiyou3g.playxiyou.Utils.LogUtils;

import java.io.IOException;
import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.xiyou3g.playxiyou.Content.AttenContent.CheckList;
import static com.example.xiyou3g.playxiyou.Content.AttenContent.attendPerBean;
import static com.example.xiyou3g.playxiyou.Content.AttenContent.islogin;

/**
 * Created by Lance
 * on 2017/7/20.
 */

public class APerFragment extends Fragment {
    @BindView(R.id.attend_image)
    ImageView aimage;

    @BindView(R.id.aname)
    TextView aname;

    @BindView(R.id.asex)
    TextView asex;

    @BindView(R.id.anum)
    TextView anum;

    @BindView(R.id.aacade)
    TextView aacadem;

    @BindView(R.id.aphone)
    TextView aphone;

    @BindView(R.id.amajor)
    TextView amajor;

    @BindView(R.id.aclass)
    TextView aclass;

    @BindView(R.id.aidentify)
    TextView aidenti;

    @BindView(R.id.aexit)
    Button aexit;

    @BindView(R.id.isCheckInfor)
    TextView isHasCheckInfor;

    //考勤信息统计RecycleView;
    @BindView(R.id.checkRec)
    RecyclerView checkList;

    //图表的考勤信息统计Charts;
    @BindView(R.id.chartsRecycleView)
    RecyclerView chartsRecycler;

    private CheckInforAdapter adapter;
    public static PreHandler checkHandler;
    private PieAdapter pieAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogUtils.INSTANCE.e("Attend:","success");
        if(CheckList.size() == 0){
            GetCheckInfor.INSTANCE.getCheckInfor(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    HandleCheckInfor.INSTANCE.handleCheckInfor(response);
                }
            });
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.atten_per_fragment,container,false);
        ButterKnife.bind(this, view);
        initWight();

        checkHandler = new PreHandler(isHasCheckInfor, checkList, adapter, pieAdapter);
        return view;
    }

    public static class PreHandler extends Handler {
        private WeakReference<TextView> textViewWeak;
        private WeakReference<RecyclerView> recyclerViewWeak;
        private WeakReference<CheckInforAdapter> checkInforAdapterWeak;
        private WeakReference<PieAdapter> pieAdapterWeak;

        PreHandler(TextView textView, RecyclerView recyclerView, CheckInforAdapter checkInforAdapter, PieAdapter pieAdapter) {
            textViewWeak = new WeakReference<>(textView);
            recyclerViewWeak = new WeakReference<>(recyclerView);
            checkInforAdapterWeak = new WeakReference<>(checkInforAdapter);
            pieAdapterWeak = new WeakReference<>(pieAdapter);
        }

        @Override
        public void handleMessage(Message msg) {
            TextView isHasCheckInfor = textViewWeak.get();
            RecyclerView checkList = recyclerViewWeak.get();
            CheckInforAdapter adapter = checkInforAdapterWeak.get();
            PieAdapter pieAdapter = pieAdapterWeak.get();
            if (isHasCheckInfor != null && checkList != null && adapter != null && pieAdapter != null) {
                switch (msg.what){
                    case 72:
                        //LogUtils.INSTANCE.e("checkInfor:",CheckList.size()+"  "+CheckList.get(2).getCourseName());
                        if(CheckList.size() > 0){
                            isHasCheckInfor.setVisibility(View.GONE);
                            checkList.setVisibility(View.VISIBLE);
                        }
                        adapter.notifyDataSetChanged();
                        pieAdapter.notifyDataSetChanged();
                        break;
                }
            }
        }
    }

    private void initWight() {
        aimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(getContext(),R.style.DialogTheme);
                dialog.setContentView(R.layout.look_imageview);
                ImageView lookView = (ImageView) dialog.findViewById(R.id.originView);
                Glide.with(getContext())
                        .load("http://jwkq.xupt.edu.cn:8080/Common/GetPhotoByBH?xh="+attendPerBean.getNum())
                        .fitCenter()
                        .into(lookView);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            }
        });

        if(CheckList.size() > 0){
            isHasCheckInfor.setVisibility(View.GONE);
        }else{
            isHasCheckInfor.setVisibility(View.VISIBLE);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        checkList.setLayoutManager(linearLayoutManager);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        chartsRecycler.setLayoutManager(gridLayoutManager);

        adapter = new CheckInforAdapter(CheckList);
        checkList.setAdapter(adapter);
        pieAdapter = new PieAdapter(CheckList);
        chartsRecycler.setAdapter(pieAdapter);

        aexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                islogin = 0;
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("温馨提示：");
                builder.setMessage("注销成功！");
                builder.setPositiveButton("继续", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        attenHandler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                Log.e("exitsuccess","click");
//                                FragmentManager manager = getActivity().getSupportFragmentManager();
//                                FragmentTransaction transaction = manager.beginTransaction();
//                                transaction.replace(R.id.main_container,new AttendUnlogFragment());
//                                transaction.commit();
//                            }
//                        },500);
                        Intent intent = new Intent(getContext(),AttenLoginActivity.class);
                        startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                        getActivity().finish();
                    }
                }).create().show();
            }
        });

        Glide.with(getContext())
                .load("http://jwkq.xupt.edu.cn:8080/Common/GetPhotoByBH?xh="+attendPerBean.getNum())
                .error(R.drawable.schoollogo)
                .into(aimage);
        aname.setText(attendPerBean.getName());
        asex.setText(attendPerBean.getSex());
        anum.setText(attendPerBean.getNum());
        aacadem.setText(attendPerBean.getAcademy());
        aphone.setText(attendPerBean.getPhone());
        amajor.setText(attendPerBean.getMajor());
        aclass.setText(attendPerBean.getClassroom());
        aidenti.setText(attendPerBean.getIdentity());
    }
}
