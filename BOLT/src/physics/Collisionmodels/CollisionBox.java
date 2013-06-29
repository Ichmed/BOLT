package physics.Collisionmodels;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import util.MathHelper;
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
	
	public static CollisionBox createCollisionBox(Vector3f... points)
	{
		float minX = points[0].x;
		float minY = points[0].y;
		float minZ = points[0].z;
		float maxX = points[0].x;
		float maxY = points[0].y;
		float maxZ = points[0].z;
		Vector3f pointBack = new Vector3f();
		Vector3f pointFront = new Vector3f();
		Vector3f pointLeft = new Vector3f();
		Vector3f pointRight = new Vector3f();
		Vector3f pointTop = new Vector3f();
		Vector3f pointBottom = new Vector3f();
		for(int a = 0; a < points.length; a++)
		{
			if(points[a].x < minX)
			{
				minX = points[a].x;
				pointLeft =points[a];
			}
			else if(points[a].x > maxX)
			{
				maxX = points[a].x;
				pointRight =points[a];
			}
			if(points[a].y < minY)
			{
				minY = points[a].y;
				pointBack =points[a];
			}
			else if(points[a].y > maxY)
			{
				maxY = points[a].y;
				pointFront =points[a];
			}
			if(points[a].z < minZ)
			{
				minZ = points[a].z;
				pointBottom =points[a];
			}
			else if(points[a].z > maxZ)
			{
				maxZ = points[a].z;
				pointTop =points[a];
			}
		}
		Vector3f maxPointBack = new Vector3f();
		Vector3f maxPointFront = new Vector3f();
		Plane rotationPlane = new Plane(new Vector3f(0, 0, 1), new Vector3f(0, 0, 0));
		Vector3f bestNormalFront = new Vector3f(0, 1, 0);
		float minDistanceFrontBack = Math.abs(maxY - minY);
		Vector3f normalFront = new Vector3f(0, 1, 0);
		float distanceFrontBack = Math.abs(maxY - minY);
		Plane front = new Plane(normalFront, pointBack);
		Plane back = new Plane(normalFront, pointFront);
		for(int degree = 1; degree < 180; degree++)
		{
			MathHelper.rotateVector(normalFront, degree, rotationPlane);
			normalFront.normalise();
			front = new Plane(normalFront, pointFront);
			front.TransformToHesseNormalForm();
			Vector3f maxFrontPoint = new Vector3f (0, 0, 0);
			float maxFrontDis = 0;
			for(int i = 0; i < points.length; i++)
			{
				float frontDis = MathHelper.calculateDistancePointPlane(points[i], front);
				if(frontDis > maxFrontDis)
				{
					maxFrontDis = frontDis;
					maxFrontPoint = points[i];
				}
			}
			if(maxFrontDis == 0)
			{
				ArrayList<Float> distances = new ArrayList<Float>();
				for(int i = 0; i < points.length; i++)
					distances.add(Math.abs(MathHelper.calculateDistancePointPlane(points[i], front)));
				float mindistance = Float.MAX_VALUE;
				for(int i = 0; i < distances.size(); i++)
					if(distances.get(i) <= mindistance)
					{
						maxFrontPoint = points[i];
						mindistance = distances.get(i);
					}
			}
			front = new Plane(normalFront, maxFrontPoint);
			front.TransformToHesseNormalForm();
			back = new Plane(normalFront, pointBack);
			back.TransformToHesseNormalForm();
			Vector3f maxBackPoint = new Vector3f (0, 0, 0);
			float maxBackDis = 0;
			for(int i = 0; i < points.length; i++)
			{
				float backDis = MathHelper.calculateDistancePointPlane(points[i], back);
				if(backDis > maxBackDis)
				{
					maxBackDis = backDis;
					maxBackPoint = points[i];
				}
			}
			if(maxBackDis == 0)
			{
				ArrayList<Float> distances = new ArrayList<Float>();
				for(int i = 0; i < points.length; i++)
					distances.add(Math.abs(MathHelper.calculateDistancePointPlane(points[i], back)));
				float mindistance = Float.MAX_VALUE;
				for(int i = 0; i < distances.size(); i++)
					if(distances.get(i) <= mindistance)
					{
						maxBackPoint = points[i];
						mindistance = distances.get(i);
					}
			}
			back = new Plane(normalFront, maxBackPoint);
			back.TransformToHesseNormalForm();
			distanceFrontBack = Math.abs(MathHelper.calculateDistancePointPlane(maxBackPoint, front));
			if(distanceFrontBack < minDistanceFrontBack)
			{
				minDistanceFrontBack = distanceFrontBack;
				maxPointFront = maxFrontPoint;
				maxPointBack = maxBackPoint;
			}
			pointFront = maxFrontPoint;
			pointBack = maxBackPoint;
		}
		// TODO ->
		front = new Plane(bestNormalFront, maxPointBack);
		back = new Plane(bestNormalFront, maxPointFront);
		Vector3f maxPointLeft = new Vector3f();
		Vector3f maxPointRight = new Vector3f();
		rotationPlane = new Plane(bestNormalFront, new Vector3f(0, 0, 0));
		Vector3f bestNormalRight = new Vector3f(-1, 0, 0);
		float bestDistanceLeftRight = Math.abs(maxX - minX);
		Vector3f normalRight = new Vector3f(-1, 0, 0);
		float distanceLeftRight = Math.abs(maxX - minX);
		Plane left = new Plane(normalRight, pointRight);
		Plane right = new Plane(normalRight, pointLeft);
		for(int degree = 1; degree < 180; degree++)
		{
			MathHelper.rotateVector(normalRight, degree, rotationPlane);
			normalRight.normalise();
			left = new Plane(normalRight, pointLeft);
			left.TransformToHesseNormalForm();
			right = new Plane(normalRight, pointRight);
			right.TransformToHesseNormalForm();
		}
		Vector3f bestNormalTop = new Vector3f();
		Vector3f.cross(bestNormalRight, bestNormalFront, bestNormalTop);
		Vector3f maxPointBottom = new Vector3f();
		Vector3f maxPointTop = new Vector3f();
		//Code for right Top and Bottom Plane missing
		float bestDistanceTopBottom = Math.abs(maxZ - minZ);
		return new CollisionBox();
	}
}
