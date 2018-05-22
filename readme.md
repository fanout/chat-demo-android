# Fanout Android Chat Demo
> Demo app to represent how Fanout eventstream works on Android

A barebone chat app running on Fanout's eventstream functionality to provide a minimalistic and realtime chatting experience.


## Features

- [x] Select a username to use within a room
- [x] Send/Receive realtime messages to/from a specific room
- [x] Retreive message history using GET API calls

# okhttp-eventsource

Okhttp-eventsource is a Java EventSource implementation based on OkHttp. This library allows Java developers to consume Server Sent Events (SSE) from a remote API. The server sent events spec is defined here: https://html.spec.whatwg.org/multipage/server-sent-events.html . The referenced library can be found here: https://github.com/launchdarkly/okhttp-eventsource

### Installation

To install okhttp-eventsource, add the following dependency in your gradle:


```sh
implementation 'com.launchdarkly:okhttp-eventsource:1.8.0'
```

### Setup

Setup EventSource in your activity or fragment:

```java
import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.EventSource;
import com.launchdarkly.eventsource.MessageEvent;

…

private void setUpEventSource() {
    try {
        // declare class level variable
        mEventSource = new EventSource.Builder(new EventHandler() {
            @Override
            public void onOpen() throws Exception {
            }

            @Override
            public void onClosed() throws Exception {
            }

            @Override
            public void onMessage(String event, MessageEvent messageEvent) throws Exception {
            }

            @Override
            public void onComment(String comment) throws Exception {
            }

            @Override
            public void onError(Throwable t) {
            }
        }, new URI(ADD_YOUR_URL_HERE)).build();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
```

After setting this up, start event listening in onResume and cancel event listening in onStop like this:

```java
@Override
protected void onResume() {
    super.onResume();
    mEventSource.start();
}

@Override
protected void onStop() {
    mEventSource.close();
    super.onStop();
}
```
