package no.mofifo.imber.injection;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module providing a reference to the application context.
 * Created by andre on 10.04.17.
 */
@Module
public class AppModule {

    private Application application;

    public AppModule(Application application) {
        this.application= application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return application;
    }
}
