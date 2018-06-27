package entities;

import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import terrain.Terrain;

public class Player extends Entity {
	private static final float RUN_SPEED = 20;
	private static final float TURN_SPEED = 160;
	public static final float GRAVITY= -50;
	private static final float JUMP_POWER = 30;
	private float upwardsSpeed = 0;
	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	private boolean isJumping = false;
	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale,EntityType.PLAYER);
		super.increaseRotation(0, 180, 0);
	}
	public void move(Terrain terrain,List<Entity> entities, Player player,Loader loader){
		checkInputs(entities, player,terrain,loader);
		super.increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
		super.increasePosition(dx, 0, dz);
		upwardsSpeed+=GRAVITY * DisplayManager.getFrameTimeSeconds();
		super.increasePosition(0, upwardsSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		float terrainHeight;
		if(terrain != null){
			terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);
		}else{
			terrainHeight = 0;
		}
		if(super.getPosition().getY()<terrainHeight){
			super.getPosition().setY(terrainHeight);
			upwardsSpeed = 0;
			isJumping = false;
		}
	}
	private void jump(){
		if(!isJumping){
			this.upwardsSpeed = JUMP_POWER;
			isJumping = true;
		}
	}
	public void checkInputs(List<Entity> entities, Player player,Terrain terrain, Loader loader){
		if(Keyboard.isKeyDown(Keyboard.KEY_W)){
			this.currentSpeed = RUN_SPEED;
		}else if(Keyboard.isKeyDown(Keyboard.KEY_S)){
			this.currentSpeed = -RUN_SPEED;
		}else{
			this.currentSpeed = 0;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D)){	
			this.currentTurnSpeed = -TURN_SPEED;
		}else if(Keyboard.isKeyDown(Keyboard.KEY_A)){
			this.currentTurnSpeed = TURN_SPEED;
		}else{
			this.currentTurnSpeed = 0;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
			jump();
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_C)){
			for(Entity e : entities){
				if(e.getPosition().x > player.getPosition().x - 10 && e.getPosition().x < player.getPosition().x + 10 && e.getPosition().z > player.getPosition().z - 10 && e.getPosition().z < player.getPosition().z + 10){
					if(e.getType() == EntityType.TREE){
					entities.remove(e);
					break;
					}
				}
			}
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)){
			if(Keyboard.isKeyDown(Keyboard.KEY_X)){
				DisplayManager.close();
			}
		}
	}

}
