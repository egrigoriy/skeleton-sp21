package gitlet;

import java.util.*;

/**
 * Represents a Split Commit Finder using DAG under hood
 *
 *  @author Grigoriy Emiliyanov
 */
public class SplitCommitFinder {
    private final Map<String, List<String>> adjMap = new HashMap<>();

    private static class Distance {
        private String hash;
        private Integer distance;
        Distance(String hash, Integer distance) {
            this.hash = hash;
            this.distance = distance;
        }
    }

    /**
     * Returns the split commit (latest common ancestor) of two given commits
     * @param c1
     * @param c2
     * @return commit
     */
    public static Commit find(Commit c1, Commit c2) {
        SplitCommitFinder dag = new SplitCommitFinder();
        dag.addSourceNode(c1);
        dag.addSourceNode(c2);
        return dag.getLatestCommonAncestor(c1, c2);
    }

    /**
     * Adds given commit and its ancestors to the adjacency map
     * @param c
     */
    private void addSourceNode(Commit c) {
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

    /**
     * Returns commits ids of all commits in the given list
     * @param commits
     * @return commits ids
     */
    private List<String> getUids(List<Commit> commits) {
        List<String> uids = new ArrayList<>();
        for (Commit commit : commits) {
            uids.add(commit.getUid());
        }
        return uids;
    }

    /**
     * Returns a list of non-null parents of the given commit
     * @param c
     * @return list of non-null parents
     */
    private static List<Commit> getCommitParents(Commit c) {
        List<Commit> result = new LinkedList<>();
        String firstParent = c.getFirstParent();
        String secondParent = c.getSecondParent();
        if (firstParent != null) {
            result.add(Repository.getCommit(firstParent));
        }
        if (secondParent != null) {
            result.add(Repository.getCommit(secondParent));
        }
        return result;
    }

    /**
     * Returns the latest common ancestor of two given commits
     * @param c1
     * @param c2
     * @return commit
     */
    private Commit getLatestCommonAncestor(Commit c1, Commit c2) {
        Map<String, Integer> dist1 = getDistances(c1.getUid());
        Map<String, Integer> dist2 = getDistances(c2.getUid());
        Set<String> keys = new HashSet<>();
        keys.addAll(dist1.keySet());
        keys.addAll(dist2.keySet());

        String best = "";
        Integer shortestLevel = 999;
        for (String key : keys) {
            if (dist1.containsKey(key) && dist2.containsKey(key)) {
                Integer candidate = Math.min(dist1.get(key), dist2.get(key));
                if (candidate < shortestLevel) {
                    best = key;
                    shortestLevel = candidate;
                }
            }
        }
        return Repository.getCommit(best);
    }

    /**
     * Returns the distances from given uid to its ancestors in form of map
     * @param startUid
     * @return a map of distances
     */
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
}
