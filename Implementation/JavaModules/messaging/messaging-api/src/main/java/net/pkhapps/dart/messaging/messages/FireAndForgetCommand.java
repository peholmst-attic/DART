package net.pkhapps.dart.messaging.messages;

public interface FireAndForgetCommand extends Command {

    interface Builder<M extends FireAndForgetCommand, B extends Builder<M, B>> extends Command.Builder<M, B>{
    }
}
