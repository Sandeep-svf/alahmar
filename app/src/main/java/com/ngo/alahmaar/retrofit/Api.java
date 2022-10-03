package com.ngo.alahmaar.retrofit;


import com.ngo.alahmaar.model.AboutTeamModel;
import com.ngo.alahmaar.model.AddCommentModel;
import com.ngo.alahmaar.model.CommonSuccessMsgModel;
import com.ngo.alahmaar.model.CommunityCommentListModel;
import com.ngo.alahmaar.model.CommunityPostModel;
import com.ngo.alahmaar.model.EventListModel;
import com.ngo.alahmaar.model.GetUserProfileModel;
import com.ngo.alahmaar.model.LoginModel;
import com.ngo.alahmaar.model.MsgSuccessModel;
import com.ngo.alahmaar.model.NotificationListModel;
import com.ngo.alahmaar.model.RegistrationModel;
import com.ngo.alahmaar.model.TotalNumberNotification;
import com.ngo.alahmaar.model.UserAccountVerificationModel;
import com.ngo.alahmaar.model.UserFormListModel;


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


    @GET("user-update-profile")
    Call<GetUserProfileModel> get_user_profile_details(@Header("authorization") String authorization);

    @Multipart
    @PUT("user-update-profile")
    Call<GetUserProfileModel> update_user_profile_details(@Header("authorization") String authorization,
                                                          @Part("id") RequestBody userId,
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
    @POST("user-rest-password")
    Call<RegistrationModel>  reset_password (
                                             @Field("phone_number") String phone_number,
                                            @Field("new_password") String new_password);


    @FormUrlEncoded
    @POST("changepassword")
    Call<MsgSuccessModel>     change_password (@Header("authorization") String authorization,
                                               @Field("old_password") String old_password,
                                               @Field("new_password") String new_password);



    @GET("user-get-notifications")
    Call<NotificationListModel> notification_list (@Header("authorization") String authorization);


    @FormUrlEncoded
    @POST("logout")
    Call<RegistrationModel> logout (@Header("authorization") String authorization,
                                    @Field("refresh") String refresh);


    @FormUrlEncoded
    @POST("total-comments")
    Call<CommunityCommentListModel> comments_list (@Header("authorization") String authorization,@Field("id") String userId);


   @FormUrlEncoded
    @PUT("add-community-comment")
    Call<AddCommentModel> ADD_COMMENT_MODEL_CALL (@Header("authorization") String authorization,
                                                  @Field("user") String user,
                                                  @Field("community_post") String community_post,
                                                  @Field("id") String id,
                                                  @Field("comment") String comment);


    @FormUrlEncoded
    @POST("verify-phone-number")
    Call<RegistrationModel> verify_account_by_phone (@Field("phone") String phone_number);

    @FormUrlEncoded
    @POST(" delete-comment-message")
    Call<CommonSuccessMsgModel> COMMON_SUCCESS_MSG_MODEL_CALL (@Header("authorization") String authorization,
                                                               @Field("community_post") String community_post,
                                                               @Field("id") String id);

    @FormUrlEncoded
    @POST("add-community-comment")
    Call<AddCommentModel> ADD_COMMENT_MODEL_CALL_POST (@Header("authorization") String authorization,
                                                  @Field("user") String user,
                                                  @Field("community_post") String community_post,
                                                  @Field("comment") String comment);

    @GET("total-user-notifications")
    Call<TotalNumberNotification> TOTAL_NUMBER_NOTIFICATION_CALL(@Header("authorization") String authorization);


    @FormUrlEncoded
    @POST("user-delete-notifications")
    Call<CommonSuccessMsgModel> COMMON_SUCCESS_MSG_MODEL_CALL_NOTIFICAITON(@Header("authorization") String authorization,
                                                              @Field("id") String id);


    @FormUrlEncoded
    @POST("user-clear-all-notifications")
    Call<CommonSuccessMsgModel> COMMON_SUCCESS_MSG_MODEL_CALL_NOTIFICAITON_CLEAR_ALL(@Header("authorization") String authorization,
                                                                           @Field("id") String id);


    @GET("company-team-members-details")
    Call<AboutTeamModel> ABOUT_TEAM_MODEL_CALL(@Header("authorization") String authorization);


    @FormUrlEncoded
    @POST("community-like")
    Call<MsgSuccessModel> MSG_SUCCESS_MODEL_CALL_LIKE_COMMUNITY(@Header("authorization") String authorization,
                                                                @Field("liked") String liked,
                                                                 @Field("community") String community,
                                                                @Field("user") String user);

}
