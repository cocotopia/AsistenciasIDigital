package com.idigital.asistenciasidigital.register;

import android.location.Location;

/**
 * Created by USUARIO on 24/05/2017.
 */

class RegisterInteractorImpl implements RegisterInteractor {

    private RegisterRepository registerRepository;

    public RegisterInteractorImpl() {
        this.registerRepository = new RegisterRepositoryImpl();
    }

    @Override
    public void sendEnterRegister(String userId, String idQuarter, int flag, int distance, Location location) {
        registerRepository.sendEnterRegister(userId, idQuarter, flag, distance, location);
    }

    @Override
    public void sendExitRegister(String userId, String idQuarter, int flag, int distance, Location location) {
        registerRepository.sendExitRegister(userId, idQuarter, flag, distance, location);
    }
}