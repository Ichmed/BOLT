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
	
	public CollisionBox(Vector3f... dots)
	{
		float minX = dots[0].x;
		float minY = dots[0].y;
		float minZ = dots[0].z;
		float maxX = dots[0].x;
		float maxY = dots[0].y;
		float maxZ = dots[0].z;
		for(int a = 0; a < dots.length; a++)
		{
			if(dots[a].x < minX)
				minX = dots[a].x;
			else if(dots[a].x > maxX)
				maxX = dots[a].x;
			if(dots[a].y < minY)
				minY = dots[a].y;
			else if(dots[a].y > maxY)
				maxY = dots[a].y;
			if(dots[a].z < minZ)
				minZ = dots[a].z;
			else if(dots[a].z > maxZ)
				maxZ = dots[a].z;
		}
		startingPoint = new Vector3f(minX, minY, minZ);
		width = new Vector3f(maxX - minX, 0, 0);
		height = new Vector3f(0, 0, maxZ - minZ);
		depth = new Vector3f(0, maxY - minY, 0);
		Vector3f.add ((Vector3f)depth.scale (0.5f), (Vector3f)width.scale (0.5f), middle);
		Vector3f.add ((Vector3f)middle, (Vector3f)height.scale (0.5f), middle);
	}
	
	public CollisionBox (float mass, Vector3f... dots)
	{
		this(dots);
		this.mass = mass;
	}
	
	public static CollisionBox createCollisionBox(Vector3f... dots)
	{
		float minX = dots[0].x;
		float minY = dots[0].y;
		float minZ = dots[0].z;
		float maxX = dots[0].x;
		float maxY = dots[0].y;
		float maxZ = dots[0].z;
		Vector3f dotMinX = new Vector3f();
		Vector3f dotMinY = new Vector3f();
		Vector3f dotMinZ = new Vector3f();
		Vector3f dotMaxX = new Vector3f();
		Vector3f dotMaxY = new Vector3f();
		Vector3f dotMaxZ = new Vector3f();
		for(int a = 0; a < dots.length; a++)
		{
			if(dots[a].x < minX)
			{
				minX = dots[a].x;
				dotMinX =dots[a];
			}
			else if(dots[a].x > maxX)
			{
				maxX = dots[a].x;
				dotMaxX =dots[a];
			}
			if(dots[a].y < minY)
			{
				minY = dots[a].y;
				dotMinY =dots[a];
			}
			else if(dots[a].y > maxY)
			{
				maxY = dots[a].y;
				dotMaxY =dots[a];
			}
			if(dots[a].z < minZ)
			{
				minZ = dots[a].z;
				dotMinZ =dots[a];
			}
			else if(dots[a].z > maxZ)
			{
				maxZ = dots[a].z;
				dotMaxZ =dots[a];
			}
		}
		Vector3f bestNormalFront = new Vector3f(0, maxY - minY, 0);
		bestNormalFront.normalise ();
		Vector3f bestNormalLeft = new Vector3f(maxX - minX, 0, 0);
		bestNormalLeft.normalise ();
		Vector3f bestNormalTop = new Vector3f(0, 0, maxZ - minZ);
		bestNormalTop.normalise ();
		float distanceFrontBack = maxY - minY;
		float distanceLeftRight = maxX - minX;
		float distanceTopBottom = maxZ - minZ;
		Plane front = new Plane(bestNormalFront, dotMinY);
		front.TransformToHesseNormalForm ();
		Plane back = new Plane(bestNormalFront, dotMaxY);
		back.TransformToHesseNormalForm ();
		Plane left = new Plane(bestNormalLeft, dotMaxX);
		left.TransformToHesseNormalForm ();
		Plane right = new Plane(bestNormalLeft, dotMinX);
		right.TransformToHesseNormalForm ();
		Plane top = new Plane(bestNormalTop, dotMaxZ);
		top.TransformToHesseNormalForm ();
		Plane bottom = new Plane(bestNormalTop, dotMinZ);
		bottom.TransformToHesseNormalForm ();
		return new CollisionBox();
	}
}
