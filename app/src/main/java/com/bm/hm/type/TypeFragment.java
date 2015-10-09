package com.bm.hm.type;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.bm.hm.R;
import com.bm.hm.bean.Type;
import com.bm.hm.constant.Urls;
import com.bm.hm.course.CourseActivity;
import com.bm.hm.http.BaseData;
import com.bm.hm.http.HttpVolleyRequest;
import com.bm.hm.me.MessageActivity;
import com.bm.hm.util.DialogUtils;
import com.bm.hm.view.NoScrollGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TypeFragment extends Fragment implements View.OnClickListener {

    private View view;
    private TextView tv_title;
    private ImageView iv_message;
    private NoScrollGridView gv_english_type, gv_video_type, gv_content_type;
    private Button btn_type_sure;
    public LinearLayout ll_video,ll__content;
    private List<Type> listLevel1,listLevel2,listLevel3;
    private TypeAdapter level1Adapter,level2Adapter,level3Adapter;
    private int selectLevel=0;
    private String levle1Id="",levle2Id="",levle3Ids="";
    private String levle1Name = "";

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

        initView();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();

        selectLevel = 0;
        getTypeList("0");
        listLevel2.clear();
        level2Adapter.notifyDataSetChanged();
        listLevel3.clear();
        level3Adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            return view;
        }

        if (view == null) {
            view = inflater.inflate(R.layout.fg_type, container, false);
        }

        return view;
    }

    private void initView() {
        tv_title = (TextView) view.findViewById(R.id.tv_type_title);
        tv_title.setText("分类");

        //右边的图片
        iv_message = (ImageView) view.findViewById(R.id.iv_message);
        iv_message.setOnClickListener(this);

        //二级和三级分类
        ll_video=(LinearLayout)view.findViewById(R.id.ll__video);
        ll__content=(LinearLayout)view.findViewById(R.id.ll__content);

        //英语分类
        gv_english_type = (NoScrollGridView) view.findViewById(R.id.gv_english_type);

        listLevel1 = new ArrayList<Type>();
        level1Adapter = new TypeAdapter(getActivity(),listLevel1);
        gv_english_type.setAdapter(level1Adapter);
        gv_english_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectLevel = 1;

                reset();
                listLevel1.get(position).setSelect(true);
                level1Adapter.notifyDataSetChanged();

                levle2Id = "";
                levle3Ids = "";

                levle1Name = listLevel1.get(position).name;
                if(position==0){
                    selectLevel = 0;
//                    levle1Id = "0";
                    getTypeList("0");
                }else{
                    levle1Id = String.valueOf(listLevel1.get(position).id);
                    getTypeList(Integer.toString(listLevel1.get(position).id));
                }
            }
        });

        //视频分类
        gv_video_type = (NoScrollGridView) view.findViewById(R.id.gv_video_type);
        listLevel2 = new ArrayList<Type>();
        level2Adapter = new TypeAdapter(getActivity(),listLevel2);
        gv_video_type.setAdapter(level2Adapter);

        gv_video_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectLevel = 2;

                reset();
                listLevel2.get(position).setSelect(true);
                level2Adapter.notifyDataSetChanged();

                levle2Id = String.valueOf(listLevel2.get(position).id);
                getTypeList(Integer.toString(listLevel2.get(position).id));
            }
        });

        //内容分类
        gv_content_type = (NoScrollGridView) view.findViewById(R.id.gv_content_type);
        listLevel3 = new ArrayList<Type>();
        level3Adapter = new TypeAdapter(getActivity(),listLevel3);
        gv_content_type.setAdapter(level3Adapter);
        gv_content_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                levle3Ids +=listLevel3.get(position).id+",";

                listLevel3.get(position).setSelect(true);
                level3Adapter.notifyDataSetChanged();
            }
        });

        btn_type_sure = (Button) view.findViewById(R.id.btn_type_sure);
        btn_type_sure.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_message:
                startActivity(new Intent(getActivity(), MessageActivity.class));
                break;
            case R.id.btn_type_sure:
                Intent intent = new Intent(getActivity(),CourseActivity.class);

                if(!levle3Ids.equals("")) {
                    if (levle3Ids.endsWith(",")) {
                        levle3Ids = levle3Ids.substring(0, levle3Ids.length() - 1);
                    }
                    intent.putExtra("level3TypeIds", levle3Ids);
                }else if(!levle2Id.equals("")){
                    intent.putExtra("level2TypeId", levle2Id);
                }else{
                    intent.putExtra("level1TypeId",levle1Id);
                }

                intent.putExtra("type",levle1Name);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void getTypeList(String pid) {
        DialogUtils.showProgressDialog("正在加载",getActivity());
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("pid",pid);
        HttpVolleyRequest<BaseData> request = new HttpVolleyRequest<BaseData>(getActivity());
        request.HttpVolleyRequestPost(Urls.GET_COURSE_TYPE, param, BaseData.class, Type.class,
                getTypeListSuccessListener(), null);

    }

    public Response.Listener<BaseData> getTypeListSuccessListener() {
        return new Response.Listener<BaseData>()
        {

            @Override
            public void onResponse(BaseData response) {
                DialogUtils.cancleProgressDialog();
                if (selectLevel == 0) {
                    listLevel1.clear();
                    listLevel1.add(new Type("全部", true));
                    listLevel1.addAll(response.data.list);
                    level1Adapter.notifyDataSetChanged();

                    levle1Name = "全部";
                    selectLevel = 1;
                    ll_video.setVisibility(View.INVISIBLE);
                    ll__content.setVisibility(View.INVISIBLE);
                    listLevel2.clear();
                    level2Adapter.notifyDataSetChanged();
                    listLevel3.clear();
                    level3Adapter.notifyDataSetChanged();
                }else if (selectLevel == 1) {
                    ll_video.setVisibility(View.VISIBLE);

                    listLevel2.clear();
                    listLevel2.addAll(response.data.list);
                    level2Adapter.notifyDataSetChanged();

                    selectLevel = 2;
                    if(listLevel2!=null && listLevel2.size()>0) {
                        listLevel2.get(0).setSelect(true);
                        levle2Id = String.valueOf(listLevel2.get(0).id);
                        getTypeList(String.valueOf(listLevel2.get(0).id));
                        ll_video.setVisibility(View.VISIBLE);
                    }else{
                        ll_video.setVisibility(View.INVISIBLE);
                    }
                }else if(selectLevel == 2){
                    ll__content.setVisibility(View.VISIBLE);

                    listLevel3.clear();
                    listLevel3.addAll(response.data.list);
                    level3Adapter.notifyDataSetChanged();

                    if(listLevel3!=null && listLevel3.size()>0) {
                        listLevel3.get(0).setSelect(true);
                        levle3Ids = listLevel3.get(0).id + ",";
                        ll__content.setVisibility(View.VISIBLE);
                    }else {
                        ll__content.setVisibility(View.INVISIBLE);
                    }
                }
            }
        };
    }

    private void reset(){
        if(selectLevel == 1){
            for (Type type : listLevel1){
                if(type.isSelect()){
                    type.setSelect(!type.isSelect());
                    break;
                }
            }
        }else if(selectLevel == 2){
            for (Type type : listLevel2){
                if(type.isSelect()){
                    type.setSelect(!type.isSelect());
                    break;
                }
            }
        }
    }

}
