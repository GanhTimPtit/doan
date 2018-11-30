package com.ptit.edu.store.admin.controller;

import com.ptit.edu.store.admin.models.data.TopicNotificationResponse;
import com.ptit.edu.store.admin.models.data.TopicNotification;
import com.ptit.edu.store.response_model.HeaderConstant;
import com.ptit.edu.store.utils.HttpPostRequestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class FirebaseMessagingService {
    public static final String FIREBASE_FCM_API = "fcm.googleapis.com/fcm/send";

    public static final String TYPE = "type";
    public static final String CREATED_DATE = "createdDate";
    public static final String LOGO_URL = "logoUrl";
    public static final String TITLE = "title";
    public static final String MESSAGE = "message";
    public static final String STATUS = "status";
    private static final String CUSTOMER_NAME = "customerName";
    private static final String AVATAR_URL = "avatarUrl";
    private static final String ID = "id";

    @Autowired
    private RestTemplate restTemplate;
    public String firebaseServiceAPIKey="AIzaSyBrK4DhMlTLGKEMH8zfhFbqi55cF8-MSCE";

    public TopicNotificationResponse sendNotification(TopicNotification topicNotification) {
        if (topicNotification == null) {
            return null;
        }
        return new HttpPostRequestBuilder(restTemplate)
                .withUrl(FIREBASE_FCM_API)
                .withProtocol(HttpPostRequestBuilder.HTTPS)
                .addToHeader(HeaderConstant.AUTHORIZATION, "key=" + firebaseServiceAPIKey)
                .setContentType(MediaType.APPLICATION_JSON)
                .setJsonBody(topicNotification)
                .execute(TopicNotificationResponse.class);
    }



    public static TopicNotification createOrderSuccessMessage(String userID,
                                                                 String message,
                                                                 int status,
                                                                 Date createdDate) {

        return new TopicNotification(userID, createOrderSuccessData(message, status, createdDate));
    }

    public static Map<String, String> createOrderSuccessData(String message, int status,
                                                                Date createdDate) {
        Map<String, String> data = new HashMap<>();
        data.put(TYPE,"confirm_order");
        data.put(MESSAGE, message);
        data.put(STATUS, status+"");
        data.put(CREATED_DATE, createdDate.getTime() + "");
        return data;
    }


}
