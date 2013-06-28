package physics.Collisionmodels;

import org.lwjgl.util.vector.Vector3f;
import util.Plane;

public class CollisionBox
{
	public Vector3f startingPoint = new Vector3f(0, 0, 0);
	public Vector3f middle = new Vector3f (0, 0, 0);
	public Vector3f depth = new Vector3f (0, 0, 0);
	public Vector3f width = new Vector3f (0, 0, 0);
	public Vector3f height = new Vector3f (0, 0, 0);
	public float mass = 0;
	
	public CollisionBox () {	}
	
	public CollisionBox (Vector3f startingPoint, Vector3f depth, Vector3f width, Vector3f height)
	{
		this.startingPoint = startingPoint;
		this.depth = depth;
		this.width = width;
		this.height = height;
		Vector3f.add ((Vector3f)depth.scale (0.5f), (Vector3f)width.scale (0.5f), middle);
		Vector3f.add ((Vector3f)middle, (Vector3f)height.scale (0.5f), middle);
	}
	
	public CollisionBox (float mass, Vector3f startingPoint, Vector3f depth, Vector3f width, Vector3f height)
	{
		this(startingPoint, depth, width, height);
		this.mass = mass;
	}
	
	public CollisionBox(Vector3f... points)
	{
		float minX = points[0].x;
		float minY = points[0].y;
		float minZ = points[0].z;
		float maxX = points[0].x;
		float maxY = points[0].y;
		float maxZ = points[0].z;
		for(int a = 0; a < points.length; a++)
		{
			if(points[a].x < minX)
				minX = points[a].x;
			else if(points[a].x > maxX)
				maxX = points[a].x;
			if(points[a].y < minY)
				minY = points[a].y;
			else if(points[a].y > maxY)
				maxY = points[a].y;
			if(points[a].z < minZ)
				minZ = points[a].z;
			else if(points[a].z > maxZ)
				maxZ = points[a].z;
		}
		startingPoint = new Vector3f(minX, minY, minZ);
		width = new Vector3f(maxX - minX, 0, 0);
		height = new Vector3f(0, 0, maxZ - minZ);
		depth = new Vector3f(0, maxY - minY, 0);
		Vector3f.add ((Vector3f)depth.scale (0.5f), (Vector3f)width.scale (0.5f), middle);
		Vector3f.add ((Vector3f)middle, (Vector3f)height.scale (0.5f), middle);
	}
	
	public CollisionBox (float mass, Vector3f... points)
	{
		this(points);
		this.mass = mass;
	}
	
	//TODO ->
	
	public static CollisionBox createCollisionBox(Vector3f... points)
	{
		float minX = points[0].x;
		float minY = points[0].y;
		float minZ = points[0].z;
		float maxX = points[0].x;
		float maxY = points[0].y;
		float maxZ = points[0].z;
		Vector3f dotMinX = new Vector3f();
		Vector3f dotMinY = new Vector3f();
		Vector3f dotMinZ = new Vector3f();
		Vector3f dotMaxX = new Vector3f();
		Vector3f dotMaxY = new Vector3f();
		Vector3f dotMaxZ = new Vector3f();
		for(int a = 0; a < points.length; a++)
		{
			if(points[a].x < minX)
			{
				minX = points[a].x;
				dotMinX =points[a];
			}
			else if(points[a].x > maxX)
			{
				maxX = points[a].x;
				dotMaxX =points[a];
			}
			if(points[a].y < minY)
			{
				minY = points[a].y;
				dotMinY =points[a];
			}
			else if(points[a].y > maxY)
			{
				maxY = points[a].y;
				dotMaxY =points[a];
			}
			if(points[a].z < minZ)
			{
				minZ = points[a].z;
				dotMinZ =points[a];
			}
			else if(points[a].z > maxZ)
			{
				maxZ = points[a].z;
				dotMaxZ =points[a];
			}
		}
		Vector3f bestNormalFront = new Vector3f(0, maxY - minY, 0);
		Vector3f bestNormalLeft = new Vector3f(maxX - minX, 0, 0);
		Vector3f bestNormalTop = new Vector3f(0, 0, maxZ - minZ);
		float distanceFrontBack = maxY - minY;
		float distanceLeftRight = maxX - minX;
		float distanceTopBottom = maxZ - minZ;
		Vector3f NormalFront = new Vector3f(0, maxY - minY, 0);
		Vector3f NormalLeft = new Vector3f(maxX - minX, 0, 0);
		Vector3f NormalTop = new Vector3f(0, 0, maxZ - minZ);
		bestNormalFront.normalise ();
		bestNormalLeft.normalise ();
		bestNormalTop.normalise ();
		NormalFront.normalise ();
		NormalLeft.normalise ();
		NormalTop.normalise ();
		Plane front = new Plane(bestNormalFront, dotMinY);
		Plane back = new Plane(bestNormalFront, dotMaxY);
		Plane left = new Plane(bestNormalLeft, dotMaxX);
		Plane right = new Plane(bestNormalLeft, dotMinX);
		Plane top = new Plane(bestNormalTop, dotMaxZ);
		Plane bottom = new Plane(bestNormalTop, dotMinZ);
		front.TransformToHesseNormalForm ();
		back.TransformToHesseNormalForm ();
		left.TransformToHesseNormalForm ();
		right.TransformToHesseNormalForm ();
		top.TransformToHesseNormalForm ();
		bottom.TransformToHesseNormalForm ();
		return new CollisionBox();
	}
}
