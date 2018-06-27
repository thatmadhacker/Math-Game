package entities;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

	private float distanceFromPlayer = 50;
	private float angleAroundPlayer = 0;

	private Vector3f position = new Vector3f(0, 0, 0);
	private float pitch = 90;
	private float rotation;
	private float yaw = 180;
	private boolean moveBackX = false, moveBackY = false;
	private float roll;
	private Player player;

	public Camera(Player player) {
		this.player = player;
	}

	public void move() {
		this.position = new Vector3f(player.getPosition());
		position.y += 9;
		// calculateZoom();
		// calculateAngleAroundPlayer();
		calculatePitch();
		calculateYaw();
		// float horizontalDistance = calculateHorizontalDistance();
		// float verticalDistance = calculateVerticalDistance();
		// calculateCameraPosition(horizontalDistance, verticalDistance);	
		this.yaw = 180 - (player.getRotY() + rotation);
		//player.setRotY(yaw);
		}

	private void calculateYaw() {
		// if(Mouse.isButtonDown(0)){
		if (!moveBackX) {
			float angleChange = Mouse.getDX() * 0.3f;
			rotation -= angleChange;
			Mouse.setCursorPosition(Mouse.getX(), Display.getHeight() / 2);
			moveBackX = true;
			// }
		} else {
			moveBackX = false;
		}
	}

	@SuppressWarnings("unused")
	private void calculateCameraPosition(float horizontal, float vertical) {
		float theta = player.getRotY() + angleAroundPlayer;
		float offsetX = (float) (horizontal * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horizontal * Math.cos(Math.toRadians(theta)));
		position.x = player.getPosition().x - offsetX;
		position.z = player.getPosition().z - offsetZ;
		position.y = player.getPosition().getY() + vertical;
	}

	@SuppressWarnings("unused")
	private float calculateHorizontalDistance() {
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}

	@SuppressWarnings("unused")
	private float calculateVerticalDistance() {
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public float getRoll() {
		return roll;
	}

	public void setRoll(float roll) {
		this.roll = roll;
	}

	@SuppressWarnings("unused")
	private void calculateZoom() {
		float zoomLevel = Mouse.getDWheel() * 0.1f;
		distanceFromPlayer -= zoomLevel;
	}

	private void calculatePitch() {
		// if(Mouse.isButtonDown(1)){
		if (!moveBackY) {
			float pitchChange = Mouse.getDY() * 0.1f;
			pitch -= pitchChange;
			Mouse.setCursorPosition(Display.getWidth() / 2, Mouse.getY());
			moveBackY = true;
			// }
		} else {
			moveBackY = false;
		}
	}

	@SuppressWarnings("unused")
	private void calculateAngleAroundPlayer() {
		if (Mouse.isButtonDown(0)) {
			float angleChange = Mouse.getDX() * 0.3f;
			angleAroundPlayer -= angleChange;
		}
	}
}
