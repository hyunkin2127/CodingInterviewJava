package graph;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.PriorityQueue;

/** https://namu.wiki/w/%EB%8B%A4%EC%9D%B5%EC%8A%A4%ED%8A%B8%EB%9D%BC%20%EC%95%8C%EA%B3%A0%EB%A6%AC%EC%A6%98
 *  Created by Heon on 2015-10-13.
 *  망함 - 버리고 다시 만들자
 */
public class ShortestPathFinder {

	//우선 배열 4개

	int vNum; // 정점 갯수
	int[][] edges; // 간선 정보 : edges[1][3] = 2  --> 1,3번 정점은 연결되어 있으며 그 간선의 가중치는 2. 만약 MAX_VALUE 라면 연결없음
	int[] dist; // 정점 도착에 필요한 비용 : dist[2] == 5 -->  2번 정점에 도달하는데 필요한 총 비용은 5
	int[] path; // 최단 경로 역추적용 배열 : path[3] == 2 --> 3번 정점은 2번을 거쳐서 간다.
	boolean[] foundPathVertex; // 최단경로를 발견한 정점집합. : foundPathVertex[3] = true --> 3번 정점의 경로가 결정됨
	Node[] foundNodes;


	public ShortestPathFinder(int vNum){
		this.vNum = vNum;
		edges = new int[vNum+1][vNum+1]; // 0,0점은 버리기 위해서
		dist = new int[vNum+1];
		path = new int[vNum+1];
		foundPathVertex = new boolean[vNum];

		// 간선정보 초기화
		for(int i=0;i<vNum+1; i++){
			//인접행렬 초기화
			for(int j=0; j<vNum+1; j++){
				if(i==j) edges[i][j] = 0;
				else	edges[i][j] = Integer.MAX_VALUE;
			}
			//공집합을 만든다.
			path[i] = Integer.MAX_VALUE;
		}
	}

	public void insertEdges(int from, int to, int weight){
		edges[from][to] = weight;
	}

	public void findPath(int start){
		foundPathVertex[start] = true;
		dist[start] = 0;
		path[start] = 0;

		for(int i=0; i<vNum+1; i++){
			dist[i] = edges[start][i]; // dist[i] : 정점 i까지 가는데 필요한 비용(가중치합)
		}
				class Edge{
					public int from;
					public int to;
					public int weight;

					public Edge(int from, int to, int weight){
						this.from = from;
						this.to = to;
						this.weight = weight;
			}
		}

		PriorityQueue pq = new PriorityQueue<Edge>(edges.length, new Comparator<Edge>() {
			@Override
			public int compare(Edge e1, Edge e2) { // 오름차순 형태로 정렬 시킨다.
				return e1.weight - e2.weight;
			}
		});

		for(int i=0; i<vNum; i++){
			for(int j=0; j<vNum; j++){ //start를 시작점으로하는 모든 간선정보를 큐에 넣고
				pq.add(new Edge(start, j, edges[start][j]));
			}
			Edge min = (Edge) pq.remove(); // 그중에서 최소가중치값을 갖는 간선을 선택. 이후 큐안에는 간선정보가 계속 남아있다.

			if(dist[i] > dist[start] + min.weight){
				dist[i] = dist[start] + min.weight;
				path[i] = start;
			}
		}
	}

	public void addNodesToPQ(int from, PriorityQueue pq){
		//우선순위큐를 선언해서 from 정점으로부터 이어진 노드들을 담고
		for(int i=0; i<vNum+1; i++){
			if(edges[from][i] != Integer.MAX_VALUE){ //from으로 부터 이어진 노드들을 추가해야되는데
				//이전 노드까지의 비용에 간선의 가중치를 더해서 넣자.
				//그럴려면 이전 정점의 코스트가 필요하다. --> 노드 정보를 모아서 관리해야 함 --> 이게 dist????
				//pq에 새로운 노드(해당노드까지가는데 드는비용, 노드 번호)를 생성하여 삽입
				pq.add(new Node(dist[from]+edges[from][i], from));
			}
		}
	};

