package com.challengesstore.data.api.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by felix on 1/11/18
 */

public class UserTokenInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXUyJ9.eyJ1c2VybmFtZSI6IkFydGVtIiwiZXhwIjoiMTUxNjIxMTM2MSIsImlhdCI6IjE1MTU4NTEzNjEifQ.eOVuO_plxecLYbIpAET5UGVs7KmIEXAhZJFO7ROQV31F-VL-BfAWcIw7T3lQqNwdN2eOXJJBOfpnYYLc6Y5nttqWbRANPO9TPj8MSC-3NjyBHM8Zb5BkBomGf7R3zzZRu2rtoKJ0iaUaCo6qHZBkPpX27xZrWAEwtK3kfN32PBJ_m7EQmbOdd4KoVgxo76YAimL8_go2tOd1z_q5pfjkfxm1P3FQ4tCx_zzgqStktN1-YLzbUNfW9AN4sPgZZAJFA5Zfdr2JfX_XgWqdZb5LIsM60CHmwD5tFtXJldmMuesx21u0kEVVswQiNNlbczqcbL_Czha_hxuzUS50ezBP_ov11Jk2h3CwdyXMDS8BqgZipEOeY8fXyDj2AsbzMA5jmMYGAt-l7twSam4_7hMkGDPMyXs9fO9z42aFQPnF6nqJ6BVgNbMUZ492CpC4Vyor6FVJgX_EpD83CkOFIyZl1P38ODLNh88fndrMmaosKK52zC-9z8WYywEj9QE70CONTUA1ZM8nqHySVwgJvSxMHTpwHBQncen3sGTcAgeWYl_bsPYWH9Yrd9CXNQlhQZFNF5K0VFDPBgA1XJMgLOdrN3QkjUjCtCFDEvkYm47yXsxE2I6AYqNWb_KZNGNQ9rTuPMbw2Bseoyubatb9CMMDOPZ-tufB5UL3dMcuNn0IdSM";
        Request newRequest  = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer " + token)
                .build();
        return chain.proceed(newRequest);

    }
}
