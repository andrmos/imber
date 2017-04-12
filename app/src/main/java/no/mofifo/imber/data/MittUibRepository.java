package no.mofifo.imber.data;

import java.util.List;

import no.mofifo.imber.models.Course;
import no.mofifo.imber.retrofit.MittUibClient;

/**
 * Created by andre on 10.04.17.
 */

public class MittUibRepository implements MittUibDataSource {

    MittUibClient client;

    public MittUibRepository(MittUibClient client) {
        this.client = client;
    }

    @Override
    public void getFavoriteCourses(Callback<List<Course>> callback) {
        
    }

}
