package no.mofifo.imber.data;

import java.util.List;

import javax.inject.Singleton;

import no.mofifo.imber.models.Course;
import no.mofifo.imber.retrofit.MittUibClient;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by andre on 10.04.17.
 */
@Singleton
public class MittUibRepository implements MittUibDataSource {

    MittUibClient client;

    public MittUibRepository(MittUibClient client) {
        this.client = client;
    }

    @Override
    public void getFavoriteCourses(final Callback<List<Course>> callback) {
        Call<List<Course>> call = client.getFavoriteCourses();
        call.enqueue(new retrofit2.Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure();
                }
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
                callback.onFailure();
            }
        });
    }

}
