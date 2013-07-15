package util;

import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Vector3f;

public abstract class MathHelper
{	
	public static float calculateDistancePointPlane(Vector3f point, Plane plane)
	{
		plane.TransformToHesseNormalForm();
		return (plane.normal.x * point.x + plane.normal.y * point.y + plane.normal.z * point.z - plane.startingPoint.x * plane.normal.x -
				plane.startingPoint.y * plane.normal.y - plane.startingPoint.z * plane.normal.z);
	}
	
	public static double pyth(float a, float b)
	{
		return Math.sqrt (a * a + b * b);
	}
	
	public static void rotateVector(Vector3f vector, float degree, Plane rotationPlane)
	{
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
		Matrix3f.transform (rotationMatrix, vector, vector);
	}
	
	public static float clamp (float f, float min, float max)
	{
		return Math.max(min, Math.min(f, max));
	}
	
	public static Vector3f createPerpendicularVector(Vector3f startingVector)
	{
		Vector3f perpendicularVector = new Vector3f(1,1,((-startingVector.x-startingVector.y)/startingVector.z));
		perpendicularVector.normalise();
		return perpendicularVector;
	}
	
	public static Vector3f intersectLineWithPlane(Line line, Plane plane)
	{
		plane.TransformToHesseNormalForm();
		return new Vector3f ();
	}
	
	public static Vector3f intersectPlaneWithPlane(Plane plane1, Plane plane2)
	{
		
		return new Vector3f ();
	}
}
