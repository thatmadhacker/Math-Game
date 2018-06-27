package entities;

import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;
import toolbox.Maths;

public class Entity {

	protected TexturedModel model;
	protected Vector3f position;
	protected float rotX,rotY,rotZ;
	protected float scale;
	protected int textureIndex = 0;
	public boolean active = false;
	protected EntityType type;
	
	public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale, EntityType type, int textureIndex) {
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
		this.type = type;
		this.textureIndex = textureIndex;
		getModel().getRawModel().getHitbox().adjustForTransformationMatrix(Maths.createTransformationMatrix(position, rotX, rotY, rotZ, scale));
	}
	
	public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale, EntityType type) {
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
		this.type = type;
		getModel().getRawModel().getHitbox().adjustForTransformationMatrix(Maths.createTransformationMatrix(position, rotX, rotY, rotZ, scale));
	}
	
	public float getTextureXOffset(){
		int column = textureIndex%model.getTexture().getNumberOfRows();
		return (float)column / (float) model.getTexture().getNumberOfRows();
	}
	
	public float getTextureYOffset(){
		int row = textureIndex/model.getTexture().getNumberOfRows();
		return (float)row/(float)model.getTexture().getNumberOfRows();
	}
	
	public EntityType getType() {
		return type;
	}

	public void setType(EntityType type) {
		this.type = type;
	}

	public void increasePosition(float dx, float dy, float dz){
		this.position.x+=dx;
		this.position.y+=dy;
		this.position.z+=dz;
		getModel().getRawModel().getHitbox().adjustForTransformationMatrix(Maths.createTransformationMatrix(position, rotX, rotY, rotZ, scale));
	}
	public void increaseRotation(float dx,float dy,float dz){
		this.rotX+=dx;
		this.rotY+=dy;
		this.rotZ+=dz;
		getModel().getRawModel().getHitbox().adjustForTransformationMatrix(Maths.createTransformationMatrix(position, rotX, rotY, rotZ, scale));
	}
	public TexturedModel getModel() {
		return model;
	}
	public void setModel(TexturedModel model) {
		this.model = model;
	}
	public Vector3f getPosition() {
		return position;
	}
	public void setPosition(Vector3f position) {
		this.position = position;
		getModel().getRawModel().getHitbox().adjustForTransformationMatrix(Maths.createTransformationMatrix(position, rotX, rotY, rotZ, scale));
	}
	public float getRotX() {
		return rotX;
	}
	public void setRotX(float rotX) {
		this.rotX = rotX;
		getModel().getRawModel().getHitbox().adjustForTransformationMatrix(Maths.createTransformationMatrix(position, rotX, rotY, rotZ, scale));
	}
	public float getRotY() {
		return rotY;
	}
	public void setRotY(float rotY) {
		this.rotY = rotY;
		getModel().getRawModel().getHitbox().adjustForTransformationMatrix(Maths.createTransformationMatrix(position, rotX, rotY, rotZ, scale));
	}
	public float getRotZ() {
		return rotZ;
	}
	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
		getModel().getRawModel().getHitbox().adjustForTransformationMatrix(Maths.createTransformationMatrix(position, rotX, rotY, rotZ, scale));
	}
	public float getScale() {
		return scale;
	}
	public void setScale(float scale) {
		this.scale = scale;
		getModel().getRawModel().getHitbox().adjustForTransformationMatrix(Maths.createTransformationMatrix(position, rotX, rotY, rotZ, scale));
	}
	
}
