package physics.Collisionmodels;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import util.MathHelper;
import util.Plane;

//TODO -> Change axis form: x-axis is right-back-axis, y-axis is front-back-axis, z-axis is height-axis
//						to: x-axis is right-back-axis, y-axis is height-axis, z-axis is front-back-axis

public class CollisionBox
{
	public Vector3f startingPoint = new Vector3f(0, 0, 0);
	public Vector3f middle = new Vector3f (0, 0, 0);
	public Vector3f depth = new Vector3f (0, 0, 0);
	public Vector3f width = new Vector3f (0, 0, 0);
	public Vector3f height = new Vector3f (0, 0, 0);
	public float mass = 0;
	
	public CollisionBox () {	}
	
	/**
	 * 
	 * @param startingPoint
	 * @param depth
	 * @param width
	 * @param height
	 */
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
		//TODO change to code: createCollisionBox
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
		//Initializing standards to start with
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
		//setting the standards to a start value
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
		//Initializing "best values" for front and back Points and distances
		Vector3f bestPointBack = new Vector3f();
		Vector3f bestPointFront = new Vector3f();
		Vector3f bestNormalFront = new Vector3f(0, 1, 0);
		float minDistanceFrontBack = Math.abs(maxY - minY);
		Vector3f normalFront = new Vector3f(0, 1, 0);
		//working values for Planes and distances
		float distanceFrontBack = Math.abs(maxY - minY);
		Plane front = new Plane(normalFront, pointBack);
		Plane back = new Plane(normalFront, pointFront);
		//Rotating Planes around the object to a max value of 180° where the planes are just swapped versions of the starting planes
		Plane rotationPlane = new Plane(new Vector3f(0, 0, 1), new Vector3f(0, 0, 0));
		//Planes will rotate around the y-axis
		for(int degree = 1; degree < 180; degree++)
		{
			//Rotating frontPlane and transforming it to the standard (HesseNormalForm)
			MathHelper.rotateVector(normalFront, degree, rotationPlane);
			normalFront.normalise();
			front = new Plane(normalFront, pointFront);
			front.TransformToHesseNormalForm();
			//Initializing temporary maximum values
			Vector3f maxFrontPoint = new Vector3f (0, 0, 0);
			float maxFrontDis = 0;
			//calculating if the frontPlane has to be moved outwards
			for(int i = 0; i < points.length; i++)
			{
				float frontDis = MathHelper.calculateDistancePointPlane(points[i], front);
				if(frontDis > maxFrontDis)
				{
					maxFrontDis = frontDis;
					maxFrontPoint = points[i];
				}
			}
			//calculating if the frontPlane has to be moved inwards
			if(maxFrontDis == 0)
			{
				//Calculating every distance
				ArrayList<Float> distances = new ArrayList<Float>();
				for(int i = 0; i < points.length; i++)
					distances.add(Math.abs(MathHelper.calculateDistancePointPlane(points[i], front)));
				//Comparing the distances and setting the minimum distance
				float mindistance = Float.MAX_VALUE;
				for(int i = 0; i < distances.size(); i++)
					if(distances.get(i) <= mindistance)
					{
						maxFrontPoint = points[i];
						mindistance = distances.get(i);
					}
			}
			//setting the temporary bestFrontPlane and normalize it
			front = new Plane(normalFront, maxFrontPoint);
			front.TransformToHesseNormalForm();
			//Initializing temporary maximum values
			back = new Plane(normalFront, pointBack);
			back.TransformToHesseNormalForm();
			Vector3f maxBackPoint = new Vector3f (0, 0, 0);
			float maxBackDis = 0;
			//calculating if the frontPlane has to be moved outwards
			for(int i = 0; i < points.length; i++)
			{
				float backDis = MathHelper.calculateDistancePointPlane(points[i], back);
				if(backDis > maxBackDis)
				{
					maxBackDis = backDis;
					maxBackPoint = points[i];
				}
			}
			//calculating if the frontPlane has to be moved inwards
			if(maxBackDis == 0)
			{
				ArrayList<Float> distances = new ArrayList<Float>();
				for(int i = 0; i < points.length; i++)
					distances.add(Math.abs(MathHelper.calculateDistancePointPlane(points[i], back)));
				//Comparing the distances and setting the minimum distance
				float mindistance = Float.MAX_VALUE;
				for(int i = 0; i < distances.size(); i++)
					if(distances.get(i) <= mindistance)
					{
						maxBackPoint = points[i];
						mindistance = distances.get(i);
					}
			}
			//setting the final (best)Values (for the next rotation as compareValues)
			back = new Plane(normalFront, maxBackPoint);
			back.TransformToHesseNormalForm();
			distanceFrontBack = Math.abs(MathHelper.calculateDistancePointPlane(maxBackPoint, front));
			if(distanceFrontBack < minDistanceFrontBack)
			{
				minDistanceFrontBack = distanceFrontBack;
				bestPointFront = maxFrontPoint;
				bestPointBack = maxBackPoint;
			}
			pointFront = maxFrontPoint;
			pointBack = maxBackPoint;
		}
		//setting the final best front/back planes
		front = new Plane(bestNormalFront, bestPointBack);
		back = new Plane(bestNormalFront, bestPointFront);
		//
		//
		//
		//   !!!WORKING ON LEFT RIGHT!!!
		//
		//
		//
		//Initializing "best values" for left and right Points and distances
		Vector3f bestPointLeft = new Vector3f();
		Vector3f bestPointRight = new Vector3f();
		Vector3f bestNormalLeft = MathHelper.createPerpendicularVector(bestNormalFront);
		float minDistanceLeftRight = Math.abs(maxX - minX);
		Vector3f normalLeft = bestNormalLeft;
		//working values for Planes and distances
		float distanceLeftRight = Math.abs(maxX - minX);
		Plane left = new Plane(normalLeft, pointLeft);
		Plane right = new Plane(normalLeft, pointRight);
		//Rotating Planes around the object to a max value of 180° where the planes are just swapped versions of the starting planes
		rotationPlane = new Plane(bestNormalFront, new Vector3f(0, 0, 0));
		//Planes will rotate around the y-axis
		for(int degree = 1; degree < 180; degree++)
		{
			//Rotating frontPlane and transforming it to the standard (HesseNormalForm)
			MathHelper.rotateVector(normalLeft, degree, rotationPlane);
			normalLeft.normalise();
			left = new Plane(normalLeft, pointFront);
			left.TransformToHesseNormalForm();
			//Initializing temporary maximum values
			Vector3f maxFrontPoint = new Vector3f (0, 0, 0);
			float maxFrontDis = 0;
			//calculating if the frontPlane has to be moved outwards
			for(int i = 0; i < points.length; i++)
			{
				float frontDis = MathHelper.calculateDistancePointPlane(points[i], left);
				if(frontDis > maxFrontDis)
				{
					maxFrontDis = frontDis;
					maxFrontPoint = points[i];
				}
			}
			//calculating if the frontPlane has to be moved inwards
			if(maxFrontDis == 0)
			{
				//Calculating every distance
				ArrayList<Float> distances = new ArrayList<Float>();
				for(int i = 0; i < points.length; i++)
					distances.add(Math.abs(MathHelper.calculateDistancePointPlane(points[i], left)));
				//Comparing the distances and setting the minimum distance
				float mindistance = Float.MAX_VALUE;
				for(int i = 0; i < distances.size(); i++)
					if(distances.get(i) <= mindistance)
					{
						maxFrontPoint = points[i];
						mindistance = distances.get(i);
					}
			}
			//setting the temporary bestFrontPlane and normalize it
			left = new Plane(normalLeft, maxFrontPoint);
			left.TransformToHesseNormalForm();
			//Initializing temporary maximum values
			right = new Plane(normalLeft, pointBack);
			right.TransformToHesseNormalForm();
			Vector3f maxBackPoint = new Vector3f (0, 0, 0);
			float maxBackDis = 0;
			//calculating if the frontPlane has to be moved outwards
			for(int i = 0; i < points.length; i++)
			{
				float backDis = MathHelper.calculateDistancePointPlane(points[i], right);
				if(backDis > maxBackDis)
				{
					maxBackDis = backDis;
					maxBackPoint = points[i];
				}
			}
			//calculating if the frontPlane has to be moved inwards
			if(maxBackDis == 0)
			{
				ArrayList<Float> distances = new ArrayList<Float>();
				for(int i = 0; i < points.length; i++)
					distances.add(Math.abs(MathHelper.calculateDistancePointPlane(points[i], right)));
				//Comparing the distances and setting the minimum distance
				float mindistance = Float.MAX_VALUE;
				for(int i = 0; i < distances.size(); i++)
					if(distances.get(i) <= mindistance)
					{
						maxBackPoint = points[i];
						mindistance = distances.get(i);
					}
			}
			//setting the final (best)Values (for the next rotation as compareValues)
			right = new Plane(normalLeft, maxBackPoint);
			right.TransformToHesseNormalForm();
			distanceLeftRight = Math.abs(MathHelper.calculateDistancePointPlane(maxBackPoint, left));
			if(distanceLeftRight < minDistanceLeftRight)
			{
				minDistanceLeftRight = distanceLeftRight;
				bestPointRight = maxFrontPoint;
				bestPointLeft = maxBackPoint;
			}
			pointFront = maxFrontPoint;
			pointBack = maxBackPoint;
		}
		//setting the final best front/back planes
		left = new Plane(bestNormalLeft, bestPointLeft);
		right = new Plane(bestNormalLeft, bestPointRight);
		//
		//
		//
		//
		Vector3f maxPointLeft = new Vector3f();
		Vector3f maxPointRight = new Vector3f();
		rotationPlane = new Plane(bestNormalLeft, new Vector3f(0, 0, 0));
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
		// TODO -> top/bottom
		Vector3f bestNormalTop = new Vector3f();
		Vector3f.cross(bestNormalRight, bestNormalLeft, bestNormalTop);
		Vector3f maxPointBottom = new Vector3f();
		Vector3f maxPointTop = new Vector3f();
		//Code for right Top and Bottom Plane missing
		float bestDistanceTopBottom = Math.abs(maxZ - minZ);
		return new CollisionBox();
	}
}
