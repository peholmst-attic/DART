# DART Message Formats 1.0

DART uses JSON messages sent over AMQP to communicate with different components.
Message formats are versioned so that they can evolve independently of the
implementations of the components.

The messaging is asynchronous, which means that a sender should *always* be prepared
for situations where:
* a response arrives without a request.
* a response arrives more than once.
* a response arrives but is in an invalid or unknown format.
* no response arrives at all.

How these situations are dealt with is up to the individual senders.

## Message classes

Generally, messages can be grouped into four different classes:

* **Request**: The message is a request for additional information. It expects a response within a reasonable time.
* **Response**: The message is a response to a previously received request. It should be possible to relate the response
to the request in some way, for example by using the `conversation-id`.
* **Command**: The message orders the system to perform a specific operation. Some operations are fire-and-forget and do not require a response, whereas other operations require a response (normally success/failure). Operations that require a response must use the `conversation-id`.
* **Broadcast**: The message is sent to many subscribing listeners without being explicitly requested. No responses.

## General message format

All messages are encoded in UTF-8. They also have the same format which consists of a header part and
a body part:

```JSON
{
  "header": {
    "type": "my-message-type",
    "version": "1.0",
    "timestamp": "2016-05-13T21:30:59Z",
    "conversation-id": 12345
  },
  "body": {
  }
}
```

The `header` is always required and consists of the following fields:

* `type`: The type of the message. *Required.*
* `version`: The version of the message format (in this case always 1.0). *Required.*
* `timestamp`: The timestamp when the client first created the message, in ISO 8601. *Optional* for requests, *required* for all other message classes.
* `conversation-id`: A numeric ID that can be used to group together related messages,
for example when the sender expects a response. *Optional* except for some commands.

Messages can also contain additional header fields.

The `body` is either required or optional depending on the message and can contain anything.

## Application messages

Here are the specifications for the application messages:

* [Resource Messages](resource-messages.md)
