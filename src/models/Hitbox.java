package models;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class Hitbox {
	private float width;
	private float height;
	private float depth;
	private List<Vector3f> points;
	public Hitbox(List<Vector3f> points) {
		this.points = points;
	}
	
	public float getDepth() {
		return depth;
	}

	public float getWidth() {
		return width;
	}
	public float getHeight() {
		return height;
	}
	public void adjustForTransformationMatrix(Matrix4f transformationMatrix){
		float smallestX = 0;
		float smallestY = 0;
		float largestX = 0;
		float largestY = 0;
		float smallestZ = 0;
		float largestZ = 0;
		for(Vector3f point : points){
			Matrix4f point2 = new Matrix4f();
			point2.m00 = point.getX();
			point2.m10 = point.getY();
			point2.m20 = point.getZ();
			point2.m30 = 1;
			Matrix4f.mul(transformationMatrix, point2, point2);
			Vector3f worldPos = new Vector3f(point2.m00,point2.m10,point2.m20);
			if(worldPos.x < smallestX){
				smallestX = worldPos.x;
			}
			if(worldPos.x > largestX){
				largestX = worldPos.x;
			}
			if(worldPos.y < smallestY){
				smallestY = worldPos.y;
			}
			if(worldPos.y > largestY){
				largestY = worldPos.y;
			}
			if(worldPos.z < smallestZ){
				smallestZ = worldPos.z;
			}
			if(worldPos.z > largestZ){
				largestZ = worldPos.z;
			}
		}
		width = largestX - smallestX;
		height = largestY - smallestY;
		depth = largestZ - smallestZ;
	}

}
