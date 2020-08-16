	import java.util.*;
import java.io.*;
public class cowjump
{
	public static void main(String[] args)
	{
		Scanner scan = new Scanner(System.in);
		int n = scan.nextInt();
		Set[] collide = new Set[n];
		for(int i=0; i<collide.length; i++)
		{
			collide[i] = new HashSet<Integer>();
		}
		point[] points = new point[2 * n];
		point[] index = new point[2 * n];
		for(int i=0; i<n; i++)
		{
			int x1 = scan.nextInt();
			int y1 = scan.nextInt();
			int x2 = scan.nextInt();
			int y2 = scan.nextInt();
			if(x2>x1 || x2 == x1 && y2>y1)
			{
			points[2* i] = new point(x1, y1, i, false);
			points[2 * i + 1] = new point(x2, y2, i, true);

				}
			else
			{
				points[2 * i ] = new point(x2, y2, i, false);	
				points[2* i + 1] = new point(x1, y1, i, true);
			}
			index[2 * i] = points[2*i];	
			index[2*i +1] = points[2*i+1];
		}
		Arrays.sort(points);
		int res = -1;
		TreeSet<point> tree = new TreeSet<point>(new compare());
		for(int i=0; i<points.length; i++)
		{
			if(!points[i].isRight)
			{
				tree.add(points[i]);
				//System.out.println(index[2 * points[i].index].x + "->" + points[i].y);
				point high = tree.higher(points[i]);
				point low = tree.lower(points[i]);
				if(low != null)
				{
					low = index[low.index * 2];
				}
				if(high != null)
				{
					high = index[high.index* 2];
				}
				if(low != null && intersect(points[i], index[2*points[i].index+1], low, index[2*low.index+1]))
				{
					System.out.println(low.x + "->" + low.y + "low");
					System.out.println(points[i].x + "->" + points[i].y + "points");
					collide[points[i].index].add(low.index);
					collide[low.index].add(points[i].index);
				}
				if(high != null && intersect(points[i], index[2*points[i].index+1], high, index[2*high.index+1]))
				{
					
					collide[points[i].index].add(high.index);
					collide[high.index].add(points[i].index);
				}
				tree.add(index[2 * points[i].index+1]);
			}
			else
			{
				point low = tree.lower(points[i]);
				point high = tree.higher(points[i]);
				if(low != null && high != null)
				{
					low = index[low.index * 2];
					high = index[high.index * 2];
					if(intersect(low, index[2 *low.index + 1], index[high.index * 2], index[2 * high.index + 1]))
					{
						collide[low.index].add(high.index * 2);
						collide[high.index].add(low.index * 2);
					}
				}
				tree.remove(index[2 * points[i].index]);
				tree.remove(index[2 * points[i].index+1]);
			}
		}
		System.out.println("break");
		System.out.println(intersect(new point(2,1, 0, false), new point(6,1, 0, true), new point(1,5, 0, false), new point(4,0,0, true)));
		int max_index = -1;
		for(int i=0; i<collide.length; i++)
		{
			if(collide[i].size() > 1)
			{
				max_index = i;
				break;
			}
		}

		System.out.println(max_index + 1);
	}
	static boolean onSegment(point p, point q, point r) 
	{ 
    if (q.x <= Math.max(p.x, r.x) && q.x >= Math.min(p.x, r.x) && 
        q.y <= Math.max(p.y, r.y) && q.y >= Math.min(p.y, r.y)) 
       return true; 
  
   	 return false; 
	} 
	public static boolean intersect(point p1, point p2, point q1, point q2)
	{
		double one = cross(p1, p2, q1);
		double two = cross(p1, p2, q2);
		double three =cross(q1, q2, p1);
		double four = cross(q1, q2, p2);
		System.out.println(p1.x + "," + p1.y + "->" + p2.x + "," + p2.y + "->" + q1.x + "," + q1.y + "->" + q2.x + "," + q2.y);
		System.out.println(one + "->" + two + "->" + three + "->" + four);
		if(one == 0 && two == 0 && three == 0 && four == 0)return true;
		return ((one>0 && two<0) || (one<0 && two>0)) && ((three<0 && four>0) || (four<0 && three>0));
	}
	public static double cross(point p, point q, point r) 
	{ 
    // See https://www.geeksforgeeks.org/orientation-3-ordered-points/ 
    // for details of below formula. 
    	return (p.x - q.x) * (q.y - r.y) - ((p.y - q.y) * (q.x - r.x));
	} 
}
class point implements Comparable<point>
{
	int x;
	int y;
	int index;
	boolean isRight;
	public point(int x, int y, int index, boolean isRight)
	{
		this.x = x;
		this.y = y;
		this.index =index;
		this.isRight = isRight;
	}
	public int compareTo(point other)
	{
		if(this.x == other.x)
		{ 
			return this.y - other.y;
		}
		return this.x - other.x;
	}
}
class compare implements Comparator<point>
{
	public int compare(point one, point two)
	{
		if(one.x == two.x && one.y == two.y)return one.index - two.index;
		if(one.y == two.y)return one.x - two.x;
		return one.y - two.y;
	}
}