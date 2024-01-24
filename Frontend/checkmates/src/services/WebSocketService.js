import {Client} from '@stomp/stompjs';

class WebSocketService {
    constructor(url) {
        this.url = url;
        this.stompClient = new Client({
            brokerURL: this.url,
            debug: function (str) {
                console.log('STOMP: ' + str);
            },
            reconnectDelay: 5000, // Optional: Automatic reconnection delay in ms
        });
        this.connected = false;
        this.subscriptions ={}
    }

    connect(onConnect, onError) {
        this.stompClient.onConnect = frame => {
            this.connected = true;
            console.log('Connected: ' + frame);
            if (onConnect) onConnect(frame);
        };

        this.stompClient.onStompError = frame => {
            console.error('Broker reported error: ' + frame.headers['message']);
            console.error('Additional details: ' + frame.body);
            if (onError) onError(frame);
        };

        this.stompClient.activate();
    }

    disconnect() {
        return new Promise((resolve, reject) => {
            if (this.stompClient && this.connected) {
                this.stompClient.deactivate().then(() => {
                    this.connected = false;
                    resolve();
                }).catch(error => {
                    console.error("Error during disconnection:", error);
                    reject(error);
                });
            } else {
                resolve();
            }
        });
    }


    isConnected() {
        return this.connected && this.stompClient.active;
    }

    subscribe(topic, onMessage) {
        if (this.stompClient && this.isConnected()) {
            return this.stompClient.subscribe(topic, message => {
                if (onMessage) onMessage(JSON.parse(message.body));
            });
        } else {
            console.error("Cannot subscribe, STOMP connection is not established");
        }
    }

    sendMessage(destination, message) {
        if (this.stompClient && this.isConnected()) {
            const msg = typeof message === 'string' ? message : JSON.stringify(message);
            this.stompClient.publish({ destination, body: msg });
        } else {
            console.error("Cannot send message, STOMP connection is not established");
        }
    }

    unsubscribe(subscription) {
        if (subscription) {
            subscription.unsubscribe();
        }
    }
    // registerSubscription(topic, onMessage) {
    //     if (this.stompClient && this.isConnected()) {
    //         const subscription = this.stompClient.subscribe(topic, message => {
    //             if (onMessage) onMessage(JSON.parse(message.body));
    //         });
    //
    //     } else {
    //         console.error("Cannot register subscription, STOMP connection is not established");
    //         return null;
    //     }
    // }
    registerSubscription(topic, onMessage) {
        if (!this.isConnected()) {
            console.error("Cannot register subscription, STOMP connection is not established");
            return null;
        }
        if (this.isSubscribed(topic)) {
            console.log(`Already subscribed to ${topic}`);
            return this.subscriptions[topic];
        }
        const subscription = this.stompClient.subscribe(topic, message => {
            if (onMessage) onMessage(JSON.parse(message.body));
        });
        this.subscriptions[topic] = subscription;
        return subscription;
    }
    removeSubscription(topic) {
        const subscription = this.subscriptions[topic];
        if (subscription) {
            subscription.unsubscribe();
            delete this.subscriptions[topic];
            console.log(`Unsubscribed from topic: ${topic}`);
        } else {
            console.error("Cannot remove subscription, no subscription for topic:", topic);
        }
    }

    isSubscribed(topic) {
        return !!this.subscriptions[topic];
    }
    subscribeToChatTopic(topic, onMessageReceived) {
        if (this.stompClient && this.isConnected()) {
            const subscription = this.stompClient.subscribe(topic, message => {
                if (onMessageReceived) onMessageReceived(JSON.parse(message.body));
            });
            this.subscriptions[topic] = subscription;
        } else {
            console.error("Cannot subscribe, STOMP connection is not established");
        }
    }
}

export default new WebSocketService('ws://localhost:8080/ws');
