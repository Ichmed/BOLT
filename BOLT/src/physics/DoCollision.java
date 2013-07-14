package physics;

import org.lwjgl.util.vector.Vector3f;
import physics.Collisionmodels.CollisionSphere;
import physics.Collisionmodels.CollisionBox;

public abstract class DoCollision
{
	public static Vector3f measureSpherePenetration(CollisionSphere sphere1, CollisionSphere sphere2)
	{
		//Vector for distance between the 2 spheres middles
		Vector3f dist = new Vector3f();
		Vector3f.sub (sphere1.middle, sphere2.middle, dist);
		float tempLength = dist.length ();
		//If the 2 Spheres do not collide then the Method will terminate here
		if(tempLength - (sphere1.radius + sphere2.radius) >= 0)
			return new Vector3f(0 ,0 ,0);
		//else put distance to the length of the real surface distance of the Spheres
		dist.normalise ();
		Vector3f p = new Vector3f();
		p = (Vector3f) (dist.scale (tempLength - (sphere1.radius + sphere2.radius)));
		return p;
	}
	
	public static void correctSpherePenetration(CollisionSphere sphere1, CollisionSphere sphere2)
	{
		//Ratio with what each of the different spheres will be pushed away
		float massRatio = sphere1.mass / (sphere1.mass + sphere2.mass);
		Vector3f penetration = measureSpherePenetration( sphere1, sphere2);
		//Moving the Spheres to the right position
		sphere1.middle.translate (penetration.x * massRatio, penetration.y * massRatio, penetration.z * massRatio);
		sphere2.middle.translate ((-penetration.x) * (1 - massRatio), (-penetration.y) * (1 - massRatio), (-penetration.z) * (1 - massRatio));
	}
}
