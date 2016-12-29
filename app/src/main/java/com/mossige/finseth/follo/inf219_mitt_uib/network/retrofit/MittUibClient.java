package com.mossige.finseth.follo.inf219_mitt_uib.network.retrofit;

import com.mossige.finseth.follo.inf219_mitt_uib.models.User;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by andre on 29.12.16.
 */

public interface MittUibClient {

    @GET("users/self/profile")
    Call<User> getProfile();

}
