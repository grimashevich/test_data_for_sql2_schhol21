package services;

import models.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.IntStream;

public class Generator {

    GenContext context;
    private final List<Peer> peers;
    private final List<Task> tasks;
    private final Random random;
    private final String stateStart;
    private final String stateSuccess;
    private final String stateFailure;

    public Generator(GenContext genContext) {
        this.context = genContext;
        this.peers = genContext.getPeerList();
        this.tasks = genContext.getTaskList();
        this.random = Objects.requireNonNullElseGet(genContext.getRandom(), Random::new);
        stateStart = genContext.getStateStart();
        stateSuccess = genContext.getStateSuccess();
        stateFailure = genContext.getStateFailure();
    }

    public void generate() {
        int taskCount = tasks.size();

        for (Peer peer: peers) {
            int peerProgress = random.nextInt(taskCount + 1); //Кол-во выполненных задач от всего курса

            int currentTaskIndex = 0;
            Task currentTask;
            /* Добавляем успешно пройденные проверки, в том числе не с первого раза */
            while (currentTaskIndex < peerProgress) {
                currentTask = tasks.get(currentTaskIndex);

                Check currentCheck = addCheck(peer, currentTask);
                P2p firstP2p = addFirstP2p(peer,currentTask, currentCheck);

                /* Добавляем fail при p2p проверках */
                if (random.nextInt(100) < 20) {
                    addSecondP2p(stateFailure, firstP2p);
                    continue;
                }
                P2p secondP2p = addSecondP2p(stateSuccess, firstP2p);

                /* Добавляем fail при проверках Verter-ом*/
                if (currentTask.isCheckedByVerter()) {
                    if (random.nextInt(100) < 40) {
                        addVerter(stateFailure, currentCheck, secondP2p);
                        continue;
                    }
                    addVerter(stateSuccess, currentCheck, secondP2p);
                }
                addXp(currentTask, currentCheck);
                currentTaskIndex++;
            }
            /* Добавляем незаконченные задачи (проверка провалена или не проводилась)*/
            if (random.nextInt(100) < 80 && currentTaskIndex < context.getTaskList().size()) {
                currentTask = tasks.get(currentTaskIndex);

                Check currentCheck = addCheck(peer, currentTask);
                P2p firstP2p = addFirstP2p(peer,currentTask, currentCheck);

                /* Пиры, которые имею открытую p2p проверку */
                if (random.nextInt(100) < 30) {
                    continue;
                }

                /* Добавляем fail при p2p проверках */
                if (random.nextInt(100) < 20) {
                    addSecondP2p(stateFailure, firstP2p);
                    continue;
                }
                P2p secondP2p = addSecondP2p(stateSuccess, firstP2p);

                if (currentTask.isCheckedByVerter() && random.nextInt(100) < 40) {
                    addVerter(stateFailure, currentCheck, secondP2p);
                    continue;
                }
            }

            /* Генерим френд-лист */
            int friendCount = context.getFriendsCount(peer);
            for (int i = Math.max(0, random.nextInt(15) - friendCount); i > 0;) {
                Peer friendPeer = context.getPeerList().get(random.nextInt(context.getPeerList().size()));
                if (peer.equals(friendPeer) || context.isTheyFriends(peer, friendPeer)) {
                    continue;
                }
                Friends friends = peer.getNickName().compareTo(friendPeer.getNickName()) < 0 ?
                        new Friends(Friends.getNextId(Friends.class), peer.getNickName(), friendPeer.getNickName()) :
                        new Friends(Friends.getNextId(Friends.class), friendPeer.getNickName(), peer.getNickName());
                context.getFriendsList().add(friends);
                i--;
            }

            /* Генерим рекомендации */
            for (int i = Math.max(0, random.nextInt(20)); i > 0;) {
                Peer recommendedPeer = context.getPeerList().get(random.nextInt(context.getPeerList().size()));
                if (peer.equals(recommendedPeer) || context.isThereRecommendation(peer, recommendedPeer)) {
                    continue;
                }
                Recommendations r = new Recommendations(Recommendations.getNextId(Recommendations.class),
                        peer.getNickName(), recommendedPeer.getNickName());
                context.getRecommendationsList().add(r);
                i--;
            }

            /* Генерим посещения */
            LocalDate currentDay = context.getLearningStartDate().plusDays(random.nextInt(30));
            while (currentDay.isBefore(LocalDate.now())
                    && currentDay.isBefore(context.getLastCheckDate(peer).plusDays(30))) {
                addTimeTracking(peer, currentDay);
                currentDay = currentDay.plusDays(random.nextInt(30));
            }
            context.getPeersInfo().get(peer).setNextIncompleteTaskIndex(currentTaskIndex);
        }
        context.getTimeTrackingList().sort((t1, t2) ->
                !t1.getEvent_date().isEqual(t2.getEvent_date()) ?
                t1.getEvent_date().compareTo(t2.getEvent_date())
                :t1.getEvent_time().compareTo(t2.getEvent_time()));
        IntStream.range(1, context.getTimeTrackingList().size() + 1)
                .forEach(i -> context.getTimeTrackingList().get(i - 1).setId(i));
    }

