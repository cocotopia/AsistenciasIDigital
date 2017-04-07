package com.idigital.asistenciasidigital;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.idigital.asistenciasidigital.adapter.RecyclerReportAdapter;
import com.idigital.asistenciasidigital.api.IDigitalClient;
import com.idigital.asistenciasidigital.api.IDigitalService;
import com.idigital.asistenciasidigital.model.Report;
import com.idigital.asistenciasidigital.response.ReportResponse;
import com.idigital.asistenciasidigital.util.SimpleDividerItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportActivity extends AppCompatActivity {

    private static final String TAG = RegisterActivity.class.getSimpleName();

    @BindView(R.id.ryv_report)
    RecyclerView ryvReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        ButterKnife.bind(this);

        fetchReport();
    }

    private void fetchReport() {

        IDigitalService service = IDigitalClient.getClubService();
        Call<ReportResponse> call = service.getReport();
        call.enqueue(new Callback<ReportResponse>() {
            @Override
            public void onResponse(Call<ReportResponse> call, Response<ReportResponse> response) {
                if (response.isSuccessful()) {

                    ReportResponse responseList = response.body();
                    fillRecyclerView(responseList.getData());
                }

                Log.i(TAG, response.raw().toString());
            }

            @Override
            public void onFailure(Call<ReportResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error en servicio", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fillRecyclerView(List<Report> data) {

        ryvReport.setLayoutManager(new LinearLayoutManager(this));
        ryvReport.addItemDecoration(new SimpleDividerItemDecoration(this));
        ryvReport.setAdapter(new RecyclerReportAdapter(data));
    }
}
