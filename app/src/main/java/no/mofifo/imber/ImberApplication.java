package no.mofifo.imber;

import android.app.Application;

import no.mofifo.imber.injection.ApiComponent;
import no.mofifo.imber.injection.ApiModule;
import no.mofifo.imber.injection.AppModule;
import no.mofifo.imber.injection.DaggerApiComponent;

/**
 * Custom Application subclass instantiated before any other Activities.
 * This is created to allow the use of Singletons (ex. a class fetching data from API) in the application.
 * The singletons used will live as long as this custom application lives.
 *
 * Created by andre on 10.04.17.
 */
public class ImberApplication extends Application {

    private ApiComponent apiComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        apiComponent = DaggerApiComponent.builder()
                .appModule(new AppModule(this))
                .apiModule(new ApiModule())
                .build();
    }

    public ApiComponent getApiComponent() {
        return apiComponent;
    }
}
