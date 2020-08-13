package org.gary.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.gary.behavior.Behavior;
import org.gary.behavior.DeckList;

public class HistoricalCallBehavior extends Behavior {
    private final Map<String, Stack<DeckList<String>>> historicalCalls;
    private final Behavior behavior;

    public HistoricalCallBehavior(Behavior behavior){
        historicalCalls = new HashMap<>();
        this.behavior = behavior;
    }

    @Override
    public void run(DeckList<String> message, MessageChannel channel) {
        Stack<DeckList<String>> calls = historicalCalls.get(Context.getCaller().getId() + ":" + channel.getId());
        calls.pop();
        behavior.run(new DeckList<>(calls.peek().getAll()), channel);
    }

    public void store(MessageChannel channel, DeckList<String> message) {
        historicalCalls.computeIfAbsent(Context.getCaller().getId() + ":" + channel.getId(),
                s -> {Stack<DeckList<String>> stack = new Stack<>(); stack.setSize(2); return stack;}).push(message);
    }
}
