package util;

import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Vector3f;

/**
 * 
 * @author Felix & Michael
 *
 */
public abstract class MathHelper
{
	/**
	 * The pythagorean-theorem
	 * @param a the first cathetus
	 * @param b the second cathetus
	 * @return the hypotenuse
	 */
	public static double pyth(float a, float b)
	{
		return Math.sqrt (a * a + b * b);
	}
	
	/**
	 * Clamps a value between two others
	 * @param value
	 * @param min
	 * @param max
	 * @return min if the value is smaller then min, max if the value is greater than max and the value itself if it is between min and max
	 */
	public static float clamp (float value, float min, float max)
	{
		return Math.max(min, Math.min(value, max));
	}
	
	/**
	 * A method for rotating a vector in a plane (around its normal vector) about degree degree
	 * @param vector the vector which should be rotated
	 * @param degree the degree about which the vector should be rotated
	 * @param rotationPlane the plane within the vector should be rotated
	 */
	public static void rotateVector(Vector3f vector, float degree, Plane rotationPlane)
	{
		//Creating a Matrix for rotating the Vector
		Matrix3f rotationMatrix = new Matrix3f();
		rotationMatrix.m00 = (float)(rotationPlane.normal.x * rotationPlane.normal.x * (1 - Math.cos (degree)) + Math.cos (degree));
		rotationMatrix.m01 = (float)(rotationPlane.normal.x * rotationPlane.normal.y * (1 - Math.cos (degree)) - rotationPlane.normal.z * Math.sin (degree));
		rotationMatrix.m02 = (float)(rotationPlane.normal.x * rotationPlane.normal.z * (1 - Math.cos (degree)) + rotationPlane.normal.y * Math.cos (degree));
		rotationMatrix.m10 = (float)(rotationPlane.normal.x * rotationPlane.normal.y * (1 - Math.cos (degree)) + rotationPlane.normal.z * Math.cos (degree));
		rotationMatrix.m11 = (float)(rotationPlane.normal.y * rotationPlane.normal.y * (1 - Math.cos (degree)) + Math.cos (degree));
		rotationMatrix.m12 = (float)(rotationPlane.normal.y * rotationPlane.normal.z * (1 - Math.cos (degree)) + rotationPlane.normal.x * Math.cos (degree));
		rotationMatrix.m20 = (float)(rotationPlane.normal.x * rotationPlane.normal.z * (1 - Math.cos (degree)) + rotationPlane.normal.y * Math.cos (degree));
		rotationMatrix.m21 = (float)(rotationPlane.normal.z * rotationPlane.normal.y * (1 - Math.cos (degree)) + rotationPlane.normal.x * Math.cos (degree));
		rotationMatrix.m22 = (float)(rotationPlane.normal.z * rotationPlane.normal.z * (1 - Math.cos (degree)) + Math.cos (degree));
		//adopt rotatoinMatrix on vector
		Matrix3f.transform (rotationMatrix, vector, vector);
	}
	
	/**
	 * creates a normalised perpendicular vector to the starting vector
	 * @param startingVector the Vector on which the other one should be vertical on
	 * @return the perpendicular vector
	 */
	public static Vector3f createPerpendicularVector(Vector3f startingVector)
	{
		if(!(startingVector.x == 1 && startingVector.y == 1))
		{
			Vector3f perpendicularVector = new Vector3f(1,1,((-startingVector.x-startingVector.y)/startingVector.z));
			perpendicularVector.normalise();
			return perpendicularVector;
		}
		else
		{
			Vector3f perpendicularVector = new Vector3f(2,-3,((-startingVector.x-startingVector.y)/startingVector.z));
			perpendicularVector.normalise();
			return perpendicularVector;
		}
	}
	
	/**
	 * calculates the distance between a point and a plane
	 * @param point the point
	 * @param plane the plane
	 * @return the distance
	 */
	public static float calculateDistancePointPlane(Vector3f point, Plane plane)
	{
		plane.transformToHesseNormalForm();
		return (plane.normal.x * point.x + plane.normal.y * point.y + plane.normal.z * point.z - plane.startingPoint.x * plane.normal.x -
				plane.startingPoint.y * plane.normal.y - plane.startingPoint.z * plane.normal.z);
	}
	
	/**
	 * intersects a line with a plane
	 * @param line the line
	 * @param plane the plane
	 * @return the Vector3f to the point if parallel method returns the nullVector
	 */
	public static Vector3f intersectLineWithPlane(Line line, Plane plane)
	{
		//Checks weather the line is parallel to the plane
		if(Vector3f.dot(line.direction, plane.normal) == 0)
			return new Vector3f(0, 0, 0);
		plane.transformToHesseNormalForm();
		//Calculates the factor for the direction-vector of the line
		float factor = (Vector3f.dot(plane.normal, plane.startingPoint)-Vector3f.dot(line.direction, line.startingPoint))/
						Vector3f.dot(line.direction, plane.normal);
		return line.getPoint(factor);
	}
	
