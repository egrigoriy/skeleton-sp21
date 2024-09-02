package gitlet;

import java.util.*;

public class DAG {
    private final Map<String, List<String>> adjMap = new HashMap<>();

    private class Distance {
        private String hash;
        private Integer distance;
        public Distance(String hash, Integer distance) {
            this.hash = hash;
            this.distance = distance;
        }
    }
    public void addSourceNode(Commit c) {
        // generate graph fill adjMap
//        System.out.println("SOURCE NODE: " + c.getMessage());
        List<Commit> parents = getCommitParents(c);
        adjMap.put(c.getUid(), getUids(parents));
//        System.out.println("PARENTS: " + parents);
        Queue<Commit> queue = new LinkedList<>(parents);
        while (!queue.isEmpty()) {
            Commit current = queue.poll();
            // add parents to todo list
            parents = getCommitParents(current);
            queue.addAll(parents);
            // treat current
            adjMap.put(current.getUid(), getUids(parents));
        }
    }

    public Map<String, Integer> getDistances(Commit c) {
        Map<String, Integer> distances = new HashMap<>();
        Queue<Distance> queue = new LinkedList<>();
        queue.add(new Distance(c.getUid(), 0));
        while (!queue.isEmpty()) {
            Distance current = queue.poll();
            distances.put(current.hash, current.distance);
            for (Commit parent : getCommitParents(Persistor.readCommit(current.hash))) {
                queue.add(new Distance(parent.getUid(), current.distance + 1));
            }
        }
        return distances;
    }
    public Commit getLatestCommonAncestor(Commit c1, Commit c2) {
        Map<String, Integer> dist1 = getDistances(c1);
        Map<String, Integer> dist2 = getDistances(c2);
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
        return Persistor.readCommit(best);
    }


    private List<Commit> getCommitParents(Commit c) {
        List<Commit> result = new LinkedList<>();
        String firstParent = c.getFirstParent();
        String secondParent = c.getSecondParent();
        if (firstParent != null) {
            result.add(Persistor.readCommit(firstParent));
        }
        if (secondParent != null) {
            result.add(Persistor.readCommit(secondParent));
        }
        return result;
    }

    private List<String> getUids(List<Commit> commits) {
        List<String> uids = new ArrayList<>();
        for (Commit commit : commits) {
            uids.add(commit.getUid());
        }
        return uids;
    }

    @Override
    public String toString() {
        return adjMap.toString();
    }
}

