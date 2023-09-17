import models.Friends;
import models.GenContext;
import models.Peer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class FriendshipTests {
    GenContext context;
    Peer peer1;
    Peer peer2;
    Peer peer3;

    public FriendshipTests() throws Exception {
        context =  new GenContext();
        peer1 = new Peer("peer1", LocalDate.of(1984, 1, 1));
        peer2 = new Peer("peer2", LocalDate.of(1984, 1, 1));
        peer3 = new Peer("peer3", LocalDate.of(1984, 1, 1));
        context.getPeerList().add(peer1);
        context.getPeerList().add(peer2);
        context.getPeerList().add(peer3);
    }

    @Test
    void checkFriendship() {
        assertEquals(0, context.getFriendsCount(peer1));
        assertFalse(context.isTheyFriends(peer1, peer2));
        context.getFriendsList().add(new Friends(1, peer1.getNickName(), peer2.getNickName()));
        assertTrue(context.isTheyFriends(peer1, peer2));
        assertEquals(1, context.getFriendsCount(peer1));
        assertEquals(1, context.getFriendsCount(peer2));
    }
}