 //정점에 이르는 비용을 노드에 담아 이 노드를 큐에 넣고 돌리는 방식은
	//한 정점에 이르는 경로가 여러가지 일경우에
	public void findShortestPath(int start){

		dist[start] = 0; //시작점까지의 비용은 0
		path[start] = 0; //시작점 이전에 들러야할 정점은 없음

		for(int i=1; i<vNum+1; i++){
			dist[i] = edges[start][i]; // dist[i] : 정점 i까지 가는데 필요한 비용(가중치합)
		}

		PriorityQueue pq = new PriorityQueue<Node>(edges.length, new Comparator<Node>() {
			@Override
			public int compare(Node e1, Node e2) { // 오름차순 형태로 정렬 시킨다.
				return e1.cost - e2.cost;
			}
		});
		HashMap<Integer, Node> hm = new HashMap<Integer, Node>(edges.length);
		pq.add(new Node(dist[start]+edges[0][start], start, start));
		for(int from=start; from<vNum+1 && path[from] != Integer.MAX_VALUE; from++){
//			addNodesToPQ(i, pq);// 시작점을 넣고

			for(int to=1; to<vNum+1; to++){
				if(edges[from][to] != Integer.MAX_VALUE && from != to){//from으로 부터 이어진 노드들을 추가해야되는데
					//이전 노드까지의 비용에 간선의 가중치를 더해서 넣자.
					//그럴려면 이전 정점의 코스트가 필요하다. --> 노드 정보를 모아서 관리해야 함 --> 이게 dist????
					//pq에 새로운 노드(해당노드까지가는데 드는비용, 노드 번호)를 생성하여 삽입
					System.out.println("dist["+from+"]:"+ dist[from] + " // edges["+from+"]["+to+"]:"+ edges[from][to]);
					Node n = new Node(dist[from] + edges[from][to], to, from);
					pq.add(n);
					hm.put(to, n);
				}
			}
			Iterator it = pq.iterator();
			Node tempNode;
			while(it.hasNext()){
				tempNode = (Node) it.next();
				System.out.println("!!!!!! " + tempNode.nodeNum+" // "+ tempNode.cost);
			}

			Node minNode = (Node)pq.remove(); // 최소값을 갖는 노드를 반환받았다
			System.out.println("minNode info : nodeNum : "+ minNode.nodeNum+" // cost : " + minNode.cost + " // prevNode : "+minNode.prevNode + " // dist[nodeNum] : " + dist[minNode.nodeNum]);
			//
			// 여기서 문제는 큐 안에 노드 정보가 계속 남아있다는 것.
			// 정점 반환기준을 해당정점에 이르는 최소값만을 기준으로 하기 때문에
			// 이후 정점 선택시,  이전선택된 정점에 연결되어 있지 점이라 할지라도 단순히 cost가 최소인 정점이 반환된다.
			// 그래서 연걸되어있지 않은 정점들로 그래프 최단거리가 구성될 수 있다.

			// 해결책 : 지나온 정점에 대해서만 다음 간선을 선택하도록 해야한다. 어떻게 ???
			// 1. 방문 여부 검사 :  할 수는 있겠지만 뭔가 복잡해 질듯 만약 한다면 스패닝트리에서 사용한 방법 응용하면 될듯
			//		--> path 배열이나 distance 배열을 이용해서 하면 될거같음. --> 이거 간단하게 할려면 hashmap 써야되는데 너무 일이 커지는거 아냐???.

			// 2. 큐를 매번 새롭게 생성한다 : 이게 가장 쉬울듯???? 이렇게 하면 큐에 기존의 노드가 남아있지 않게 되니까
			//	선택되지 않은 정점에 연결된 노드들은 자연스럽게 버려지게 될거 같은데. -->> 이걸로
			//	2번 방식 문제점 : 오로지 from으로 부터 이어지는 정점만을 대상으로 하여 다음 정점을 선택하기 때문에
			//	이전 정점에 연결되어 있으나 비용이 커서 선택되지 못했던 정점들(그러나 현재 노드의 간선비용보다는 적은)이 배제된다.
			// - 이방식 안됨. 기존에 multistage.java는 다단계 그래프로 한번 정점을 선택하고나면 이전단계에 대해선 생각할 필요가 없었지만
			// 원래의 다익스트라 알고리즘은 다름


			if(hm.containsKey(minNode.prevNode) &&  minNode.cost <= dist[minNode.nodeNum] ){
				dist[minNode.nodeNum] = minNode.cost;
				path[minNode.nodeNum] = minNode.prevNode; // from을 사용하면 연결되지 않은 정점과 연결되는 상황이 발생한다.
				System.out.println("dist["+minNode.nodeNum+"] : "+dist[minNode.nodeNum] +" // path["+minNode.nodeNum+"] : "+path[minNode.nodeNum]);
			}
			System.out.println("=================================================");
		}
	}

	public PriorityQueue<Node> createQueue(int len){
		return new PriorityQueue<Node>(len, new Comparator<Node>() {
			@Override
			public int compare(Node e1, Node e2) {
				return e1.cost - e2.cost;
			}
		});
	}


	public void showWay(){

		for(int i=vNum; i>0; i--){
			System.out.print(path[i]+" <= ");
		}

	}

	public int choose( int n)	{
		int min = Integer.MAX_VALUE;
		int min_index = 0;

		for (int i = 0; i < n; i++)		{
			if (!foundPathVertex[i]){ // S에 속하지 않는 정점 중에서
				if (min > dist[i]){
					min = dist[i];
					min_index = i; // 가장 작은 값을 찾음
				}
			}
		}
		return min_index; // 가장 작은 값을 반환
	}


	public void findWay(int start){
		for(int i=0; i<vNum+1; i++){
			dist[i] = edges[start][i];
			path[i] = start;
		}
		foundPathVertex[start] = true;
		dist[start] = 0;
		for (int i = 0; i < vNum - 2; i++){
			int u = choose(vNum); // dist에서 가장 작은 값을 찾음
			//--> v에 연결된 정점들 중에서 가장 간선비용이 적은 정점을 선택한다.

			// (여기서 s[w]=FALSE)
			foundPathVertex[u] = true; // choose 함수가 반환한 정점이 u 이므로 u는 최단경로중 하나로 결정됨 --> u를 true로
			for (int w = 0; w < vNum; w++){
				if(!foundPathVertex[w]){ // S에 속하지 않는 정점 중에서
					// 다른 정점을 거쳐서 가는 것이 더 비용이 적게 든다면
					if(dist[u] + edges[u][w] < dist[w]){// 그 정점을 택함
						dist[w] = dist[u] + edges[u][w];
						path[w] = u; // 역추적을 위한 path 배열에 저장// w로 가는데 u를 거쳐서 갈것
					}
				}
			}
		}
	}

}
