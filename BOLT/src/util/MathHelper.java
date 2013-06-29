package util;

import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Vector3f;

public abstract class MathHelper
{
	//TODO: Intersection between a plane and a graph
	
	public static float calculateDistancePointPlane(Vector3f point, Plane hesseNormalForm)
	{
		return (hesseNormalForm.normal.x * point.x + hesseNormalForm.normal.y * point.y + hesseNormalForm.normal.z * point.z - hesseNormalForm.startingPoint.x * hesseNormalForm.normal.x -
				hesseNormalForm.startingPoint.y * hesseNormalForm.normal.y - hesseNormalForm.startingPoint.z * hesseNormalForm.normal.z);
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
}
