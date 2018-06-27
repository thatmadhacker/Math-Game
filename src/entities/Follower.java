package entities;

import java.util.List;
import java.util.Random;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import engineTester.MainGameLoop;
import models.TexturedModel;
import renderEngine.DisplayManager;
import terrain.Terrain;

public class Follower extends Entity{
	double timer = 0;
	private Vector2f destination = new Vector2f();
	private Vector3f velocity = new Vector3f(0,0,0);
	private Random random;
	public Follower(TexturedModel model, Vector3f position,
			EntityType type, Player p) {
		super(model, position, 0,0,0,1, type,0);
		random = new Random();
		pickDestination(p);
	}
	public void update(Player p, List<Terrain> terrains){
		timer += DisplayManager.getFrameTimeSeconds();
		if(timer > .5){
			timer = 0;
			textureIndex++;
			if(textureIndex > model.getTexture().getNumberOfRows()){
				textureIndex = 0;
			}
		}
		Terrain t = null;
		for (Terrain terrain : terrains) {
			if (terrain.getX() <= getPosition().x) {
				if (terrain.getX() + Terrain.SIZE > getPosition().x) {
					if (terrain.getZ() <= getPosition().z) {
						if (terrain.getZ() + Terrain.SIZE > getPosition().z) {
							t = terrain;
							break;
						}
					}
				}
			}
		}
		if(t == null){
			MainGameLoop.entities.remove(this);
		}
		position.x += velocity.x * DisplayManager.getFrameTimeSeconds();
		position.y += velocity.y * DisplayManager.getFrameTimeSeconds();
		position.z += velocity.z * DisplayManager.getFrameTimeSeconds();
		if(position.y != t.getHeightOfTerrain(position.x, position.z)){
			position.y = t.getHeightOfTerrain(position.x, position.z);
		}
		pickDirection();
		if(position.x > destination.x - 10 && position.x < destination.x + 10 && position.z > destination.y - 10 && position.x < destination.y + 10){
			pickDestination(p);
		}
	}
	private void pickDestination(Player p){
		destination = new Vector2f();
		destination.x = p.getPosition().x + random.nextInt(200) + 1 - 100;
		destination.y = p.getPosition().z + random.nextInt(200) + 1 - 100;
	}
	private void pickDirection(){
		velocity.x = 10;
		rotY = getAngle(new Vector2f(position.x,position.z), destination);
	}
	public float getAngle(Vector2f first, Vector2f second) {
	    float angle = (float) Math.toDegrees(Math.atan2(second.y - first.y, second.x - first.x));

	    if(angle < 0){
	        angle += 360;
	    }

	    return angle;
	}

}
