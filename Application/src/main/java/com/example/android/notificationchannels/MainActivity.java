/*
 * Copyright 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.notificationchannels;

import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

/** Display main screen for sample. Displays controls for sending test notifications. */
public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int NOTIFICATION_FOLLOW = 1100;
    private static final int NOTIFICATION_UNFOLLOW = 1101;
    private static final int NOTIFICATION_DM_FRIEND = 1200;
    private static final int NOTIFICATION_DM_COWORKER = 1201;

    /*
     * A view model for interacting with the UI elements.
     */
    private MainUi mUIModel;

    /*
     * A helper class for initializing notification channels and sending notifications.
     */
    private NotificationHelper mNotificationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNotificationHelper = new NotificationHelper(this);
        mUIModel = new MainUi(findViewById(R.id.activity_main));
    }

    /**
     * Send activity notifications.
     *
     * @param id The ID of the notification to create
     */
    private void sendNotification(int id) {
        Notification.Builder notificationBuilder = null;
        switch (id) {
            case NOTIFICATION_FOLLOW:
                notificationBuilder =
                        mNotificationHelper.getNotificationFollower(
                                getString(R.string.follower_title_notification),
                                getString(R.string.follower_added_notification_body,
                                        mNotificationHelper.getRandomName()));
                break;

            case NOTIFICATION_UNFOLLOW:
                notificationBuilder =
                        mNotificationHelper.getNotificationFollower(
                                getString(R.string.follower_title_notification),
                                getString(R.string.follower_removed_notification_body,
                                        mNotificationHelper.getRandomName()));
                break;

            case NOTIFICATION_DM_FRIEND:
                notificationBuilder =
                        mNotificationHelper.getNotificationDM(
                                getString(R.string.direct_message_title_notification),
                                getString(R.string.dm_friend_notification_body,
                                        mNotificationHelper.getRandomName()));
                break;

            case NOTIFICATION_DM_COWORKER:
                notificationBuilder =
                        mNotificationHelper.getNotificationDM(
                                getString(R.string.direct_message_title_notification),
                                getString(R.string.dm_coworker_notification_body,
                                        mNotificationHelper.getRandomName()));
                break;
        }
        if (notificationBuilder != null) {
            mNotificationHelper.notify(id, notificationBuilder);
        }
    }

    /** Send Intent to load system Notification Settings for this app. */
    private void goToNotificationSettings() {
        Intent i = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
        i.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
        startActivity(i);
    }

    /**
     * Send intent to load system Notification Settings UI for a particular channel.
     *
     * @param channel Name of channel to configure
     */
    private void goToNotificationChannelSettings(String channel) {
        Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
        intent.putExtra(Settings.EXTRA_CHANNEL_ID, channel);
        startActivity(intent);
    }

    /**
     * View model for interacting with Activity UI elements. (Keeps core logic for sample separate.)
     */
    class MainUi implements View.OnClickListener {

        private MainUi(View root) {

            // Setup the buttons
            (root.findViewById(R.id.birthday_follower_button)).setOnClickListener(this);
            (root.findViewById(R.id.life_follower_button)).setOnClickListener(this);
            (root.findViewById(R.id.follower_channel_settings_button)).setOnClickListener(this);
            (root.findViewById(R.id.friend_dm_button)).setOnClickListener(this);
            (root.findViewById(R.id.coworker_dm_button)).setOnClickListener(this);
            (root.findViewById(R.id.dm_channel_settings_button)).setOnClickListener(this);
            (root.findViewById(R.id.go_to_settings_button)).setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.birthday_follower_button:
                    sendNotification(NOTIFICATION_FOLLOW);
                    break;
                case R.id.life_follower_button:
                    sendNotification(NOTIFICATION_UNFOLLOW);
                    break;
                case R.id.follower_channel_settings_button:
                    goToNotificationChannelSettings(NotificationHelper.FOLLOWERS_CHANNEL);
                    break;
                case R.id.friend_dm_button:
                    sendNotification(NOTIFICATION_DM_FRIEND);
                    break;
                case R.id.coworker_dm_button:
                    sendNotification(NOTIFICATION_DM_COWORKER);
                    break;
                case R.id.dm_channel_settings_button:
                    goToNotificationChannelSettings(NotificationHelper.DIRECT_MESSAGE_CHANNEL);
                    break;
                case R.id.go_to_settings_button:
                    goToNotificationSettings();
                    break;
                default:
                    Log.e(TAG, getString(R.string.error_click));
                    break;
            }
        }
    }
}
