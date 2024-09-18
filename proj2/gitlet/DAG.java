package gitlet;

import java.util.*;

/**
 * Represents an interface for an object to be stored
 *
 *  @author Grigoriy Emiliyanov
 */
public class DAG {
    private final Map<String, List<String>> adjMap = new HashMap<>();

    private static class Distance {
        private String hash;
        private Integer distance;
        Distance(String hash, Integer distance) {
            this.hash = hash;
            this.distance = distance;
        }
    }
    public void addSourceNode(Commit c) {
        List<Commit> parents = getCommitParents(c);
        adjMap.put(c.getUid(), getUids(parents));
        Queue<Commit> queue = new LinkedList<>(parents);
        while (!queue.isEmpty()) {
            Commit current = queue.poll();
            parents = getCommitParents(current);
            queue.addAll(parents);
            adjMap.put(current.getUid(), getUids(parents));
        }
    }

    public Map<String, Integer> getDistances(String startUid) {
        Map<String, Integer> distances = new HashMap<>();
        Queue<Distance> queue = new LinkedList<>();
        queue.add(new Distance(startUid, 0));
        while (!queue.isEmpty()) {
            Distance current = queue.poll();
            distances.put(current.hash, current.distance);
            for (String parentUid : adjMap.get(current.hash)) {
                queue.add(new Distance(parentUid, current.distance + 1));
            }
        }
        return distances;
    }

    public Commit getLatestCommonAncestor(Commit c1, Commit c2) {
        Map<String, Integer> dist1 = getDistances(c1.getUid());
        Map<String, Integer> dist2 = getDistances(c2.getUid());
        Set<String> keys = new HashSet<>();
        keys.addAll(dist1.keySet());
        keys.addAll(dist2.keySet());

        String best = "";
        Integer shortestLevel = 99;
        for (String key : keys) {
            if (dist1.containsKey(key) && dist2.containsKey(key)) {
                Integer candidate = Math.min(dist1.get(key), dist2.get(key));
                if (candidate < shortestLevel) {
                    best = key;
                    shortestLevel = candidate;
                }
            }
        }
        return Store.readCommit(best);
    }

    private List<String> getUids(List<Commit> commits) {
        List<String> uids = new ArrayList<>();
        for (Commit commit : commits) {
            uids.add(commit.getUid());
        }
        return uids;
    }

    public static List<Commit> getCommitParents(Commit c) {
        List<Commit> result = new LinkedList<>();
        String firstParent = c.getFirstParent();
        String secondParent = c.getSecondParent();
        if (firstParent != null) {
            result.add(Store.readCommit(firstParent));
        }
        if (secondParent != null) {
            result.add(Store.readCommit(secondParent));
        }
        return result;
    }
    @Override
    public String toString() {
        return adjMap.toString();
    }

    public static Commit findSplitCommit(Commit c1, Commit c2) {
        DAG dag = new DAG();
        dag.addSourceNode(c1);
        dag.addSourceNode(c2);
        return dag.getLatestCommonAncestor(c1, c2);
    }
}

