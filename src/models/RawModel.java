package models;

public class RawModel {
	private int vaoID;
	private int vertexCount;
	private Hitbox hitbox;
	private String loc;
	public RawModel(int vaoID, int vertexCount, Hitbox hitbox, String loc){
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
		this.hitbox = hitbox;
		this.loc = loc;
	}
	
	public RawModel(int vaoID, int vertexCount) {
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
	}

	public int getVaoID() {
		return vaoID;
	}
	public int getVertexCount() {
		return vertexCount;
	}
	public Hitbox getHitbox() {
		return hitbox;
	}

	public String getLoc() {
		return loc;
	}
	
	
}
