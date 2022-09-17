package com.webnmobapps.alahmaar.retrofit;


import com.webnmobapps.alahmaar.model.CommunityCommentListModel;
import com.webnmobapps.alahmaar.model.CommunityPostModel;
import com.webnmobapps.alahmaar.model.EventListModel;
import com.webnmobapps.alahmaar.model.GetUserProfileModel;
import com.webnmobapps.alahmaar.model.LoginModel;
import com.webnmobapps.alahmaar.model.NotificationListModel;
import com.webnmobapps.alahmaar.model.RegistrationModel;
import com.webnmobapps.alahmaar.model.UserAccountVerificationModel;
import com.webnmobapps.alahmaar.model.UserFormListModel;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public interface Api {



    @Multipart
    @POST("user-register")
    Call<RegistrationModel> registration(@Part("username") RequestBody username,
                                         @Part("email") RequestBody email,
                                         @Part("phone_number") RequestBody phone_number,
                                         @Part("password") RequestBody password,
                                         @Part MultipartBody.Part image);


    @FormUrlEncoded
    @POST("login")
    Call<LoginModel>login(@Field("email") String email,
                          @Field("password") String password);


    @FormUrlEncoded
    @POST("changepassword")
    Call<LoginModel>forgot_password(@Field("old_password") String old_password,
                          @Field("new_password") String new_password);



    @FormUrlEncoded
    @POST("verify-phone-number")
    Call<UserAccountVerificationModel> user_account_verification(@Field("phone") String phone);


    @GET("forms-lists")
    Call<UserFormListModel> user_form_list();

    @GET("community-discussions-deatils")
    Call<CommunityPostModel> community_post_list(@Header("authorization") String authorization);

    @GET("event-details")
    Call<EventListModel> event_list();

    @Headers("Content-Type: application/json")
    @GET("user-update-profile")
    Call<GetUserProfileModel> get_user_profile_details(@Header("authorization") String authorization);

    @Multipart
    @PUT("user-update-profile")
    Call<GetUserProfileModel> update_user_profile_details(@Header("authorization") String authorization,
                                                          @Part("userId") RequestBody userId,
                                                          @Part("username") RequestBody username,
                                                          @Part("email") RequestBody email,
                                                          @Part("phone_number") RequestBody phone_number,
                                                          @Part MultipartBody.Part image);



    @FormUrlEncoded
    @POST("contact-inquiry")
    Call<RegistrationModel> contact_us_form (@Field("name") String name, @Field("email") String email,
                                             @Field("message") String message,
                                             @Field("phone_number") String phoneNumber
                                            );


    @FormUrlEncoded
    @POST("rest-password")
    Call<RegistrationModel> reset_password (@Field("phone_number") String phone_number,
                                            @Field("new_password") String new_password);



    @GET("user-get-notifications")
    Call<NotificationListModel> notification_list (@Header("authorization") String authorization);


    @FormUrlEncoded
    @POST("logout")
    Call<RegistrationModel> logout (@Header("authorization") String authorization,
                                    @Field("refresh") String refresh);


    @FormUrlEncoded
    @POST("total-comments")
    Call<CommunityCommentListModel> comments_list (@Field("id") String userId);


    @FormUrlEncoded
    @POST("verify-phone-number")
    Call<RegistrationModel> verify_account_by_phone (@Field("phone") String phone_number);



}