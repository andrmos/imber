package no.mofifo.imber.data;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Singleton;

import no.mofifo.imber.models.Course;
import no.mofifo.imber.retrofit.MittUibClient;
import no.mofifo.imber.retrofit.PaginationUtils;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by andre on 10.04.17.
 */
@Singleton
public class MittUibRepository implements MittUibDataSource {

    private MittUibClient client;

    /* The URLs to the next pages for calls */
    private HashMap<UUID, String> nextPageUrls;

    // TODO Handle pagination for all calls.

    @Inject
    public MittUibRepository(MittUibClient client) {
        this.client = client;
        nextPageUrls = new HashMap<>();
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
                nextPageUrls.put(UUID.randomUUID(), PaginationUtils.getNextPageUrl(response.headers()));
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
                callback.onFailure();
            }
        });
    }

    /**
     * Returns true if this is the first call.
     *
     * @param id The id of the call.
     */
    private boolean firstCall(UUID id) {
        return nextPageUrls.containsKey(id);
    }

}
