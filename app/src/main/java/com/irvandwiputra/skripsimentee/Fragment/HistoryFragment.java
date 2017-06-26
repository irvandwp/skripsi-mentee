package com.irvandwiputra.skripsimentee.Fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.irvandwiputra.skripsimentee.Model.Order;
import com.irvandwiputra.skripsimentee.Model.ResponseStatus;
import com.irvandwiputra.skripsimentee.Model.Token;
import com.irvandwiputra.skripsimentee.R;
import com.irvandwiputra.skripsimentee.RecyclerView.HistoryAdapter;
import com.irvandwiputra.skripsimentee.Utility.Constant;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    private ProgressDialog progressDialog;
    public static final String TAG = HistoryFragment.class.getSimpleName();
    public ArrayList<Order> orderArrayList;
    public Order[] orders;
    @Bind(R.id.txtDefaultHistory)
    public TextView txtDefaultHistory;
    @Bind(R.id.historyRecycler)
    public RecyclerView historyRecyclerView;

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        ButterKnife.bind(this, view);
        progressDialog = new ProgressDialog(getActivity(), ProgressDialog.STYLE_SPINNER);
        orderArrayList = new ArrayList<>();
        getAllHistories();

        return view;
    }

    private void getAllHistories() {
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        OkHttpClient okHttpClient = new OkHttpClient();

        Token token = new Token(Constant.getToken(getContext()));
        Log.i(TAG, "getAllHistories: " + Token.createJSON(token));

        Request request = new Request.Builder()
                .url(Constant.URL_ORDER)
                .addHeader("Content-Type", "application/json")
                .post(Token.createJSONRequest(token))
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String responseData = response.body().string();
                    Log.i(TAG, "onResponse: " + response.code());
                    Log.i(TAG, "onResponse: " + responseData);
                    if (response.isSuccessful()) {
                        orders = Order.parseJSON(responseData);
                        for (Order order : orders) {
                            Order eachOrder = new Order();
                            eachOrder.setOrder_no(order.getOrder_no());
                            eachOrder.setCourse_name(order.getCourse_name());
                            eachOrder.setOrder_description(order.getOrder_description());
                            eachOrder.setStatus(order.getStatus());
                            orderArrayList.add(eachOrder);
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                HistoryAdapter historyAdapter = new HistoryAdapter(orderArrayList, getContext());
                                RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
                                historyRecyclerView.setAdapter(historyAdapter);
                                historyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                historyRecyclerView.addItemDecoration(itemDecoration);
                            }
                        });
                    } else {
                        final ResponseStatus responseStatus = ResponseStatus.parseJSON(responseData);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                txtDefaultHistory.setText(responseStatus.getMessage());
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    response.close();
                }
            }
        });
    }
}
