High-Availability Message Consumers
===================================

In a system such as DART, some messages are more important than others. For example, it is vital that a message that
dispatches a unit to an accident is delivered properly, whereas a message informing the system of the geographical
coordinates of a unit is less important since a new message will be sent anyway within a few seconds.

This POC experiments with the important messages.

## Assumptions

Things that have been left out of this POC but that should be taken care of in a real-world application:

- The database is clustered and is available behind the same hostname at all times.
- The RabbitMQ broker is clustered and is available behind the same hostname at all times.
- The RabbitMQ broker is trusted and all messages delivered through it are authentic.

## Main points of interest

Essentially the specifications of the POC:

- A message producer must know when a message has been successfully handled by a consumer. If this does not happen,
  the message should be resent.
    - Message confirmations can not be used to make sure that a message has been processed by a consumer. A reply-to queue
      must be used for this purpose.
- Multiple identical message consumers listen for messages on the same queue. Only one of them should receive a message
  and it should not matter which one - the end result is always the same. If a message consumer fails before having 
  properly handled a message, the RabbitMQ broker should resend the message to another consumer.
    - RabbitMQ will automatically take care of this as long as message acknowledgments are sent properly.
- All message consumers should be able to cope with message duplicates, even when the message was originally sent to and
  handled by a different message consumer. No side effects should occur if a message arrives more than once.
    - Maintaining a list of processed messages is not a good idea since it will eat a lot of memory or disk space.
      Instead, the consumers should be implemented in such a way that they don't duplicate the 'actions' that are implied by the messages.      

These points are not tested using automatic tests. Instead, they are tested by running the applications in different order,
messing with the broker, changing message parameters and rerunning, etc.
