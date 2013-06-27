package physics;

import org.lwjgl.util.vector.Vector3f;
import physics.Collisionmodels.CollisionSphere;
import physics.Collisionmodels.CollisionBox;

public abstract class DoCollision
{
	public static Vector3f measureSpherePenetration(CollisionSphere sphere1, CollisionSphere sphere2)
	{
		Vector3f temp = new Vector3f();
		Vector3f.sub (sphere1.middle, sphere2.middle, temp);
		float tempLength = temp.length ();
		if(tempLength - (sphere1.radius + sphere2.radius) >= 0)
			return new Vector3f(0 ,0 ,0);
		temp.normalise ();
		Vector3f p = new Vector3f();
		p = (Vector3f) (temp.scale (tempLength - (sphere1.radius + sphere2.radius)));
		return p;
	}
	
	public static void correctSpherePenetration(CollisionSphere sphere1, CollisionSphere sphere2)
	{
		float massRatio = sphere1.mass / (sphere1.mass + sphere2.mass);
		Vector3f penetration = measureSpherePenetration( sphere1, sphere2);
		sphere1.middle.translate (penetration.x * massRatio, penetration.y * massRatio, penetration.z * massRatio);
		sphere2.middle.translate ((-penetration.x) * (1 - massRatio), (-penetration.y) * (1 - massRatio), (-penetration.z) * (1 - massRatio));
	}
}
