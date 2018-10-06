package com.jimmy.htmlplayer.businesslogic.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.jimmy.htmlplayer.ui.UIConstants;

public class MessageService extends Service {
    public MessageService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null) {
            sendMessage(intent.getIntExtra(UIConstants.KEY_MESSAGE_CATEGORY_EXTRA, 1),
                    intent.getIntExtra(UIConstants.KEY_MESSAGE_SELECTED_EXTRA, 0));
        }
        return super.onStartCommand(intent, flags, startId);
    }

    // Send an Intent with an action named UIConstants.KEY_MESSAGE_FILTER_INTENT. The Intent
    // sent should
    // be received by the ReceiverActivity.
    private void sendMessage(int category, int selectedInx) {

        Intent intent = new Intent(UIConstants.KEY_MESSAGE_FILTER_INTENT);
        // You can also include some extra data.
        intent.putExtra(UIConstants.KEY_MESSAGE_CATEGORY_EXTRA, category);
        intent.putExtra(UIConstants.KEY_MESSAGE_SELECTED_EXTRA, selectedInx);
        Log.e("sender", "Broadcasting message, category: " + category + " , slide no# " + selectedInx );
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

}
