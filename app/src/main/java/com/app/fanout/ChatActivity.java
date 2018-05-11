package com.app.fanout;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.fanout.communication.Api;
import com.app.fanout.model.ChatModel;
import com.app.fanout.model.MessagesResponse;
import com.google.gson.Gson;
import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.EventSource;
import com.launchdarkly.eventsource.MessageEvent;

import java.net.URI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {

    private ChatAdapter mChatAdapter;
    public static String sUsername;
    private RecyclerView mRecyclerView;
    private EditText mMessage;
    private ProgressBar mProgressBar;
    private boolean isLoading;
    private EventSource mEventSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        setUpRecyclerView();
        setUpEventSource();
        sUsername = getIntent() != null && getIntent().hasExtra("Username") ? getIntent().getStringExtra("Username") : "Unknown User";
        (findViewById(R.id.send)).setOnClickListener(getOnClickListener());
        mMessage = findViewById(R.id.message);
        mProgressBar = findViewById(R.id.progressBar);

    }

    private void setUpRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mChatAdapter = new ChatAdapter();
        mRecyclerView.setAdapter(mChatAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mEventSource.start();
        getMessages();
    }

    @Override
    protected void onStop() {
        mEventSource.close();
        super.onStop();
    }

    private View.OnClickListener getOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(mMessage.getText().toString())) {
                    postMessage();
                }
            }
        };
    }

    private void postMessage() {
        if (!isLoading) {
            isLoading = true;
            mProgressBar.setVisibility(View.VISIBLE);
            Api.getInstance().getEndPoint().sendMessage("default", sUsername, mMessage.getText().toString()).enqueue(new Callback<ChatModel>() {
                @Override
                public void onResponse(@NonNull Call<ChatModel> call, @NonNull Response<ChatModel> response) {
                    isLoading = false;
                    mProgressBar.setVisibility(View.GONE);
                    if (response.isSuccessful()) {
                        mMessage.getText().clear();
                    } else {
                        Toast.makeText(ChatActivity.this, response.code() + ": error! Please try later", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ChatModel> call, @NonNull Throwable t) {
                    isLoading = false;
                    mProgressBar.setVisibility(View.GONE);
                    Toast.makeText(ChatActivity.this, "Some error occurred! Please try later", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void getMessages() {
        if (!isLoading) {
            isLoading = true;
            mProgressBar.setVisibility(View.VISIBLE);
            Api.getInstance().getEndPoint().getMessages("default").enqueue(new Callback<MessagesResponse>() {
                @Override
                public void onResponse(@NonNull Call<MessagesResponse> call, @NonNull Response<MessagesResponse> response) {
                    isLoading = false;
                    mProgressBar.setVisibility(View.GONE);
                    if (response.isSuccessful()) {
                        try {
                            if (response.body().messages.size() > 0) {
                                mChatAdapter.addAll(response.body().messages);
                                mRecyclerView.smoothScrollToPosition(mChatAdapter.getItemCount() - 1);
                            }
                        } catch (NullPointerException ex) {
                            ex.printStackTrace();
                        }

                    } else {
                        Toast.makeText(ChatActivity.this, response.code() + ": error! Please try later", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<MessagesResponse> call, @NonNull Throwable t) {
                    isLoading = false;
                    mProgressBar.setVisibility(View.GONE);
                    Toast.makeText(ChatActivity.this, "Some error occurred! Please try later", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private void setUpEventSource() {
        try {
            mEventSource = new EventSource.Builder(new EventHandler() {
                @Override
                public void onOpen() throws Exception {

                }

                @Override
                public void onClosed() throws Exception {

                }

                @Override
                public void onMessage(String event, MessageEvent messageEvent) throws Exception {
                    if (event.equalsIgnoreCase("message")) {
                        ChatModel item = new Gson().fromJson(messageEvent.getData(), ChatModel.class);
                        if (item != null) {
                            mChatAdapter.add(item);
                            mRecyclerView.smoothScrollToPosition(mChatAdapter.getItemCount() - 1);
                        }
                    }
                }

                @Override
                public void onComment(String comment) throws Exception {

                }

                @Override
                public void onError(Throwable t) {

                }
            }, new URI(Api.EndPoints.API_BASE_URL + "events/?channel=room-default")).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
