package com.enation.javashop.reactnative.android.addressselector;

import android.util.JsonReader;
import android.util.Log;

import com.enation.javashop.reactnative.android.addressselector.model.Region;
import com.enation.javashop.reactnative.android.addressselector.widget.DistrictSelectorView;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.JsonWriter;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

/**
 * Created by LDD on 2017/11/3.
 */

public class AddressSelectorModule extends ReactContextBaseJavaModule {

    private static final  String AddressDataEvent = "AddressDataEvent";
    private DistrictSelectorView<Region> addressSelector;

    public AddressSelectorModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Nullable
    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        constants.put("DataEvent", AddressDataEvent);
        return constants;
    }

    @Override
    public String getName() {
        return "AddressSelectorModule";
    }

    @ReactMethod
    public void create(final Callback callback){
        getCurrentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                addressSelector = new DistrictSelectorView<>(getCurrentActivity(),getReactApplicationContext());
                addressSelector.setRegionListener(new DistrictSelectorView.RegionListener<Region>() {
                    @Override
                    public void setPickData(Region previousData) {
                        getReactApplicationContext()
                                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                                .emit(AddressDataEvent,previousData == null ?0:previousData.getId());
                    }

                    @Override
                    public void getResult(Region per, Region city, Region region, Region town) {
                        String perStr = "";
                        String cityStr = "";
                        String regionStr = "";
                        String townStr = "";
                        try {
                            if (per !=null){
                                perStr = new JSONStringer().object()
                                        .key("id")
                                        .value(per.getId().intValue())
                                        .key("name")
                                        .value(per.getName())
                                        .key("type")
                                        .value(per.getType())
                                        .key("parentId")
                                        .value(per.getParentId().intValue())
                                        .endObject()
                                        .toString();
                            }
                            if (city !=null){
                                cityStr = new JSONStringer().object()
                                        .key("id")
                                        .value(city.getId().intValue())
                                        .key("name")
                                        .value(city.getName())
                                        .key("type")
                                        .value(city.getType())
                                        .key("parentId")
                                        .value(city.getParentId().intValue())
                                        .endObject()
                                        .toString();
                            }
                            if (region !=null){
                                regionStr = new JSONStringer().object()
                                        .key("id")
                                        .value(region.getId().intValue())
                                        .key("name")
                                        .value(region.getName())
                                        .key("type")
                                        .value(region.getType())
                                        .key("parentId")
                                        .value(region.getParentId().intValue())
                                        .endObject()
                                        .toString();
                            }
                            if (town !=null){
                                townStr = new JSONStringer().object()
                                        .key("id")
                                        .value(town.getId().intValue())
                                        .key("name")
                                        .value(town.getName())
                                        .key("type")
                                        .value(town.getType())
                                        .key("parentId")
                                        .value(town.getParentId().intValue())
                                        .endObject()
                                        .toString();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        callback.invoke(perStr,cityStr,regionStr,townStr);
                    }
                });
                addressSelector.show();
            }
        });
    }

    @ReactMethod
    public void setData(ReadableArray data){
          if (data ==null ||data.size()==0 ||addressSelector ==null){
              Log.w("AddressSelectorModule","视图为初始化，或数据源为空");
              return;
          }
          ArrayList<Region> regions = new ArrayList<>();
          ArrayList dataSource = data.toArrayList();
          for (int i = 0; i < dataSource.size(); i++) {
              HashMap<String,Object> map = (HashMap) dataSource.get(i);
              Region region = new Region();
              region.setId((Double) map.get("id"));
              region.setType((Double) map.get("type"));
              region.setParentId((Double) map.get("parentId"));
              region.setName((String) map.get("name"));
              regions.add(region);
          }
          addressSelector.setData(regions);
    }
}
