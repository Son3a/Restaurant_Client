package com.nsb.restaurant.activity.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nsb.restaurant.R;
import com.nsb.restaurant.adapter.CategoryCompactAdapter;
import com.nsb.restaurant.adapter.FoodOrderingAdapter;
import com.nsb.restaurant.adapter.FoodToOrderAdapter;
import com.nsb.restaurant.databinding.ActivityOrderFoodBinding;
import com.nsb.restaurant.fragment.BookingFragment;
import com.nsb.restaurant.listener.CategoryListener;
import com.nsb.restaurant.listener.FoodListener;
import com.nsb.restaurant.model.BookingModel;
import com.nsb.restaurant.model.CategoryModel;
import com.nsb.restaurant.model.FoodModel;
import com.nsb.restaurant.util.Constant;
import com.nsb.restaurant.util.LoadingDialog;
import com.nsb.restaurant.util.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.StringEntity;

public class OrderFoodActivity extends AppCompatActivity implements CategoryListener, FoodListener {
    private static ActivityOrderFoodBinding binding;
    private List<FoodModel> foodModelListToOrder;// danh sach mon an theo loai mon
    public static List<FoodModel> foodModelListOrdering;// danh sach mon an se duoc dat
    private List<CategoryModel> categoryModelList;
    private CategoryCompactAdapter categoryCompactAdapter;
    private FoodToOrderAdapter foodToOrderAdapter;
    private FoodOrderingAdapter foodOrderingAdapter;
    private BottomSheetDialog bottomPayment;
    private View bottomPaymentView;
    private Button buttonConfirm;
    private EditText textDeposit;
    private ImageButton buttonCLoseBottom;
    public static int totalMoneyPay = 0, depositMoney = 0, totalMoneyy = 0;
    private TextView textVND;
    private List<Boolean> listSelected;
    private static String accessToken;
    private LoadingDialog loadingDialog;

    //    public static boolean isAddFood = false;
    private PreferenceManager preferenceManager;
    public static String idTable = "";
    public static String idBooking = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityOrderFoodBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        bottomPaymentView = getLayoutInflater().inflate(R.layout.bottom_payment, null);


