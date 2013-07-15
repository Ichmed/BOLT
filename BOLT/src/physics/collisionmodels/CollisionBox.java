package physics.collisionmodels;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import util.MathHelper;
import util.Plane;

//TODO -> Change axis form: x-axis is right-back-axis, y-axis is front-back-axis, z-axis is height-axis
//						to: x-axis is right-back-axis, y-axis is height-axis, z-axis is front-back-axis

/**
 * a Box shaped CollisionBox
 * @author Felix Schmidt
 */
public class CollisionBox
{
	/**
	 * one edge of the box where all the other vectors start
	 */
	public Vector3f startingPoint = new Vector3f(0, 0, 0);
	/**
	 * the middle of the box
	 */
	public Vector3f middle = new Vector3f (0, 0, 0);
	/**
	 * the depth of the box
	 */
	public Vector3f depth = new Vector3f (0, 0, 0);
	/**
	 * the height of the box
	 */
	public Vector3f width = new Vector3f (0, 0, 0);
	/**
	 * the width of the box
	 */
	public Vector3f height = new Vector3f (0, 0, 0);
	/**
	 * the mass of the ellipsoid
	 * used to calculate falling of objects
	 */
	public float mass = 0;
	
	/**
	 * creates a standard CollisionBox object with all values set to 0
	 */
	public CollisionBox () {	}
	
	/**
	 * creates a CollisionBox with a startingPoint, depth, width and height but mass set to 0
	 * @param startingPoint one edge of the box where all the other vectors start
	 * @param depth the depth of the box
	 * @param width the width of the box
	 * @param height the height of the box
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
	
	/**
	 * creates a CollisionBox with a mass, startingPoint, depth, width and height
	 * @param mass the mass of the surrounded object used to calculate falling of objects
	 * @param startingPoint one edge of the box where all the other vectors start
	 * @param depth the depth of the box
	 * @param width the width of the box
	 * @param height the height of the box
	 */
	public CollisionBox (float mass, Vector3f startingPoint, Vector3f depth, Vector3f width, Vector3f height)
	{
		this(startingPoint, depth, width, height);
		this.mass = mass;
	}
	
