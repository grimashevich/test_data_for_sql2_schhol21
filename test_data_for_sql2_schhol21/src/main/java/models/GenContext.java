package models;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import services.ImportFromCsv;

import java.sql.Time;
import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
public class GenContext {
    @Setter(AccessLevel.NONE)
    private final String stateStart = "Start";
    @Setter(AccessLevel.NONE)
    private final String stateSuccess = "Success";
    @Setter(AccessLevel.NONE)
    private final String stateFailure = "Failure";
    @Setter(AccessLevel.NONE)
    private final int maxDayToNextCheck = 30; // В этом дней диапазоне будет сгенерирована следующая проверка

    private Random random;
    private LocalDate learningStartDate = LocalDate.of(2020, 1, 1); // Дата начала проверок;

    private List<Peer> peerList;
    private List<Task> taskList;
    private List<Check> checkList;
    private List<P2p> p2pList;
    private List<Verter> verterList;
    private List<Xp> xpList;
    private List<TransferredPoints> transferredPointsList;
    private List<Friends> friendsList;
    private List<Recommendations> recommendationsList;
    private List<TimeTracking> timeTrackingList;

    @Setter(AccessLevel.NONE)
    private Map<Peer, PeerInfo> peersInfo;

    public GenContext() throws Exception {
        this(null);
    }

    public GenContext(Long randomSeed) throws Exception {
        random =  randomSeed == null ? new Random() : new Random(randomSeed);
        peerList = ImportFromCsv.importFromFile("peers.csv", Peer.class);
        taskList = ImportFromCsv.importFromFile("tasks.csv", Task.class);
        checkList = new ArrayList<>();
        p2pList = new ArrayList<>();
        verterList = new ArrayList<>();
        xpList = new ArrayList<>();
        peersInfo = new HashMap<>();
        friendsList = new ArrayList<>();
        recommendationsList = new ArrayList<>();
        timeTrackingList = new ArrayList<>();
        for (Peer peer: peerList) {
            peersInfo.put(peer, new PeerInfo());
        }
    }

    @Data
    public static class PeerInfo {
        private int nextIncompleteTaskIndex;

        public PeerInfo() {
            nextIncompleteTaskIndex = 0;
        }
    }

    public LocalDate getLastCheckDate(Peer peer) {
        return checkList.stream()
                .filter(c -> c.getPeer().equals(peer.getNickName()))
                .map(Check::getCheck_date)
                .reduce((first, second) -> second)
                .orElse(learningStartDate);
    }

    public Check getLastCheck(Peer peer) {
        return checkList.stream()
                .filter(c -> c.getPeer().equals(peer.getNickName()))
                .reduce((first, second) -> second)
                .orElse(null);
    }

    public boolean isTheyFriends(Peer peer1, Peer peer2) {
        return  friendsList.stream()
                .anyMatch(f ->  f.getPeer1().equals(peer1.getNickName())
                                && f.getPeer2().equals(peer2.getNickName())
                             || f.getPeer1().equals(peer2.getNickName())
                                && f.getPeer2().equals(peer1.getNickName())
                );
    }

    public int getFriendsCount(Peer peer) {
        return (int) friendsList.stream()
                .filter(f -> f.getPeer1().equals(peer.getNickName()) || f.getPeer2().equals(peer.getNickName()))
                .count();
    }

    public boolean isThereRecommendation(Peer peer, Peer recommendedPeer) {
        return recommendationsList.stream()
                .anyMatch(r -> r.getPeer().equals(peer.getNickName())
                        && r.getRecommended_peer().equals(recommendedPeer.getNickName()));
    }
}
