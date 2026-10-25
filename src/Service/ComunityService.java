package Service;

import domain.Friendship;
import domain.User;

import java.util.*;

public class ComunityService {
    private final UserService userService;
    private final FriendshipService friendshipService;

    public ComunityService(UserService userService, FriendshipService friendshipService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
    }

    private Map<Long, List<Long>> adjacencyList() {
        Map<Long, List<Long>> adjList = new HashMap<>();
        for (User user : userService.getAllUsers()) {
            adjList.put(user.getID(), new ArrayList<>());
        }
        for (Friendship friendship : friendshipService.getAllFriendships()) {
            Long id1 = friendship.getFirstFriend().getID();
            Long id2 = friendship.getSecondFriend().getID();
            if (adjList.containsKey(id1) && adjList.containsKey(id2)) {
                adjList.get(id1).add(id2);
                adjList.get(id2).add(id1);
            }
        }
        return adjList;
    }

    private List<Long> dfs(Long startNode, Set<Long> visitedNodes, Map<Long, List<Long>> adjList) {
        List<Long> component = new ArrayList<>();
        Stack<Long> stack = new Stack<>();
        stack.push(startNode);
        visitedNodes.add(startNode);

        while (!stack.isEmpty()) {
            Long node = stack.pop();
            component.add(node);
            for (Long neighbour : adjList.get(node)) {
                if (!visitedNodes.contains(neighbour)) {
                    visitedNodes.add(neighbour);
                    stack.push(neighbour);
                }
            }
        }
        return component;
    }

    /**
     * afisare comunitati, nr de componente conexe
     */
    public int getNumberOfCommunities() {
        Map<Long, List<Long>> adjList = adjacencyList();
        Set<Long> visitedNodes = new HashSet<>();
        int communityNumber = 0;

        for (Long node : adjList.keySet()) {
            if (!visitedNodes.contains(node)) {
                dfs(node, visitedNodes, adjList);
                communityNumber++;
            }
        }
        return communityNumber;
    }

    /**
     * determina cel mai scurt drum de la startNode la toate celelalte noduri din componenta sa
     * returneaza distanta maxima gasita
     */
    private int longestPathFromNode(Long startNode, Map<Long, List<Long>> adjList) {
        Queue<Long> queue = new LinkedList<>();
        Map<Long, Integer> distances = new HashMap<>();

        queue.add(startNode);
        distances.put(startNode, 0);
        int maxDistance = 0;

        while (!queue.isEmpty()) {
            Long currentNode = queue.poll();
            int currentDist = distances.get(currentNode);

            for (Long neighbour : adjList.get(currentNode)) {
                if (!distances.containsKey(neighbour)) {
                    distances.put(neighbour, currentDist + 1);
                    maxDistance = Math.max(maxDistance, currentDist + 1);
                    queue.add(neighbour);
                }
            }
        }
        return maxDistance;
    }

    /**
     * ruleaza algm de cel mai lung nod pe fiecare nod
     */
    private int getLongestPathInComponent(List<Long> component, Map<Long, List<Long>> adjList) {
        int maxPath = 0;
        for (Long node : component) {
            int longestFromNode = longestPathFromNode(node, adjList);
            if (longestFromNode > maxPath) {
                maxPath = longestFromNode;
            }
        }
        return maxPath;
    }

    /**
     * identifica toate comp conexe prin dfs
     * pt fiecare componenta calculeaza diametrul
     * comunitatea cu diametrul cel mai mare este returnata
     */
    public List<Long> getMostSociableCommunity() {
        Map<Long, List<Long>> adjList = adjacencyList();
        Set<Long> visitedNodes = new HashSet<>();

        List<Long> mostSociableCommunity = new ArrayList<>();
        int maxLongestPath = -1;

        for (Long node : adjList.keySet()) {
            if (!visitedNodes.contains(node)) {
                List<Long> component = dfs(node, visitedNodes, adjList);
                int componentLongestPath = getLongestPathInComponent(component, adjList);

                if (componentLongestPath > maxLongestPath) {
                    maxLongestPath = componentLongestPath;
                    mostSociableCommunity = component;
                }
            }
        }
        return mostSociableCommunity;
    }
}