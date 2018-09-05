package user.test.com.waldogallery.http;

import java.io.IOException;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class BasicAuthenticationInterceptor implements Interceptor {

    private String token ="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2NvdW50X2lkIjoiZjdkMWI5ZWYtYWE0Yi00YjY3LWFhZjQtYzYzZmE1MDYzOWM0Iiwicm9sZXMiOlsiY29uc3VtZXIiXSwiaXNzIjoid2FsZG86Y29yZSIsImdyYW50cyI6WyJhbGJ1bXM6dmlldzpwdWJsaWMiLCJhbGJ1bXM6ZWRpdDpvd25lZCIsImFsYnVtczpjcmVhdGU6cHJpdmF0ZSIsImFsYnVtczp2aWV3OmpvaW5lZCIsImFsYnVtczpkZWxldGU6b3duZWQiLCJhbGJ1bXM6Y3JlYXRlOnB1YmxpYyIsImFsYnVtczpjcmVhdGU6b3duZWQiLCJhbGJ1bXM6dmlldzpvd25lZCJdLCJleHAiOjE1MzgzNDI5NDEsImlhdCI6MTUzNTc1MDk0MX0.9Uc1Oi1fnpjTlfPxCIaKqyw-Zxgd0mfV56LamR-F7uo";


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request authenticatedRequest = request.newBuilder()
                .header("Authorization", "Bearer " + token).build();
        return chain.proceed(authenticatedRequest);
    }
}