package com.idigital.asistenciasidigital.register;

import android.location.Location;
import android.util.Log;

import com.idigital.asistenciasidigital.api.IDigitalClient;
import com.idigital.asistenciasidigital.api.IDigitalService;
import com.idigital.asistenciasidigital.lib.EventBus;
import com.idigital.asistenciasidigital.lib.GreenRobotEventBus;
import com.idigital.asistenciasidigital.register.events.RegisterEvent;
import com.idigital.asistenciasidigital.response.RegisterResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by USUARIO on 24/05/2017.
 */

class RegisterRepositoryImpl implements RegisterRepository {

    private static final String TAG = RegisterRepositoryImpl.class.getSimpleName();

    @Override
    public void sendEnterRegister(String userId, String idQuarter, int flag, int distance, Location location) {

        IDigitalService service = IDigitalClient.getIDigitalService();
        Call<RegisterResponse> call = service.postRegistry(userId, idQuarter, flag, distance,
                location.getLatitude(), location.getLongitude());
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {

                if (response.isSuccessful()) {
                    RegisterResponse registerResponse = response.body();

                    if (registerResponse.getBlocking()) {
                        postEvent(RegisterEvent.onUserBlocking);
                    } else {
                        if (registerResponse.getCode() == 0) {
                            postEvent(RegisterEvent.onSendEnterRegisterSuccess, registerResponse.getMessage());
                        } else {
                            postEvent(RegisterEvent.onSendRegisterError, registerResponse.getMessage());
                        }
                    }
                }
                Log.i(TAG, response.raw().toString());
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                t.printStackTrace();
                postEvent(RegisterEvent.onSendRegisterFailure);
            }
        });
    }

    @Override
    public void sendExitRegister(String userId, String idQuarter, int flag, int distance, Location location) {

        IDigitalService service = IDigitalClient.getIDigitalService();
        Call<RegisterResponse> call = service.postUpdateMovement(userId, idQuarter, flag, distance,
                location.getLatitude(), location.getLongitude());
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {

                if (response.isSuccessful()) {
                    RegisterResponse registerResponse = response.body();

                    if (registerResponse.getBlocking()) {
                        postEvent(RegisterEvent.onUserBlocking);
                    } else {
                        if (registerResponse.getCode() == 0) {
                            postEvent(RegisterEvent.onSendExitRegisterSuccess, registerResponse.getMessage());
                        } else {
                            postEvent(RegisterEvent.onSendRegisterError, registerResponse.getMessage());
                        }
                    }
                }
                Log.i(TAG, response.raw().toString());
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                t.printStackTrace();
                postEvent(RegisterEvent.onSendRegisterFailure);
            }
        });
    }

    private void postEvent(int type) {
        postEvent(type, null);
    }

    private void postEvent(int type, String message) {
        RegisterEvent registerEvent = new RegisterEvent();
        registerEvent.setEventType(type);
        if (message != null) {
            registerEvent.setMesage(message);
        }

        EventBus eventBus = GreenRobotEventBus.getInstance();
        eventBus.post(registerEvent);
    }
}