	/**
	 * creates a CollisionBox based on the object which should be surrounded given as points but with mass set to 0
	 * @param points all the points of the object
	 */
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
		startingPoint = new Vector3f(minX, minY, minZ);
		width = new Vector3f(maxX - minX, 0, 0);
		height = new Vector3f(0, 0, maxZ - minZ);
		depth = new Vector3f(0, maxY - minY, 0);
	}
	
	/**
	 * creates a CollisionBox with a given mass based on the surrounded object given as points
	 * @param mass the mass of the surrounded object
	 * @param points all the points of the object
	 */
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
		//
		//Adjusting front/back-Plane
		//
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
			MathHelper.rotateVector(normalFront, degree, rotationPlane);
			normalFront.normalise();
			//
			//Rotating frontPlane and transforming it to the standard (HesseNormalForm)
			//
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
					if(distances.get(i) < mindistance)
					{
						maxFrontPoint = points[i];
						mindistance = distances.get(i);
					}
			}
			//setting the temporary bestFrontPlane and normalize it
			front = new Plane(normalFront, maxFrontPoint);
			front.TransformToHesseNormalForm();
			//
			//Rotating backPlane and transforming it to the standard (HesseNormalForm)
			//
			back = new Plane(normalFront, pointBack);
			back.TransformToHesseNormalForm();
			//Initializing temporary maximum values
			Vector3f maxBackPoint = new Vector3f (0, 0, 0);
			float maxBackDis = 0;
			//calculating if the backPlane has to be moved outwards
			for(int i = 0; i < points.length; i++)
			{
				float backDis = MathHelper.calculateDistancePointPlane(points[i], back);
				if(backDis > maxBackDis)
				{
					maxBackDis = backDis;
					maxBackPoint = points[i];
				}
			}
			//calculating if the backPlane has to be moved inwards
			if(maxBackDis == 0)
			{
				ArrayList<Float> distances = new ArrayList<Float>();
				for(int i = 0; i < points.length; i++)
					distances.add(Math.abs(MathHelper.calculateDistancePointPlane(points[i], back)));
				//Comparing the distances and setting the minimum distance
				float mindistance = Float.MAX_VALUE;
				for(int i = 0; i < distances.size(); i++)
					if(distances.get(i) < mindistance)
					{
						maxBackPoint = points[i];
						mindistance = distances.get(i);
					}
			}
			//setting the round-best-Values (for the next rotation as compareValues)
			back = new Plane(normalFront, maxBackPoint);
			back.TransformToHesseNormalForm();
			distanceFrontBack = Math.abs(MathHelper.calculateDistancePointPlane(maxBackPoint, front));
			if(distanceFrontBack < minDistanceFrontBack)
			{
				minDistanceFrontBack = distanceFrontBack;
				bestPointFront = maxFrontPoint;
				bestPointBack = maxBackPoint;
				bestNormalFront = normalFront;
			}
			pointFront = maxFrontPoint;
			pointBack = maxBackPoint;
		}
		//setting the final best front/back planes
		front = new Plane(bestNormalFront, bestPointBack);
		back = new Plane(bestNormalFront, bestPointFront);
		//
		//Adjusting left/right-Plane
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
		//Planes will rotate around the normalVector of the left/right-Plane
		for(int degree = 1; degree < 180; degree++)
		{
			MathHelper.rotateVector(normalLeft, degree, rotationPlane);
			normalLeft.normalise();
			//
			//Rotating leftPlane and transforming it to the standard (HesseNormalForm)
			//
			left = new Plane(normalLeft, pointLeft);
			left.TransformToHesseNormalForm();
			//Initializing temporary maximum values
			Vector3f maxLeftPoint = new Vector3f (0, 0, 0);
			float maxLeftDis = 0;
			//calculating if the leftPlane has to be moved outwards
			for(int i = 0; i < points.length; i++)
			{
				float leftDis = MathHelper.calculateDistancePointPlane(points[i], left);
				if(leftDis > maxLeftDis)
				{
					maxLeftDis = leftDis;
					maxLeftPoint = points[i];
				}
			}
			//calculating if the leftPlane has to be moved inwards
			if(maxLeftDis == 0)
			{
				//Calculating every distance
				ArrayList<Float> distances = new ArrayList<Float>();
				for(int i = 0; i < points.length; i++)
					distances.add(Math.abs(MathHelper.calculateDistancePointPlane(points[i], left)));
				//Comparing the distances and setting the minimum distance
				float mindistance = Float.MAX_VALUE;
				for(int i = 0; i < distances.size(); i++)
					if(distances.get(i) < mindistance)
					{
						maxLeftPoint = points[i];
						mindistance = distances.get(i);
					}
			}
			//setting the temporary bestLeftPlane and normalize it
			left = new Plane(normalLeft, maxLeftPoint);
			left.TransformToHesseNormalForm();
			//
			//Rotating rightPlane and transforming it to the standard (HesseNormalForm)
			//
			right = new Plane(normalLeft, pointRight);
			right.TransformToHesseNormalForm();
			//Initializing temporary maximum values
			Vector3f maxRightPoint = new Vector3f (0, 0, 0);
			float maxRightDis = 0;
			//calculating if the leftPlane has to be moved outwards
			for(int i = 0; i < points.length; i++)
			{
				float rightDis = MathHelper.calculateDistancePointPlane(points[i], right);
				if(rightDis > maxRightDis)
				{
					maxRightDis = rightDis;
					maxRightPoint = points[i];
				}
			}
			//calculating if the leftPlane has to be moved inwards
			if(maxRightDis == 0)
			{
				ArrayList<Float> distances = new ArrayList<Float>();
				for(int i = 0; i < points.length; i++)
					distances.add(Math.abs(MathHelper.calculateDistancePointPlane(points[i], right)));
				//Comparing the distances and setting the minimum distance
				float mindistance = Float.MAX_VALUE;
				for(int i = 0; i < distances.size(); i++)
					if(distances.get(i) < mindistance)
					{
						maxRightPoint = points[i];
						mindistance = distances.get(i);
					}
			}
			//setting the round-best-Values (for the next rotation as compareValues)
			right = new Plane(normalLeft, maxRightPoint);
			right.TransformToHesseNormalForm();
			distanceLeftRight = Math.abs(MathHelper.calculateDistancePointPlane(maxRightPoint, left));
			if(distanceLeftRight < minDistanceLeftRight)
			{
				minDistanceLeftRight = distanceLeftRight;
				bestPointRight = maxLeftPoint;
				bestPointLeft = maxRightPoint;
				bestNormalLeft = normalLeft;
			}
			pointLeft = maxLeftPoint;
			pointRight = maxRightPoint;
		}
		//setting the final best left/right planes
		left = new Plane(bestNormalLeft, bestPointLeft);
		right = new Plane(bestNormalLeft, bestPointRight);
		//
		//Adjusting topPlane
		//
		//Initializing and setting the normalVector for top/bottom-plane
		Vector3f bestNormalTop = new Vector3f();
		Vector3f.cross(bestNormalFront, bestNormalLeft, bestNormalTop);
		bestNormalTop.normalise();
		//Initializing best values for top point
		Vector3f maxPointTop = new Vector3f();
		//working values for Planes
		Plane top = new Plane(bestNormalTop, pointTop);
		top.TransformToHesseNormalForm();
		float maxTopDis = 0;
		//calculating if the topPlane has to be moved outwards
		for(int i = 0; i < points.length; i++)
		{
			float topDis = MathHelper.calculateDistancePointPlane(points[i], top);
			if(topDis > maxTopDis)
			{
				maxTopDis = topDis;
				maxPointTop = points[i];
			}
		}
		//calculating if the topPlane has to be moved inwards
		if(maxTopDis == 0)
		{
			ArrayList<Float> distances = new ArrayList<Float>();
			for(int i = 0; i < points.length; i++)
				distances.add(Math.abs(MathHelper.calculateDistancePointPlane(points[i], top)));
			//Comparing the distances and setting the minimum distance
			float mindistance = Float.MAX_VALUE;
			for(int i = 0; i < distances.size(); i++)
				if(distances.get(i) < mindistance)
				{
					maxPointTop = points[i];
					mindistance = distances.get(i);
				}
		}
		top = new Plane(bestNormalTop, maxPointTop);
		top.TransformToHesseNormalForm();
		//
		//Adjusting bottomPlane
		//
		//Initializing best values for bottom point
		Vector3f maxPointBottom = new Vector3f();
		//working values for Planes
		Plane bottom = new Plane(bestNormalTop, pointBottom);
		bottom.TransformToHesseNormalForm();
		float maxBottomDis = 0;
		//calculating if the bottomPlane has to be moved outwards
		for(int i = 0; i < points.length; i++)
		{
			float bottomDis = MathHelper.calculateDistancePointPlane(points[i], bottom);
			if(bottomDis > maxBottomDis)
			{
				maxBottomDis = bottomDis;
				maxPointBottom = points[i];
			}
		}
		//calculating if the bottomPlane has to be moved inwards
		if(maxBottomDis == 0)
		{
			ArrayList<Float> distances = new ArrayList<Float>();
			for(int i = 0; i < points.length; i++)
				distances.add(Math.abs(MathHelper.calculateDistancePointPlane(points[i], bottom)));
			//Comparing the distances and setting the minimum distance
			float mindistance = Float.MAX_VALUE;
			for(int i = 0; i < distances.size(); i++)
				if(distances.get(i) < mindistance)
				{
					maxPointBottom = points[i];
					mindistance = distances.get(i);
				}
		}
		bottom = new Plane(bestNormalTop, maxPointBottom);
		bottom.TransformToHesseNormalForm();
		//
		//Calculating the edgePoints of the collisionBox
		//
		//startingPoint
		//TODO Points -> Vectors
		return new CollisionBox();
	}
}
