package com.hse_miem.fb_miem.dagger;

import lombok.Synchronized;

/**
 * Created by Egor on 18/05/16.
 */
public class InjectorClass {

    private static RestComponent restComponent;

    @Synchronized
    public static RestComponent getRestComponent(){
        if(restComponent == null){
            restComponent = DaggerRestComponent.create();
        }
        return restComponent;
    }
}
