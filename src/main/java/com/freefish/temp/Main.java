package com.freefish.temp;

import java.util.*;

public class Main {

    // public static void main(String[] args) {
    //     //     int maxDis = Integer.maxDis_VALUE;
    //     //     Scanner scanner = new Scanner(System.in);
    //     //     int n = scanner.nextInt();
    //     //     int m = scanner.nextInt();
    //     //     int k = scanner.nextInt();
    //     //     List<int[]>[] graph = new List[n + 1];
    //     //     for (int i = 0; i < n + 1; ++i) {
    //     //         graph[i] = new LinkedList<>();
    //     //     }
    //     //     for (int i = 0; i < m; ++i) {
    //     //         int u = scanner.nextInt();
    //     //         int v = scanner.nextInt();
    //     //         int w = scanner.nextInt();
    //     //         graph[u].add(new int[]{v, w});
    //     //         graph[v].add(new int[]{u, w});
    //     //     }
    //     //     int[] distTo = new int[n + 1];
    //     //     Arrays.fill(distTo, maxDis);
    //     //     distTo[1] = 0;
    //     //     PriorityQueue<int[]> queue = new PriorityQueue<>(Comparator.comparingInt(o -> o[1]));
    //     //     queue.offer(new int[]{1, 0, 0});
    //     //     while (!queue.isEmpty()) {
    //     //         int[] node = queue.poll();
    //     //         int u = node[0], dis = node[1], lastLen = node[2];
    //     //         if (dis > distTo[u]) {
    //     //             continue;
    //     //         }
    //     //         for (int[] neigh : graph[u]) {
    //     //             int v = neigh[0], w = neigh[1];
    //     //             int len = w;
    //     //             if (w == k * lastLen) {
    //     //                 len = (k - 1) * lastLen;
    //     //             }
    //     //             if (dis + len < distTo[v]) {
    //     //                 distTo[v] = dis + len;
    //     //                 queue.offer(new int[]{v, distTo[v], len});
    //     //             }
    //     //         }
    //     //     }
    //     //     for(int i = 1; i < n; ++i) {
    //     //         System.out.print((distTo[i] != maxDis ? distTo[i] : -1) + " ");
    //     //     }
    //     //     System.out.println(distTo[n] != maxDis ? distTo[n] : -1);
    //     // }
    private static int idx = 0;
    private static int n;
    public static void main(String[] args) {
        int maxDis = Integer.MAX_VALUE;
        int maxCount = (int)(1e7 + 1);
        Map<Integer, Map<Integer, List<Integer>>> edgesIn = new HashMap<>();
        Map<Integer, Map<Integer, List<Integer>>> edgesOut = new HashMap<>();
        int[][] edges = new int[maxCount][3];;
        int[] distTo = new int[maxCount];
        Arrays.fill(distTo, maxDis);
        int[] pre = new int[maxCount];
        Arrays.fill(pre, -1);
        Scanner scanner = new Scanner(System.in);
        n = scanner.nextInt();
        int m = scanner.nextInt();
        int k = scanner.nextInt();
        for (int i = 0; i < m; ++i) {
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            int w = scanner.nextInt();
            addEdge(edgesIn, edgesOut, edges, pre, u, v, w);
            addEdge(edgesIn, edgesOut, edges, pre, v, u, w);
        }
        distTo[1] = 0;
        int count = n;
        for (int i = 1; i <= n; ++i) {
            if (!edgesIn.containsKey(i)) {
                continue;
            }
            for (Map.Entry<Integer, List<Integer>> entry : edgesIn.get(i).entrySet()) {
                int w = entry.getKey();
                List<Integer> nodes = entry.getValue();
                if (edgesOut.get(i).containsKey(w * k)) {
                    int x = ++count, y = ++count;
                    for (Integer u : nodes) {
                        addEdge(edgesIn, edgesOut, edges, pre, u, x, w);
                    }
                    for (Integer u : edgesOut.get(i).get(k * w)) {
                        addEdge(edgesIn, edgesOut, edges, pre, y, u, k * w);
                    }
                    addEdge(edgesIn, edgesOut, edges, pre, x, y, -w);
                }
            }
        }
        Queue<Integer> queue = new LinkedList<>();
        boolean[] visited = new boolean[maxCount];
        queue.offer(1);
        visited[1] = true;
        while (!queue.isEmpty()) {
            int cur = queue.poll();
            visited[cur] = false;
            for (int i = pre[cur]; i != -1; i = edges[i][2]) {
                int x = edges[i][0], w = edges[i][1];
                if (distTo[cur] + w < distTo[x]) {
                    distTo[x] = distTo[cur] + w;
                    if (!visited[x]) {
                        visited[x] = true;
                        queue.offer(x);
                    }
                }
            }
        }
        for(int i = 1; i < n; ++i) {
            System.out.print((distTo[i] != maxDis ? distTo[i] : -1) + " ");
        }
        System.out.println(distTo[n] != maxDis ? distTo[n] : -1);
    }

    private static void addEdge(Map<Integer, Map<Integer, List<Integer>>> edgesIn,
    Map<Integer, Map<Integer, List<Integer>>> edgesOut, int[][] edges, int[] pre, int u, int v, int w) {
        edges[idx] = new int[]{v, w, pre[u]};
        if (u <= n) {
            edgesOut.putIfAbsent(u, new HashMap<>());
            Map<Integer, List<Integer>> wgtIdxMap = edgesOut.get(u);
            List<Integer> list = wgtIdxMap.getOrDefault(w, new LinkedList<>());
            list.add(v);
            wgtIdxMap.put(w, list);
        }
        if (v <= n) {
            edgesIn.putIfAbsent(v, new HashMap<>());
            Map<Integer, List<Integer>> wgtIdxMap = edgesIn.get(v);
            List<Integer> list = wgtIdxMap.getOrDefault(w, new LinkedList<>());
            list.add(u);
            wgtIdxMap.put(w, list);
        }
        pre[u] = idx++;
    }
}
// 4 3 0
// 1 2 2
// 2 3 4
// 3 1 5

// 0 2 5 -1

// 3 3 2
// 1 2 2
// 2 3 4
// 3 1 5

// 0 2 4