        init();
        setEvent();
    }

    private void init() {
        loadingDialog = new LoadingDialog(OrderFoodActivity.this);
        setFoodToOrderAdapter();
        setFoodOrderingAdapter();
        setCategoryAdapter();
        buttonConfirm = bottomPaymentView.findViewById(R.id.buttonSubmit);
        textDeposit = bottomPaymentView.findViewById(R.id.textMoney);
        buttonCLoseBottom = bottomPaymentView.findViewById(R.id.imageClose);
        textVND = bottomPaymentView.findViewById(R.id.textVND);

        bottomPayment = new BottomSheetDialog(OrderFoodActivity.this);
        bottomPayment.setContentView(bottomPaymentView);
        preferenceManager = new PreferenceManager(OrderFoodActivity.this);
    }

    private void setEvent() {
        checkActivity();
        getCategories();
        openBottomPayment();
        closeBottomPayment();
        setStateButtonPay();
        setStateIconVND();
        confirmDeposit();
        skipClick();
    }

    private void skipClick() {
        binding.textSkip.setOnClickListener(v -> {
            try {
                booking();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private void checkActivity() {
//        if (getIntent().getExtras().containsKey(Constant.ID_BOOKING)) {
//            isAddFood = true;
//            preferenceManager.putBoolean(Constant.IS_ADD_FOOD, true);
//            binding.textSkip.setVisibility(View.GONE);
//            binding.textNotify.setText("Bạn hãy đặt thêm món tại đây");
//            binding.textNotify.setVisibility(View.GONE);
//            getBookingAndFood(getIntent().getStringExtra(Constant.ID_BOOKING));
//            idBooking = getIntent().getStringExtra(Constant.ID_BOOKING);

//        } else {
//            isAddFood = false;
//            preferenceManager.putBoolean(Constant.IS_ADD_FOOD, false);
//        }
    }

    private void booking() throws JSONException {
        String url = Constant.URL_DEV + "/booking/booking-table";
//        binding.recycleViewListFoods.setVisibility(View.GONE);
        RequestQueue queue = Volley.newRequestQueue(OrderFoodActivity.this);

        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < BookingFragment.idTables.size(); i++) {
            Log.d("idTabless", BookingFragment.idTables.get(i));
            jsonArray.put(BookingFragment.idTables.get(i));
        }

        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("amountPeople", BookingFragment.numOfPeople);
        jsonRequest.put("timeBooking", BookingFragment.timeBookingTable);
        jsonRequest.put("note", BookingFragment.stringNote);
        jsonRequest.put("idTables", jsonArray);


        JsonObjectRequest sr = new JsonObjectRequest(Request.Method.POST, url, jsonRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                binding.pbLoadingAll.setVisibility(View.INVISIBLE);
                BookingModel bookingModel = (BookingModel) getIntent().getSerializableExtra(Constant.BOOKING_OBJECT);
                try {
                    bookingModel.setIdBooking(response.getJSONObject("data").getString("mapd"));

                    Intent intent = new Intent(OrderFoodActivity.this, BookingSuccessActivity.class);
                    intent.putExtra(Constant.BOOKING_OBJECT, bookingModel);
                    finish();
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", error.toString());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("authorization", preferenceManager.getString(Constant.TOKEN));
                return headers;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(
                0,
                -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(sr);
    }

    private void confirmDeposit() {
        buttonConfirm.setOnClickListener(v -> {
            loadingDialog.showDialog();
            int deposit = (int) (0.3 * totalMoneyPay);
            String stringDeposit = textDeposit.getText().toString().replace(".", "").trim();
            if (Integer.parseInt(stringDeposit) < deposit) {
                Toast.makeText(this, "Số tiền cọc phải lớn hơn hoặc bằng 30% tổng tiền", Toast.LENGTH_SHORT).show();
                textDeposit.setText(Constant.formatSalary(String.valueOf(deposit)));
            } else if (Integer.parseInt(stringDeposit) >= totalMoneyPay) {
                depositMoney = totalMoneyPay;
                getAccessToken();
            } else {

                depositMoney = Integer.valueOf(stringDeposit);
                getAccessToken();
            }

        });
    }

    private void setStateIconVND() {
        textDeposit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Log.d("Total", textDeposit.getText());

                if (!textDeposit.getText().toString().equals("")) {
                    textVND.setVisibility(View.VISIBLE);
                } else {
                    textVND.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public static void setTotalMoney(List<FoodModel> listFoods) {
        totalMoneyPay = 0;
        totalMoneyy = 0;
        for (FoodModel foodModel : listFoods) {
            totalMoneyPay += Integer.valueOf(foodModel.getPrice()) * foodModel.getNumOfFood();
            totalMoneyy += Integer.valueOf(foodModel.getPrice()) * foodModel.getNumOfFood();
        }

        Log.d("TotalMoney", "total: " + totalMoneyPay);
        binding.textTotal.setText(Constant.formatSalary(String.valueOf(totalMoneyPay)) + " vnd");
    }

    private void closeBottomPayment() {
        buttonCLoseBottom.setOnClickListener(v -> {
            bottomPayment.dismiss();
        });
    }

    private void checkFoodChange() {
//        listFoodChanged.clear();
//        boolean isSame;
//        for (int i = 0; i < listFoodFirst.size(); i++) {
//            isSame = false;
//            for (int j = 0; j < foodModelListOrdering.size(); j++) {
//                if (listFoodFirst.get(i).getId().equals(foodModelListOrdering.get(j).getId())) {
//                    isSame = true;
//                    int num = listFoodFirst.get(i).getNumOfFood() - foodModelListOrdering.get(j).getNumOfFood();
//                    if (num > 0) {
//                        listFoodChanged.add(new FoodModel(listFoodFirst.get(i).getId(), num));
//                    }
//                    break;
//                }
//            }
//            if (!isSame) {
//                listFoodChanged.add(new FoodModel(listFoodFirst.get(i).getId(), 0));
//            }
//        }
//
//        for (int i = 0; i < listFoodChanged.size(); i++) {
//            Log.d("FoodChange", listFoodChanged.get(i).getName() + "  " + listFoodChanged.get(i).getNumOfFood());
//        }
    }

    private void openBottomPayment() {
        binding.textPay.setOnClickListener(v -> {
            checkFoodChange();
            if (getIntent().getExtras().containsKey(Constant.ID_BOOKING)) {
                try {
                    changeNumOfFood(getIntent().getStringExtra(Constant.ID_BOOKING));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            int deposit = (int) (0.3 * totalMoneyPay);
            textDeposit.setText(Constant.formatSalary(String.valueOf(deposit)));
            textVND.setVisibility(View.VISIBLE);
            bottomPayment.show();

        });
    }

    private void addFoodToBooking(String idBooking) throws JSONException {
        String url = Constant.URL_DEV + "/booking/add-foods-to-booking";

        JSONArray jsonArray = new JSONArray();
//        for (int i = 0; i < listFoodBooked.size(); i++) {
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("idFood", listFoodBooked.get(i).getId());
//            jsonObject.put("numOfFood", listFoodBooked.get(i).getNumOfFood());
//            jsonObject.put("price", listFoodBooked.get(i).getPrice());
//            jsonArray.put(jsonObject);
//        }

        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("idBooking", idBooking);
        jsonRequest.put("idTable", idTable);
        jsonRequest.put("listFoodAdd", jsonArray);

        RequestQueue queue = Volley.newRequestQueue(OrderFoodActivity.this);

        JsonObjectRequest sr = new JsonObjectRequest(Request.Method.POST, url, jsonRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(OrderFoodActivity.this, "Successful!", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", error.toString());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("authorization", preferenceManager.getString(Constant.TOKEN));
                return headers;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(
                0,
                -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(sr);
    }


    private void setStateButtonPay() {
        binding.textTotal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (foodModelListOrdering.size() == 0) {
                    binding.textNotify.setVisibility(View.VISIBLE);
                } else {
                    binding.textNotify.setVisibility(View.GONE);
                }

                if (totalMoneyPay == 0) {
                    binding.textPay.setBackgroundResource(R.color.primary_opacity);
                    binding.textPay.setEnabled(false);
                } else {
                    binding.textPay.setBackgroundResource(R.color.primary);
                    binding.textPay.setEnabled(true);
                }

//                if (listFoodFirst.size() != foodModelListOrdering.size()) {
//                    binding.textPay.setBackgroundResource(R.color.primary);
//                    binding.textPay.setText("Xác nhận");
//                    binding.textPay.setEnabled(true);
//                    return;
//                } else {
//                    for (FoodModel food1 : listFoodFirst) {
//                        boolean isSame = false;
//                        for (FoodModel food2 : foodModelListOrdering) {
//                            if (food1.getId().equals(food2.getId())) {
//                                isSame = true;
//                                if (food1.getNumOfFood() != food2.getNumOfFood()) {
//                                    binding.textPay.setBackgroundResource(R.color.primary);
//                                    binding.textPay.setText("Xác nhận");
//                                    binding.textPay.setEnabled(true);
//                                    return;
//                                }
//                            }
//                        }
//                        if (!isSame) {
//                            binding.textPay.setBackgroundResource(R.color.primary);
//                            binding.textPay.setText("Xác nhận");
//                            binding.textPay.setEnabled(true);
//                            return;
//                        }
//                    }
//                    Log.d("foodd1", "not change");
//                    binding.textPay.setBackgroundResource(R.color.primary_opacity);
//                    binding.textPay.setEnabled(false);
//                }
//
            }
        });
    }

    private void setFoodToOrderAdapter() {
        foodModelListToOrder = new ArrayList<>();
        foodToOrderAdapter = new FoodToOrderAdapter(foodModelListToOrder, this);

        binding.recycleViewListFoods.setAdapter(foodToOrderAdapter);
    }

    private void setFoodOrderingAdapter() {
        foodModelListOrdering = new ArrayList<>();
        foodOrderingAdapter = new FoodOrderingAdapter(foodModelListOrdering, OrderFoodActivity.this, this);
        binding.recycleViewOrderingFood.setAdapter(foodOrderingAdapter);
    }

    private void setCategoryAdapter() {
        categoryModelList = new ArrayList<>();
        listSelected = new ArrayList<>();
        categoryCompactAdapter = new CategoryCompactAdapter(categoryModelList, this, listSelected);
        binding.recycleViewCategory.setAdapter(categoryCompactAdapter);
    }

    private void getCategories() {
        String url = Constant.URL_DEV + "/category/get-categories";
        binding.pbLoadingAll.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(OrderFoodActivity.this);

        JsonObjectRequest sr = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data").getJSONArray(0);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        categoryModelList.add(new CategoryModel(
                                jsonObject.get("MALOAI").toString().trim(),
                                jsonObject.get("TEN").toString().trim(),
                                jsonObject.get("HINH_ANH").toString().trim()));
                        Log.d("Category", categoryModelList.get(i).getImage());

                        listSelected.add(false);
                    }
                    OrderFoodActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            categoryCompactAdapter.notifyDataSetChanged();
                            binding.pbLoadingAll.setVisibility(View.INVISIBLE);
                        }
                    });
                } catch (JSONException e) {
                    Log.d("Error", e.getMessage());
                    OrderFoodActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            binding.pbLoadingAll.setVisibility(View.INVISIBLE);
                            binding.textError.setText("Lỗi máy chủ!");
                            binding.textError.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", error.toString());
                OrderFoodActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        binding.pbLoadingAll.setVisibility(View.INVISIBLE);
                        binding.textError.setText("Lỗi máy chủ!");
                        binding.textError.setVisibility(View.VISIBLE);
                    }
                });
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(
                0,
                -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(sr);
    }


    private void getFoodsByCategory(String idCategory) {
        String url = Constant.URL_DEV + "/food/get-food-by-category/" + idCategory;
        binding.pbLoadingCategories.setVisibility(View.VISIBLE);
        binding.recycleViewListFoods.setVisibility(View.INVISIBLE);
        foodModelListToOrder.clear();

        RequestQueue queue = Volley.newRequestQueue(OrderFoodActivity.this);


        JsonObjectRequest sr = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data").getJSONArray(0);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        foodModelListToOrder.add(new FoodModel(
                                jsonObject.get("MAMA").toString().trim(),
                                jsonObject.get("TEN").toString().trim(),
                                jsonObject.get("GIA").toString().trim(),
                                jsonObject.get("HINH_ANH").toString().trim()));
                        Log.d("Food", foodModelListToOrder.get(i).getImage());
                    }
                    foodToOrderAdapter.notifyDataSetChanged();
                    binding.pbLoadingCategories.setVisibility(View.GONE);
                    binding.recycleViewListFoods.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    Log.d("Error", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", error.toString());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(
                0,
                -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(sr);
    }

    private void createOrder(String accessToken) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Accept", "application/json");
        client.addHeader("Content-type", "application/json");
        client.addHeader("Authorization", "Bearer " + accessToken);

        String order = "{"
                + "\"intent\": \"CAPTURE\","
                + "\"purchase_units\": [\n" +
                "      {\n" +
                "        \"amount\": {\n" +
                "          \"currency_code\": \"USD\",\n" +
                "          \"value\": \"" + Constant.convertToUsMoney(depositMoney) + "\"\n" +
                "        }\n" +
                "      }\n" +
                "    ],\"application_context\": {\n" +
                "        \"brand_name\": \"Restaurant\",\n" +
                "        \"return_url\": \"app://restaurant.com\",\n" +
                "        \"cancel_url\": \"app://restaurant.com\"\n" +
                "    }}";

        HttpEntity entity = new StringEntity(order, "utf-8");
        client.post(this, "https://api-m.sandbox.paypal.com/v2/checkout/orders", entity, "application/json", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
                Log.e("RESPONSE", response);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                Log.i("RESPONSE", response);
                try {
                    JSONArray links = new JSONObject(response).getJSONArray("links");

                    //iterate the array to get the approval link
                    for (int i = 0; i < links.length(); ++i) {
                        JSONObject linkObj = links.getJSONObject(i);
                        Log.i("RESPONSE", linkObj.toString());
                        String rel = linkObj.getString("rel");
                        if (rel.equals("approve")) {
                            String link = linkObj.getString("href");
                            //redirect to this link via CCT
                            loadingDialog.hideDialog();
                            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                            CustomTabsIntent customTabsIntent = builder.build();
                            finish();
                            customTabsIntent.launchUrl(OrderFoodActivity.this, Uri.parse(link));
                        }
                    }

                } catch (JSONException e) {
                    loadingDialog.hideDialog();
                    e.printStackTrace();
                }
            }
        });
    }


    String encodeStringToBase64() {
        String input = Constant.CLIENT_ID + ":" + Constant.CLIENT_SECRET;
        String encodedString = Base64.getEncoder().encodeToString(input.getBytes());
        return encodedString;
    }

    public static String getMyAccessToken() {
        return accessToken;
    }

    private void getAccessToken() {
        String AUTH = encodeStringToBase64();
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Accept", "application/json");
        client.addHeader("Content-type", "application/x-www-form-urlencoded");
        client.addHeader("Authorization", "Basic " + AUTH);
        String jsonString = "grant_type=client_credentials";

        HttpEntity entity = new StringEntity(jsonString, "utf-8");

        client.post(this, "https://api-m.sandbox.paypal.com/v1/oauth2/token", entity, "application/x-www-form-urlencoded", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
                Log.e("RESPONSE", response);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                try {
                    JSONObject jobj = new JSONObject(response);
                    accessToken = jobj.getString("access_token");
                    createOrder(accessToken);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getBookingAndFood(String idBooking) {
        String url = Constant.URL_DEV + "/food/get-booking-and-food/" + idBooking;

        RequestQueue queue = Volley.newRequestQueue(OrderFoodActivity.this);

        JsonObjectRequest sr = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    idTable = response.getJSONArray("data").getJSONArray(0).getJSONObject(0).getString("MABAN");
                    //idBooking = response.getJSONArray("data").getJSONArray(0).getJSONObject(0).getString("MAPD");
                    JSONArray jsonArray = response.getJSONArray("data").getJSONArray(1);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String gia = jsonObject.get("GIA").toString().trim();
                        int soluong = Integer.parseInt(jsonObject.get("SO_LUONG").toString().trim());
                        foodModelListOrdering.add(new FoodModel(
                                jsonObject.get("MAMA").toString().trim(),
                                jsonObject.get("TEN").toString().trim(),
                                gia,
                                jsonObject.get("HINH_ANH").toString().trim(),
                                soluong
                        ));

//                        listFoodFirst.add(new FoodModel(
//                                jsonObject.get("MAMA").toString().trim(),
//                                jsonObject.get("TEN").toString().trim(),
//                                gia,
//                                jsonObject.get("HINH_ANH").toString().trim(),
//                                soluong
//                        ));
                        Log.d("Food", foodModelListOrdering.get(i).getImage());
                    }

                    foodOrderingAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.d("Error", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", error.toString());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(
                0,
                -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(sr);
    }

//    private boolean isChange(FoodModel foodModel) {
//        int num = 0;
//        for (int i = 0; i < listFoodFirst.size(); i++) {
//            if (listFoodFirst.get(i).getId().equals(foodModel.getId())) {
//                num = listFoodFirst.get(i).getNumOfFood();
//            }
//        }
//        if (num == 0) {
//            return true;
//        }
//
//        for (int i = 0; i < foodModelListOrdering.size(); i++) {
//            if (foodModelListOrdering.get(i).getId().equals(foodModel.getId())) {
//                if (foodModelListOrdering.get(i).getNumOfFood() < num) {
//                    return false;
//                } else {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }

    @Override
    public void onClickCategory(CategoryModel categoryModel) {
        getFoodsByCategory(categoryModel.getId());
    }

    @Override
    public void onClickFood(FoodModel foodModel) {
        //Đặt thêm món-------------------------------------------
//        if (isChange(foodModel)) {
//            boolean isAdd = true;
//            for (int i = 0; i < listFoodBooked.size(); i++) {
//                if (listFoodBooked.get(i).getId().equals(foodModel.getId())) {
//                    int numOfFood = listFoodBooked.get(i).getNumOfFood();
//                    numOfFood++;
//                    listFoodBooked.get(i).setNumOfFood(numOfFood);
//                    isAdd = false;
//                    break;
//                }
//            }
//
//            if (isAdd) {
//                listFoodBooked.add(new FoodModel(
//                        foodModel.getId(),
//                        foodModel.getName(),
//                        foodModel.getPrice(),
//                        foodModel.getImage(),
//                        1));
//            }
//        }
        //------------------------------------------------------

        int position = foodModelListOrdering.size();
        for (int i = 0; i < foodModelListOrdering.size(); i++) {
            if (foodModelListOrdering.get(i).getId().equals(foodModel.getId())) {
                int numOfFood = foodModelListOrdering.get(i).getNumOfFood();
                numOfFood++;
                foodModelListOrdering.get(i).setNumOfFood(numOfFood);
                foodOrderingAdapter.notifyItemChanged(i);
                binding.recycleViewOrderingFood.smoothScrollToPosition(i);
                setTotalMoney(foodModelListOrdering);
//                if (isAddFood == true) {
//                    setTotalMoney(listFoodBooked);
//                } else {
//                    setTotalMoney(foodModelListOrdering);
//                }
                return;
            }
        }
        foodModel.setNumOfFood(1);
        foodModelListOrdering.add(foodModel);
        foodOrderingAdapter.notifyItemInserted(position);
        setTotalMoney(foodModelListOrdering);
//        if (isAddFood == true) {
//            setTotalMoney(listFoodBooked);
//        } else {
//            setTotalMoney(foodModelListOrdering);
//        }

        binding.recycleViewOrderingFood.smoothScrollToPosition(position);
    }

    private void changeNumOfFood(String idBooking) throws JSONException {
//        String url = Constant.URL_DEV + "/booking/update-num-of-food-user";
//
//        JSONArray jsonArray = new JSONArray();
//        for (int i = 0; i < listFoodChanged.size(); i++) {
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("idFood", listFoodChanged.get(i).getId());
//            jsonObject.put("numOfFood", listFoodChanged.get(i).getNumOfFood());
//            jsonArray.put(jsonObject);
//        }
//
//        JSONObject jsonRequest = new JSONObject();
//        jsonRequest.put("idBooking", idBooking);
//        jsonRequest.put("listFoodChange", jsonArray);
//
//        RequestQueue queue = Volley.newRequestQueue(OrderFoodActivity.this);
//
//        JsonObjectRequest sr = new JsonObjectRequest(Request.Method.PUT, url, jsonRequest, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                Toast.makeText(OrderFoodActivity.this, "Successful!", Toast.LENGTH_SHORT).show();
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("Error", error.toString());
//            }
//        }) {
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Content-Type", "application/json");
//                headers.put("authorization", preferenceManager.getString(Constant.TOKEN));
//                return headers;
//            }
//        };
//        sr.setRetryPolicy(new DefaultRetryPolicy(
//                0,
//                -1,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        queue.add(sr);
    }


    @Override
    public void onRemove(FoodModel foodModel) {
//        listFoodBooked.removeIf(f -> f.getId().equals(foodModel.getId()));

        for (int i = 0; i < foodModelListOrdering.size(); i++) {
            if (foodModelListOrdering.get(i).getId().equals(foodModel.getId())) {
                foodModelListOrdering.remove(i);
                foodOrderingAdapter.notifyItemRemoved(i);
                break;
            }
        }

//        for (FoodModel foodModel1 : listFoodBooked) {
//            if (foodModel.getId().equals(foodModel1.getId())) {
//                listFoodBooked.remove(foodModel1);
//                break;
//            }
//        }

//        if (isAddFood == true) {
//            setTotalMoney(listFoodBooked);
//        } else {
//            setTotalMoney(foodModelListOrdering);
//        }
        setTotalMoney(foodModelListOrdering);


        Log.d("TotalMoney", "Remove: " + totalMoneyPay);

    }

}