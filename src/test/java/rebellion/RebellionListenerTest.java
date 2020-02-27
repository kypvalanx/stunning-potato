package rebellion;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class RebellionListenerTest {
    private RebellionListener rebellionListener;

    @Before
    public void before(){
        rebellionListener = new RebellionListener();
    }

    @Test
    @Ignore
    public void currentSheetPrints(){
        Message message = mock(Message.class);
        when(message.getContentRaw()).thenReturn("rebellion sheet");
        JDA jda = mock(JDA.class);
        MessageChannel channel = mock(MessageChannel.class);
        when(channel.sendMessage(any(String.class))).then(new Answer<MessageAction>() {
            @Override
            public MessageAction answer(InvocationOnMock invocation) throws Throwable {
                String param = invocation.getArgumentAt(0, String.class);
                assertThat(param).isEqualTo("Rebellion Level: 0\n" +
                        "Rebellion Max Level: 0\n" +
                        "Rebellion Members: 0\n" +
                        "Rebellion Supporters: 0\n" +
                        "Kintargo Population: 11,900\n" +
                        "Rebellion Treasury: 0\n" +
                        "Rebellion Min Treasury: 0\n" +
                        "Notoriety: 0\n" +
                        "Rebellion Actions: 1\n" +
                        "Max Teams: 2\n" +
                        "Teams Available: 0\n" +
                        "Available Actions: Change Officer Role, Dismiss Team, Guarantee Event, Lie Low, Recruit Supporters, Recruit Team, Special Action, Upgrade Team\n" +
                        "Focused Check Bonus: +2\n");
                return mock(MessageAction.class);
            }
        });
        when(message.getChannel()).thenReturn(channel);
        @NotNull MessageReceivedEvent event = new MessageReceivedEvent(jda, 0L, message);

        rebellionListener.onMessageReceived(event);
        verify(channel.sendMessage(any(String.class)));
    }

}