    private Check addCheck(Peer peer, Task task) {
        LocalDate lastCheckDate = context.getLastCheckDate(peer)
                .plusDays(random.nextInt(context.getMaxDayToNextCheck()));
        Check currentCheck = new Check(
                Check.getNextId(Check.class),
                peer.getNickName(),
                task.getTitle(),
                lastCheckDate
        );
        context.getCheckList().add(currentCheck);
        return currentCheck;
    }

    private P2p addFirstP2p(Peer peer, Task task, Check lastCheck) {
        if (lastCheck == null) {
            lastCheck = context.getLastCheck(peer);
        }
        P2p firstP2p = new P2p(
                P2p.getNextId(P2p.class),
                lastCheck.getId(),
                getRandomAnotherPeer(peers, peer).getNickName(),
                stateStart,
                LocalTime.of(0, 0, 0).plusSeconds(random.nextInt(3600 * 23 + 1800))
        );
        context.getP2pList().add(firstP2p);
        return firstP2p;
    }

    private P2p addSecondP2p(String state, P2p firstP2p) {
        LocalTime secondP2pCheckTime = firstP2p.getCheck_time().plusSeconds
                (
                    random.nextInt(15*60,
                            LocalTime.of(23, 59, 29).toSecondOfDay()
                                    - firstP2p.getCheck_time().toSecondOfDay()
                    )
                );
        P2p secondP2p = new P2p(
                P2p.getNextId(P2p.class),
                firstP2p.getChecks_id(),
                firstP2p.getChecking_peer(),
                state,
                secondP2pCheckTime
        );
        context.getP2pList().add(secondP2p);
        return secondP2p;
    }

    private void addVerter(String state, Check check, P2p secondP2p) {
        context.getVerterList().add(
                new Verter(
                        Verter.getNextId(Verter.class),
                        check.getId(),
                        stateStart,
                        secondP2p.getCheck_time().plusSeconds(random.nextInt(6))
                )
        );
        context.getVerterList().add(
                new Verter(
                        Verter.getNextId(Verter.class),
                        check.getId(),
                        state,
                        secondP2p.getCheck_time().plusSeconds(random.nextInt(5, 30))
                )
        );
    }

    private void addXp(Task task, Check check) {
        int xpAmount = random.nextInt(10) >= 7 ? task.getMax_xp()
                : random.nextInt((int) (task.getMax_xp() * 0.5), task.getMax_xp());
        context.getXpList().add(new Xp(Xp.getNextId(Xp.class), check.getId(), xpAmount));
    }


    private Peer getRandomAnotherPeer(List<Peer> peerList, Peer excludedPeer) {
        if (peerList.size() < 2) {
            throw new RuntimeException("peerList size must be >= 2");
        }
        Peer peer;
        int i = 0;
        while ((peer = peerList.get(random.nextInt(peerList.size()))).equals(excludedPeer)) {
            if (i > 10) {
                throw new RuntimeException("Can't get another peer 10 times");
            }
            i++;
        }
        return peer;
    }

    private void addTimeTracking(Peer peer, LocalDate date) {
        int inOutCount = random.nextInt(1, 11);
        int secondsPerDay = 60 * 60 * 24;
        List<LocalTime> timeList = new ArrayList<>(inOutCount * 2);
        LocalTime time = LocalTime.of(0, 0, 0);
        for (int i = 0; i < inOutCount * 2; i++) {
            timeList.add(time.plusSeconds(random.nextInt(secondsPerDay)));
        }
        Collections.sort(timeList);
        for (int i = 0; i < inOutCount; i += 2) {
            context.getTimeTrackingList().add(
                    new TimeTracking(TimeTracking.getNextId(TimeTracking.class),  peer.getNickName(),  date,
                            timeList.get(i),1));
            context.getTimeTrackingList().add(
                    new TimeTracking(TimeTracking.getNextId(TimeTracking.class),  peer.getNickName(),  date,
                            timeList.get(i + 1),2));
        }
    }
}
