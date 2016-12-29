package com.mossige.finseth.follo.inf219_mitt_uib.network.retrofit;

import com.mossige.finseth.follo.inf219_mitt_uib.models.Course;
import com.mossige.finseth.follo.inf219_mitt_uib.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by andre on 29.12.16.
 */

public interface MittUibClient {

    /**
     * @return The logged in users profile data.
     */
    @GET("users/self/profile")
    Call<User> getProfile();

    /**
     * @return The active courses of the logged in user.
     */
    @GET("courses")
    Call<List<Course>> getCourses();



}
