package com.zhongyong.jamod.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.reflect.TypeToken;
import com.zhongyong.jamod.apis.ApiClient;
import com.zhongyong.jamod.apis.MySubscriber;
import com.zhongyong.jamod.model.ModBusGateWayModel;
import com.zhongyong.smarthome.App;
import com.zhongyong.smarthome.R;
import com.zhongyong.smarthome.adapter.ViewHolder;
import com.zhongyong.smarthome.api.HttpResult;
import com.zhongyong.smarthome.base.BaseActivity;
import com.zhongyong.smarthome.base.BasicAdapter;
import com.zhongyong.smarthome.base.ItemMenuDeleteCreator;
import com.zhongyong.smarthome.utils.SharePreferenceUtils;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

import static com.zhongyong.jamod.apis.ApiClient.call;

/**
 * Created by fyc on 2018/1/2.
 */

public class ModBusGateWayListActivity extends BaseActivity {
    @Bind(R.id.gatewaysLv)
    SwipeMenuListView mMenuListView;
    @Bind(R.id.cab_titleBack_iv)
    ImageView backIv;
    List<ModBusGateWayModel> mList = new ArrayList<>();
    BasicAdapter<ModBusGateWayModel> mAdapter;
    View footer;
    String preferenceGateWay;
    String scene;
    ModBusGateWayModel mModel = new ModBusGateWayModel();

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_modbus;
    }

    @Override
    protected void initViews() {
        setCustomTitle("modBus网关");
        backIv.setVisibility(View.VISIBLE);
        footer = LayoutInflater.from(this).inflate(R.layout.footer_gateway_add, null);
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                scene = bundle.getString("scene");
                if (scene.equals("library")) {
                    preferenceGateWay = "modBusGateWayModels_library";
                } else if (scene.equals("classroom")) {
                    preferenceGateWay = "modBusGateWayModels_classroom";
                } else if (scene.equals("kitchen")) {
                    preferenceGateWay = "modBusGateWayModels_kitchen";
                } else if (scene.equals("laboratory")) {
                    preferenceGateWay = "modBusGateWayModels_laboratory";
                }
            }
        }

    }

    @Override
    protected void initAdapter() {
        mAdapter = new BasicAdapter<ModBusGateWayModel>(this, mList, R.layout.item_gateway) {
            @Override
            protected void render(ViewHolder holder, ModBusGateWayModel item, int position) {
                holder.setText(R.id.itemAdd_name_tv, item.getName());
                holder.setText(R.id.itemAdd_ip_tv, item.getIp());
                holder.setText(R.id.itemAdd_id_tv, item.getUnitId() + "");
            }
        };
        mMenuListView.setAdapter(mAdapter);
        mMenuListView.setMenuCreator(new ItemMenuDeleteCreator(this));
        mMenuListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                //if login ，synchronize data to service
                if (App.getUser() != null) {
                    call(ApiClient.getApis().deleteModBus(mList.get(position).getId()), new MySubscriber<HttpResult<Void>>() {
                        @Override
                        public void onError(Throwable e) {
                            showToast(getResources().getString(R.string.systemError));
                        }

                        @Override
                        public void onNext(HttpResult<Void> result) {
                            if (result.getResultCode() == 200) {
                                mList.remove(position);
                                mAdapter.notifyDataSetChanged();
                                showToast(result.getMessage());
                            } else {
                                showToast(result.getMessage());
                            }
                        }
                    });

                } else {
                    mList.remove(position);
                    mAdapter.notifyDataSetChanged();
                    //本地操作，重新设置偏好设置数据
                    SharePreferenceUtils.put(ModBusGateWayListActivity.this, preferenceGateWay, mList);
                }
                return false;
            }
        });
        mMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("modBusGateWayModel", mList.get(position));
                go(EnvironmentDetectorDetailActivity.class, bundle);
            }
        });
        if (mMenuListView.getFooterViewsCount() == 0) {
            mMenuListView.addFooterView(footer);
            footer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("preferenceGateWay", preferenceGateWay);
                    bundle.putString("sceneId", scene);
                    go(CreateModbusActivity.class, bundle);
                }
            });
        }

    }

    @Override
    protected void initData() {
        //如果是登录状态则从服务器拉取数据，如果不是登录状态则从本地拉取数据
        if (App.getUser() != null) {
            mModel.setSceneId(scene);
            mModel.setUserId(App.getUser().getUserId());
            call(ApiClient.getApis().getModBusList(mModel), new MySubscriber<HttpResult<List<ModBusGateWayModel>>>() {
                @Override
                public void onError(Throwable e) {
                    showToast(getResources().getString(R.string.systemError));
                }

                @Override
                public void onNext(HttpResult<List<ModBusGateWayModel>> result) {
                    if (result.getResultCode() == 200) {
                        mList.clear();
                        mList.addAll(result.data);
                        mAdapter.notifyDataSetChanged();
                        //   showToast(result.getMessage());
                    } else if (result.getResultCode() == 400) {
                        showToast(result.getMessage());
                    }
                }
            });
        } else {
            //如果没有登录，则从本地拉取数据
            List<ModBusGateWayModel> mModBusGateWayModels = (List<ModBusGateWayModel>) SharePreferenceUtils.get(ModBusGateWayListActivity.this, preferenceGateWay, new TypeToken<List<ModBusGateWayModel>>() {
            }.getType());
            if (mModBusGateWayModels != null && mModBusGateWayModels.size() > 0) {
                mList.addAll(mModBusGateWayModels);
                mAdapter.notifyDataSetChanged();
            }
        }

    }

    @Override
    protected void initListener() {
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Subscribe
    public void AddGatewayEvent(ModBusGateWayModel model) {
        if (model != null) {
            if (model.isFlag()) {
                initData();
            } else {
                mList.add(model);
                mAdapter.notifyDataSetChanged();
            }
        }

    }
}