	//TODO ->
	/**
	 * intersects a plane with another one
	 * @param plane1 the first plane
	 * @param plane2 the second plane
	 * @return the intersectionLine of the 2 planes if parallel a Line with 0-initialization will be returned
	 */
	public static Line intersectPlaneWithPlane(Plane plane1, Plane plane2)
	{
		plane1.transformToHesseNormalForm();
		plane2.transformToHesseNormalForm();
		//Checks weather the planes are parallel
		if(plane1.normal.x == plane2.normal.x && plane1.normal.y == plane2.normal.y && plane1.normal.z == plane2.normal.z)
			return new Line (0, 0, 0, new Vector3f(0,0,0));
		Vector3f direction = new Vector3f();
		direction.normalise();
		Vector3f.cross(plane1.normal, plane2.normal, direction);
		//checks if plane1 is parallel to the x-axis
		if(plane1.normal.x == 0)
		{
			//If plane1 is parallel to the xy-plane
			if(plane1.normal.y == 0)
			{
				float z = plane1.startingPoint.z;
				//Check if plane2 is parallel to the x-axis
				 if(plane2.normal.x == 0)
				{
					float y = (plane2.normal.x * plane2.startingPoint.x + plane2.normal.y * plane2.startingPoint.y + plane2.normal.z * plane2.startingPoint.z -
								plane2.normal.z * z) / plane2.normal.y;
					return new Line(0, y, z, direction);
				}
				//Check if plane 2 is parallel to the z-axis
				else if(plane2.normal.z == 0)
				{
					float x = (plane2.normal.x * plane2.startingPoint.x + plane2.normal.y * plane2.startingPoint.y + plane2.normal.z * plane2.startingPoint.z -
								plane2.normal.z * z) / plane2.normal.x;
					return new Line(x, 0, z, direction);
				}
				else
				{
					float y = (plane2.normal.x * plane2.startingPoint.x + plane2.normal.y * plane2.startingPoint.y + plane2.normal.z * plane2.startingPoint.z -
								plane2.normal.z * z) / plane2.normal.y;
					return new Line(0, y, z, direction);
				}
			}
			//if plane1 is parallel to the xz-plane
			else if(plane1.normal.z == 0)
			{
				float y = plane1.startingPoint.y;
				//Check if plane2 is parallel to the x-axis
				 if(plane2.normal.x == 0)
				{
					float z = (plane2.normal.x * plane2.startingPoint.x + plane2.normal.y * plane2.startingPoint.y + plane2.normal.z * plane2.startingPoint.z -
								plane2.normal.y * y) / plane2.normal.z;
					return new Line(0, y, z, direction);
				}
				//Check if plane 2 is parallel to the y-axis
				else if(plane2.normal.y == 0)
				{
					float x = (plane2.normal.x * plane2.startingPoint.x + plane2.normal.y * plane2.startingPoint.y + plane2.normal.z * plane2.startingPoint.z -
								plane2.normal.y * y) / plane2.normal.x;
					return new Line(x, y, 0, direction);
				}
				else
				{
					float z = (plane2.normal.x * plane2.startingPoint.x + plane2.normal.y * plane2.startingPoint.y + plane2.normal.z * plane2.startingPoint.z -
								plane2.normal.y * y) / plane2.normal.z;
					return new Line(0, y, z, direction);
				}
			}
			//checks if plane2 is parallel to the x-axis too
			else if(plane2.normal.x == 0)
			{
				float z = (Vector3f.dot(plane2.normal, plane2.startingPoint) -
							(plane1.normal.y * plane1.startingPoint.y - plane1.normal.z * plane1.startingPoint.z) *	(plane2.normal.y / plane1.normal.y)) /
							(plane2.normal.z - (plane2.normal.y * plane1.normal.z) / plane1.normal.y);
				float y = (Vector3f.dot(plane1.normal, plane2.startingPoint) - plane1.normal.z * z) / plane1.normal.y;
				return new Line(0, y, z, direction);
			}
			//Check if plane2 is parallel to the y-axis
			else if(plane2.normal.y == 0)
			{
				float x = (Vector3f.dot(plane2.normal, plane2.startingPoint) -
							(plane1.normal.y * plane1.startingPoint.y - plane1.normal.z * plane1.startingPoint.z) *	(plane2.normal.y / plane1.normal.y)) /
							(plane2.normal.x - (plane2.normal.y * plane1.normal.x) / plane1.normal.y);
				float y = (Vector3f.dot(plane1.normal, plane2.startingPoint) - plane1.normal.x * x) / plane1.normal.y;
				return new Line(x, y, 0, direction);
			}
			else
			{
				float x = (Vector3f.dot(plane2.normal, plane2.startingPoint) -
							(plane1.normal.y * plane1.startingPoint.y - plane1.normal.z * plane1.startingPoint.z) *	(plane2.normal.z / plane1.normal.z)) /
							(plane2.normal.x - (plane2.normal.y * plane1.normal.x) / plane1.normal.y);
				float z = (Vector3f.dot(plane1.normal, plane2.startingPoint) - plane1.normal.x * x) / plane1.normal.z;
				return new Line(x, 0, z, direction);
			}
		}
		//checks if plane1 is parallel to the y-axis
		else if(plane1.normal.y == 0)
		{
			
		}
		//checks if plane1 is parallel to the z-axis
		else if(plane1.normal.z == 0)
		{
			
		}
		//checks if plane2 is parallel to the x-axis
		else if(plane2.normal.x == 0)
		{
			
		}
		//checks if plane2 is parallel to the y-axis
		else if(plane2.normal.y == 0)
		{
			
		}
		//checks if plane2 is parallel to the z-axis
		else if(plane2.normal.z == 0)
		{
			
		}
		return new Line ();
	}
}
