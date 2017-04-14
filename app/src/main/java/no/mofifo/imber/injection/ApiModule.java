package no.mofifo.imber.injection;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import hirondelle.date4j.DateTime;
import no.mofifo.imber.R;
import no.mofifo.imber.data.MittUibDataSource;
import no.mofifo.imber.data.MittUibRepository;
import no.mofifo.imber.retrofit.DateTimeDeserializer;
import no.mofifo.imber.retrofit.MittUibClient;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Dagger module providing references to dependencies required to use the Mitt UiB API.
 * Created by andre on 10.04.17.
 */
@Module
public class ApiModule {

    public ApiModule() {
    }

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Application application) {
        // TODO Should migrate to using the default shared preferences, instead of having to specify a name path every time.
//        return PreferenceManager.getDefaultSharedPreferences(application);
        return application.getSharedPreferences(application.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(DateTime.class, new DateTimeDeserializer()); // Add custom deserializer
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(final SharedPreferences sharedPreferences) {
        OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
        // Add 'Authorization' header to every request
        httpBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                String accessToken = "Bearer " + sharedPreferences.getString("access_token", "");
                Request.Builder requestBuilder = original.newBuilder()
                        .header("Authorization", accessToken)
                        .method(original.method(), original.body());

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        return httpBuilder.build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
        String baseUrl = "https://mitt.uib.no/api/v1/";

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(baseUrl)
                .client(okHttpClient);

        return retrofitBuilder.build();
    }

    @Provides
    @Singleton
    MittUibClient provideMittUibClient(Retrofit retrofit) {
        return retrofit.create(MittUibClient.class);
    }

    @Provides
    @Singleton
    MittUibDataSource provideMittUibDataSource(MittUibClient client) {
        return new MittUibRepository(client);
    }
    
}
