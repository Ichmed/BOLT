package util.math;

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
}
